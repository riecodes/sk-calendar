package com.mycompany.sk.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Attendance Log Dialog matching the 7.png mockup design
 * Allows tracking attendance for SK officials at specific events
 */
public class AttendanceLogDialog extends JDialog {
    
    private CalendarScreen.Event event;
    private JLabel eventLabel;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton backButton;
    
    // Attendance tracking
    private Map<String, ButtonGroup> attendanceGroups;
    private Map<String, JRadioButton> presentButtons;
    private Map<String, JRadioButton> absentButtons;
    private Map<String, JRadioButton> excuseButtons;
    
    // SK Officials (from profiles)
    private String[] skMembers = {
        "Juan Dela Cruz",
        "Maria Santos",
        "Pedro Rodriguez"
    };
    
    public AttendanceLogDialog(Dialog parent, CalendarScreen.Event event) {
        super(parent, "Attendance Log", true);
        this.event = event;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents() {
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Initialize attendance tracking maps
        attendanceGroups = new HashMap<>();
        presentButtons = new HashMap<>();
        absentButtons = new HashMap<>();
        excuseButtons = new HashMap<>();
        
        // Create event label
        eventLabel = new JLabel();
        eventLabel.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 18));
        eventLabel.setForeground(new Color(33, 37, 41));
        eventLabel.setPreferredSize(new Dimension(500, 50));
        eventLabel.setOpaque(true);
        eventLabel.setBackground(new Color(200, 200, 200));
        eventLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Set event text
        if (event != null) {
            eventLabel.setText(event.getTitle());
        }
        
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
        JLabel eventTitleLabel = createStyledLabel("EVENT:");
        mainPanel.add(eventTitleLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 0);
        mainPanel.add(eventLabel, gbc);
        
        // Attendance table header
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 0, 10, 0);
        JPanel headerRowPanel = createAttendanceHeader();
        mainPanel.add(headerRowPanel, gbc);
        
        // Attendance rows for each member
        for (int i = 0; i < skMembers.length; i++) {
            gbc.gridx = 0; gbc.gridy = 2 + i;
            gbc.gridwidth = 2;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 0, 5, 0);
            JPanel memberRow = createAttendanceRow(skMembers[i]);
            mainPanel.add(memberRow, gbc);
        }
        
        return mainPanel;
    }
    
    private JPanel createAttendanceHeader() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // NAME column
        gbc.gridx = 0; gbc.weightx = 0.4;
        JLabel nameHeader = new JLabel("NAME");
        nameHeader.setFont(FontUtil.getAzoSansFont(Font.BOLD, 18));
        nameHeader.setForeground(new Color(33, 37, 41));
        headerPanel.add(nameHeader, gbc);
        
        // PRESENT column
        gbc.gridx = 1; gbc.weightx = 0.2;
        JLabel presentHeader = new JLabel("PRESENT");
        presentHeader.setFont(FontUtil.getAzoSansFont(Font.BOLD, 18));
        presentHeader.setForeground(new Color(33, 37, 41));
        headerPanel.add(presentHeader, gbc);
        
        // ABSENT column
        gbc.gridx = 2; gbc.weightx = 0.2;
        JLabel absentHeader = new JLabel("ABSENT");
        absentHeader.setFont(FontUtil.getAzoSansFont(Font.BOLD, 18));
        absentHeader.setForeground(new Color(33, 37, 41));
        headerPanel.add(absentHeader, gbc);
        
        // EXCUSE column
        gbc.gridx = 3; gbc.weightx = 0.2;
        JLabel excuseHeader = new JLabel("EXCUSE");
        excuseHeader.setFont(FontUtil.getAzoSansFont(Font.BOLD, 18));
        excuseHeader.setForeground(new Color(33, 37, 41));
        headerPanel.add(excuseHeader, gbc);
        
        return headerPanel;
    }
    
    private JPanel createAttendanceRow(String memberName) {
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBackground(new Color(240, 240, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // NAME field
        gbc.gridx = 0; gbc.weightx = 0.4;
        JLabel nameField = new JLabel();
        nameField.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 16));
        nameField.setForeground(new Color(33, 37, 41));
        nameField.setPreferredSize(new Dimension(200, 40));
        nameField.setOpaque(true);
        nameField.setBackground(new Color(200, 200, 200));
        nameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        nameField.setText(memberName);
        rowPanel.add(nameField, gbc);
        
        // Create button group for this member
        ButtonGroup attendanceGroup = new ButtonGroup();
        attendanceGroups.put(memberName, attendanceGroup);
        
        // PRESENT radio button
        gbc.gridx = 1; gbc.weightx = 0.2;
        JRadioButton presentButton = createStyledRadioButton();
        presentButtons.put(memberName, presentButton);
        attendanceGroup.add(presentButton);
        rowPanel.add(presentButton, gbc);
        
        // ABSENT radio button
        gbc.gridx = 2; gbc.weightx = 0.2;
        JRadioButton absentButton = createStyledRadioButton();
        absentButtons.put(memberName, absentButton);
        attendanceGroup.add(absentButton);
        rowPanel.add(absentButton, gbc);
        
        // EXCUSE radio button
        gbc.gridx = 3; gbc.weightx = 0.2;
        JRadioButton excuseButton = createStyledRadioButton();
        excuseButtons.put(memberName, excuseButton);
        attendanceGroup.add(excuseButton);
        rowPanel.add(excuseButton, gbc);
        
        return rowPanel;
    }
    
    private JRadioButton createStyledRadioButton() {
        JRadioButton radioButton = new JRadioButton();
        radioButton.setBackground(new Color(240, 240, 240));
        radioButton.setPreferredSize(new Dimension(30, 30));
        radioButton.setFocusPainted(false);
        radioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return radioButton;
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
        saveButton.addActionListener(e -> saveAttendance());
        updateButton.addActionListener(e -> updateAttendance());
        deleteButton.addActionListener(e -> deleteAttendance());
        backButton.addActionListener(e -> dispose());
    }
    
    private void saveAttendance() {
        Map<String, String> attendanceData = collectAttendanceData();
        
        // Save attendance data to database
        // For now, just show success message
        StringBuilder summary = new StringBuilder("Attendance saved:\n");
        for (Map.Entry<String, String> entry : attendanceData.entrySet()) {
            summary.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        JOptionPane.showMessageDialog(this, summary.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateAttendance() {
        Map<String, String> attendanceData = collectAttendanceData();
        
        // Update attendance data in database
        JOptionPane.showMessageDialog(this, "Attendance updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteAttendance() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this attendance record?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Delete attendance record from database
            JOptionPane.showMessageDialog(this, "Attendance record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
    
    private Map<String, String> collectAttendanceData() {
        Map<String, String> attendanceData = new HashMap<>();
        
        for (String member : skMembers) {
            String status = "Not Set";
            if (presentButtons.get(member).isSelected()) {
                status = "Present";
            } else if (absentButtons.get(member).isSelected()) {
                status = "Absent";
            } else if (excuseButtons.get(member).isSelected()) {
                status = "Excuse";
            }
            attendanceData.put(member, status);
        }
        
        return attendanceData;
    }
} 