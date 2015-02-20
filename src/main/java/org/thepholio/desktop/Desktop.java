package org.thepholio.desktop;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.Double.MAX_VALUE;
import static java.lang.System.getenv;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import static javafx.scene.layout.Priority.ALWAYS;
import static org.thepholio.desktop.Utils.RESAMPLINGS;
import static org.thepholio.desktop.Utils.SAMPLES;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 20, 2015
 */
public class Desktop extends Application {

    private final ImageLoadingService imageLoading = new ImageLoadingService();

    private final ImageFittingService imageFitting = new ImageFittingService();

    private ImageView imageView = new ImageView();

    private ComboBox<File> samplesCB = new ComboBox<>();

    private Text statusBar = new Text();

    public Desktop() {
        imageView.setCache(true);

        samplesCB.setItems(SAMPLES);
        samplesCB.setMaxWidth(MAX_VALUE);
        samplesCB.setOnAction(event -> displaySelectedImage());

        statusBar.setId("status");
        statusBar.textProperty().bind(imageLoading.messageProperty());

        imageLoading.setOnSucceeded(event -> {
            imageFitting.setImage((BufferedImage) event.getSource().getValue());
            imageFitting.restart();
        });
        imageFitting.setOnSucceeded(event -> {
            BufferedImage image = (BufferedImage) event.getSource().getValue();
            imageView.setImage(image == null ? null : toFXImage(image, null));
        });
    }

    private void displaySelectedImage() {
        imageView.setImage(null);
        imageFitting.setImage(null);

        @SuppressWarnings("unchecked") SelectionModel<File> selection = samplesCB.getSelectionModel();
        if (selection.isEmpty()) {
            return;
        }

        imageLoading.setImageInput(selection.getSelectedItem());
        imageLoading.restart();
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
        scroll.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(oldValue)) {
                return;
            }

            imageFitting.setBounds(newValue);
            imageFitting.restart();
        });

        final ToggleGroup toggleGroup = new ToggleGroup();

        ContextMenu imageContextMenu = new ContextMenu();
        ObservableList<MenuItem> items = imageContextMenu.getItems();
        for (String label : RESAMPLINGS.keySet()) {
            RadioMenuItem item = new RadioMenuItem(label);
            item.setToggleGroup(toggleGroup);

            if (imageFitting.getFilter().equals(RESAMPLINGS.get(label))) {
                item.setSelected(true);
            }

            item.setOnAction(event -> {
                imageFitting.setFilter(RESAMPLINGS.get(item.getText()));
                imageFitting.restart();
            });
            items.add(item);
        }
        scroll.setContextMenu(imageContextMenu);

        // The stack pane allows content aligning and also centers it by default!
        scroll.setContent(new StackPane(imageView));

        root.add(scroll, 0, 1);
        GridPane.setHgrow(scroll, ALWAYS);
        GridPane.setVgrow(scroll, ALWAYS);
    }

    public static void main(String[] args) {

        // Set up logging to the console:
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(getenv("DEBUG") != null ? Level.FINEST : Level.INFO);
        rootLogger.addHandler(new ConsoleHandler());

        // Set up logging to a file as well:
        Handler fileHandler;
        try {
            fileHandler = new FileHandler("thepholio-desktop.log"); // a file in the current working directory
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        ImageIO.setUseCache(false);

        // Launch the JavaFX app:
        launch(args);
    }
}
