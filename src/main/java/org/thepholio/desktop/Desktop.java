package org.thepholio.desktop;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.Double.MAX_VALUE;
import static javafx.scene.layout.Priority.ALWAYS;
import static org.thepholio.desktop.Config.SAMPLES;

public class Desktop extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        StackPane stack = new StackPane(imageView);
        ScrollPane scroll = new ScrollPane();
        scroll.setPannable(true);
        scroll.setContent(stack);
        StackPane.setAlignment(imageView, Pos.CENTER);

        Text statusBar = new Text("Found " + SAMPLES.size() + " samples.");
        statusBar.setId("status");
        statusBar.setTextAlignment(TextAlignment.RIGHT);

        ComboBox samplesCB = new ComboBox(SAMPLES);
        samplesCB.setMaxWidth(MAX_VALUE);
        if (SAMPLES.size() > 0) {
            samplesCB.getSelectionModel().select(0);
            try {
                imageView.setImage(new Image(
                    ((File) samplesCB.getSelectionModel().getSelectedItem()).toURI().toURL().toExternalForm()));
            } catch (Exception e) {
                statusBar.setText(e.getMessage());
            }
        }

        samplesCB.setOnAction(event -> {
            try {
                imageView.setImage(new Image(
                    ((File) samplesCB.getSelectionModel().getSelectedItem()).toURI().toURL().toExternalForm()));
            } catch (Exception e) {
                statusBar.setText(e.getMessage());
            }
        });

        GridPane root = new GridPane();
        root.setPadding(new Insets(4));
        root.setHgap(4);
        root.setVgap(4);
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(MAX_VALUE);

        root.add(samplesCB, 0, 0);
        GridPane.setHgrow(samplesCB, ALWAYS);

        root.add(scroll, 0, 1);
        GridPane.setHgrow(scroll, ALWAYS);
        GridPane.setVgrow(scroll, ALWAYS);

        root.add(statusBar, 0, 2);
        GridPane.setHgrow(statusBar, ALWAYS);
        GridPane.setHalignment(statusBar, HPos.RIGHT);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth() * 4 / 5, screenBounds.getHeight() * 4 / 5);
        scene.getStylesheets().add(getClass().getResource("/themes/default/main.css").toExternalForm());

        primaryStage.setTitle("ThePholio Desktop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
