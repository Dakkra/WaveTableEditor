package com.dakkra.openwav;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Chris Soderquist(Dakkra)
 *         email: contact@dakkra.com
 *         web: http://dakkra.com
 *         <p>
 *         A home made wevfile decoder for use with WaveTableEditor
 *         Supports RIFF PCM only for the time being
 *         <p>
 *         Based on the specifiction provided by http://soundfile.sapp.org/doc/WaveFormat/
 *         And http://www.signalogic.com/index.pl?page=ms_waveform
 */
public class WavFile {
    //wave file constants
    //"RIFF" in ascii
    private static final int RIFF_CHUNK_ID = 0x52494646;
    //"WAVE" in ascii
    private static final int WAVE_FORMAT = 0x57415645;
    //"fmt" in ascii
    private static final int SUBCHUNK1ID = 0x666d7420;
    //PCM uses 16. That's the only wave we will support. Read as little endian
    private static final int SUBCHUNK1SIZE = 16;
    //Format = 1 for pcm. Others mean compression
    private static final int AUDIO_FORMAT = 1;
    //"data" in ascii
    private static final int SUBCHUNK2ID = 0x64617461;
    //Data should start at byte 44
    private static final int DATA_START_OFFSET = 44;

    //Wave file header
    //RIFF section
    private int chunkID;
    private LittleEndianInt chunkSize;
    private int format;
    //fmt section
    private int subchunk1ID;
    private LittleEndianInt subchunk1Size;
    private LittleEndianShort audioFormat;
    private LittleEndianShort numChannels;
    private LittleEndianInt samplerate;
    private LittleEndianInt byteRate;
    private LittleEndianShort blockAlign;
    private LittleEndianShort bitsPerSample;
    //Data header
    private int subchunk2ID;
    private LittleEndianInt subchunk2Size;
    //Input stream
    private InputStream inputStream;
    //Sample position in bytes
    private long dataOffset = 0;
    //ByteBuffer for conversion from byte[] to integers
    private ByteBuffer bb;

    public WavFile(File inputFile) {
        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Failed to get input stream for file");
        }
    }

    public WavFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Opens wav file stream. Fills fields respectively
     *
     * @return Returns false if failed read or incorrect wave data
     * @throws Exception
     */
    public boolean open() throws Exception {
        return readHeader();
    }

    /**
     * Closes the stream to source file. All fields still remain
     *
     * @throws IOException
     */
    public void close() throws IOException {
        inputStream.close();
    }

    private boolean readHeader() throws Exception {
        //Declare buffers for use in storing bytes read in form inputStream
        byte intByteBuffer[] = new byte[4];
        byte shortByteBuffer[] = new byte[2];
        //ChunkID
        inputStream.read(intByteBuffer);
        chunkID = bytesToInt(intByteBuffer);
        //Check to see if valid RIFF file
        if (chunkID != RIFF_CHUNK_ID)
            throw new WavFileException("NOT A VALID RIFF FILE: chunkid [" + chunkID + "]");

        //ChunkSize
        inputStream.read(intByteBuffer);
        chunkSize = new LittleEndianInt(bytesToInt(intByteBuffer));

        //Format
        inputStream.read(intByteBuffer);
        format = bytesToInt(intByteBuffer);
        if (format != WAVE_FORMAT)
            throw new WavFileException("INVALID WAV FORMAT");


        //Subchunk1ID
        inputStream.read(intByteBuffer);
        subchunk1ID = bytesToInt(intByteBuffer);
        if (subchunk1ID != SUBCHUNK1ID)
            throw new WavFileException("INVALID SUBCHUNK 1 ID");


        //SubChunk1Size
        inputStream.read(intByteBuffer);
        subchunk1Size = new LittleEndianInt(bytesToInt(intByteBuffer));
        if (subchunk1Size.convert() != SUBCHUNK1SIZE)
            throw new WavFileException("NON PCM FILES ARE NOT SUPPORTED: chunk size[" + subchunk1Size.convert() + "]");


        //Audio Format
        inputStream.read(shortByteBuffer);
        audioFormat = new LittleEndianShort(bytesToShort(shortByteBuffer));
        if (audioFormat.convert() != AUDIO_FORMAT)
            throw new WavFileException("COMPRESSED WAVE FILE NOT SUPPORTED: format[" + audioFormat.convert() + "]");


        //NumChannels
        inputStream.read(shortByteBuffer);
        numChannels = new LittleEndianShort(bytesToShort(shortByteBuffer));
        if (numChannels.convert() > 2 || numChannels.convert() < 0)
            throw new WavFileException("INVALID NUMBER OF CHANNELS: numChannels[" + numChannels.convert() + "]");

        //SampleRate
        inputStream.read(intByteBuffer);
        samplerate = new LittleEndianInt(bytesToInt(intByteBuffer));

        //ByteRate
        inputStream.read(intByteBuffer);
        byteRate = new LittleEndianInt(bytesToInt(intByteBuffer));

        //BlockAlign
        inputStream.read(shortByteBuffer);
        blockAlign = new LittleEndianShort(bytesToShort(shortByteBuffer));

        //BitsPerSample
        //We only support 16
        inputStream.read(shortByteBuffer);
        bitsPerSample = new LittleEndianShort(bytesToShort(shortByteBuffer));

        //SubChunk2ID
        inputStream.read(intByteBuffer);
        subchunk2ID = bytesToInt(intByteBuffer);
        if (subchunk2ID != SUBCHUNK2ID)
            throw new WavFileException("INVALID DATA HEADER");

        //Subchunk2Size
        inputStream.read(shortByteBuffer);
        subchunk2Size = new LittleEndianInt(bytesToShort(shortByteBuffer));

        //Everything loaded fine
        return true;
    }

    /**
     * Gets next long integer sample
     *
     * @return Long integer sample
     */
    private long readSample(long skipFrames) throws IOException, WavFileException {
        long sample = 0;
        byte buffer[] = new byte[bitsPerSample.convert() / 8];
        inputStream.skip(skipFrames);
        int delta = inputStream.read(buffer);
        if (delta != -1) {
            dataOffset += delta;
        }

        switch (bitsPerSample.convert()) {
            case 16: {
                sample = bytesToShort(buffer);
                break;
            }
            default:
                break;
        }

        return sample;
    }

    /**
     * Test to see if the wav file has two channels
     *
     * @return numChannels == 2
     */
    public boolean isStereo() {
        return (numChannels.convert() == 2);
    }

    //META DATA

    /**
     * Returns number of channels claimed by the wav file
     * @return Number of channels
     */
    public int getNumChannels() {
        return numChannels.convert();
    }

    /**
     * Returns the sample rate claimed by the wav file
     * @return Sample rate
     */
    public int getSampleRate() {
        return samplerate.convert();
    }

    /**
     * Returns the bit depth of and individual sample in the wave file
     * @return bit depth of a sample
     */
    public int getBitDepth() {
        return bitsPerSample.convert();
    }

    /**
     * Caclulated from file meta data
     *
     * @return fileSize form meta data
     */
    public int getFileSize() {
        return subchunk1Size.convert() + 8;
    }

    /**
     * Calculated form the file meta data
     * Returns the number of sample frames contained in the data
     * @return Number of sample frames
     */
    public int getNumFrames() {
        return (chunkSize.convert() / blockAlign.convert());
    }

    //FRAME GRABBING - double

    /**
     * Reads double values into array buffer
     *
     * @param frameBuffer array to read into
     * @return number of samples read
     * @throws IOException
     */
    public int readFrames(double[] frameBuffer) throws IOException, WavFileException {
        return readFrames(frameBuffer, 0);
    }

    /**
     * Reads double values into array buffer
     *
     * @param frameBuffer array to read into
     * @param skipFrames  amount of frames to skip
     * @return number of smaples read
     * @throws IOException
     */
    public int readFrames(double[] frameBuffer, int skipFrames) throws IOException, WavFileException {
        int samplesRead = 0;
        for (int f = 0; f < frameBuffer.length; f++) {
            frameBuffer[f] = (double) readSample(skipFrames) / (double) (Long.MAX_VALUE >> (64 - bitsPerSample.convert()));
            samplesRead++;
        }
        return samplesRead;
    }

    //UTIL

    /**
     * Converts an array of 8 bytes into a long integer
     * @param bytes
     * @return Converted long integer
     * @throws WavFileException
     */
    public long bytesToLong(byte bytes[]) throws WavFileException {
        if (bytes.length != 8)
            throw new WavFileException("INVALID ARRAY SIZE. MUST BE 8");

        bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getLong();
    }

    /**
     * Converts an array of 4 bytes into an integer
     * @param bytes
     * @return Converted integer
     * @throws WavFileException
     */
    public int bytesToInt(byte bytes[]) throws WavFileException {
        if (bytes.length != 4)
            throw new WavFileException("INVALID ARRAY SIZE. MUST BE 4");

        bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getInt();
    }

    /**
     * Converts an array of 2 bytes into a short integer
     * @param bytes
     * @return Converted short integer
     * @throws WavFileException
     */
    public short bytesToShort(byte bytes[]) throws WavFileException {
        if (bytes.length != 2)
            throw new WavFileException("INVALID ARRAY SIZE. MUST BE 2");

        bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getShort();
    }
}
