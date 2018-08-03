package com.dakkra.wavetableeditor;

import com.dakkra.wavetableeditor.waveconcept.WaveTable;
import com.dakkra.wavetableeditor.waveconcept.WaveTableListener;

public class ApplicationData {
    private static WaveTable masterWaveTable;

    /**
     * Initializes the shared application data
     */
    public static void init() {
        masterWaveTable = new WaveTable();
    }

    /**
     * Registers a WaveTableListener with the master WaveTable
     *
     * @param listener listener to register
     */
    public static void registerWaveTableListener(WaveTableListener listener) {
        masterWaveTable.addWaveTableListener(listener);
    }

    /**
     * Gets the master WaveTable
     */
    public static WaveTable getMasterWaveTable() {
        return masterWaveTable;
    }
}
