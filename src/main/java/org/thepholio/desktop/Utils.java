package org.thepholio.desktop;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static javafx.collections.FXCollections.emptyObservableList;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 21, 2015
 */
public class Utils {

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

        try {
            File samplesDir =
                new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            samplesDir = new File(samplesDir.isFile() ? samplesDir.getParentFile() : samplesDir, "samples");

            return observableArrayList(filter != null ? samplesDir.listFiles(filter) : samplesDir.listFiles());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return emptyObservableList();
        }
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
