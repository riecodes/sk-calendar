package com.mycompany.sk.calendar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Enhanced JFileChooser with image preview functionality
 */
public class ImagePreviewFileChooser extends JFileChooser {
    
    private ImagePreviewPanel previewPanel;
    
    public ImagePreviewFileChooser() {
        super();
        
        // Set up file filter for images
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif", "bmp");
        setFileFilter(imageFilter);
        
        // Create and add preview panel
        previewPanel = new ImagePreviewPanel();
        setAccessory(previewPanel);
        
        // Listen for selection changes
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
                    File selectedFile = (File) evt.getNewValue();
                    previewPanel.setImageFile(selectedFile);
                }
            }
        });
        
        // Set dialog title
        setDialogTitle("Select Profile Photo");
        setApproveButtonText("Select Image");
    }
    
    /**
     * Preview panel that shows image thumbnails
     */
    private class ImagePreviewPanel extends JPanel {
        private JLabel imageLabel;
        private JLabel infoLabel;
        private File currentFile;
        
        public ImagePreviewPanel() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(200, 300));
            setBorder(BorderFactory.createTitledBorder("Preview"));
            
            // Image display label
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
            imageLabel.setPreferredSize(new Dimension(180, 200));
            imageLabel.setBorder(BorderFactory.createLoweredBevelBorder());
            
            // Info label
            infoLabel = new JLabel("<html><center>Select an image<br>to preview</center></html>");
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            infoLabel.setForeground(Color.GRAY);
            
            add(imageLabel, BorderLayout.CENTER);
            add(infoLabel, BorderLayout.SOUTH);
        }
        
        public void setImageFile(File file) {
            currentFile = file;
            
            if (file == null || !file.exists() || !isImageFile(file)) {
                clearPreview();
                return;
            }
            
            try {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    // Scale image to fit preview area
                    Image scaledImage = scaleImage(image, 180, 200);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                    
                    // Update info label
                    String fileName = file.getName();
                    long fileSize = file.length();
                    String sizeStr = formatFileSize(fileSize);
                    
                    infoLabel.setText(String.format(
                        "<html><center><b>%s</b><br>%dx%d pixels<br>Size: %s</center></html>",
                        fileName, image.getWidth(), image.getHeight(), sizeStr));
                } else {
                    clearPreview();
                }
            } catch (Exception e) {
                clearPreview();
            }
        }
        
        private void clearPreview() {
            imageLabel.setIcon(null);
            infoLabel.setText("<html><center>Select an image<br>to preview</center></html>");
        }
        
        private boolean isImageFile(File file) {
            String name = file.getName().toLowerCase();
            return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
                   name.endsWith(".png") || name.endsWith(".gif") || 
                   name.endsWith(".bmp");
        }
        
        private Image scaleImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            
            // Calculate scaling factor
            double scaleX = (double) maxWidth / originalWidth;
            double scaleY = (double) maxHeight / originalHeight;
            double scale = Math.min(scaleX, scaleY);
            
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);
            
            return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        }
        
        private String formatFileSize(long size) {
            if (size < 1024) return size + " B";
            else if (size < 1024 * 1024) return (size / 1024) + " KB";
            else return (size / (1024 * 1024)) + " MB";
        }
    }
    
    /**
     * Static method to show the enhanced file chooser
     */
    public static File showImageChooser(Component parent) {
        ImagePreviewFileChooser chooser = new ImagePreviewFileChooser();
        int result = chooser.showOpenDialog(parent);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
} 