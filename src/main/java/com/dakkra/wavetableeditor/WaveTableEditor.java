package com.dakkra.wavetableeditor;

import java.io.File;
import java.io.IOException;

public class WaveTableEditor {
    public static void main(String[] args) {
        System.out.println("Hello this is a test");
        System.out.println("Exporting test sample...");
        WaveTable wt = new WaveTable();
        wt.generateSine(1.0f);
        try {
            TableExporter.export(wt, new File("sample.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
