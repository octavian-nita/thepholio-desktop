package org.thepholio.desktop;

import javafx.embed.swing.SwingNode;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 27, 2015
 */
public class SwingImageNode extends SwingNode {

    public static final Dimension NO_SIZE = new Dimension();

    private final JComponent imageView = new JComponent() {

        {
            setOpaque(true);
        }

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
    };

    private volatile BufferedImage image;

    public void setImage(BufferedImage image) {
        this.image = image;
        setContent(imageView); // no need to re-create the Swing component, just force re-validate / repaint
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
