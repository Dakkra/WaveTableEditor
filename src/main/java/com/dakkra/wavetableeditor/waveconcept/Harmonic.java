package com.dakkra.wavetableeditor.waveconcept;

public class Harmonic {

    private float amplitude;
    private int harmonicValue;

    /**
     * Creates a harmonic idea with the provided amplitude and value
     * <p>
     * Amplitude ranges from 0-1
     * Harmonic Values range from 1-INF
     * <p>
     * If either values is not valid, the invalid value will be set to 1
     *
     * @param amplitude     amplitude of this harmonic
     * @param harmonicValue value of this harmonic
     */
    public Harmonic(int harmonicValue, float amplitude) {
        this.amplitude = amplitude;
        this.harmonicValue = harmonicValue;
        if (this.amplitude < 0 || this.amplitude > 1)
            this.amplitude = 1f;
        if (this.harmonicValue < 1)
            this.harmonicValue = 1;
    }

    /**
     * Gets the amplitude of this harmonic
     */
    public float getAmplitude() {
        return amplitude;
    }

    /**
     * Gets the value of this harmonic
     */
    public int getHarmonicValue() {
        return harmonicValue;
    }
}
