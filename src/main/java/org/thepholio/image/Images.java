package org.thepholio.image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 06, 2015
 */
public class Images {

    public static final GraphicsConfiguration GRAPHICS_CONFIGURATION =
        getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    public static final BufferedImage EMPTY_IMAGE = GRAPHICS_CONFIGURATION.createCompatibleImage(1, 1);

    public static final ColorConvertOp COLOR_CONVERTER = new ColorConvertOp(null);

    /**
     * @return the size (in bytes) the provided <code>image</code> occupies in memory
     */
    public static int size(BufferedImage image) {
        if (image == null) {
            return 0;
        }
        DataBuffer buffer = image.getRaster().getDataBuffer();
        return buffer.getSize() * (DataBuffer.getDataTypeSize(buffer.getDataType()) / 8);
    }

    public static BufferedImage load(File imageFile) throws IOException {
        ImageInputStream input = null;
        try {
            input = ImageIO.createImageInputStream(imageFile);

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(input);
            if (!imageReaders.hasNext()) {
                // TODO: clean this to throw either a better exception type or log or...
                throw new RuntimeException("No reader for " + imageFile);
            }

            ImageReader reader = imageReaders.next();
            try {
                reader.setInput(input);

                // Prepare / allocate an image to be displayed:
                BufferedImage image =
                    GRAPHICS_CONFIGURATION.createCompatibleImage(reader.getWidth(0), reader.getHeight(0));
                int imageType = image.getType();

                boolean canReadDirectly = false;
                for (Iterator<ImageTypeSpecifier> readerImageTypes = reader.getImageTypes(0);
                     readerImageTypes.hasNext(); ) {
                    if (imageType == readerImageTypes.next().getBufferedImageType()) {
                        canReadDirectly = true;
                        break;
                    }
                }

                if (canReadDirectly) {
                    ImageReadParam param = reader.getDefaultReadParam();
                    param.setDestination(image);
                    reader.read(0, param);
                } else {
                    BufferedImage imageToConvert = reader.read(0);
                    COLOR_CONVERTER.filter(imageToConvert, image);
                    imageToConvert.flush();
                }

                return image;
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
}
