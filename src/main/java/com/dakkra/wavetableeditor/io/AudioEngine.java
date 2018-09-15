package com.dakkra.wavetableeditor.io;

import com.dakkra.wavetableeditor.ApplicationData;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioEngine {


    private static AudioFormat audioFormat;
    private static DataLine.Info datalineInfo;
    private static SourceDataLine dataLine;
    private static boolean isPlaying;

    /**
     * Starts the audio engine and opens the audio line
     *
     * @throws LineUnavailableException
     */
    public static void start() throws LineUnavailableException {
        audioFormat = new AudioFormat(44100, 16, 1, true, true);
        datalineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        dataLine = (SourceDataLine) AudioSystem.getLine(datalineInfo);
        isPlaying = false;
        dataLine.open(audioFormat);
        dataLine.start();
    }

    /**
     * Stops the audio engine and closes the audio line
     */
    public static void stop() {
        dataLine.flush();
        dataLine.stop();
        dataLine.close();
    }

    /**
     * Plays the master WaveTable though the primary system audio device
     */
    public static void playbackTable() {
        if (!isPlaying) {
            isPlaying = true;
            short[] higherPitchSample = new short[2048];
            short[] samples = ApplicationData.getMasterWaveTable().getSamples();

            //Scale samples to not be loud 30% volume
            for (int i = 0; i < samples.length; i++)
                samples[i] = (short) Math.round(samples[i] * 0.3);

            //Resample into new array for a higher frequency tone
            for (int i = 0; i < higherPitchSample.length; i++)
                higherPitchSample[i] = samples[(i * 9) % 2048];

            byte[] pcmData = Arrays.copyOfRange(WaveEncode.generateData(higherPitchSample), 44, 4140);
            dataLine.start();
            for (int i = 0; i < 5; i++) {
                dataLine.write(pcmData, 0, pcmData.length);
            }
            dataLine.drain();
            dataLine.stop();
            isPlaying = false;
        }
    }

    /**
     * Calls playbackTable() in a separate thread;
     */
    public static void threadedPlayback() {
        Thread thread = new Thread(AudioEngine::playbackTable);
        thread.start();
    }

}
