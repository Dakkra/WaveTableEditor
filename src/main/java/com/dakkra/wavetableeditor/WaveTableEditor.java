package com.dakkra.wavetableeditor;

import com.dakkra.wavetableeditor.io.TableExporter;

import java.io.File;

public class WaveTableEditor {
    public static void main(String[] args) {
        System.out.println("Exporting test sample...");
        WaveTable wt = new WaveTable();
        wt.generateSine(1.0f);
        wt.generatePulse(0.5f);
        wt.generateSaw();
        int[] arr = new int[1000];
        for (int i = 1; i < 1001; i++)
            arr[i - 1] = i;
        wt.generateFromHarmonics(arr);
        TableExporter.threadedExport(wt, new File("sample.wav"));
    }
}
