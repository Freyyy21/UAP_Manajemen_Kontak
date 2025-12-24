package com.uap.view;

import com.uap.controller.LoginController;
import com.uap.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private LoginController loginController;

    public LoginView() {
        loginController = new LoginController();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Contact Manager - Login");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(33, 150, 243);
                Color color2 = new Color(21, 101, 192);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);

        // Logo/Title area
        JLabel titleLabel = new JLabel("Contact Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(70, 40, 350, 40);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Manage your contacts efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setBounds(115, 85, 250, 20);
        mainPanel.add(subtitleLabel);

        // Login card
        JPanel loginCard = new JPanel();
        loginCard.setBackground(Color.WHITE);
        loginCard.setBounds(50, 140, 350, 320);
        loginCard.setLayout(null);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(33, 33, 33));
        welcomeLabel.setBounds(30, 20, 290, 30);
        loginCard.add(welcomeLabel);

        JLabel instructionLabel = new JLabel("Please login to continue");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructionLabel.setForeground(new Color(117, 117, 117));
        instructionLabel.setBounds(30, 50, 290, 20);
        loginCard.add(instructionLabel);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(new Color(66, 66, 66));
        usernameLabel.setBounds(30, 90, 290, 20);
        loginCard.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBounds(30, 115, 290, 40);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginCard.add(usernameField);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(66, 66, 66));
        passwordLabel.setBounds(30, 165, 290, 20);
        loginCard.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBounds(30, 190, 290, 40);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginCard.add(passwordField);

        // Login button
        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setBounds(30, 250, 290, 45);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(false);

        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(25, 118, 210));
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(33, 150, 243));
            }
        });

        loginButton.addActionListener(e -> handleLogin());
        loginCard.add(loginButton);

        // Demo credentials info
        JLabel demoLabel = new JLabel("<html><center>Demo Accounts:<br/>fazel / fazel123<br/>faizul / faizul123</center></html>");
        demoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        demoLabel.setForeground(new Color(117, 117, 117));
        demoLabel.setBounds(30, 305, 290, 50);
        demoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginCard.add(demoLabel);

        mainPanel.add(loginCard);

        // Footer
        JLabel footerLabel = new JLabel("Contact Manager. All rights reserved.");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(255, 255, 255, 150));
        footerLabel.setBounds(100, 480, 300, 20);
        mainPanel.add(footerLabel);

        add(mainPanel);

        // Enter key listener
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            User user = loginController.authenticate(username, password);

            // Show success message
            JOptionPane.showMessageDialog(this,
                    "Welcome, " + user.getUsername() + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            // Open dashboard
            SwingUtilities.invokeLater(() -> {
                new DashboardView(user).setVisible(true);
                dispose();
            });

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
            passwordField.setText("");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
    }

}