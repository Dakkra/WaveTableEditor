package com.dakkra.wavetableeditor;

import com.dakkra.wavetableeditor.io.TableExporter;

import java.io.File;

public class WaveTableEditor {
    public static void main(String[] args) {
        System.out.println("Exporting test sample...");
        WaveTable wt = new WaveTable();
        wt.generateSine(1.0f);
        wt.generatePulse(0.75f);
        wt.generateSaw();
        TableExporter.threadedExport(wt, new File("sample.wav"));
    }
}
