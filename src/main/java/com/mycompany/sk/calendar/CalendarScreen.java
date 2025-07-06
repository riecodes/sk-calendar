package com.mycompany.sk.calendar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

/**
 * Calendar screen for the SK Calendar system
 */
public class CalendarScreen extends JFrame {
    
    private LocalDate currentDate;
    private LocalDate selectedDate;
    private YearMonth currentMonth;
    
    // UI Components
    private JLabel monthYearLabel;
    private JButton prevMonthButton;
    private JButton nextMonthButton;
    private JPanel calendarGridPanel;
    private JPanel agendaPanel;
    private JTextArea eventsTextArea;
    private JButton addEventButton;
    private JButton backButton;
    
    // Calendar grid buttons
    private JButton[][] dayButtons;
    private final String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    
    // Events data
    private List<Event> events;
    
    public CalendarScreen() {
        currentDate = LocalDate.now();
        selectedDate = currentDate;
        currentMonth = YearMonth.from(currentDate);
        events = new ArrayList<>();
        
        initializeComponents();
        setupLayout();
        loadEvents();
        updateCalendar();
        setupEventHandlers();
        
        setTitle("SK Calendar - Event Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initializeComponents() {
        getContentPane().setBackground(new Color(25, 57, 94)); // Dark blue background
        
        // Initialize day buttons grid
        dayButtons = new JButton[6][7]; // 6 weeks, 7 days
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main content panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Footer with back button
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(25, 57, 94));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Calendar section (left side)
        JPanel calendarSection = createCalendarSection();
        mainPanel.add(calendarSection, BorderLayout.CENTER);
        
        // Agenda section (right side)
        JPanel agendaSection = createAgendaSection();
        mainPanel.add(agendaSection, BorderLayout.EAST);
        
        return mainPanel;
    }
    
    private JPanel createCalendarSection() {
        JPanel calendarSection = new JPanel(new BorderLayout());
        calendarSection.setBackground(new Color(25, 57, 94));
        calendarSection.setPreferredSize(new Dimension(750, 600));
        
        // Header with month/year navigation
        JPanel headerPanel = createCalendarHeader();
        calendarSection.add(headerPanel, BorderLayout.NORTH);
        
        // Calendar grid
        JPanel gridPanel = createCalendarGrid();
        calendarSection.add(gridPanel, BorderLayout.CENTER);
        
        return calendarSection;
    }
    
    private JPanel createCalendarHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 57, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Previous month button
        prevMonthButton = createNavigationButton("back.png");
        headerPanel.add(prevMonthButton, BorderLayout.WEST);
        
        // Month/Year label
        monthYearLabel = new JLabel();
        monthYearLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 48));
        monthYearLabel.setForeground(Color.WHITE);
        monthYearLabel.setHorizontalAlignment(JLabel.CENTER);
        headerPanel.add(monthYearLabel, BorderLayout.CENTER);
        
        // Next month button
        nextMonthButton = createNavigationButton("next.png");
        headerPanel.add(nextMonthButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JButton createNavigationButton(String imageName) {
        // Load image icon
        ImageIcon icon = ImageUtil.loadScaledImage(imageName, 40, 40);
        if (icon == null) {
            System.err.println("Could not load navigation image: " + imageName);
        }
        
        JButton button = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(60, 60));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
    
    private JPanel createCalendarGrid() {
        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setBackground(new Color(25, 57, 94));
        
        // Day headers (SUN, MON, TUE, etc.)
        JPanel dayHeadersPanel = createDayHeaders();
        gridContainer.add(dayHeadersPanel, BorderLayout.NORTH);
        
        // Calendar grid
        calendarGridPanel = new JPanel(new GridLayout(6, 7, 2, 2));
        calendarGridPanel.setBackground(new Color(25, 57, 94));
        calendarGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Create day buttons
        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                JButton dayButton = createDayButton();
                dayButtons[week][day] = dayButton;
                calendarGridPanel.add(dayButton);
            }
        }
        
        gridContainer.add(calendarGridPanel, BorderLayout.CENTER);
        
        return gridContainer;
    }
    
    private JPanel createDayHeaders() {
        JPanel headersPanel = new JPanel(new GridLayout(1, 7, 2, 0));
        headersPanel.setBackground(new Color(25, 57, 94));
        
        for (String dayName : dayNames) {
            JLabel dayLabel = new JLabel(dayName);
            dayLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 18));
            dayLabel.setForeground(new Color(255, 193, 7)); // Yellow headers
            dayLabel.setHorizontalAlignment(JLabel.CENTER);
            dayLabel.setPreferredSize(new Dimension(100, 40));
            dayLabel.setOpaque(true);
            dayLabel.setBackground(new Color(255, 193, 7, 30)); // Semi-transparent yellow
            dayLabel.setBorder(BorderFactory.createRaisedBevelBorder());
            headersPanel.add(dayLabel);
        }
        
        return headersPanel;
    }
    
    private JButton createDayButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Determine button state and colors
                Color bgColor = new Color(192, 192, 192); // Default gray
                Color textColor = new Color(33, 37, 41);
                
                if (isDaySelected()) {
                    bgColor = new Color(255, 193, 7); // Yellow for selected
                    textColor = new Color(33, 37, 41);
                } else if (getText().equals(String.valueOf(currentDate.getDayOfMonth())) && 
                          isCurrentMonthDay()) {
                    bgColor = new Color(40, 167, 69); // Green for today
                    textColor = Color.WHITE;
                } else if (hasEventsOnDay()) {
                    bgColor = new Color(0, 123, 255); // Blue for days with events
                    textColor = Color.WHITE;
                }
                
                // Draw background
                g2.setColor(bgColor);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                
                // Draw border
                g2.setColor(new Color(108, 117, 125));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                
                // Draw text
                if (!getText().isEmpty()) {
                    g2.setColor(textColor);
                    g2.setFont(FontUtil.getAzoSansFont(Font.BOLD, 16));
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2;
                    g2.drawString(getText(), x, y);
                }
                
                g2.dispose();
            }
            
            private boolean isDaySelected() {
                if (selectedDate == null || getText().isEmpty()) return false;
                try {
                    int day = Integer.parseInt(getText());
                    return selectedDate.getDayOfMonth() == day && 
                           selectedDate.getMonth() == currentMonth.getMonth() &&
                           selectedDate.getYear() == currentMonth.getYear();
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            
            private boolean isCurrentMonthDay() {
                return currentMonth.getMonth() == currentDate.getMonth() &&
                       currentMonth.getYear() == currentDate.getYear();
            }
            
            private boolean hasEventsOnDay() {
                if (getText().isEmpty()) return false;
                try {
                    int day = Integer.parseInt(getText());
                    LocalDate buttonDate = currentMonth.atDay(day);
                    return events.stream().anyMatch(event -> event.getDate().equals(buttonDate));
                } catch (Exception e) {
                    return false;
                }
            }
        };
        
        button.setPreferredSize(new Dimension(100, 80));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
    
    private JPanel createAgendaSection() {
        JPanel agendaSection = new JPanel(new BorderLayout());
        agendaSection.setBackground(new Color(192, 192, 192)); // Light gray background
        agendaSection.setPreferredSize(new Dimension(400, 600));
        agendaSection.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Agenda header
        JLabel agendaLabel = new JLabel("AGENDA:");
        agendaLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 32));
        agendaLabel.setForeground(new Color(25, 57, 94));
        agendaLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        agendaSection.add(agendaLabel, BorderLayout.NORTH);
        
        // Events display
        JPanel eventsPanel = createEventsPanel();
        agendaSection.add(eventsPanel, BorderLayout.CENTER);
        
        // Action buttons
        addEventButton = createAddEventButton();
        JButton editEventButton = createActionButton("EDIT EVENT", new Color(255, 193, 7));
        JButton attendanceLogButton = createActionButton("ATTENDANCE LOG", new Color(0, 123, 255));
        
        editEventButton.addActionListener(e -> openEditEventDialog());
        attendanceLogButton.addActionListener(e -> openAttendanceLogDialog());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(192, 192, 192));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(addEventButton);
        buttonPanel.add(editEventButton);
        buttonPanel.add(attendanceLogButton);
        agendaSection.add(buttonPanel, BorderLayout.SOUTH);
        
        return agendaSection;
    }
    
    private JPanel createEventsPanel() {
        JPanel eventsPanel = new JPanel(new BorderLayout());
        eventsPanel.setBackground(Color.WHITE);
        eventsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Events header
        JLabel eventsLabel = new JLabel("EVENTS");
        eventsLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 18));
        eventsLabel.setForeground(new Color(25, 57, 94));
        eventsPanel.add(eventsLabel, BorderLayout.NORTH);
        
        // Events text area
        eventsTextArea = new JTextArea();
        eventsTextArea.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 14));
        eventsTextArea.setEditable(false);
        eventsTextArea.setBackground(Color.WHITE);
        eventsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        eventsTextArea.setLineWrap(true);
        eventsTextArea.setWrapStyleWord(true);
        eventsTextArea.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add mouse listener for event editing
        eventsTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openEditEventDialog();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(eventsTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        eventsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return eventsPanel;
    }
    
    private JButton createAddEventButton() {
        return createActionButton("ADD EVENT", new Color(25, 57, 94));
    }
    
    private JButton createActionButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(140, 45));
        button.setFont(FontUtil.getAzoSansFont(Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(25, 57, 94));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        
        // Back button
        backButton = new JButton("BACK") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(108, 117, 125));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(FontUtil.getAzoSansFont(Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        
        footerPanel.add(backButton);
        
        return footerPanel;
    }
    
    private void setupEventHandlers() {
        // Month navigation
        prevMonthButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
            updateAgenda();
        });
        
        nextMonthButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
            updateAgenda();
        });
        
        // Day selection
        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                final int w = week, d = day;
                dayButtons[week][day].addActionListener(e -> {
                    String dayText = dayButtons[w][d].getText();
                    if (!dayText.isEmpty()) {
                        try {
                            int dayNum = Integer.parseInt(dayText);
                            selectedDate = currentMonth.atDay(dayNum);
                            updateCalendar();
                            updateAgenda();
                        } catch (Exception ex) {
                            // Ignore invalid day selections
                        }
                    }
                });
            }
        }
        
        // Add event button
        addEventButton.addActionListener(e -> openAddEventDialog());
        
        // Back button
        backButton.addActionListener(e -> {
            dispose();
            // Return to welcome screen - Note: In a real app, you'd pass the user object
            SwingUtilities.invokeLater(() -> {
                // For now, we'll just close this window
                // In a complete implementation, you'd need to pass the current user
                System.out.println("Returning to welcome screen...");
            });
        });
    }
    
    private void updateCalendar() {
        // Update month/year label
        monthYearLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase());
        
        // Clear all day buttons
        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                dayButtons[week][day].setText("");
            }
        }
        
        // Get first day of month and its day of week
        LocalDate firstDay = currentMonth.atDay(1);
        int startDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Convert to 0-6 (Sun-Sat)
        
        // Fill in the days
        int daysInMonth = currentMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            int week = (day + startDayOfWeek - 1) / 7;
            int dayOfWeek = (day + startDayOfWeek - 1) % 7;
            if (week < 6) {
                dayButtons[week][dayOfWeek].setText(String.valueOf(day));
            }
        }
        
        // Repaint calendar
        calendarGridPanel.repaint();
    }
    
    private void updateAgenda() {
        StringBuilder agendaText = new StringBuilder();
        
        if (selectedDate != null) {
            List<Event> dayEvents = events.stream()
                .filter(event -> event.getDate().equals(selectedDate))
                .toList();
            
            if (dayEvents.isEmpty()) {
                agendaText.append("No events scheduled for ")
                          .append(selectedDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")))
                          .append(".");
            } else {
                for (Event event : dayEvents) {
                    agendaText.append("\u2022 ").append(event.getTitle()).append("\n");
                }
            }
        }
        
        eventsTextArea.setText(agendaText.toString());
    }
    
    private void loadEvents() {
        // Load events from database
        events = DatabaseUtil.getAllEvents();
        updateAgenda();
    }
    
    private void openAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog(this, selectedDate);
        dialog.setVisible(true);
        
        // Refresh events after dialog closes
        loadEvents();
        updateCalendar();
        updateAgenda();
    }
    
    private void openEditEventDialog() {
        if (selectedDate == null) {
            return;
        }
        
        // Get events for the selected date
        List<Event> dayEvents = events.stream()
            .filter(event -> event.getDate().equals(selectedDate))
            .toList();
        
        if (dayEvents.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No events found for the selected date.", 
                "No Events", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Event eventToEdit;
        if (dayEvents.size() == 1) {
            // Only one event, edit it directly
            eventToEdit = dayEvents.get(0);
        } else {
            // Multiple events, let user choose
            String[] eventTitles = dayEvents.stream()
                .map(Event::getTitle)
                .toArray(String[]::new);
            
            String selectedTitle = (String) JOptionPane.showInputDialog(
                this,
                "Select an event to edit:",
                "Select Event",
                JOptionPane.QUESTION_MESSAGE,
                null,
                eventTitles,
                eventTitles[0]
            );
            
            if (selectedTitle == null) {
                return; // User cancelled
            }
            
            eventToEdit = dayEvents.stream()
                .filter(event -> event.getTitle().equals(selectedTitle))
                .findFirst()
                .orElse(null);
        }
        
        if (eventToEdit != null) {
            AddEventDialog dialog = new AddEventDialog(this, selectedDate, eventToEdit);
            dialog.setVisible(true);
            
            // Refresh events after dialog closes
            loadEvents();
            updateCalendar();
            updateAgenda();
        }
    }
    
    private void openAttendanceLogDialog() {
        if (selectedDate == null) {
            return;
        }
        
        // Get events for the selected date
        List<Event> dayEvents = events.stream()
            .filter(event -> event.getDate().equals(selectedDate))
            .toList();
        
        if (dayEvents.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No events found for the selected date.", 
                "No Events", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Event selectedEvent;
        if (dayEvents.size() == 1) {
            // Only one event, show attendance for it directly
            selectedEvent = dayEvents.get(0);
        } else {
            // Multiple events, let user choose
            String[] eventTitles = dayEvents.stream()
                .map(Event::getTitle)
                .toArray(String[]::new);
            
            String selectedTitle = (String) JOptionPane.showInputDialog(
                this,
                "Select an event to view attendance:",
                "Select Event",
                JOptionPane.QUESTION_MESSAGE,
                null,
                eventTitles,
                eventTitles[0]
            );
            
            if (selectedTitle == null) {
                return; // User cancelled
            }
            
            selectedEvent = dayEvents.stream()
                .filter(event -> event.getTitle().equals(selectedTitle))
                .findFirst()
                .orElse(null);
        }
        
        if (selectedEvent != null) {
            AttendanceLogDialog dialog = new AttendanceLogDialog(this, selectedEvent);
            dialog.setVisible(true);
        }
    }
    
    /**
     * Simple Event class for calendar events
     */
    public static class Event {
        private int id;
        private String title;
        private LocalDate date;
        private String time;
        private String location;
        private int attendingOfficialsCount;
        private int createdBy;
        
        public Event() {}
        
        public Event(String title, LocalDate date, String time, String location) {
            this.title = title;
            this.date = date;
            this.time = time;
            this.location = location;
            this.attendingOfficialsCount = 1; // Default to 1
        }
        
        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public int getAttendingOfficialsCount() { return attendingOfficialsCount; }
        public void setAttendingOfficialsCount(int attendingOfficialsCount) { this.attendingOfficialsCount = attendingOfficialsCount; }
        
        public int getCreatedBy() { return createdBy; }
        public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    }
} 