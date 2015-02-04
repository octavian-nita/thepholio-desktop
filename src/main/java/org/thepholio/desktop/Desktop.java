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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import static java.awt.color.ColorSpace.CS_sRGB;
import static java.lang.Double.MAX_VALUE;
import static java.lang.String.format;
import static javafx.scene.layout.Priority.ALWAYS;
import static org.thepholio.desktop.Utils.EMPTY_IMAGE;
import static org.thepholio.desktop.Utils.SAMPLES;
import static org.thepholio.desktop.Utils.hrSize;
import static org.thepholio.util.Stopwatch.SW;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 20, 2015
 */
public class Desktop extends Application {

    private SwingImageNode imageNode = new SwingImageNode();

    private ComboBox samplesCB = new ComboBox();

    private Text statusBar = new Text();

    @SuppressWarnings("unchecked")
    public Desktop() {
        samplesCB.setItems(SAMPLES);
        samplesCB.setMaxWidth(MAX_VALUE);
        samplesCB.setOnAction(event -> displaySelectedImage());

        statusBar.setId("status");
    }

    private void displaySelectedImage() {
        @SuppressWarnings("unchecked") SelectionModel<File> selection = samplesCB.getSelectionModel();
        if (!selection.isEmpty()) {
            File file = selection.getSelectedItem();

            BufferedImage image;
            try {
                SW.start();
                image = loadImage(file);
                long millisLoaded = SW.stop().elapsed();

                DataBuffer buffer = image.getRaster().getDataBuffer();
                int bytes = buffer.getSize() * DataBuffer.getDataTypeSize(buffer.getDataType()) / 8;

                imageNode.setImage(image);
                statusBar.setText(
                    format(Locale.ENGLISH, "%d x %d  |  %s in memory  |  %s on disk  |  loaded in %d ms  ",
                           image.getWidth(), image.getHeight(), hrSize(bytes), hrSize(file.length()), millisLoaded));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                statusBar.setText("Error loading image!");
                imageNode.setImage(EMPTY_IMAGE);
            }
        }
    }

    private BufferedImage loadImage(File imageFile) throws Exception {
        ImageInputStream input = null;
        try {
            input = ImageIO.createImageInputStream(imageFile);

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(input);
            if (!imageReaders.hasNext()) {
                throw new IllegalArgumentException("No reader for " + imageFile);
            }

            ImageReader reader = imageReaders.next();

            try {
                reader.setInput(input);
                BufferedImage image =
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
                                       .createCompatibleImage(reader.getWidth(0), reader.getHeight(0));

                return new ColorConvertOp(ColorSpace.getInstance(CS_sRGB), null).filter(reader.read(0), image);
            } finally {
                reader.dispose();
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
