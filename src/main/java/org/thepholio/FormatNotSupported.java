package org.thepholio;

import java.io.File;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 11, 2015
 */
public class FormatNotSupported extends Exception {

    private static final long serialVersionUID = 1020150211L;

    public FormatNotSupported(File file) {
        this("Format not supported for file" + (file == null ? "" : (" " + file.getAbsolutePath())));
    }

    public FormatNotSupported() { this("Format not supported"); }

    public FormatNotSupported(String message) { super(message); }

    public FormatNotSupported(Throwable cause) { super(cause); }

    public FormatNotSupported(String message, Throwable cause) { super(message, cause); }
}
