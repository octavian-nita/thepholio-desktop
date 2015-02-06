package org.thepholio.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static org.thepholio.util.Utils.GRAPHICS_CONFIGURATION;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 06, 2015
 */
public class Images {

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

                // Prepare / allocate an image:
                BufferedImage image =
                    GRAPHICS_CONFIGURATION.createCompatibleImage(reader.getWidth(0), reader.getHeight(0));

                ImageReadParam param = reader.getDefaultReadParam();
                param.setDestination(image);
                param.setDestinationType(ImageTypeSpecifier.createFromBufferedImageType(image.getType()));

                return reader.read(0, param);

                //new ColorConvertOp(ColorSpace.getInstance(CS_sRGB), null).filter(image, image);
                //return makeCompatible(image);
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

    public static BufferedImage optimize(BufferedImage img) throws IOException {
        // Allocate the new image
        BufferedImage dstImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Check if the ColorSpace is RGB and the TransferType is BYTE.
        // Otherwise this fast method does not work as expected
        ColorModel cm = img.getColorModel();
        if (cm.getColorSpace().getType() == ColorSpace.TYPE_RGB &&
            img.getRaster().getTransferType() == DataBuffer.TYPE_BYTE) {
            //Allocate arrays
            int len = img.getWidth() * img.getHeight();
            byte[] src = new byte[len * 3];
            int[] dst = new int[len];

            // Read the src image data into the array
            img.getRaster().getDataElements(0, 0, img.getWidth(), img.getHeight(), src);

            // Convert to INT_RGB
            int j = 0;
            for (int i = 0; i < len; i++) {
                dst[i] = (((int) src[j++] & 0xFF) << 16) |
                         (((int) src[j++] & 0xFF) << 8) |
                         (((int) src[j++] & 0xFF));
            }

            // Set the dst image data
            dstImage.getRaster().setDataElements(0, 0, img.getWidth(), img.getHeight(), dst);

            return dstImage;
        }

        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(img, dstImage);

        return dstImage;
    }
}
