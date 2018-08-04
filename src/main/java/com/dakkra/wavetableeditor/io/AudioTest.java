package com.dakkra.wavetableeditor.io;

import com.dakkra.wavetableeditor.ApplicationData;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioTest {

    public static void plabackTable() throws LineUnavailableException {
        AudioFormat af = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        byte[] pcmData = Arrays.copyOfRange(WaveEncode.generateData(ApplicationData.getMasterWaveTable().getSamples()), 44, 4140);
        System.out.println(pcmData.length);
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
