package com.mycompany.sk.calendar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Modal dialog for adding new SK profiles with position validation
 */
public class AddProfileModal extends JDialog {
    
    private SKProfile newProfile;
    private JFrame parentFrame;
    
    // Form components
    private JLabel photoLabel;
    private JTextField nameField;
    private JSpinner ageSpinner;
    private JComboBox<String> sexComboBox;
    private JComboBox<String> positionComboBox;
    private JTextField committeeField;
    private JTextField sectorField;
    
    // Action buttons
    private JButton saveButton;
    private JButton cancelButton;
    
    public AddProfileModal(JFrame parent) {
        super(parent, "Add New SK Profile", true); // Modal dialog
        this.parentFrame = parent;
        this.newProfile = new SKProfile();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initializeComponents() {
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
        
        // Footer panel with buttons
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 57, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Title
        JLabel titleLabel = new JLabel("ADD NEW SK MEMBER");
        titleLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
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
        Border paddingBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);
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
        
        return cardPanel;
    }
    
    private JPanel createPhotoPanel() {
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setPreferredSize(new Dimension(180, 200));
        
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
        photoLabel.setPreferredSize(new Dimension(160, 180));
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
        gbc.insets = new Insets(12, 15, 12, 15);
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
        label.setFont(FontUtil.getAzoSansFont(Font.BOLD, 14));
        label.setForeground(new Color(33, 37, 41));
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(25) {
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
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
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
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        footerPanel.setBackground(new Color(248, 249, 250));
        
        // Cancel button
        cancelButton = createActionButton("CANCEL", new Color(108, 117, 125));
        footerPanel.add(cancelButton);
        
        // Save button
        saveButton = createActionButton("SAVE", new Color(40, 167, 69));
        footerPanel.add(saveButton);
        
        return footerPanel;
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
        
        button.setPreferredSize(new Dimension(120, 45));
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
    
    private void setupEventHandlers() {
        // Save button
        saveButton.addActionListener(e -> saveProfile());
        
        // Cancel button
        cancelButton.addActionListener(e -> dispose());
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
                newProfile.setPhotoPath("uploads/" + fileName);
                
                // Update photo display
                displayPhoto(destFile.getAbsolutePath());
                
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
            
            // If the path is relative, try it relative to working directory
            if (!imageFile.exists() && !photoPath.startsWith("/") && !photoPath.contains(":\\")) {
                imageFile = new java.io.File(System.getProperty("user.dir"), photoPath);
            }
            
            if (imageFile.exists()) {
                java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(imageFile);
                if (img != null) {
                    // Scale image to fit the photo label
                    java.awt.Image scaledImg = img.getScaledInstance(160, 180, java.awt.Image.SCALE_SMOOTH);
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
    
    private void saveProfile() {
        if (validateForm()) {
            updateProfileFromForm();
            
            // Check if position is already taken
            if (isPositionTaken(newProfile.getPosition())) {
                JOptionPane.showMessageDialog(this, 
                    "Position '" + newProfile.getPosition() + "' is already assigned to another member.\n" +
                    "Each SK position can only be held by one person.", 
                    "Position Already Taken", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int newId = DatabaseUtil.saveProfile(newProfile);
            if (newId > 0) {
                newProfile.setId(newId);
                
                JOptionPane.showMessageDialog(this, 
                    "Profile saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); // Close modal
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error saving profile to database.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean isPositionTaken(String position) {
        if (position == null || position.trim().isEmpty()) {
            return false; // Allow empty positions
        }
        
        return DatabaseUtil.isPositionTaken(position.trim());
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
        
        String selectedPosition = (String) positionComboBox.getSelectedItem();
        if (selectedPosition == null || selectedPosition.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a position.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            positionComboBox.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void updateProfileFromForm() {
        newProfile.setName(nameField.getText().trim());
        newProfile.setAge((Integer) ageSpinner.getValue());
        newProfile.setSex((String) sexComboBox.getSelectedItem());
        newProfile.setPosition((String) positionComboBox.getSelectedItem());
        newProfile.setCommittee(committeeField.getText().trim());
        newProfile.setSector(sectorField.getText().trim());
    }
} 