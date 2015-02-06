package org.thepholio.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 21, 2015
 */
public class Utils {

    public static final GraphicsConfiguration GRAPHICS_CONFIGURATION =
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    public static final BufferedImage EMPTY_IMAGE = GRAPHICS_CONFIGURATION.createCompatibleImage(1, 1);

    public static final Dimension NO_SIZE = new Dimension();

    public static final ObservableList<File> SAMPLES = getSamples();

    public static ObservableList<File> getSamples(String... types) {
        FileFilter filter = null;

        if (types != null && types.length > 0) {
            List<String> extensions = Arrays.asList(types);
            Collections.sort(extensions); // for faster lookup

            filter = pathname -> {
                String ext = pathname.getAbsolutePath();
                int extPos = ext.lastIndexOf('.');
                return extPos >= 0 && extensions.contains(ext.substring(extPos + 1));
            };
        }

        return FXCollections.observableArrayList(
            filter != null ? new File(Utils.class.getResource("/samples").getPath()).listFiles(filter)
                           : new File(Utils.class.getResource("/samples").getPath()).listFiles());
    }

    public static String hrSize(double numBytes) {
        String suffix = "KB";
        numBytes /= 1024.;
        if (Double.compare(numBytes, 1024.) >= 0) {
            suffix = "MB";
            numBytes /= 1024.;
        }
        return format("%.2f %s", numBytes, suffix);
    }

    public static String hrTime(double numMillis) {
        String suffix = "ms";
        if (Double.compare(numMillis, 1000.) >= 0) {
            suffix = "s";
            numMillis /= 1000.;
        }
        return format("%.2f %s", numMillis, suffix);
    }
}
