package com.dakkra.wavetableeditor.ui;

import com.dakkra.wavetableeditor.ApplicationData;
import com.dakkra.wavetableeditor.WaveTableEditor;
import com.dakkra.wavetableeditor.io.AudioEngine;
import com.dakkra.wavetableeditor.io.TableExporter;
import com.dakkra.wavetableeditor.ui.graphicaleditor.GraphicalEditor;
import com.dakkra.wavetableeditor.ui.harmoniceditor.HarmonicEditor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PrimaryUI extends Application {

    public static final int MINIMUM_WIDTH = 700;
    public static final int MINIMUM_HEIGHT = 400;
    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;

    private static final String APPLICATION_NAME = "WaveWorx";

    private WaveDisplay display = new WaveDisplay();
    private GraphicalEditor graphicalEditor = new GraphicalEditor();
    private HarmonicEditor harmonicEditor = new HarmonicEditor();

    /**
     * Launches this JavaFX GUI
     */
    public static void startUserInterface() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(APPLICATION_NAME);
        primaryStage.setMinWidth(MINIMUM_WIDTH);
        primaryStage.setMinHeight(MINIMUM_HEIGHT);
        BorderPane mainLayout = new BorderPane();

        //Header
        HBox header = new HBox();
        mainLayout.setTop(header);
        header.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: 1cm;");
        Text headerLabel = new Text(APPLICATION_NAME);
        headerLabel.setFill(Color.WHITE);
        header.getChildren().add(headerLabel);

        //Content BorderPane
        BorderPane contentBorderPane = new BorderPane();
        mainLayout.setCenter(contentBorderPane);

        //Menu Bar
        HBox contentMenu = new HBox();
        contentBorderPane.setTop(contentMenu);
        contentMenu.setStyle("-fx-background-color: #224466; -fx-alignment: center-left; -fx-font-size: .5cm;");
        Button sinButton = new Button("Sine");
        Button squareButton = new Button("Square");
        Button sawButton = new Button("Saw");
        Button flatButton = new Button("Flat");
        Button randButton = new Button("Random");
        Button graphButton = new Button("Graphical Editor");
        Button harmonicButton = new Button("Harmonic Editor");
        sinButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateSine(1f)));
        squareButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generatePulse(.5f)));
        sawButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateSaw()));
        flatButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateFlat()));
        randButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateFromRandomHarmonics()));
        graphButton.setOnMouseClicked((event -> graphicalEditor.show()));
        harmonicButton.setOnMouseClicked(event -> harmonicEditor.show());
        contentMenu.getChildren().addAll(sinButton, squareButton, sawButton, flatButton, randButton, graphButton, harmonicButton);

        //WaveDisplay
        contentBorderPane.setCenter(display);
        ApplicationData.registerWaveTableListener(display);

        //Export Footer
        HBox contentFooter = new HBox();
        contentBorderPane.setBottom(contentFooter);
        contentFooter.setStyle("-fx-background-color: #224466; -fx-alignment: center-right; -fx-font-size: .5cm;");
        Button exportButton = new Button("EXPORT");
        Button listenButton = new Button("LISTEN");
        exportButton.setOnMouseClicked((e) -> exportAction(primaryStage));
        listenButton.setOnMouseClicked(event -> AudioEngine.threadedPlayback());
        contentFooter.getChildren().addAll(listenButton, exportButton);

        //Footer
        HBox footer = new HBox();
        mainLayout.setBottom(footer);
        footer.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: .5cm;");
        Text footerText = new Text("Copyright (c) 2018 Christopher Soderquist");
        footerText.setFill(Color.WHITE);
        footer.getChildren().add(footerText);

        Scene scene = new Scene(mainLayout, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> handleOnWindowClose());
        primaryStage.show();
        primaryStage.toFront();
    }

    /**
     * Actions to be be fired when the export button is actuated
     */
    private void exportAction(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Wave Files", ".wav", ".WAV", ".Wav"));
        fileChooser.setInitialFileName("sample.wav");
        fileChooser.setTitle("Export Sample");
        File destinationFile = fileChooser.showSaveDialog(stage);
        if (destinationFile != null)
            TableExporter.threadedExport(ApplicationData.getMasterWaveTable(), destinationFile);
    }

    /**
     * Handles the window closing and properly shuts down the application
     */
    private void handleOnWindowClose() {
        Platform.exit();
        WaveTableEditor.shutdown();
    }
}
