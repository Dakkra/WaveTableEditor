package com.dakkra.wavetableeditor;

public class WaveTable {

    public static final int SAMPLES_IN_WAVETABLE = 2048;
    private short samples[];

    /**
     * Creates a new wave table with 2048 16-bit samples
     */
    public WaveTable() {
        samples = new short[SAMPLES_IN_WAVETABLE];
    }

    /**
     * Fills this table with samples all equal to '0'
     */
    public void generateFlat() {
        for (short sample : samples)
            sample = (short) 0;
    }

    /**
     * Generate samples and fill th is table to act as a sine
     */
    public void generateSine() {
        short magnitude = Short.MAX_VALUE;
        for (int index = 0; index < SAMPLES_IN_WAVETABLE; index++)
            samples[index] = (short) Math.round(magnitude * Math.sin((index * Math.PI) / 1024));
    }

    /**
     * Gets the samples from this wave table.
     *
     * @return array of 2048 16bit samples
     */
    public short[] getSamples() {
        return samples;
    }
}
