package org.thepholio.desktop.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import org.thepholio.desktop.Utils;
import static java.lang.String.format;
import static org.thepholio.desktop.Utils.hrSize;
import static org.thepholio.util.Stopwatch.SW;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 20, 2015
 */
public class Desktop extends javax.swing.JFrame {

    public Desktop() {
        initComponents();
        initOwnComponents();
    }

    protected void initOwnComponents() {
        scroll.getViewport().add(imageView);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width * 3 / 4, screenSize.height * 3 / 4);
        setLocationRelativeTo(null);
    }

    /**
     * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imageView = new org.thepholio.desktop.swing.ImageView();
        samplesCB = new javax.swing.JComboBox();
        statusBar = new javax.swing.JTextField();
        scroll = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ThePholio Desktop");

        samplesCB.setModel(new DefaultComboBoxModel(Utils.getSamples2()));
        samplesCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                samplesCBActionPerformed(evt);
            }
        });

        statusBar.setEditable(false);
        statusBar.setBackground(javax.swing.UIManager.getDefaults().getColor("window"));
        statusBar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        statusBar.setBorder(null);

        scroll.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBar, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(samplesCB, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(samplesCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void samplesCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_samplesCBActionPerformed
        File file = (File) samplesCB.getSelectedItem();
        if (file != null) {
            try {
                SW.start();
                BufferedImage image = ImageIO.read(file);
                long millisLoaded = SW.stop().elapsed();

                DataBuffer buffer = image.getRaster().getDataBuffer();
                int bytes = buffer.getSize() * DataBuffer.getDataTypeSize(buffer.getDataType()) / 8;

                imageView.setImage(image);

                statusBar.setText(
                    format(Locale.ENGLISH, "%d x %d  |  %s in memory  |  %s on disk  |  loaded in %d ms  ",
                           image.getWidth(), image.getHeight(), hrSize(bytes), hrSize(file.length()), millisLoaded));
            } catch (IOException ex) {
                statusBar.setText(ex.getMessage());
            }
        }
    }//GEN-LAST:event_samplesCBActionPerformed

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Desktop.class.getName()).log(java.util.logging.Level.WARNING, null, ex);
        }
        //</editor-fold>

        SwingUtilities.invokeLater(() -> new Desktop().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    org.thepholio.desktop.swing.ImageView imageView;
    protected javax.swing.JComboBox samplesCB;
    javax.swing.JScrollPane scroll;
    protected javax.swing.JTextField statusBar;
    // End of variables declaration//GEN-END:variables
}