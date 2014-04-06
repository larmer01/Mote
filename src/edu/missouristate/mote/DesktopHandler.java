package edu.missouristate.mote;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Support for working with the desktop.
 */
public final class DesktopHandler {

    // *************************************************************************
    // PRIVATE CONSTANTS
    // *************************************************************************

    /** Number of bytes in the file read buffer. */
    private static final int READ_BYTES = 4096;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a DesktopHandler.
     */
    private DesktopHandler() {
    }

    // *************************************************************************
    // PUBLIC STATIC METHODS
    // *************************************************************************
    /**
     * Return the extension for the specified file name.
     *
     * @param filename file name
     * @return extension
     */
    private static String getExtension(final String filename) {
        final String[] parsed = filename.split("\\.(?=[^\\.]+$)");
        if (parsed == null || parsed.length != 2) {
            return "";
        } else {
            return parsed[1];
        }
    }

    /**
     * Extract the specified file from this application's JAR and copy it to a
     * temporary location in the local file system. The file will be removed
     * when the JVM exits.
     *
     * @param filename file name to copy
     * @return temporary File object
     * @exception IOException if there is a problem reading/writing the file
     */
    private static File extractFile(final String filename) throws IOException {
        // Create the temporary file
        final File result = File.createTempFile("Mote", "."
                + getExtension(filename));
        result.deleteOnExit();
        // Extract
        try (
            InputStream in = DesktopHandler.class.getResourceAsStream(filename);
            OutputStream out = new FileOutputStream(result);
            ) {
            // Input stream (from the JAR)
            if (in == null) {
                throw new IOException();
            }
            // Copy
            int readBytes;
            final byte[] buffer = new byte[READ_BYTES];
            while ((readBytes = in.read(buffer)) > 0) {
                out.write(buffer, 0, readBytes);
            }
        }
        return result;
    }

    // *************************************************************************
    // PUBLIC STATIC METHODS
    // *************************************************************************
    /**
     * Open the specified file in the default desktop application.
     *
     * @param filename file name
     */
    public static void openFile(final String filename) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }
        final Desktop desktop = Desktop.getDesktop();
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            try {
                final File file = extractFile(filename);
                if (file.exists()) {
                    desktop.open(file);
                }
            } catch (IOException ex) {
                Logger.getLogger(DesktopHandler.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Open the specified web page in the default desktop browser.
     *
     * @param url URL as a string
     */
    public static void openWebPage(final String url) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }
        final Desktop desktop = Desktop.getDesktop();
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL(url).toURI());
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(DesktopHandler.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    }
}
