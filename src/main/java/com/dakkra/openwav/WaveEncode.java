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
    private static final int AUDIO_FORMAT = 1;
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
        ArrayBuilder<Byte> dataBuilder = new ArrayBuilder<>(DATA_START_OFFSET + (pcmData.length * 2));
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

        //TODO FMT AND DATA CHUNKS

        byte data[] = new byte[dataBuilder.getSize()];
        for (int index = 0; index < data.length; index++)
            data[index] = dataBuilder.get(index);
        return data;
    }

    private static void writeBytesToArrayBuilder(ArrayBuilder<Byte> builder, byte... bytes) {
        for (byte b : bytes)
            builder.add(b);
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
