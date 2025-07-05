package com.mycompany.sk.calendar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced SK Profile management screen with modern design
 */
public class SKProfileScreen extends JFrame {
    
    private SKProfile currentProfile;
    private List<SKProfile> profiles;
    private int currentProfileIndex;
    
    // Form components
    private JLabel photoLabel;
    private JTextField nameField;
    private JSpinner ageSpinner;
    private JComboBox<String> sexComboBox;
    private JComboBox<String> positionComboBox;
    private JTextField committeeField;
    private JTextField sectorField;
    
    // Action buttons
    private JButton deleteButton;
    private JButton updateButton;
    private JButton addButton;
    private JButton backButton;
    private JButton prevButton;
    private JButton nextButton;
    
    // Navigation label
    private JLabel navigationLabel;
    
    public SKProfileScreen() {
        initializeData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCurrentProfile();
    }
    
    private void initializeData() {
        // Load profiles from database
        refreshProfilesFromDatabase();
        
        if (profiles.isEmpty()) {
            // Create new empty profile if no profiles exist
            currentProfile = new SKProfile();
            profiles.add(currentProfile);
            currentProfileIndex = 0;
        } else {
            currentProfileIndex = 0;
            currentProfile = profiles.get(0);
        }
    }
    
    private void refreshProfilesFromDatabase() {
        profiles = DatabaseUtil.getAllProfiles();
        System.out.println("Loaded " + profiles.size() + " profiles from database");
    }
    
    private void initializeComponents() {
        setTitle("SK Calendar - Member Profiles");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        
        getContentPane().setBackground(new Color(248, 249, 250));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Footer panel with navigation and back button
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 57, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Title
        JLabel titleLabel = new JLabel("SK MEMBER PROFILE");
        titleLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Navigation info
        navigationLabel = new JLabel();
        navigationLabel.setFont(FontUtil.getAzoSansFont(Font.PLAIN, 16));
        navigationLabel.setForeground(Color.WHITE);
        headerPanel.add(navigationLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create card panel with shadow effect
        JPanel cardPanel = createCardPanel();
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        
        // Add shadow border
        Border shadowBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createLoweredBevelBorder()
        );
        Border paddingBorder = BorderFactory.createEmptyBorder(40, 40, 40, 40);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(shadowBorder, paddingBorder));
        
        // Content layout
        JPanel contentPanel = new JPanel(new BorderLayout(30, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Left side - Photo section
        JPanel photoPanel = createPhotoPanel();
        contentPanel.add(photoPanel, BorderLayout.WEST);
        
        // Right side - Form fields
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        cardPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Action buttons at bottom
        JPanel actionPanel = createActionPanel();
        cardPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return cardPanel;
    }
    
    private JPanel createPhotoPanel() {
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setPreferredSize(new Dimension(200, 250));
        
        // Photo placeholder
        photoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle background
                g2.setColor(new Color(108, 117, 125));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // If no image is set, show placeholder text
                if (getIcon() == null) {
                    g2.setColor(Color.WHITE);
                    g2.setFont(FontUtil.getAzoSansFont(Font.BOLD, 14));
                    FontMetrics fm = g2.getFontMetrics();
                    String text = "Click to add photo";
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2;
                    g2.drawString(text, x, y);
                } else {
                    // Draw the image icon centered
                    super.paintComponent(g2);
                }
                
                g2.dispose();
            }
        };
        photoLabel.setPreferredSize(new Dimension(180, 200));
        photoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        photoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click handler for photo upload
        photoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                uploadPhoto();
            }
        });
        
        photoPanel.add(photoLabel, BorderLayout.CENTER);
        
        return photoPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("NAME:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = createStyledTextField();
        formPanel.add(nameField, gbc);
        
        // Age and Sex fields (side by side)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("AGE:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        ageSpinner = createStyledSpinner();
        formPanel.add(ageSpinner, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("SEX:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        sexComboBox = createStyledComboBox(new String[]{"Male", "Female"});
        formPanel.add(sexComboBox, gbc);
        
        // Position field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("POSITION:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        positionComboBox = createStyledComboBox(SKProfile.SK_POSITIONS);
        formPanel.add(positionComboBox, gbc);
        
        // Committee field
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("COMMITTEE:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        committeeField = createStyledTextField();
        formPanel.add(committeeField, gbc);
        
        // Sector field
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("SECTOR:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        sectorField = createStyledTextField();
        formPanel.add(sectorField, gbc);
        
        return formPanel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FontUtil.getAzoSansFont(Font.BOLD, 16));
        label.setForeground(new Color(52, 58, 64));
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Draw border
                g2.setColor(new Color(206, 212, 218));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        field.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setOpaque(false);
        return field;
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        return comboBox;
    }
    
    private JSpinner createStyledSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(18, 15, 30, 1));
        spinner.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setBorder(
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
        return spinner;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
        actionPanel.setBackground(Color.WHITE);
        
        // Navigation arrows
        prevButton = createNavigationButton("back.png");
        actionPanel.add(prevButton);
        
        // Action buttons
        updateButton = createActionButton("UPDATE", new Color(255, 193, 7));
        deleteButton = createActionButton("DELETE", new Color(220, 53, 69));
        addButton = createActionButton("ADD NEW", new Color(0, 123, 255));
        
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        actionPanel.add(addButton);
        
        nextButton = createNavigationButton("next.png");
        actionPanel.add(nextButton);
        
        return actionPanel;
    }
    
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(100, 45));
        button.setFont(FontUtil.getAzoSansFont(Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
    
    private JButton createNavigationButton(String imageName) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fill background with rounded corners
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        // Load and set the image icon
        ImageIcon icon = ImageUtil.loadScaledImage(imageName, 24, 24);
        if (icon != null) {
            button.setIcon(icon);
        }
        
        button.setPreferredSize(new Dimension(50, 45));
        button.setBackground(new Color(108, 117, 125));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 98, 104));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(108, 117, 125));
            }
        });
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Back button
        backButton = createActionButton("BACK", new Color(108, 117, 125));
        backButton.setPreferredSize(new Dimension(100, 40));
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setBackground(new Color(248, 249, 250));
        backPanel.add(backButton);
        
        footerPanel.add(backPanel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    private void setupEventHandlers() {
        // Update button
        updateButton.addActionListener(e -> updateProfile());
        
        // Delete button
        deleteButton.addActionListener(e -> deleteProfile());
        
        // Add button
        addButton.addActionListener(e -> openAddProfileModal());
        
        // Navigation buttons
        prevButton.addActionListener(e -> navigateProfile(-1));
        nextButton.addActionListener(e -> navigateProfile(1));
        
        // Back button
        backButton.addActionListener(e -> {
            dispose(); // Close profile screen
        });
    }
    
    private void loadCurrentProfile() {
        if (currentProfile != null) {
            nameField.setText(currentProfile.getName() != null ? currentProfile.getName() : "");
            ageSpinner.setValue(currentProfile.getAge() > 0 ? currentProfile.getAge() : 18);
            sexComboBox.setSelectedItem(currentProfile.getSex() != null ? currentProfile.getSex() : "Male");
            positionComboBox.setSelectedItem(currentProfile.getPosition() != null ? currentProfile.getPosition() : "");
            committeeField.setText(currentProfile.getCommittee() != null ? currentProfile.getCommittee() : "");
            sectorField.setText(currentProfile.getSector() != null ? currentProfile.getSector() : "");
            
            // Load photo if available
            if (currentProfile.getPhotoPath() != null && !currentProfile.getPhotoPath().isEmpty()) {
                displayPhoto(currentProfile.getPhotoPath());
            } else {
                // Clear photo display and ensure placeholder text shows
                photoLabel.setIcon(null);
                photoLabel.repaint();
            }
        }
        
        updateNavigationLabel();
        updateButtonStates();
    }
    
    private void updateNavigationLabel() {
        navigationLabel.setText(String.format("Profile %d of %d", currentProfileIndex + 1, profiles.size()));
    }
    
    private void updateButtonStates() {
        prevButton.setEnabled(currentProfileIndex > 0);
        nextButton.setEnabled(currentProfileIndex < profiles.size() - 1);
        
        boolean hasData = !nameField.getText().trim().isEmpty();
        boolean isNewProfile = currentProfile.getId() == 0;
        
        updateButton.setEnabled(hasData && !isNewProfile);
        deleteButton.setEnabled(!isNewProfile);
        addButton.setEnabled(true); // Always enabled to allow adding new profiles
    }
    

    
    private void updateProfile() {
        if (validateForm()) {
            // Get original position to check if it changed
            String originalPosition = currentProfile.getPosition();
            updateCurrentProfileFromForm();
            String newPosition = currentProfile.getPosition();
            
            // Check if position changed and if new position is already taken by someone else
            if (!newPosition.equals(originalPosition) && DatabaseUtil.isPositionTakenByOther(newPosition, currentProfile.getId())) {
                JOptionPane.showMessageDialog(this, 
                    "Position '" + newPosition + "' is already assigned to another member.\n" +
                    "Each SK position can only be held by one person.", 
                    "Position Already Taken", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean success = DatabaseUtil.updateProfile(currentProfile);
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Profile updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data from database
                refreshProfilesFromDatabase();
                
                // Maintain current position
                if (currentProfileIndex < profiles.size()) {
                    currentProfile = profiles.get(currentProfileIndex);
                    loadCurrentProfile();
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error updating profile in database.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteProfile() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this profile?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            boolean success = DatabaseUtil.deleteProfile(currentProfile.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Profile deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data from database
                refreshProfilesFromDatabase();
                
                // Adjust current position after deletion
                if (profiles.isEmpty()) {
                    // No profiles left, create empty one
                    currentProfile = new SKProfile();
                    profiles.add(currentProfile);
                    currentProfileIndex = 0;
                } else {
                    // Adjust index if needed
                    if (currentProfileIndex >= profiles.size()) {
                        currentProfileIndex = profiles.size() - 1;
                    }
                    currentProfile = profiles.get(currentProfileIndex);
                }
                
                loadCurrentProfile();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting profile from database.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void navigateProfile(int direction) {
        int newIndex = currentProfileIndex + direction;
        if (newIndex >= 0 && newIndex < profiles.size()) {
            currentProfileIndex = newIndex;
            currentProfile = profiles.get(currentProfileIndex);
            loadCurrentProfile();
        }
        // Navigation is now limited to existing profiles only
        // Use ADD NEW button to create new profiles
    }
    
    private void openAddProfileModal() {
        // This will open the modal dialog for adding new profiles
        AddProfileModal modal = new AddProfileModal(this);
        modal.setVisible(true);
        
        // Refresh profiles after modal closes
        refreshProfilesFromDatabase();
        if (!profiles.isEmpty()) {
            currentProfileIndex = profiles.size() - 1; // Go to the last (newly added) profile
            currentProfile = profiles.get(currentProfileIndex);
            loadCurrentProfile();
        }
    }
    
    private void uploadPhoto() {
        // Use enhanced file chooser with image preview
        java.io.File selectedFile = ImagePreviewFileChooser.showImageChooser(this);
        
        if (selectedFile != null) {
            
            try {
                // Create uploads directory if it doesn't exist
                java.io.File uploadsDir = new java.io.File("uploads");
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs();
                }
                
                // Generate unique filename
                String fileName = "profile_" + System.currentTimeMillis() + "_" + selectedFile.getName();
                java.io.File destFile = new java.io.File(uploadsDir, fileName);
                
                // Copy file to uploads directory
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), 
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                
                // Update current profile with photo path
                currentProfile.setPhotoPath("uploads/" + fileName);
                
                // Update photo display
                displayPhoto(destFile.getAbsolutePath());
                
                // Save photo path to database if this is an existing profile
                if (currentProfile.getId() > 0) {
                    boolean success = DatabaseUtil.updateProfile(currentProfile);
                    if (success) {
                        JOptionPane.showMessageDialog(this, 
                            "Photo uploaded and saved successfully!", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Photo uploaded but failed to save to database.", 
                            "Warning", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Photo uploaded successfully! Save the profile to store the photo permanently.", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error uploading photo: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void displayPhoto(String photoPath) {
        try {
            if (photoPath == null || photoPath.trim().isEmpty()) {
                photoLabel.setIcon(null);
                photoLabel.repaint();
                return;
            }
            
            java.io.File imageFile = new java.io.File(photoPath);
            
            // If the path is relative, it might be in the working directory
            if (!imageFile.exists() && !photoPath.startsWith("/") && !photoPath.contains(":\\")) {
                // Try as absolute path first, then relative to working directory
                imageFile = new java.io.File(System.getProperty("user.dir"), photoPath);
            }
            
            if (imageFile.exists()) {
                java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(imageFile);
                if (img != null) {
                    // Scale image to fit the photo label
                    java.awt.Image scaledImg = img.getScaledInstance(180, 200, java.awt.Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(scaledImg);
                    photoLabel.setIcon(icon);
                    photoLabel.repaint();
                    return;
                }
            }
            
            // If we reach here, the image couldn't be loaded
            System.err.println("Could not load image: " + photoPath);
            photoLabel.setIcon(null);
            photoLabel.repaint();
            
        } catch (Exception e) {
            System.err.println("Error displaying photo: " + e.getMessage());
            photoLabel.setIcon(null);
            photoLabel.repaint();
        }
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a name.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        return true;
    }
    
    private void updateCurrentProfileFromForm() {
        currentProfile.setName(nameField.getText().trim());
        currentProfile.setAge((Integer) ageSpinner.getValue());
        currentProfile.setSex((String) sexComboBox.getSelectedItem());
        currentProfile.setPosition((String) positionComboBox.getSelectedItem());
        currentProfile.setCommittee(committeeField.getText().trim());
        currentProfile.setSector(sectorField.getText().trim());
    }
} 