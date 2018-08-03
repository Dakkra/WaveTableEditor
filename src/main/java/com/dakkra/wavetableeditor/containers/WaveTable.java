package com.dakkra.wavetableeditor.containers;

import java.util.ArrayList;
import java.util.Random;

public class WaveTable {

    public static final int SAMPLES_IN_WAVETABLE = 2048;
    private short samples[];
    private ArrayList<WaveTableListener> listeners;
    private Random random;

    /**
     * Creates a new wave table with 2048 16-bit samples
     */
    public WaveTable() {
        samples = new short[SAMPLES_IN_WAVETABLE];
        listeners = new ArrayList<>();
        random = new Random();
        generateFlat();
    }

    /**
     * Fills this table with samples all equal to '0'
     */
    public void generateFlat() {
        for (int index = 0; index < SAMPLES_IN_WAVETABLE; index++)
            samples[index] = 0;
        notifyListeners();
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
        notifyListeners();
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
            samples[index] = index > flipIndex ? -Short.MAX_VALUE : Short.MAX_VALUE;
        notifyListeners();
    }

    /**
     * Generates a ramp-saw
     */
    public void generateSaw() {
        short stepSize = (Short.MAX_VALUE - Short.MIN_VALUE) / SAMPLES_IN_WAVETABLE;
        for (int index = 0; index < SAMPLES_IN_WAVETABLE; index++)
            samples[index] = (short) ((index * stepSize) + Short.MIN_VALUE);
        notifyListeners();
    }

    /**
     * Generates a single cycle based upon the supplied harmonics (multiplier of the fundamental)
     * <p>
     * If improper data is supplied, generates a single cycle sine wav
     */
    public void generateFromHarmonics(int... harmonics) {
        //Region check the array, if it has no length, generate a pure sine and quit the method
        if (!(harmonics.length > 0)) {
            generateSine(1f);
            return;
        }
        //Initialize this wave table
        generateFlat();
        //Create place holder wave table for generating harmonics
        WaveTable harmonicGenerator = new WaveTable();
        //Iterate through each harmonic and process it into the wave table
        for (int index = 0; index < harmonics.length; index++) {
            //Generate the harmonic data
            harmonicGenerator.generateSine(harmonics[index]);
            short[] harmonicSamples = harmonicGenerator.getSamples();
            //Add add new harmonic to wave table while handling amplitude as 1/x
            //Scaling CFR: https://www.desmos.com/calculator/79dlswrbfi
            for (int sampleIndex = 0; sampleIndex < SAMPLES_IN_WAVETABLE; sampleIndex++)
                samples[sampleIndex] += .5 * (harmonicSamples[sampleIndex] / (index + 1)) * (1 + (1 / (Math.pow(2, (harmonics.length - 1)))));
            //If there is a faulty harmonic, generate a pure sine and quit the method
            if (harmonics[index] < 1) {
                generateSine(1f);
                return;
            }
        }
        notifyListeners();
    }

    /**
     * Generates new sample data using random harmonics and using the harmonic generator
     */
    public void generateFromRandomHarmonics() {
        int randSize = random.nextInt(1020) + 4;
        int[] harmonics = new int[randSize];
        for (int i = 0; i < randSize; i++)
            harmonics[i] = random.nextInt(299) + 1;
        generateFromHarmonics(harmonics);
        //The above already notifies listeners
    }

    /**
     * Adds a WaveTable listener to this WaveTable
     *
     * @param listener listener to add
     */
    public void addWaveTableListener(WaveTableListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets the samples from this wave table.
     *
     * @return array of 2048 16bit samples
     */
    public short[] getSamples() {
        return samples;
    }

    /**
     * Calls the update function of all WaveTable listeners registered on this WaveTable
     */
    private void notifyListeners() {
        for (WaveTableListener wl : listeners)
            wl.update(this);
    }
}
