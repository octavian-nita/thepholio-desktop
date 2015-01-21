package org.thepholio.desktop;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static org.thepholio.desktop.Config.SAMPLES;

public class Desktop extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane rootPane = new GridPane();
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setHgap(4);
        rootPane.setVgap(4);
        rootPane.setPadding(new Insets(4, 4, 4, 4));

        //rootPane.setGridLinesVisible(true);

        ComboBox samplesCB = new ComboBox(SAMPLES);
        samplesCB.setMaxWidth(Double.MAX_VALUE);
        if (SAMPLES.size() > 0) {
            samplesCB.getSelectionModel().select(0);
        }
        rootPane.add(samplesCB, 0, 0);

        Text statusBar = new Text("Loaded " + SAMPLES.size() + " samples.");
        rootPane.add(statusBar, 0, 1);

        Scene scene = new Scene(rootPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/themes/default/main.css").toExternalForm());

        primaryStage.setTitle("ThePholio Desktop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
