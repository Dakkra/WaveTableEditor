package com.dakkra.wavetableeditor.io;

import com.dakkra.wavetableeditor.ApplicationData;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioEngine {

    public static void plabackTable() throws LineUnavailableException {
        AudioFormat af = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        short[] middleCSample = new short[2048];
        short[] samples = ApplicationData.getMasterWaveTable().getSamples();

        for (int i = 0; i < middleCSample.length; i++)
            middleCSample[i] = samples[(i * 8) % 2048];

        byte[] pcmData = Arrays.copyOfRange(WaveEncode.generateData(middleCSample), 44, 4140);
        line.open(af);
        line.start();
        for (int i = 0; i < 5; i++) {
            line.write(pcmData, 0, pcmData.length);
        }
        line.drain();
        line.stop();
        line.close();
    }

    public static void threadedPlayback() {
        Thread thread = new Thread(() -> {
            try {
                plabackTable();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
