package com.mycompany.sk.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Welcome screen for the SK Calendar system - shows main navigation options
 */
public class WelcomeScreen extends JFrame {
    
    private JButton calendarButton;
    private JButton profilesButton;
    private User currentUser;
    
    public WelcomeScreen(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("SK Calendar - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set background color to white/light gray
        getContentPane().setBackground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main content panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Content panel with logo and text
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Left side - SK Logo
        JPanel logoPanel = createLogoPanel();
        contentPanel.add(logoPanel, BorderLayout.WEST);
        
        // Right side - Welcome text and buttons
        JPanel textPanel = createTextPanel();
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setPreferredSize(new Dimension(350, 400));
        
        // Load and display the large SK logo
        ImageIcon logoIcon = ImageUtil.loadScaledImage("sk-logo.png", 300, 300);
        if (logoIcon != null) {
            JLabel logoLabel = new JLabel(logoIcon);
            logoPanel.add(logoLabel);
        } else {
            // Fallback if logo can't be loaded
            JLabel placeholderLabel = new JLabel("SK LOGO");
            placeholderLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 24));
            placeholderLabel.setForeground(new Color(25, 57, 94));
            logoPanel.add(placeholderLabel);
        }
        
        return logoPanel;
    }
    
    private JPanel createTextPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Welcome TO text
        JLabel welcomeLabel = new JLabel("WELCOME TO");
        welcomeLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 48));
        welcomeLabel.setForeground(new Color(25, 57, 94)); // Dark blue
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(welcomeLabel);
        
        textPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // SK CALENDAR text
        JLabel titleLabel = new JLabel("SK CALENDAR");
        titleLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 60));
        titleLabel.setForeground(new Color(255, 193, 7)); // Yellow/orange
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(titleLabel);
        
        textPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        
        // PROCEED TO text
        JLabel proceedLabel = new JLabel("PROCEED TO");
        proceedLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 36));
        proceedLabel.setForeground(Color.BLACK);
        proceedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(proceedLabel);
        
        textPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Navigation buttons
        JPanel buttonPanel = createButtonPanel();
        textPanel.add(buttonPanel);
        
        // Add some bottom spacing
        textPanel.add(Box.createVerticalGlue());
        
        return textPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        
        // CALENDAR button
        calendarButton = createStyledButton("CALENDAR");
        calendarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(calendarButton);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // SK PROFILES button
        profilesButton = createStyledButton("SK PROFILES");
        profilesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(profilesButton);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fill background with rounded corners
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setMaximumSize(new Dimension(400, 70));
        button.setPreferredSize(new Dimension(400, 70));
        button.setFont(FontUtil.getAzoSansFont(Font.BOLD, 24));
        button.setBackground(new Color(190, 190, 190)); // Gray background
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(170, 170, 170)); // Darker gray on hover
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(190, 190, 190)); // Original gray
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Calendar button action
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCalendarView();
            }
        });
        
        // SK Profiles button action
        profilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProfilesView();
            }
        });
    }
    
    private void openCalendarView() {
        // Open the Calendar screen
        SwingUtilities.invokeLater(() -> {
            CalendarScreen calendarScreen = new CalendarScreen();
            calendarScreen.setVisible(true);
        });
    }
    
    private void openProfilesView() {
        // Open the SK Profile management screen
        SwingUtilities.invokeLater(() -> {
            SKProfileScreen profileScreen = new SKProfileScreen();
            profileScreen.setVisible(true);
        });
    }
    
    /**
     * Get the current logged-in user
     * @return User object
     */
    public User getCurrentUser() {
        return currentUser;
    }
} 