package com.dakkra.wavetableeditor.ui.graphicaleditor;

import com.dakkra.wavetableeditor.waveconcept.WaveTable;

import java.util.ArrayList;
import java.util.Collections;

public class ScalarNodesToWaveTable {

    /**
     * Converts the list of scalar nodes into a WaveTable
     *
     * @param nodes list of scalar nodes
     * @return WaveTable generated from list of nodes
     */
    public static WaveTable scalarNodesListToTable(ArrayList<ScalarCircle> nodes) {
        Collections.sort(nodes);
        short[] samples = new short[WaveTable.SAMPLES_IN_WAVETABLE];
        ScalarCircle previousNode = nodes.get(0);
        nodes.remove(0);
        for (ScalarCircle node : nodes) {
            int startingIndex = (int) Math.round(previousNode.getScalarX() * WaveTable.SAMPLES_IN_WAVETABLE);
            int endingIndex = (int) Math.round(node.getScalarX() * WaveTable.SAMPLES_IN_WAVETABLE);
            double step = 2 * ((node.getScalarY() * Short.MAX_VALUE) - (previousNode.getScalarY() * Short.MAX_VALUE)) / (startingIndex - endingIndex);
            double cVal = (-2 * previousNode.getScalarY() * Short.MAX_VALUE) - Short.MAX_VALUE;
            for (int loc = startingIndex; loc < endingIndex; loc++) {
                samples[loc] = (short) cVal;
                cVal += step;
            }
            previousNode = node;
        }
        WaveTable wt = new WaveTable();
        wt.setSamples(samples);
        return wt;
    }

}
