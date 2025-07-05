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
    private JComboBox<String> official1Dropdown;
    private JComboBox<String> official2Dropdown;
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
        "SK Secretary",
        "SK Treasurer"
    };
    
    public DutyAssignmentDialog(Dialog parent, CalendarScreen.Event event) {
        super(parent, "Duty Assignment", true);
        this.event = event;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(900, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents() {
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Create dropdowns
        official1Dropdown = createStyledComboBox();
        official2Dropdown = createStyledComboBox();
        
        // Create buttons
        saveButton = createStyledButton("SAVE", new Color(40, 167, 69));
        deleteButton = createStyledButton("DELETE", new Color(220, 53, 69));
        updateButton = createStyledButton("UPDATE", new Color(255, 193, 7));
        backButton = createStyledButton("BACK", new Color(108, 117, 125));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main form panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
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
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Comment text
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel commentLabel = new JLabel("//Depends on no. of attendees");
        commentLabel.setFont(FontUtil.getCanvaSansFont(Font.ITALIC, 16));
        commentLabel.setForeground(new Color(108, 117, 125));
        mainPanel.add(commentLabel, gbc);
        
        // OFFICIAL NO.1 dropdown
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        JLabel official1Label = createStyledLabel("OFFICIAL NO.1:");
        mainPanel.add(official1Label, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 0);
        mainPanel.add(official1Dropdown, gbc);
        
        // OFFICIAL NO.2 dropdown
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 0, 15, 0);
        JLabel official2Label = createStyledLabel("OFFICIAL NO.2:");
        mainPanel.add(official2Label, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 0);
        mainPanel.add(official2Dropdown, gbc);
        
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
    
    private void saveAssignment() {
        if (validateSelection()) {
            // Save duty assignment to database
            // For now, just show success message
            JOptionPane.showMessageDialog(this, "Duty assignment saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateAssignment() {
        if (validateSelection()) {
            // Update duty assignment in database
            JOptionPane.showMessageDialog(this, "Duty assignment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void deleteAssignment() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this duty assignment?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Delete duty assignment from database
            JOptionPane.showMessageDialog(this, "Duty assignment deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
    
    private void openAttendanceLogDialog() {
        // Create and show the attendance log dialog (7.png)
        AttendanceLogDialog attendanceDialog = new AttendanceLogDialog(this, event);
        attendanceDialog.setVisible(true);
    }
    
    private boolean validateSelection() {
        if (official1Dropdown.getSelectedIndex() == 0 && official2Dropdown.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one official.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check if same official is selected for both positions
        if (official1Dropdown.getSelectedIndex() > 0 && official2Dropdown.getSelectedIndex() > 0) {
            if (official1Dropdown.getSelectedItem().equals(official2Dropdown.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Cannot assign the same official to both positions.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
} 