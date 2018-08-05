package com.dakkra.wavetableeditor.io;

import com.dakkra.wavetableeditor.ApplicationData;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioEngine {

    /**
     * Plays the master WaveTable though the primary system audio device
     *
     * @throws LineUnavailableException
     */
    public static void playbackTable() throws LineUnavailableException {
        AudioFormat af = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        short[] higherPitchSample = new short[2048];
        short[] samples = ApplicationData.getMasterWaveTable().getSamples();

        //Scale samples to not be loud 30% volume
        for (int i = 0; i < samples.length; i++)
            samples[i] = (short) Math.round(samples[i] * 0.3);

        //Resample into new array for a higher frequency tone
        for (int i = 0; i < higherPitchSample.length; i++)
            higherPitchSample[i] = samples[(i * 8) % 2048];

        byte[] pcmData = Arrays.copyOfRange(WaveEncode.generateData(higherPitchSample), 44, 4140);
        line.open(af);
        line.start();
        for (int i = 0; i < 5; i++) {
            line.write(pcmData, 0, pcmData.length);
        }
        line.drain();
        line.stop();
        line.close();
    }

    /**
     * Calls playbackTable() in a separate thread;
     */
    public static void threadedPlayback() {
        Thread thread = new Thread(() -> {
            try {
                playbackTable();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
