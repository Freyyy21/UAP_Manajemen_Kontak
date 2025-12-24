package com.uap.view;

import com.uap.controller.ContactController;
import com.uap.model.Contact;
import com.uap.util.ValidationException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContactDetailView extends JFrame {
    private DashboardView parent;
    private ContactController contactController;
    private Contact contact;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> categoryCombo;
    private JCheckBox favoriteCheck;
    private JButton leftButton;
    private JButton rightButton;
    private JButton deleteButton;
    private boolean editMode = false;

    // Constructor: menghubungkan view dengan parent, controller, dan data contact
    public ContactDetailView(DashboardView parent, ContactController contactController, Contact contact) {
        this.parent = parent;
        this.contactController = contactController;
        this.contact = contact;
        initComponents();
        loadContactData();
        setLocationRelativeTo(parent);
    }

    // Method untuk membangun seluruh tampilan UI (header, form, button)
    private void initComponents() {
        setTitle("Contact Details - Contact Manager");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(600, 80));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Contact Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Form panel
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
        categoryCombo.setBackground(new Color(245, 245, 245));
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        categoryCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryCombo.setEnabled(false);
        formPanel.add(createFieldPanel("Category", categoryCombo));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Favorite checkbox
        favoriteCheck = new JCheckBox("Mark as Favorite ⭐");
        favoriteCheck.setFont(new Font("Segoe UI", Font.BOLD, 14));
        favoriteCheck.setBackground(Color.WHITE);
        favoriteCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        favoriteCheck.setEnabled(false);
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
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Left side - Delete button
        deleteButton = createStyledButton("Delete Contact", new Color(244, 67, 54), Color.WHITE);
        deleteButton.addActionListener(e -> handleDelete());
        buttonPanel.add(deleteButton, BorderLayout.WEST);

        // Right side - Action buttons
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightButtonPanel.setOpaque(false);

        leftButton = createStyledButton("Back", new Color(158, 158, 158), Color.WHITE);
        leftButton.addActionListener(e -> handleLeftButton());
        rightButtonPanel.add(leftButton);

        rightButton = createStyledButton("Edit", new Color(255, 152, 0), Color.WHITE);
        rightButton.addActionListener(e -> handleRightButton());
        rightButtonPanel.add(rightButton);

        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Factory method untuk membuat JTextField dengan style konsisten
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setEditable(false);
        field.setBackground(new Color(245, 245, 245));
        return field;
    }

    // Membuat panel berisi label dan field (reusable)
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

    // Membuat tombol dengan warna dan efek hover
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
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

    // Mengisi data contact ke semua field
    private void loadContactData() {
        nameField.setText(contact.getName());
        phoneField.setText(contact.getPhone());
        emailField.setText(contact.getEmail());
        categoryCombo.setSelectedItem(contact.getCategory());
        favoriteCheck.setSelected(contact.isFavorite());
    }

    // Handler tombol kiri (Back atau Cancel)
    private void handleLeftButton() {
        if (editMode) {
            // Cancel edit
            int result = JOptionPane.showConfirmDialog(this,
                    "Discard changes?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                toggleEditMode();
                loadContactData();
            }
        } else {
            // Back to dashboard
            parent.setVisible(true);
            dispose();
        }
    }

    // Handler tombol kanan (Edit atau Save)
    private void handleRightButton() {
        if (editMode) {
            // Save changes
            handleSave();
        } else {
            // Enable edit mode
            toggleEditMode();
        }
    }

    // Mengubah state edit mode dan tampilan UI
    private void toggleEditMode() {
        editMode = !editMode;

        nameField.setEditable(editMode);
        phoneField.setEditable(editMode);
        emailField.setEditable(editMode);
        categoryCombo.setEnabled(editMode);
        favoriteCheck.setEnabled(editMode);

        if (editMode) {
            nameField.setBackground(Color.WHITE);
            phoneField.setBackground(Color.WHITE);
            emailField.setBackground(Color.WHITE);
            categoryCombo.setBackground(Color.WHITE);

            leftButton.setText("Cancel");
            leftButton.setBackground(new Color(158, 158, 158));

            rightButton.setText("Save");
            rightButton.setBackground(new Color(76, 175, 80));
        } else {
            nameField.setBackground(new Color(245, 245, 245));
            phoneField.setBackground(new Color(245, 245, 245));
            emailField.setBackground(new Color(245, 245, 245));
            categoryCombo.setBackground(new Color(245, 245, 245));

            leftButton.setText("Back");
            leftButton.setBackground(new Color(158, 158, 158));

            rightButton.setText("Edit");
            rightButton.setBackground(new Color(255, 152, 0));
        }
    }

    // Menyimpan perubahan data contact melalui controller
    private void handleSave() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        Boolean favorite = favoriteCheck.isSelected();

        try {
            contact = contactController.updateContact(
                    contact.getId(), name, phone, email, category, favorite
            );

            JOptionPane.showMessageDialog(this,
                    "Contact updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            toggleEditMode();
            parent.refreshTable();

        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to update contact: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menghapus contact dengan konfirmasi
    private void handleDelete() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this contact?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            try {
                contactController.deleteContact(contact.getId());

                JOptionPane.showMessageDialog(this,
                        "Contact deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                parent.refreshTable();
                parent.setVisible(true);
                dispose();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete contact: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}