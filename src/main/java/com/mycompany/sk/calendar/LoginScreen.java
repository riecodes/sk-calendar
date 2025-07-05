package com.mycompany.sk.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login screen for the SK Calendar system
 */
public class LoginScreen extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginScreen() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("SK Calendar - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set background color to match the mockup
        getContentPane().setBackground(new Color(220, 220, 220));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel with logo and title
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel with login form
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 57, 94)); // Dark blue background
        headerPanel.setPreferredSize(new Dimension(800, 80));
        
        // Create a left panel for logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftPanel.setBackground(new Color(25, 57, 94));
        
        // Load and add the SK logo
        ImageIcon logoIcon = ImageUtil.loadScaledImage("sk-logo.png", 50, 50);
        if (logoIcon != null) {
            JLabel logoLabel = new JLabel(logoIcon);
            leftPanel.add(logoLabel);
        }
        
        // Title label
        JLabel titleLabel = new JLabel("Barangay Account");
        titleLabel.setFont(FontUtil.getAzoSansFont(Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(220, 220, 220));
        
        // Add vertical spacing
        mainPanel.add(Box.createVerticalGlue());
        
        // Login title
        JLabel loginTitle = new JLabel("LOG IN");
        loginTitle.setFont(FontUtil.getAzoSansFont(Font.BOLD, 48));
        loginTitle.setForeground(new Color(25, 57, 94));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginTitle);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Username field
        usernameField = createStyledTextField("USERNAME");
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(usernameField);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password field
        passwordField = createStyledPasswordField("PASSWORD");
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordField);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Login button
        loginButton = createStyledButton("ENTER");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginButton);
        
        // Add vertical spacing
        mainPanel.add(Box.createVerticalGlue());
        
        return mainPanel;
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        field.setMaximumSize(new Dimension(400, 50));
        field.setPreferredSize(new Dimension(400, 50));
        field.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 16));
        field.setBackground(new Color(20, 116, 186)); // Blue background
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        field.setHorizontalAlignment(JTextField.LEFT);
        field.setOpaque(false); // Make transparent to show custom background
        
        // Add focus listener to clear placeholder
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
        
        return field;
    }
    
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        field.setMaximumSize(new Dimension(400, 50));
        field.setPreferredSize(new Dimension(400, 50));
        field.setFont(FontUtil.getCanvaSansFont(Font.PLAIN, 16));
        field.setBackground(new Color(20, 116, 186)); // Blue background
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        field.setEchoChar((char) 0); // Show placeholder text initially
        field.setOpaque(false); // Make transparent to show custom background
        
        // Add focus listener to handle placeholder
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('*'); // Enable password masking
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char) 0); // Disable password masking for placeholder
                    field.setText(placeholder);
                }
            }
        });
        
        return field;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fill background with rounded corners
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw border
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setMaximumSize(new Dimension(200, 50));
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(FontUtil.getCanvaSansFont(Font.BOLD, 16));
        button.setBackground(new Color(255, 193, 7)); // Yellow background
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
                button.setBackground(new Color(255, 213, 47));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 193, 7));
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Enter key in password field
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        
        // Clear placeholder text for validation
        if (username.equals("USERNAME")) username = "";
        if (password.equals("PASSWORD")) password = "";
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password.", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Authenticate with database
        User authenticatedUser = DatabaseUtil.authenticateUser(username, password);
        
        if (authenticatedUser != null) {
            // Open the welcome screen with the authenticated user
            SwingUtilities.invokeLater(() -> {
                WelcomeScreen welcomeScreen = new WelcomeScreen(authenticatedUser);
                welcomeScreen.setVisible(true);
            });
            
            // Close login window
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password.", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            
            // Clear password field
            passwordField.setText("PASSWORD");
            passwordField.setEchoChar((char) 0);
        }
    }
} 