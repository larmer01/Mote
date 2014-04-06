package edu.missouristate.mote;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Extension to a JPanel that allows for a background image to be set.
 */
public final class ImagePanel extends JPanel {

    // *************************************************************************
    // PRIVATE FIELDS
    // *************************************************************************

    /** The background image. */
    private transient BufferedImage image;

    // *************************************************************************
    // PROTECTED METHODS
    // *************************************************************************

    /**
     * Paint each of the components in this container.
     *
     * @param graphics graphics context
     */
    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponents(graphics);
        if (image != null) {
            graphics.drawImage(image, 0, 0, null);
        }
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************

    /**
     * Return the background image.
     *
     * @return background image
     */
    public BufferedImage getBackgroundImage() {
        return image;
    }

    /**
     * Set the background image. If the image cannot be loaded, the background
     * will default to the background color.
     *
     * @param imagePath path to the image file
     */
    public void setBackgroundImage(final String imagePath) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(
                        Level.WARNING, null, ex);
        }
    }
}
