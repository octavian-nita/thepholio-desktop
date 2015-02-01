package org.thepholio.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.String.format;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 21, 2015
 */
public class Utils {

    public static final BufferedImage EMPTY_IMAGE =
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
        .createCompatibleImage(1, 1);

    public static final Dimension NO_SIZE = new Dimension();

    public static final ObservableList<File> SAMPLES = getSamples();

    public static ObservableList<File> getSamples() {
        return FXCollections.observableArrayList(new File(Utils.class.getResource("/samples").getPath()).listFiles());
    }

    public static File[] getSamples2() {
        return new File(Utils.class.getResource("/samples").getPath()).listFiles();
    }

    public static String hrSize(double numBytes) {
        String kbmb = "KB";
        double size = numBytes / 1024.;
        if (Double.compare(size, 1024.) >= 0) {
            size /= 1024.;
            kbmb = "MB";
        }
        return format("%.2f %s", size, kbmb);
    }
}
