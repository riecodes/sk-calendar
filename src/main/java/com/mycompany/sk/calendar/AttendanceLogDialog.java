package com.mycompany.sk.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Attendance Log Dialog matching the 7.png mockup design
 * Following CalendarScreen design patterns
 */
public class AttendanceLogDialog extends JDialog {
    
    private CalendarScreen.Event event;
    private JTextField eventField;
    private JPanel attendancePanel;
    private JButton saveButton;
    private JButton backButton;
    
    // Store attendance data
    private Map<Integer, String> attendanceData; // profileId -> status
    private List<SKProfile> assignedOfficials;
    
    public AttendanceLogDialog(Frame parent, CalendarScreen.Event event) {
        super(parent, "Attendance Log", true);
        this.event = event;
        this.attendanceData = new HashMap<>();
        
        initializeComponents();
        setupLayout();
        loadAttendanceData();
        setupEventHandlers();
        
        setSize(900, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    public AttendanceLogDialog(Dialog parent, CalendarScreen.Event event) {
        super(parent, "Attendance Log", true);
        this.event = event;
        this.attendanceData = new HashMap<>();
        
        initializeComponents();
        setupLayout();
        loadAttendanceData();
        setupEventHandlers();
        
        setSize(900, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents() {
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Create event field (non-editable, showing event name)
        eventField = createStyledTextField();
        eventField.setText(event.getTitle());
        eventField.setEditable(false);
        eventField.setForeground(new Color(120, 120, 120)); // Grayed out
        
        // Create buttons matching CalendarScreen style
        saveButton = createStyledButton("SAVE", new Color(40, 167, 69));
        backButton = createStyledButton("BACK", new Color(108, 117, 125));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
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
        
        JLabel titleLabel = new JLabel("ATTENDANCE LOG");
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
        
        // Attendance table
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 20, 0);
        
        attendancePanel = createAttendancePanel();
        JScrollPane scrollPane = new JScrollPane(attendancePanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, gbc);
        
        return mainPanel;
    }
    
    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header row
        JPanel headerRow = createHeaderRow();
        panel.add(headerRow);
        panel.add(Box.createVerticalStrut(10));
        
        return panel;
    }
    
    private JPanel createHeaderRow() {
        JPanel headerRow = new JPanel(new GridLayout(1, 5, 10, 0));
        headerRow.setBackground(new Color(25, 57, 94));
        headerRow.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        String[] headers = {"NAME", "POSITION", "PRESENT", "ABSENT", "EXCUSE"};
        for (String header : headers) {
            JLabel headerLabel = new JLabel(header);
            headerLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 18));
            headerLabel.setForeground(Color.WHITE);
            headerLabel.setHorizontalAlignment(JLabel.CENTER);
            headerRow.add(headerLabel);
        }
        
        return headerRow;
    }
    
    private JPanel createOfficialRow(SKProfile official, String currentStatus) {
        JPanel row = new JPanel(new GridLayout(1, 5, 10, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Name label
        JLabel nameLabel = new JLabel(official.getName());
        nameLabel.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 16));
        nameLabel.setForeground(new Color(33, 37, 41));
        nameLabel.setHorizontalAlignment(JLabel.LEFT);
        row.add(nameLabel);
        
        // Position label
        JLabel positionLabel = new JLabel(official.getPosition());
        positionLabel.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 16));
        positionLabel.setForeground(new Color(108, 117, 125));
        positionLabel.setHorizontalAlignment(JLabel.LEFT);
        row.add(positionLabel);
        
        // Radio buttons for attendance status
        ButtonGroup attendanceGroup = new ButtonGroup();
        
        JRadioButton presentButton = createStyledRadioButton("Present");
        JRadioButton absentButton = createStyledRadioButton("Absent");
        JRadioButton excuseButton = createStyledRadioButton("Excuse");
        
        attendanceGroup.add(presentButton);
        attendanceGroup.add(absentButton);
        attendanceGroup.add(excuseButton);
        
        // Set current status
        switch (currentStatus.toLowerCase()) {
            case "present":
                presentButton.setSelected(true);
                break;
            case "absent":
                absentButton.setSelected(true);
                break;
            case "excused":
                excuseButton.setSelected(true);
                break;
            default:
                presentButton.setSelected(true); // Default to present
                break;
        }
        
        // Add action listeners to update attendance data
        presentButton.addActionListener(e -> {
            if (presentButton.isSelected()) {
                attendanceData.put(official.getId(), "present");
            }
        });
        
        absentButton.addActionListener(e -> {
            if (absentButton.isSelected()) {
                attendanceData.put(official.getId(), "absent");
            }
        });
        
        excuseButton.addActionListener(e -> {
            if (excuseButton.isSelected()) {
                attendanceData.put(official.getId(), "excused");
            }
        });
        
        // Add radio button panels
        row.add(createRadioButtonPanel(presentButton));
        row.add(createRadioButtonPanel(absentButton));
        row.add(createRadioButtonPanel(excuseButton));
        
        return row;
    }
    
    private JPanel createRadioButtonPanel(JRadioButton radioButton) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.add(radioButton);
        return panel;
    }
    
    private JRadioButton createStyledRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton();
        radioButton.setBackground(Color.WHITE);
        radioButton.setFocusPainted(false);
        radioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        radioButton.setPreferredSize(new Dimension(20, 20));
        
        // Custom styling for radio button
        radioButton.setUI(new javax.swing.plaf.basic.BasicRadioButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                AbstractButton button = (AbstractButton) c;
                int size = 16;
                int x = (c.getWidth() - size) / 2;
                int y = (c.getHeight() - size) / 2;
                
                // Draw circle background
                g2.setColor(Color.WHITE);
                g2.fillOval(x, y, size, size);
                
                // Draw border
                g2.setColor(new Color(108, 117, 125));
                g2.drawOval(x, y, size, size);
                
                // Draw filled circle if selected
                if (button.isSelected()) {
                    g2.setColor(new Color(0, 123, 255));
                    g2.fillOval(x + 3, y + 3, size - 6, size - 6);
                }
                
                g2.dispose();
            }
        });
        
        return radioButton;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        
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
        label.setPreferredSize(new Dimension(150, 50));
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
        
        button.setPreferredSize(new Dimension(120, 50));
        button.setFont(FontUtil.getAzoSansFont(Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
    
    private void loadAttendanceData() {
        // Get assigned officials for this event
        assignedOfficials = DatabaseUtil.getAssignedOfficials(event.getId());
        
        if (assignedOfficials.isEmpty()) {
            JLabel noOfficialsLabel = new JLabel("No officials assigned to this event.");
            noOfficialsLabel.setFont(FontUtil.getCanvaSansFont(Font.ITALIC, 16));
            noOfficialsLabel.setForeground(new Color(108, 117, 125));
            noOfficialsLabel.setHorizontalAlignment(JLabel.CENTER);
            noOfficialsLabel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
            
            attendancePanel.removeAll();
            attendancePanel.add(noOfficialsLabel);
            attendancePanel.revalidate();
            attendancePanel.repaint();
            return;
        }
        
        // Load existing attendance records
        List<DatabaseUtil.AttendanceRecord> existingRecords = DatabaseUtil.getEventAttendance(event.getId());
        Map<Integer, String> existingAttendance = new HashMap<>();
        for (DatabaseUtil.AttendanceRecord record : existingRecords) {
            // Find profile by name (this is a limitation - ideally we'd have profile ID in attendance record)
            for (SKProfile profile : assignedOfficials) {
                if (profile.getName().equals(record.getOfficialName())) {
                    existingAttendance.put(profile.getId(), record.getAttendanceStatus());
                    attendanceData.put(profile.getId(), record.getAttendanceStatus());
                    break;
                }
            }
        }
        
        // Clear and rebuild attendance panel
        attendancePanel.removeAll();
        
        // Add header
        attendancePanel.add(createHeaderRow());
        attendancePanel.add(Box.createVerticalStrut(5));
        
        // Add official rows
        for (SKProfile official : assignedOfficials) {
            String currentStatus = existingAttendance.getOrDefault(official.getId(), "present");
            JPanel officialRow = createOfficialRow(official, currentStatus);
            attendancePanel.add(officialRow);
        }
        
        attendancePanel.revalidate();
        attendancePanel.repaint();
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> saveAttendance());
        backButton.addActionListener(e -> dispose());
    }
    
    private void saveAttendance() {
        if (attendanceData.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No attendance data to save.", 
                "No Data", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean success = true;
        for (Map.Entry<Integer, String> entry : attendanceData.entrySet()) {
            int profileId = entry.getKey();
            String status = entry.getValue();
            
            if (!DatabaseUtil.recordAttendance(event.getId(), profileId, status, "")) {
                success = false;
            }
        }
                
                if (success) {
            JOptionPane.showMessageDialog(this, 
                "Attendance saved successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                "Error saving attendance data.", 
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
        }
    }
} 