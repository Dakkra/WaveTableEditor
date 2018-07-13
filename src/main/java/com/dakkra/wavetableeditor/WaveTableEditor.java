package com.dakkra.wavetableeditor;

import java.io.File;

public class WaveTableEditor {
    public static void main(String[] args) {
        System.out.println("Hello this is a test");
        System.out.println("Exporting test sample...");
        WaveTable wt = new WaveTable();
        wt.generateSine();
        TableExporter.export(wt, new File("sample.wav"));
    }
}
