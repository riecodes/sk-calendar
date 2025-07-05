package com.mycompany.sk.calendar;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Utility class for loading images from the assets folder
 */
public class ImageUtil {
    
    /**
     * Load an image from the assets folder
     * @param imageName the name of the image file (e.g., "sk-logo.png")
     * @return ImageIcon or null if image cannot be loaded
     */
    public static ImageIcon loadImage(String imageName) {
        try {
            InputStream is = ImageUtil.class.getResourceAsStream("/com/mycompany/assets/" + imageName);
            if (is != null) {
                Image image = ImageIO.read(is);
                return new ImageIcon(image);
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + imageName);
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Load and scale an image from the assets folder
     * @param imageName the name of the image file
     * @param width desired width
     * @param height desired height
     * @return scaled ImageIcon or null if image cannot be loaded
     */
    public static ImageIcon loadScaledImage(String imageName, int width, int height) {
        ImageIcon icon = loadImage(imageName);
        if (icon != null) {
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return null;
    }
} 