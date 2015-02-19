package org.thepholio.desktop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;

import java.awt.image.BufferedImage;

import static com.twelvemonkeys.image.ImageUtil.createResampled;
import static com.twelvemonkeys.image.ResampleOp.FILTER_LANCZOS;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 19, 2015
 */
public class ImageFittingService extends Service<BufferedImage> {

    private final ObjectProperty<BufferedImage> image = new SimpleObjectProperty<>(this, "image");

    public final ObjectProperty<BufferedImage> imageProperty() { return image; }

    public final void setImage(BufferedImage image) { this.image.set(image); }

    public final BufferedImage getImage() { return image.get(); }

    private final ObjectProperty<Bounds> bounds = new SimpleObjectProperty<>(this, "bounds");

    public final ObjectProperty<Bounds> boundsProperty() { return bounds; }

    public final void setBounds(Bounds bounds) { this.bounds.set(bounds); }

    public final Bounds getBounds() { return bounds.get(); }

    @Override
    protected Task<BufferedImage> createTask() {
        return new Task<BufferedImage>() {

            @Override
            protected BufferedImage call() throws Exception {
                BufferedImage image = getImage();
                if (image == null) {
                    return null;
                }

                Bounds bounds = getBounds();
                if (bounds == null) {
                    return null;
                }

                double iW = image.getWidth(), iH = image.getHeight(), bW = bounds.getWidth(), bH = bounds.getHeight();

                if (iW > bW || iH > bH) {
                    double ratio = iW / iH;

                    if (bW < bH) { // keep width, re-compute height
                        bH = bW / ratio;
                    } else { // keep height, re-compute width
                        bW = bH * ratio;
                    }

                    return createResampled(image, (int) bW, (int) bH, FILTER_LANCZOS);
                } else {
                    return image;
                }
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
