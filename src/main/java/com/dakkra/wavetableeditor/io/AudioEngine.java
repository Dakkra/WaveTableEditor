package com.dakkra.wavetableeditor.io;

import com.dakkra.wavetableeditor.ApplicationData;

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.concurrent.*;

public class AudioEngine {


    private static SourceDataLine dataLine;
    private static boolean isPlaying;
    private static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
    private static ThreadPoolExecutor executor;

    /**
     * Starts the audio engine and opens the audio line
     *
     * @throws LineUnavailableException
     */
    public static void start() throws LineUnavailableException {
        executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, queue);
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info datalineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        dataLine = (SourceDataLine) AudioSystem.getLine(datalineInfo);
        isPlaying = false;
        dataLine.open(audioFormat);
        dataLine.start();
    }

    /**
     * Stops the audio engine and closes the audio line
     */
    public static void stop() {
        executor.shutdown();
        dataLine.flush();
        dataLine.stop();
        dataLine.close();
    }

    /**
     * Plays the master WaveTable though the primary system audio device
     */
    private static void playbackTable() {
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
        try {
            executor.execute(AudioEngine::playbackTable);
        } catch (RejectedExecutionException rej) {
            //Do nothing, this is fine
        }
    }

}
