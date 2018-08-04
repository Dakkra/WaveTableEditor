package com.dakkra.wavetableeditor.ui.graphicaleditor;

import java.util.HashMap;
import java.util.UUID;

public class DigitalGraph {
    private HashMap<UUID, DigitalNode> nodeMap;

    public DigitalGraph() {
        this.nodeMap = new HashMap<>();
    }

    public void createNode(UUID providedID, double scalarX, double scalarY) {
        nodeMap.put(providedID, new DigitalNode(scalarX, scalarY));
    }

    public void removeNode(UUID uidOfNode) {
        nodeMap.remove(uidOfNode);
    }

}
