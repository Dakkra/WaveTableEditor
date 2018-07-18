package com.dakkra.wavetableeditor;

public class WaveTable {

    public static final int SAMPLES_IN_WAVETABLE = 2048;
    private short samples[];

    /**
     * Creates a new wave table with 2048 16-bit samples
     */
    public WaveTable() {
        samples = new short[SAMPLES_IN_WAVETABLE];
        generateFlat();
    }

    /**
     * Fills this table with samples all equal to '0'
     */
    public void generateFlat() {
        for (int index = 0; index < SAMPLES_IN_WAVETABLE; index++)
            samples[index] = 0;
    }

    /**
     * Generates numCycles amount of sine for this wave table
     *
     * @param numCycles number of cycles
     */
    public void generateSine(float numCycles) {
        if (numCycles <= 0)
            numCycles = 1;
        short magnitude = Short.MAX_VALUE;
        for (int index = 0; index < SAMPLES_IN_WAVETABLE; index++)
            samples[index] = (short) Math.round((magnitude * Math.sin((index * Math.PI * numCycles) / 1024)));
    }

    /**
     * Generates a pulse wave with the given percent width
     *
     * @param width width of pulse 0-1
     */
    public void generatePulse(float width) {
        if (width < 0 || width > 1)
            width = 0.5f;
        int flipIndex = Math.round(SAMPLES_IN_WAVETABLE * width);
        for (int index = 0; index < SAMPLES_IN_WAVETABLE; index++)
            samples[index] = index > flipIndex ? Short.MIN_VALUE : Short.MAX_VALUE;
    }

    /**
     * Generates a ram-saw
     */
    public void generateSaw() {
        short stepSize = (Short.MAX_VALUE - Short.MIN_VALUE) / SAMPLES_IN_WAVETABLE;
        for (int index = 0; index < SAMPLES_IN_WAVETABLE; index++)
            samples[index] = (short) ((index * stepSize) + Short.MIN_VALUE);
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
