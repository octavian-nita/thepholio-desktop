package org.thepholio.desktop;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.Double.MAX_VALUE;
import static javafx.scene.layout.Priority.ALWAYS;
import static org.thepholio.desktop.Config.SAMPLES;

public class Desktop extends Application {

    public static class SwingImageNode extends SwingNode {

        private BufferedImage image;

        private final JComponent ui = new JComponent() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, null);
                }
            }
        };

        public SwingImageNode() { setContent(ui); }

        public void setImage(BufferedImage image) {
            this.image = image;
            SwingUtilities.invokeLater(() -> ui.repaint());
        }

        @Override
        public boolean isResizable() { return false; }

        @Override
        public double minWidth(double height) { return image != null ? image.getWidth() : 0; }

        @Override
        public double maxWidth(double height) { return image != null ? image.getWidth() : 0; }

        @Override
        public double prefWidth(double height) { return image != null ? image.getWidth() : 0; }

        @Override
        public double minHeight(double width) { return image != null ? image.getHeight() : 0; }

        @Override
        public double maxHeight(double width) { return image != null ? image.getHeight() : 0; }

        @Override
        public double prefHeight(double width) { return image != null ? image.getHeight() : 0; }
    }

    private SwingImageNode imageNode = new SwingImageNode();

    private ComboBox samplesCB = new ComboBox();

    private Text statusBar = new Text();

    private static final BufferedImage EMPTY_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    @SuppressWarnings("unchecked")
    public Desktop() {
        // imageNode.setPreserveRatio(true);
        // imageNode.setSmooth(true);
        imageNode.setCache(true);
        // imageNode.setContent(...);

        samplesCB.setItems(SAMPLES);
        samplesCB.setMaxWidth(MAX_VALUE);
        samplesCB.setOnAction(event -> displaySelectedImage());

        statusBar.setId("status");
    }

    private void displaySelectedImage() {
        @SuppressWarnings("unchecked") SelectionModel<File> selection = samplesCB.getSelectionModel();
        if (!selection.isEmpty()) {
            imageNode.setImage(loadSelectedImage(selection));
        }
    }

    private BufferedImage loadSelectedImage(SelectionModel<File> selection) {
        try {
            return ImageIO.read((selection.getSelectedItem()).toURI().toURL());
        } catch (Exception e) {
            statusBar.setText(e.getMessage());
            return EMPTY_IMAGE;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ScrollPane scroll = new ScrollPane() {

            public void requestFocus() {}
        };
        scroll.setPannable(true);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setContent(new StackPane(imageNode)); // allows content aligning and also centers it by default!

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
