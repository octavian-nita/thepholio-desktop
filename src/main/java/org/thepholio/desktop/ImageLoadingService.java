package org.thepholio.desktop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.thepholio.Stopwatch;
import org.thepholio.image.Images;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.thepholio.desktop.Utils.hrSize;
import static org.thepholio.desktop.Utils.hrTime;
import static org.thepholio.image.Images.size;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 11, 2015
 */
public class ImageLoadingService extends Service<BufferedImage> {

    private final Stopwatch stopwatch = new Stopwatch();

    private final Runtime rt = Runtime.getRuntime();

    private final ObjectProperty<Object> imageInput = new SimpleObjectProperty<>(this, "imageInput");

    public final ObjectProperty<Object> imageInputProperty() { return imageInput; }

    public final void setImageInput(Object input) { imageInput.set(input); }

    public final Object getImageInput() { return imageInput.get(); }

    @Override
    protected Task<BufferedImage> createTask() {
        return new Task<BufferedImage>() {

            @Override
            protected void scheduled() {
                Object input = getImageInput();
                updateMessage((input != null && input instanceof File) ? "Loading image from file " +
                                                                         ((File) input).getAbsolutePath() + "..." : "");
            }

            @Override
            protected BufferedImage call() throws Exception {
                Object input = getImageInput();
                if (input == null) {
                    return null;
                }

                stopwatch.reset().start();
                BufferedImage image = Images.load(input);
                long millisLoaded = stopwatch.stop().elapsed();

                String sizeOnDisk = input instanceof File ? hrSize(((File) input).length()) + " on disk  |  " : "";
                updateMessage(
                    new StringBuilder().append(image.getWidth()).append(" x ").append(image.getHeight()).append("  |  ")
                                       .append(sizeOnDisk).append(hrSize(size(image)))
                                       .append(" in memory  |  loaded in ").append(hrTime(millisLoaded)).append("  |  ")
                                       .append(hrSize(rt.maxMemory() - (rt.totalMemory() - rt.freeMemory())))
                                       .append("  memory available  ").toString());

                System.gc(); // hint a garbage collection...

                return image;
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                updateMessage(
                    throwable != null && !(throwable instanceof InterruptedException) ? throwable.getMessage() : "");
            }
        };
    }
}
