package org.thepholio.desktop;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.Double.MAX_VALUE;
import static javafx.scene.layout.Priority.ALWAYS;
import static org.thepholio.desktop.Utils.SAMPLES;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 20, 2015
 */
public class Desktop extends Application {

    private final ImageLoadingService imageLoadingService = new ImageLoadingService();

    private ImageView imageView = new ImageView();

    private ComboBox<File> samplesCB = new ComboBox<>();

    private Text statusBar = new Text();

    public Desktop() {
        imageView.setCache(true);

        samplesCB.setItems(SAMPLES);
        samplesCB.setMaxWidth(MAX_VALUE);
        samplesCB.setOnAction(event -> displaySelectedImage());

        statusBar.setId("status");
        statusBar.textProperty().bind(imageLoadingService.messageProperty());
        imageLoadingService.setOnSucceeded(event -> imageView.setImage((Image) event.getSource().getValue()));
    }

    private void displaySelectedImage() {

        @SuppressWarnings("unchecked") SelectionModel<File> selection = samplesCB.getSelectionModel();
        if (!selection.isEmpty()) {
            imageView.setImage(null);
            imageLoadingService.setImageInput(selection.getSelectedItem());
            imageLoadingService.restart();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane root = new GridPane();
        root.setPadding(new Insets(4));
        root.setHgap(4);
        root.setVgap(4);
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(MAX_VALUE);

        root.add(samplesCB, 0, 0);
        GridPane.setHgrow(samplesCB, ALWAYS);

        root.add(statusBar, 0, 2);
        GridPane.setHgrow(statusBar, ALWAYS);
        GridPane.setHalignment(statusBar, HPos.RIGHT);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth() * 3 / 4, screenBounds.getHeight() * 3 / 4);
        scene.getStylesheets().add(getClass().getResource("/themes/default/main.css").toExternalForm());

        primaryStage.setTitle("ThePholio Desktop");
        primaryStage.setScene(scene);
        primaryStage.show();

        samplesCB.getSelectionModel().select(0);

        ScrollPane scroll = new ScrollPane() {

            public void requestFocus() {}
        };
        scroll.setPannable(true);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setContent(new StackPane(imageView)); // allows content aligning and also centers it by default!

        root.add(scroll, 0, 1);
        GridPane.setHgrow(scroll, ALWAYS);
        GridPane.setVgrow(scroll, ALWAYS);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
