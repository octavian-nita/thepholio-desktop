package org.thepholio.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 21, 2015
 */
public class Config {

    public static final ObservableList<File> SAMPLES = getSamples();

    public static ObservableList<File> getSamples() {
        return FXCollections.observableArrayList(new File(Config.class.getResource("/samples").getPath()).listFiles());
    }
}
