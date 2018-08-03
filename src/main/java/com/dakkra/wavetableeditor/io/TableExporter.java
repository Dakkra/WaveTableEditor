package com.dakkra.wavetableeditor.io;

import com.dakkra.wavetableeditor.containers.WaveTable;

import java.io.*;

public class TableExporter {

    /**
     * Exports a wave table to the provided destination file
     *
     * @param waveTable       wave table to be exported
     * @param destinationFile file to export the wave table to
     */
    public static void export(WaveTable waveTable, File destinationFile) throws IOException {
        if (destinationFile.isDirectory())
            throw new IOException("Cannot write data as a directory");
        if (!destinationFile.exists())
            destinationFile.createNewFile();
        OutputStream oStream = new FileOutputStream(destinationFile);
        short[] pcmData = waveTable.getSamples();
        if (pcmData.length != 2048)
            throw new IOException("Invalid sample length");
        //Convert sample data from big endian to little endian
        for (int index = 0; index < pcmData.length; index++)
            pcmData[index] = WaveEncode.convert16bitEndian(pcmData[index]);
        //Generate data
        byte data[] = WaveEncode.generateData(pcmData);
        //Write the data to the destination file and close
        oStream.write(data);
        oStream.flush();
        oStream.close();
    }

    /**
     * Similar to export, but does this on a spawned thread
     * <p>
     * Exports a wave table to the provided destination file
     *
     * @param waveTable       wave table to be exported
     * @param destinationFile file to export the wave table to
     */
    public static void threadedExport(WaveTable waveTable, File destinationFile) {
        Thread exportThread = new Thread(() -> {
            try {
                export(waveTable, destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        exportThread.start();
    }

}
