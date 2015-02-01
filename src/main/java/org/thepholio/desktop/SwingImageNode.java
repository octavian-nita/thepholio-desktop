package org.thepholio.desktop;

import javafx.embed.swing.SwingNode;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 27, 2015
 */
public class SwingImageNode extends SwingNode {

    private static final Dimension NO_SIZE = new Dimension();

    private volatile BufferedImage image;

    private BufferedImage optimize(BufferedImage image) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        boolean isTransparent = image.getColorModel().hasAlpha();

        BufferedImage img2 = gc.createCompatibleImage(image.getWidth(), image.getHeight(),
                                                      isTransparent ? Transparency.BITMASK : Transparency.OPAQUE);
        Graphics2D g = img2.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return img2;
    }

    public void setImage(BufferedImage image) {
        this.image = optimize(image);
        setContent(new JComponent() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, null);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return image != null ? new Dimension(image.getWidth(), image.getHeight()) : NO_SIZE;
            }

            @Override
            public Dimension getMinimumSize() { return getPreferredSize(); }

            @Override
            public Dimension getMaximumSize() { return getPreferredSize(); }
        });
    }

    @Override
    public double prefWidth(double height) { return image != null ? image.getWidth() : 0; }

    @Override
    public double prefHeight(double width) { return image != null ? image.getHeight() : 0; }

    @Override
    public double minWidth(double height) { return prefWidth(height); }

    @Override
    public double maxWidth(double height) { return prefWidth(height); }

    @Override
    public double minHeight(double width) { return prefHeight(width); }

    @Override
    public double maxHeight(double width) { return prefHeight(width); }
}
