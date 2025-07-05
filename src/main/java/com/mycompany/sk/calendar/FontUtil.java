package com.mycompany.sk.calendar;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for loading and managing custom fonts
 */
public class FontUtil {
    
    private static Font azoSansFont = null;
    private static Font canvaSansFont = null;
    
    /**
     * Get Azo Sans font with fallback to Arial
     * @param style Font style (Font.PLAIN, Font.BOLD, etc.)
     * @param size Font size
     * @return Font object
     */
    public static Font getAzoSansFont(int style, int size) {
        if (azoSansFont == null) {
            azoSansFont = loadCustomFont("AzoSans-Regular.otf", "AzoSans-Bold.otf", style);
        }
        
        if (azoSansFont != null) {
            return azoSansFont.deriveFont(style, size);
        } else {
            // Fallback to Arial
            return new Font("Arial", style, size);
        }
    }
    
    /**
     * Get Canva Sans font with fallback to Arial
     * @param style Font style (Font.PLAIN, Font.BOLD, etc.)
     * @param size Font size
     * @return Font object
     */
    public static Font getCanvaSansFont(int style, int size) {
        if (canvaSansFont == null) {
            canvaSansFont = loadCustomFont("CanvaSans-Regular.otf", "CanvaSans-Bold.otf", style);
        }
        
        if (canvaSansFont != null) {
            return canvaSansFont.deriveFont(style, size);
        } else {
            // Fallback to Arial
            return new Font("Arial", style, size);
        }
    }
    
    /**
     * Load custom font from resources or system fonts
     * @param regularFontName Regular font file name
     * @param boldFontName Bold font file name  
     * @param style Requested font style
     * @return Font object or null if not found
     */
    private static Font loadCustomFont(String regularFontName, String boldFontName, int style) {
        try {
            // First, try to load the specific style (bold/regular)
            String fontFileName = (style == Font.BOLD) ? boldFontName : regularFontName;
            InputStream fontStream = FontUtil.class.getResourceAsStream("/com/mycompany/assets/" + fontFileName);
            
            // If bold font not found, fallback to regular font
            if (fontStream == null && style == Font.BOLD) {
                System.out.println("Bold font not found, using regular font: " + regularFontName);
                fontStream = FontUtil.class.getResourceAsStream("/com/mycompany/assets/" + regularFontName);
                fontFileName = regularFontName;
            }
            
            if (fontStream != null) {
                // Support both TrueType (.ttf) and OpenType (.otf) fonts
                // Java uses TRUETYPE_FONT for both .ttf and .otf files
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                System.out.println("Loaded custom font: " + fontFileName);
                return font;
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading custom font: " + e.getMessage());
        }
        
        // Try to find the font by name in system fonts
        String[] systemFontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        // Check for Azo Sans variations
        if (regularFontName.contains("AzoSans")) {
            for (String fontName : systemFontNames) {
                if (fontName.toLowerCase().contains("azo sans") || 
                    fontName.toLowerCase().contains("azosans") ||
                    fontName.toLowerCase().contains("azo-sans")) {
                    System.out.println("Found system font: " + fontName);
                    return new Font(fontName, Font.PLAIN, 12);
                }
            }
        }
        
        // Check for Canva Sans variations
        if (regularFontName.contains("CanvaSans")) {
            for (String fontName : systemFontNames) {
                if (fontName.toLowerCase().contains("canva sans") || 
                    fontName.toLowerCase().contains("canvasans") ||
                    fontName.toLowerCase().contains("canva-sans")) {
                    System.out.println("Found system font: " + fontName);
                    return new Font(fontName, Font.PLAIN, 12);
                }
            }
        }
        
        System.out.println("Custom font not found, will use fallback");
        return null;
    }
    
    /**
     * Check if a font family is available on the system
     * @param fontFamily Font family name
     * @return true if available, false otherwise
     */
    public static boolean isFontAvailable(String fontFamily) {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            if (fontName.equalsIgnoreCase(fontFamily)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * List all available system fonts (for debugging)
     */
    public static void listAvailableFonts() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        System.out.println("Available fonts:");
        for (String fontName : fontNames) {
            System.out.println("  " + fontName);
        }
    }
} 