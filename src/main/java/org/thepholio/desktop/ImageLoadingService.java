package org.thepholio.desktop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.thepholio.Stopwatch;
import org.thepholio.image.Images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;

import static java.lang.String.format;
import static org.thepholio.desktop.Utils.hrSize;
import static org.thepholio.desktop.Utils.hrTime;
import static org.thepholio.image.Images.size;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 11, 2015
 */
public class ImageLoadingService extends Service<Image> {

    private final Stopwatch stopwatch = new Stopwatch();

    private ObjectProperty<Object> imageInput = new SimpleObjectProperty<>(this, "imageInput");

    public final ObjectProperty<Object> imageInputProperty() { return imageInput; }

    public final void setImageInput(Object input) { imageInput.set(input); }

    public final Object getImageInput() { return imageInput.get(); }

    private ObjectProperty<Image> image = new SimpleObjectProperty<>(this, "image");

    public final ObjectProperty<Image> imageProperty() { return image; }

    public final void setImage(Image image) { this.image.set(image); }

    public final Image getImage() { return image.get(); }

    @Override
    protected Task<Image> createTask() {
        return new Task<Image>() {

            @Override
            protected void failed() {
                Throwable throwable = getException();

                if (throwable != null && !(throwable instanceof InterruptedException)) {
                    updateMessage(throwable.getMessage());
                }
            }

            @Override
            protected void scheduled() {
                setImage(null);
                updateMessage("");
            }

            @Override
            protected Image call() throws Exception {
                Object input = getImageInput();
                if (input == null) {
                    return null;
                }

                stopwatch.reset().start();
                BufferedImage image = Images.load(input);
                long millisLoaded = stopwatch.stop().elapsed();

                Image fxImage = SwingFXUtils.toFXImage(image, null);
                setImage(fxImage);

                String sizeOnDisk = input instanceof File ? hrSize(((File) input).length()) + " on disk  |  " : "";
                updateMessage(format(Locale.ENGLISH, "%d x %d  |  %s%s in memory  |  loaded in %s  ", image.getWidth(),
                                     image.getHeight(), sizeOnDisk, hrSize(size(image)), hrTime(millisLoaded)));

                return fxImage;
            }
        };
    }
}
