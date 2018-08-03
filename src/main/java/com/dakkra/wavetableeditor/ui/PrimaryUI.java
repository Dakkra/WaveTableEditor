package com.dakkra.wavetableeditor.ui;

import com.dakkra.wavetableeditor.ApplicationData;
import com.dakkra.wavetableeditor.io.TableExporter;
import javafx.application.Application;
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

    private WaveDisplay display = new WaveDisplay();

    /**
     * Launches this JavaFX GUI
     */
    public static void startUserInterface() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Wave Table Editor");
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(500);
        BorderPane mainLayout = new BorderPane();

        //Header
        HBox header = new HBox();
        mainLayout.setTop(header);
        header.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: 1cm;");
        Text headerLabel = new Text("Wave Table Editor");
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
        sinButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateSine(1f)));
        squareButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generatePulse(.5f)));
        sawButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateSaw()));
        flatButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateFlat()));
        randButton.setOnMouseClicked((event -> ApplicationData.getMasterWaveTable().generateFromRandomHarmonics()));
        contentMenu.getChildren().add(sinButton);
        contentMenu.getChildren().add(squareButton);
        contentMenu.getChildren().add(sawButton);
        contentMenu.getChildren().add(flatButton);
        contentMenu.getChildren().add(randButton);

        //WaveDisplay
        contentBorderPane.setCenter(display);
        ApplicationData.registerWaveTableListener(display);

        //Export Footer
        HBox contentFooter = new HBox();
        contentBorderPane.setBottom(contentFooter);
        contentFooter.setStyle("-fx-background-color: #224466; -fx-alignment: center-right; -fx-font-size: .5cm;");
        Button exportButton = new Button("EXPORT");
        exportButton.setOnMouseClicked((e) -> exportAction(primaryStage));
        contentFooter.getChildren().add(exportButton);

        //Footer
        HBox footer = new HBox();
        mainLayout.setBottom(footer);
        footer.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: .5cm;");
        Text footerText = new Text("Copyright (c) 2018 Christopher Soderquist");
        footerText.setFill(Color.WHITE);
        footer.getChildren().add(footerText);

        Scene scene = new Scene(mainLayout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Actions to be be fired when the export button is actuated
     */
    private void exportAction(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        File destinationFile = fileChooser.showSaveDialog(stage);
        TableExporter.threadedExport(ApplicationData.getMasterWaveTable(), destinationFile);
    }
}
