package org.thepholio.image;

import org.thepholio.FormatNotSupported;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 06, 2015
 */
public class Images {

    public static final GraphicsConfiguration GRAPHICS_CONFIGURATION =
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

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

    public static BufferedImage load(Object imageInput) throws FormatNotSupported, IOException {
        try (ImageInputStream input = ImageIO.createImageInputStream(imageInput)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw imageInput instanceof File ? new FormatNotSupported((File) imageInput)
                                                 : new FormatNotSupported("Input image format not supported");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(input);

                BufferedImage image =
                    GRAPHICS_CONFIGURATION.createCompatibleImage(reader.getWidth(0), reader.getHeight(0));
                ImageTypeSpecifier imageSpecifier = ImageTypeSpecifier.createFromBufferedImageType(image.getType());

                boolean canReadDirectly = false;
                for (Iterator<ImageTypeSpecifier> readerSpecifiers = reader.getImageTypes(0);
                     readerSpecifiers.hasNext(); ) {
                    if (imageSpecifier.equals(readerSpecifiers.next())) {
                        canReadDirectly = true;
                        break;
                    }
                }

                if (canReadDirectly) {
                    ImageReadParam param = reader.getDefaultReadParam();
                    param.setDestination(image);
                    reader.read(0, param);
                } else {
                    Graphics graphics = image.getGraphics();
                    graphics.drawImage(reader.read(0), 0, 0, null);
                    graphics.dispose();
                }

                return image;
            } finally {
                reader.dispose();
            }
        }
    }
}
