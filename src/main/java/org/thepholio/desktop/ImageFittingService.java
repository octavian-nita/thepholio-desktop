package org.thepholio.desktop;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;

import java.awt.image.BufferedImage;

import static com.twelvemonkeys.image.ImageUtil.createResampled;
import static com.twelvemonkeys.image.ResampleOp.FILTER_BLACKMAN_SINC;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 19, 2015
 */
public class ImageFittingService extends Service<BufferedImage> {

    //
    // The source image property (will not be modified):
    //

    private final ObjectProperty<BufferedImage> image = new SimpleObjectProperty<>(this, "image");

    public final ObjectProperty<BufferedImage> imageProperty() { return image; }

    public final void setImage(BufferedImage image) { this.image.set(image); }

    public final BufferedImage getImage() { return image.get(); }

    //
    // The target bounds property:
    //

    private final ObjectProperty<Bounds> bounds = new SimpleObjectProperty<>(this, "bounds");

    public final ObjectProperty<Bounds> boundsProperty() { return bounds; }

    public final void setBounds(Bounds bounds) { this.bounds.set(bounds); }

    public final Bounds getBounds() { return bounds.get(); }

    //
    // The resampling filter property:
    //

    private final IntegerProperty filter = new SimpleIntegerProperty(this, "filter", FILTER_BLACKMAN_SINC);

    public final IntegerProperty filterProperty() { return filter; }

    public final void setFilter(Integer filter) { this.filter.set(filter); }

    public final Integer getFilter() { return filter.get(); }

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

                if (bH < iH || bW < iW) {
                    double ratio = iW / iH;

                    if (bH > bW) {
                        bH = bW / ratio; // keep width, re-compute height
                    } else {
                        bW = bH * ratio; // keep height, re-compute width
                    }

                    return createResampled(image, (int) bW, (int) bH, getFilter().intValue());
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
