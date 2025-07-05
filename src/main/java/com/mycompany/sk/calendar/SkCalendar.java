/*
 * SK Calendar - Main Application Class
 * A desktop application for Sangguniang Kabataan officials to organize events and activities
 */

package com.mycompany.sk.calendar;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class for the SK Calendar application
 * @author eirmo
 */
public class SkCalendar {

    public static void main(String[] args) {
        // Set the system look and feel for better native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to set system look and feel: " + ex.getMessage());
        }
        
        // Initialize database
        System.out.println("Initializing database...");
        DatabaseUtil.initializeDatabase();
        
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and show the login screen
                LoginScreen loginScreen = new LoginScreen();
                loginScreen.setVisible(true);
            }
        });
    }
}
