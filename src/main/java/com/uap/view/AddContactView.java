package com.uap.view;

import com.uap.controller.ContactController;
import com.uap.model.Contact;
import com.uap.util.ValidationException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddContactView extends JFrame {
    private DashboardView parent;
    private ContactController contactController;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> categoryCombo;
    private JCheckBox favoriteCheck;

    public AddContactView(DashboardView parent, ContactController contactController) {
        this.parent = parent;
        this.contactController = contactController;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setTitle("Add New Contact - Contact Manager");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(600, 80));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Add New Contact");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Form panel with proper layout
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Name field
        formPanel.add(createFieldPanel("Name *", nameField = createTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Phone field
        formPanel.add(createFieldPanel("Phone *", phoneField = createTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Email field
        formPanel.add(createFieldPanel("Email", emailField = createTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Category field
        categoryCombo = new JComboBox<>(new String[]{"Friend", "Family", "School", "Work"});
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setBackground(Color.WHITE);
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        categoryCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(createFieldPanel("Category", categoryCombo));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Favorite checkbox
        favoriteCheck = new JCheckBox("Mark as Favorite ⭐");
        favoriteCheck.setFont(new Font("Segoe UI", Font.BOLD, 14));
        favoriteCheck.setBackground(Color.WHITE);
        favoriteCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(favoriteCheck);

        // Wrapper for form
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(new Color(245, 245, 245));
        formWrapper.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel whiteCard = new JPanel(new BorderLayout());
        whiteCard.setBackground(Color.WHITE);
        whiteCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        whiteCard.add(formPanel, BorderLayout.CENTER);

        formWrapper.add(whiteCard, BorderLayout.CENTER);
        mainPanel.add(formWrapper, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        buttonPanel.setBackground(new Color(33, 150, 243));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));

        JButton cancelButton = createStyledButton("Cancel", new Color(216, 19, 19), Color.WHITE);
        cancelButton.addActionListener(e -> handleCancel());
        buttonPanel.add(cancelButton);

        JButton saveButton = createStyledButton("Save Contact", new Color(3, 242, 51), Color.WHITE);
        saveButton.addActionListener(e -> handleSave());
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        Color hoverColor = bgColor.darker();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void handleCancel() {
        int result = JOptionPane.showConfirmDialog(this,
                "Discard this contact?",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            parent.setVisible(true);
            dispose();
        }
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        boolean favorite = favoriteCheck.isSelected();

        try {
            contactController.createContact(name, phone, email, category, favorite);

            JOptionPane.showMessageDialog(this,
                    "Contact added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            parent.refreshTable();
            parent.setVisible(true);
            dispose();

        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to add contact: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}