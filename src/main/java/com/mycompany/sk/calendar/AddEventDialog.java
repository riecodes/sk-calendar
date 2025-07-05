package com.mycompany.sk.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Enhanced Add Event Dialog matching the 5.png mockup design
 */
public class AddEventDialog extends JDialog {
    
    private JTextField eventField;
    private JTextField timeField;
    private JTextField placeField;
    private JTextField attendingOfficialsField;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton assignOfficialsButton;
    
    private LocalDate selectedDate;
    private CalendarScreen.Event currentEvent;
    private boolean isEditMode = false;
    
    public AddEventDialog(Frame parent, LocalDate date) {
        super(parent, "Add Event", true);
        this.selectedDate = date;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    public AddEventDialog(Frame parent, LocalDate date, CalendarScreen.Event event) {
        this(parent, date);
        this.currentEvent = event;
        this.isEditMode = true;
        populateFields();
    }
    
    private void initializeComponents() {
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Create form fields
        eventField = createStyledTextField();
        timeField = createStyledTextField();
        placeField = createStyledTextField();
        attendingOfficialsField = createStyledTextField();
        
        // Create buttons
        saveButton = createStyledButton("SAVE", new Color(40, 167, 69));
        deleteButton = createStyledButton("DELETE", new Color(220, 53, 69));
        updateButton = createStyledButton("UPDATE", new Color(255, 193, 7));
        assignOfficialsButton = createStyledButton("ASSIGN OFFICIALS", new Color(108, 117, 125));
        
        // Initially show appropriate buttons
        updateButtonVisibility();
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
        
        JLabel titleLabel = new JLabel("ADD EVENT");
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
        
        // EVENT field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel eventLabel = createStyledLabel("EVENT:");
        mainPanel.add(eventLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 0);
        mainPanel.add(eventField, gbc);
        
        // TIME field
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 0, 15, 0);
        JLabel timeLabel = createStyledLabel("TIME:");
        mainPanel.add(timeLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 0);
        mainPanel.add(timeField, gbc);
        
        // PLACE field
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 0, 15, 0);
        JLabel placeLabel = createStyledLabel("PLACE:");
        mainPanel.add(placeLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 0);
        mainPanel.add(placeField, gbc);
        
        // ATTENDING OFFICIALS field
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 0, 15, 0);
        JLabel attendingLabel = createStyledLabel("ATTENDING OFFICIALS:");
        mainPanel.add(attendingLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 0);
        mainPanel.add(attendingOfficialsField, gbc);
        
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
        buttonPanel.add(assignOfficialsButton);
        
        return buttonPanel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
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
        
        field.setPreferredSize(new Dimension(500, 50));
        field.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 18));
        field.setForeground(new Color(33, 37, 41));
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setOpaque(false);
        
        return field;
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
    
    private void updateButtonVisibility() {
        if (isEditMode) {
            saveButton.setVisible(false);
            deleteButton.setVisible(true);
            updateButton.setVisible(true);
        } else {
            saveButton.setVisible(true);
            deleteButton.setVisible(false);
            updateButton.setVisible(false);
        }
    }
    
    private void populateFields() {
        if (currentEvent != null) {
            eventField.setText(currentEvent.getTitle());
            timeField.setText(currentEvent.getTime() != null ? currentEvent.getTime() : "");
            placeField.setText(currentEvent.getLocation() != null ? currentEvent.getLocation() : "");
            // For now, attending officials field is empty - will be populated from database in future
            attendingOfficialsField.setText("");
        }
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> saveEvent());
        updateButton.addActionListener(e -> updateEvent());
        deleteButton.addActionListener(e -> deleteEvent());
        assignOfficialsButton.addActionListener(e -> openAssignOfficialsDialog());
    }
    
    private void saveEvent() {
        if (validateFields()) {
            CalendarScreen.Event event = new CalendarScreen.Event();
            event.setTitle(eventField.getText().trim());
            event.setTime(timeField.getText().trim());
            event.setLocation(placeField.getText().trim());
            event.setDate(selectedDate);
            event.setDescription(""); // Will be enhanced later
            event.setCreatedBy(1); // Default admin user
            
            int eventId = DatabaseUtil.saveEvent(event);
            if (eventId > 0) {
                JOptionPane.showMessageDialog(this, "Event saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving event.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateEvent() {
        if (currentEvent != null && validateFields()) {
            currentEvent.setTitle(eventField.getText().trim());
            currentEvent.setTime(timeField.getText().trim());
            currentEvent.setLocation(placeField.getText().trim());
            
            if (DatabaseUtil.updateEvent(currentEvent)) {
                JOptionPane.showMessageDialog(this, "Event updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating event.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteEvent() {
        if (currentEvent != null) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this event?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                if (DatabaseUtil.deleteEvent(currentEvent.getId())) {
                    JOptionPane.showMessageDialog(this, "Event deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting event.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void openAssignOfficialsDialog() {
        // First save or update the event if needed
        if (isEditMode) {
            updateEvent();
        } else {
            // For new events, we need to save first
            if (validateFields()) {
                CalendarScreen.Event event = new CalendarScreen.Event();
                event.setTitle(eventField.getText().trim());
                event.setTime(timeField.getText().trim());
                event.setLocation(placeField.getText().trim());
                event.setDate(selectedDate);
                event.setDescription("");
                event.setCreatedBy(1);
                
                int eventId = DatabaseUtil.saveEvent(event);
                if (eventId > 0) {
                    event.setId(eventId);
                    this.currentEvent = event;
                    this.isEditMode = true;
                    updateButtonVisibility();
                    
                    // Open assign officials dialog
                    openDutyAssignmentDialog();
                } else {
                    JOptionPane.showMessageDialog(this, "Please save the event first.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        if (currentEvent != null) {
            openDutyAssignmentDialog();
        }
    }
    
    private void openDutyAssignmentDialog() {
        // Create and show the duty assignment dialog (6.png)
        DutyAssignmentDialog dutyDialog = new DutyAssignmentDialog(this, currentEvent);
        dutyDialog.setVisible(true);
    }
    
    private boolean validateFields() {
        if (eventField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an event title.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (placeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a location.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
} 