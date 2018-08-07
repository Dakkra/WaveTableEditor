package com.dakkra.wavetableeditor;

import com.dakkra.wavetableeditor.io.AudioEngine;
import com.dakkra.wavetableeditor.ui.PrimaryUI;

import javax.sound.sampled.LineUnavailableException;

public class WaveTableEditor {
    public static void main(String[] args) {
        ApplicationData.init();
        try {
            AudioEngine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        PrimaryUI.startUserInterface();
    }

    public static void shutdown() {
        AudioEngine.stop();
    }
}
