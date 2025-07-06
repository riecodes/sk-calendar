package com.mycompany.sk.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Duty Assignment Dialog matching the 6.png mockup design
 * Allows assigning SK officials to specific events
 */
public class DutyAssignmentDialog extends JDialog {
    
    private CalendarScreen.Event event;
    private int numberOfOfficials;
    private boolean isEditMode;
    private JComboBox<String>[] officialDropdowns;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton backButton;
    
    // Available SK officials
    private String[] skOfficials = {
        "Select Official",
        "SK Chairman",
        "SK Kagawad 1",
        "SK Kagawad 2", 
        "SK Kagawad 3",
        "SK Kagawad 4",
        "SK Kagawad 5",
        "SK Kagawad 6",
        "SK Kagawad 7",
        "Secretary",
        "Treasurer"
    };
    
    public DutyAssignmentDialog(Dialog parent, CalendarScreen.Event event, int numberOfOfficials, boolean isEditMode) {
        super(parent, "Duty Assignment", true);
        this.event = event;
        this.numberOfOfficials = Math.max(1, Math.min(10, numberOfOfficials)); // Ensure between 1-10
        this.isEditMode = isEditMode;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        // Set a reasonable maximum height and enable scrolling for large numbers of officials
        int baseHeight = 350;
        int contentHeight = numberOfOfficials * 80;
        int maxHeight = 700; // Maximum dialog height
        
        int dialogHeight = Math.min(baseHeight + contentHeight, maxHeight);
        setSize(900, dialogHeight);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    @SuppressWarnings("unchecked")
    private void initializeComponents() {
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Create dynamic dropdowns array
        officialDropdowns = new JComboBox[numberOfOfficials];
        for (int i = 0; i < numberOfOfficials; i++) {
            officialDropdowns[i] = createStyledComboBox();
        }
        
        // Create buttons
        saveButton = createStyledButton("SAVE", new Color(40, 167, 69));
        deleteButton = createStyledButton("DELETE", new Color(220, 53, 69));
        updateButton = createStyledButton("UPDATE", new Color(255, 193, 7));
        backButton = createStyledButton("BACK", new Color(108, 117, 125));
        
        // Show/hide buttons based on mode
        if (isEditMode) {
            // Edit mode: show UPDATE, DELETE, and BACK
            saveButton.setVisible(false);
            deleteButton.setVisible(true);
            updateButton.setVisible(true);
            
            // Load existing assignments
            loadExistingAssignments();
        } else {
            // New assignment mode: show SAVE and BACK only
            saveButton.setVisible(true);
            deleteButton.setVisible(false);
            updateButton.setVisible(false);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main form panel with scrolling
        JPanel mainPanel = createMainPanel();
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        scrollPane.setBackground(new Color(240, 240, 240));
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        
        JLabel titleLabel = new JLabel("DUTY ASSIGNMENT");
        titleLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 48));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        
        headerPanel.add(titleLabel);
        
        return headerPanel;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setLayout(new GridBagLayout());
        
        // Set preferred size to accommodate all officials (for proper scrolling)
        int contentHeight = 120 + (numberOfOfficials * 80) + 40; // Comment + officials + padding
        mainPanel.setPreferredSize(new Dimension(820, contentHeight));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Comment text
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel commentLabel = new JLabel("//Assign " + numberOfOfficials + " official(s) to this event");
        commentLabel.setFont(FontUtil.getCanvaSansFont(Font.ITALIC, 16));
        commentLabel.setForeground(new Color(108, 117, 125));
        mainPanel.add(commentLabel, gbc);
        
        // Dynamically add official dropdowns
        for (int i = 0; i < numberOfOfficials; i++) {
            // Official label
            gbc.gridx = 0; gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(15, 0, 15, 0);
            
            JLabel officialLabel = createStyledLabel("OFFICIAL NO." + (i + 1) + ":");
            mainPanel.add(officialLabel, gbc);
            
            // Official dropdown
            gbc.gridx = 1; gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(15, 20, 15, 0);
            mainPanel.add(officialDropdowns[i], gbc);
        }
        
        return mainPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);
        
        return buttonPanel;
    }
    
    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(skOfficials) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                g2.setColor(new Color(200, 200, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        comboBox.setPreferredSize(new Dimension(500, 50));
        comboBox.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 18));
        comboBox.setForeground(new Color(33, 37, 41));
        comboBox.setBackground(new Color(200, 200, 200));
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        return comboBox;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FontUtil.getAzoSansFont(Font.BOLD, 24));
        label.setForeground(new Color(33, 37, 41));
        label.setPreferredSize(new Dimension(250, 50));
        return label;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(180, 50));
        button.setFont(FontUtil.getAzoSansFont(Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> saveAssignment());
        updateButton.addActionListener(e -> updateAssignment());
        deleteButton.addActionListener(e -> deleteAssignment());
        backButton.addActionListener(e -> {
            dispose();
            // Open attendance log dialog
            openAttendanceLogDialog();
        });
    }
    
    private void loadExistingAssignments() {
        // Load existing assignments for this event
        List<SKProfile> assignedOfficials = DatabaseUtil.getAssignedOfficials(event.getId());
        
        // Populate dropdowns with existing assignments
        for (int i = 0; i < Math.min(assignedOfficials.size(), officialDropdowns.length); i++) {
            SKProfile profile = assignedOfficials.get(i);
            String officialTitle = profile.getPosition();
            
            // Find and select the matching item in the dropdown
            for (int j = 0; j < skOfficials.length; j++) {
                if (skOfficials[j].equals(officialTitle)) {
                    officialDropdowns[i].setSelectedIndex(j);
                    break;
                }
            }
        }
    }
    
    private void saveAssignment() {
        if (validateSelection()) {
            // Get selected officials and save to database
            List<Integer> selectedProfileIds = getSelectedProfileIds();
            
            if (!selectedProfileIds.isEmpty()) {
                boolean success = DatabaseUtil.assignOfficialsToEvent(event.getId(), selectedProfileIds);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Duty assignment saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving duty assignment.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void updateAssignment() {
        if (validateSelection()) {
            // Update duty assignment in database (same as save - replace existing)
            List<Integer> selectedProfileIds = getSelectedProfileIds();
            
            boolean success = DatabaseUtil.assignOfficialsToEvent(event.getId(), selectedProfileIds);
            if (success) {
                JOptionPane.showMessageDialog(this, "Duty assignment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating duty assignment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteAssignment() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this duty assignment?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Delete duty assignment from database (assign empty list)
            boolean success = DatabaseUtil.assignOfficialsToEvent(event.getId(), new java.util.ArrayList<>());
            if (success) {
                JOptionPane.showMessageDialog(this, "Duty assignment deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting duty assignment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private List<Integer> getSelectedProfileIds() {
        List<Integer> profileIds = new java.util.ArrayList<>();
        
        for (JComboBox<String> dropdown : officialDropdowns) {
            if (dropdown.getSelectedIndex() > 0) {
                String selectedPosition = (String) dropdown.getSelectedItem();
                
                // Find the profile ID for this position
                List<SKProfile> allProfiles = DatabaseUtil.getAllProfiles();
                for (SKProfile profile : allProfiles) {
                    if (profile.getPosition().equals(selectedPosition)) {
                        profileIds.add(profile.getId());
                        break;
                    }
                }
            }
        }
        
        return profileIds;
    }
    
    private void openAttendanceLogDialog() {
        // Create and show the attendance log dialog (7.png)
        AttendanceLogDialog attendanceDialog = new AttendanceLogDialog(this, event);
        attendanceDialog.setVisible(true);
    }
    
    private boolean validateSelection() {
        // Check if at least one official is selected
        boolean hasSelection = false;
        for (JComboBox<String> dropdown : officialDropdowns) {
            if (dropdown.getSelectedIndex() > 0) {
                hasSelection = true;
                break;
            }
        }
        
        if (!hasSelection) {
            JOptionPane.showMessageDialog(this, "Please select at least one official.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check for duplicate selections
        for (int i = 0; i < officialDropdowns.length; i++) {
            if (officialDropdowns[i].getSelectedIndex() > 0) {
                String selectedOfficial = (String) officialDropdowns[i].getSelectedItem();
                
                // Check against all other dropdowns
                for (int j = i + 1; j < officialDropdowns.length; j++) {
                    if (officialDropdowns[j].getSelectedIndex() > 0) {
                        String otherOfficial = (String) officialDropdowns[j].getSelectedItem();
                        if (selectedOfficial.equals(otherOfficial)) {
                            JOptionPane.showMessageDialog(this, 
                                "Cannot assign the same official (" + selectedOfficial + ") to multiple positions.", 
                                "Validation Error", 
                                JOptionPane.WARNING_MESSAGE);
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
} 