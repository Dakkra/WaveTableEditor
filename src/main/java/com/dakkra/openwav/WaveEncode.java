package com.dakkra.openwav;

import com.dakkra.wavetableeditor.util.ArrayBuilder;

import java.nio.ByteBuffer;

public class WaveEncode {
    //WAVE File constants
    //"RIFF" in ascii
    private static final int RIFF_CHUNK_ID = 0x52494646;
    //"WAVE" in ascii
    private static final int WAVE_FORMAT = 0x57415645;
    //"fmt" in ascii
    private static final int SUBCHUNK1ID = 0x666d7420;
    //16bit PCM
    private static final int SUBCHUNK1SIZE = 16;
    //Format = 1 for pcm
    private static final short AUDIO_FORMAT = 1;
    //Mono sample
    private static final short NUM_CHANNELS = 1;
    //Samplerate
    private static final int SAMPLE_RATE = 44100;
    //Bit depth
    private static final short BIT_DEPTH = 16;
    //"data" in ascii
    private static final int SUBCHUNK2ID = 0x64617461;
    //Data should start at byte 44
    private static final int DATA_START_OFFSET = 44;

    /**
     * Generates all the bytes for a properly encoded 16bit mono PCM wave file
     *
     * @return byte array represented an encoded 16 bit mono PCM wave file
     */
    public static byte[] generateData(short[] pcmData) {
        //Bytes for the whole encoded file
        ArrayBuilder<Byte> dataBuilder = new ArrayBuilder<>(new Byte[DATA_START_OFFSET + (pcmData.length * 2)]);
        //"RIFF"
        byte buffer[] = ByteBuffer.allocate(4).putInt(RIFF_CHUNK_ID).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        //CHUNK SIZE
        int chunkSize = (dataBuilder.getSize() - 8);
        chunkSize = convert32bitEndian(chunkSize);
        buffer = ByteBuffer.allocate(4).putInt(chunkSize).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        //"WAVE"
        buffer = ByteBuffer.allocate(4).putInt(WAVE_FORMAT).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        //"FMT"
        buffer = ByteBuffer.allocate(4).putInt(SUBCHUNK1ID).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(4).putInt(convert32bitEndian(SUBCHUNK1SIZE)).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(2).putShort(convert16bitEndian(AUDIO_FORMAT)).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(2).putShort(convert16bitEndian(NUM_CHANNELS)).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(4).putInt(convert32bitEndian(SAMPLE_RATE)).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(4).putInt(convert32bitEndian(SAMPLE_RATE * BIT_DEPTH / 8)).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(2).putShort(convert16bitEndian((short) (BIT_DEPTH / 8))).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(2).putShort(convert16bitEndian(BIT_DEPTH)).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(4).putInt(SUBCHUNK2ID).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        buffer = ByteBuffer.allocate(4).putInt(convert32bitEndian(pcmData.length * BIT_DEPTH / 8)).array();
        writeBytesToArrayBuilder(dataBuilder, buffer);
        writePCMDataToArrayBuilder(dataBuilder, pcmData);
        //Convert arrayBuilder to a primitive array of bytes
        byte data[] = new byte[dataBuilder.getSize()];
        for (int index = 0; index < data.length; index++)
            data[index] = dataBuilder.get(index);
        return data;
    }

    private static void writeBytesToArrayBuilder(ArrayBuilder<Byte> builder, byte... bytes) {
        for (byte b : bytes)
            builder.add(b);
    }

    private static void writePCMDataToArrayBuilder(ArrayBuilder<Byte> builder, short[] pcmData) {
        byte buffer[];
        for (short s : pcmData) {
            buffer = ByteBuffer.allocate(2).putShort(s).array();
            writeBytesToArrayBuilder(builder, buffer);
        }
    }

    /**
     * Converts the endian format of a short (16 bit) integer
     *
     * @return inverse endian of the input
     */
    public static short convert16bitEndian(short input) {
        return (short) (((input & 0xff) << 8) | (((input >> 8) & 0xff)));
    }


    /**
     * Converts the endian format of a int (32 bit) integer
     *
     * @return inverse endian of the input
     */
    public static int convert32bitEndian(int integer) {
        return integer << 24 | integer >> 8 & 0xff00 | integer << 8 & 0xff0000 | integer >>> 24;
    }
}
