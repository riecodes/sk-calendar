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
    private JButton attendanceButton;
    
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
        updateButtonVisibility(); // Make sure buttons are set correctly for edit mode
        populateFields();
    }
    
    private void initializeComponents() {
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Create form fields with placeholders
        eventField = createStyledTextField("Enter event name...");
        timeField = createStyledTextField("e.g., 2:00 PM, 10:30 AM");
        placeField = createStyledTextField("Enter location...");
        attendingOfficialsField = createStyledTextField("How many officials will attend? (1-10)");
        
        // Create buttons
        saveButton = createStyledButton("SAVE", new Color(40, 167, 69));
        deleteButton = createStyledButton("DELETE", new Color(220, 53, 69));
        updateButton = createStyledButton("UPDATE", new Color(255, 193, 7));
        assignOfficialsButton = createStyledButton("ASSIGN OFFICIALS", new Color(108, 117, 125));
        attendanceButton = createStyledButton("ATTENDANCE", new Color(0, 123, 255));
        
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
        
        String titleText = isEditMode ? "EDIT EVENT" : "ADD EVENT";
        JLabel titleLabel = new JLabel(titleText);
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
        buttonPanel.add(attendanceButton);
        
        return buttonPanel;
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
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
        
        // Add focus listener to handle placeholder behavior
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(new Color(33, 37, 41)); // Normal text color
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(120, 120, 120)); // Placeholder color (lighter)
                }
            }
        });
        
        // Set initial placeholder color
        field.setForeground(new Color(120, 120, 120));
        
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
            // Edit mode: show UPDATE, DELETE, ASSIGN OFFICIALS, and ATTENDANCE
            saveButton.setVisible(false);
            saveButton.setEnabled(false);
            deleteButton.setVisible(true);
            deleteButton.setEnabled(true);
            updateButton.setVisible(true);
            updateButton.setEnabled(true);
            assignOfficialsButton.setEnabled(true);
            attendanceButton.setVisible(true);
            attendanceButton.setEnabled(true);
        } else {
            // New event mode: show SAVE and ASSIGN OFFICIALS
            saveButton.setVisible(true);
            saveButton.setEnabled(true);
            deleteButton.setVisible(false);
            deleteButton.setEnabled(false);
            updateButton.setVisible(false);
            updateButton.setEnabled(false);
            assignOfficialsButton.setEnabled(true);
            attendanceButton.setVisible(false);
            attendanceButton.setEnabled(false);
        }
    }
    
    private void populateFields() {
        if (currentEvent != null) {
            // Set event title
            if (currentEvent.getTitle() != null && !currentEvent.getTitle().trim().isEmpty()) {
                eventField.setText(currentEvent.getTitle());
                eventField.setForeground(new Color(33, 37, 41)); // Normal text color
            } else {
                eventField.setText("Enter event name...");
                eventField.setForeground(new Color(120, 120, 120)); // Placeholder color
            }
            
            // Set time
            if (currentEvent.getTime() != null && !currentEvent.getTime().trim().isEmpty()) {
                timeField.setText(currentEvent.getTime());
                timeField.setForeground(new Color(33, 37, 41)); // Normal text color
            } else {
                timeField.setText("e.g., 2:00 PM, 10:30 AM");
                timeField.setForeground(new Color(120, 120, 120)); // Placeholder color
            }
            
            // Set location
            if (currentEvent.getLocation() != null && !currentEvent.getLocation().trim().isEmpty()) {
                placeField.setText(currentEvent.getLocation());
                placeField.setForeground(new Color(33, 37, 41)); // Normal text color
            } else {
                placeField.setText("Enter location...");
                placeField.setForeground(new Color(120, 120, 120)); // Placeholder color
            }
            
            // Set attending officials count
            if (currentEvent.getAttendingOfficialsCount() > 0) {
                attendingOfficialsField.setText(String.valueOf(currentEvent.getAttendingOfficialsCount()));
                attendingOfficialsField.setForeground(new Color(33, 37, 41)); // Normal text color
            } else {
                attendingOfficialsField.setText("How many officials will attend? (1-10)");
                attendingOfficialsField.setForeground(new Color(120, 120, 120)); // Placeholder color
            }
        }
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> {
            if (isEditMode) {
                System.err.println("WARNING: Save button clicked in edit mode - this shouldn't happen!");
                JOptionPane.showMessageDialog(this, "Error: Save button should not be active in edit mode. Please use UPDATE instead.", "Internal Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saveEvent();
        });
        updateButton.addActionListener(e -> updateEvent());
        deleteButton.addActionListener(e -> deleteEvent());
        assignOfficialsButton.addActionListener(e -> openAssignOfficialsDialog());
        attendanceButton.addActionListener(e -> openAttendanceDialog());
    }
    
    private void saveEvent() {
        if (validateFields()) {
            CalendarScreen.Event event = new CalendarScreen.Event();
            event.setTitle(getFieldText(eventField, "Enter event name..."));
            event.setTime(getFieldText(timeField, "e.g., 2:00 PM, 10:30 AM"));
            event.setLocation(getFieldText(placeField, "Enter location..."));
            event.setDate(selectedDate);
            event.setCreatedBy(1); // Default admin user
            
            // Set attending officials count
            String officialsText = getFieldText(attendingOfficialsField, "How many officials will attend? (1-10)");
            int attendingCount = 1; // Default to 1
            if (!officialsText.isEmpty()) {
                try {
                    attendingCount = Integer.parseInt(officialsText);
                } catch (NumberFormatException e) {
                    attendingCount = 1;
                }
            }
            event.setAttendingOfficialsCount(attendingCount);
            
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
            currentEvent.setTitle(getFieldText(eventField, "Enter event name..."));
            currentEvent.setTime(getFieldText(timeField, "e.g., 2:00 PM, 10:30 AM"));
            currentEvent.setLocation(getFieldText(placeField, "Enter location..."));
            
            // Set attending officials count
            String officialsText = getFieldText(attendingOfficialsField, "How many officials will attend? (1-10)");
            int attendingCount = 1; // Default to 1
            if (!officialsText.isEmpty()) {
                try {
                    attendingCount = Integer.parseInt(officialsText);
                } catch (NumberFormatException e) {
                    attendingCount = 1;
                }
            }
            currentEvent.setAttendingOfficialsCount(attendingCount);
            
            if (DatabaseUtil.updateEvent(currentEvent)) {
                JOptionPane.showMessageDialog(this, "Event updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating event.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Updates the event data in memory and database without closing the dialog
     * Returns true if successful, false otherwise
     */
    private boolean updateEventData() {
        if (currentEvent != null && validateFields()) {
            currentEvent.setTitle(getFieldText(eventField, "Enter event name..."));
            currentEvent.setTime(getFieldText(timeField, "e.g., 2:00 PM, 10:30 AM"));
            currentEvent.setLocation(getFieldText(placeField, "Enter location..."));
            
            // Set attending officials count
            String officialsText = getFieldText(attendingOfficialsField, "How many officials will attend? (1-10)");
            int attendingCount = 1; // Default to 1
            if (!officialsText.isEmpty()) {
                try {
                    attendingCount = Integer.parseInt(officialsText);
                } catch (NumberFormatException e) {
                    attendingCount = 1;
                }
            }
            currentEvent.setAttendingOfficialsCount(attendingCount);
            
            return DatabaseUtil.updateEvent(currentEvent);
        }
        return false;
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
            // For edit mode, update event data without closing dialog
            if (updateEventData()) {
                // Only proceed to duty assignment if update was successful
                openDutyAssignmentDialog();
            }
            // If update fails, validation messages will be shown by updateEventData()
        } else {
            // For new events, we need to save first
            if (validateFields()) {
                CalendarScreen.Event event = new CalendarScreen.Event();
                event.setTitle(getFieldText(eventField, "Enter event name..."));
                event.setTime(getFieldText(timeField, "e.g., 2:00 PM, 10:30 AM"));
                event.setLocation(getFieldText(placeField, "Enter location..."));
                event.setDate(selectedDate);
                event.setCreatedBy(1);
                
                // Set attending officials count
                String officialsText = getFieldText(attendingOfficialsField, "How many officials will attend? (1-10)");
                int attendingCount = 1; // Default to 1
                if (!officialsText.isEmpty()) {
                    try {
                        attendingCount = Integer.parseInt(officialsText);
                    } catch (NumberFormatException e) {
                        attendingCount = 1;
                    }
                }
                event.setAttendingOfficialsCount(attendingCount);
                
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
            // If validation fails, error messages will be shown by validateFields()
        }
    }
    
    private void openDutyAssignmentDialog() {
        // Get the number of attending officials from the event object
        int numberOfOfficials = currentEvent.getAttendingOfficialsCount();
        
        // Fallback to parsing the field if the event doesn't have the count
        if (numberOfOfficials <= 0) {
            String officialsText = getFieldText(attendingOfficialsField, "How many officials will attend? (1-10)");
            
            if (!officialsText.isEmpty()) {
                try {
                    numberOfOfficials = Integer.parseInt(officialsText);
                } catch (NumberFormatException e) {
                    numberOfOfficials = 1; // Default to 1 if invalid
                }
            } else {
                numberOfOfficials = 1; // Default to 1 if empty
            }
        }
        
        // Create and show the duty assignment dialog (6.png)
        DutyAssignmentDialog dutyDialog = new DutyAssignmentDialog(this, currentEvent, numberOfOfficials, isEditMode);
        dutyDialog.setVisible(true);
    }
    
    private void openAttendanceDialog() {
        if (currentEvent != null && currentEvent.getId() > 0) {
            // Update event data first to ensure it's saved
            if (updateEventData()) {
                AttendanceLogDialog attendanceDialog = new AttendanceLogDialog(this, currentEvent);
                attendanceDialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please save the event first before managing attendance.", 
                "Event Not Saved", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private boolean validateFields() {
        // Check event field
        String eventText = getFieldText(eventField, "Enter event name...");
        if (eventText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an event title.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            eventField.requestFocus();
            return false;
        }
        
        // Validate time field
        String timeText = getFieldText(timeField, "e.g., 2:00 PM, 10:30 AM");
        if (!timeText.isEmpty() && !isValidTimeFormat(timeText)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid time format (e.g., 2:00 PM, 10:30 AM).", 
                "Invalid Time Format", 
                JOptionPane.WARNING_MESSAGE);
            timeField.requestFocus();
            return false;
        }
        
        // Check place field
        String placeText = getFieldText(placeField, "Enter location...");
        if (placeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a location.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            placeField.requestFocus();
            return false;
        }
        
        // Validate attending officials field
        String officialsText = getFieldText(attendingOfficialsField, "How many officials will attend? (1-10)");
        if (!officialsText.isEmpty() && !isValidOfficialsFormat(officialsText)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a number between 1 and 10 for the number of attending officials.", 
                "Invalid Number", 
                JOptionPane.WARNING_MESSAGE);
            attendingOfficialsField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Helper method to get actual text from field, excluding placeholder text
     */
    private String getFieldText(JTextField field, String placeholder) {
        String text = field.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }
    
    /**
     * Validates time format - accepts 12-hour format with AM/PM
     * Examples: 2:00 PM, 10:15 AM, 12:30 PM
     */
    private boolean isValidTimeFormat(String time) {
        if (time == null || time.trim().isEmpty()) {
            return true; // Empty time is valid
        }
        
        time = time.trim().toUpperCase();
        
        // Pattern for 12-hour format (H:MM AM/PM or HH:MM AM/PM)
        // Allow optional space before AM/PM and handle 12-hour format properly
        String pattern12 = "^(1[0-2]|0?[1-9]):[0-5][0-9]\\s?(AM|PM)$";
        
        return time.matches(pattern12);
    }
    
    /**
     * Validates attending officials count format
     * Accepts: only a single number from 1 to 10
     * Examples: "1", "5", "10"
     */
    private boolean isValidOfficialsFormat(String officials) {
        if (officials == null || officials.trim().isEmpty()) {
            return true; // Empty officials field is valid
        }
        
        officials = officials.trim();
        
        // Check if it's a single valid integer between 1 and 10
        try {
            int officialCount = Integer.parseInt(officials);
            if (officialCount < 1 || officialCount > 10) {
                return false; // Only numbers 1-10 are valid (max SK officials)
            }
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
        
        return true;
    }
} 