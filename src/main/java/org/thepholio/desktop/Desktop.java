package org.thepholio.desktop;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static java.lang.Double.MAX_VALUE;
import static javafx.scene.layout.Priority.ALWAYS;
import static org.thepholio.desktop.Config.SAMPLES;

public class Desktop extends Application {

    private ImageView imageView = new ImageView();

    private ComboBox samplesCB = new ComboBox();

    private Text statusBar = new Text();

    public Desktop() {
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        samplesCB.setItems(SAMPLES);
        samplesCB.setMaxWidth(MAX_VALUE);
        samplesCB.setOnAction(event -> displaySelectedImage());

        statusBar.setId("status");
    }

    private void displaySelectedImage() {
        @SuppressWarnings("unchecked") SelectionModel<File> selectionModel = samplesCB.getSelectionModel();
        if (!selectionModel.isEmpty()) {
            try {
                imageView.setImage(getImage((selectionModel.getSelectedItem()).toURI().toURL()));
            } catch (Exception e) {
                statusBar.setText(e.getMessage());
            }
        }
    }

    private Image getImage(URL url) throws IOException {
        return SwingFXUtils.toFXImage(ImageIO.read(url), null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ScrollPane scroll = new ScrollPane() {

            public void requestFocus() {}
        };
        scroll.setPannable(true);
        scroll.setFitToWidth(true);
        scroll.setContent(new StackPane(imageView)); // allows content aligning and also centers it by default!

        statusBar.setText("Found " + SAMPLES.size() + " samples.");

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
        Scene scene = new Scene(root, screenBounds.getWidth() * 3 / 4, screenBounds.getHeight() * 3 / 4);
        scene.getStylesheets().add(getClass().getResource("/themes/default/main.css").toExternalForm());

        primaryStage.setTitle("ThePholio Desktop");
        primaryStage.setScene(scene);
        primaryStage.show();

        samplesCB.getSelectionModel().select(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
