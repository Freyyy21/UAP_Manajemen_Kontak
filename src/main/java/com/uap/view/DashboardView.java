package com.uap.view;

import com.uap.controller.ContactController;
import com.uap.model.Contact;
import com.uap.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardView extends JFrame {
    private User currentUser;
    private ContactController contactController;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JCheckBox favoriteFilter;
    private TableRowSorter<DefaultTableModel> sorter;

    public DashboardView(User user) {
        this.currentUser = user;
        this.contactController = new ContactController();
        initComponents();
        loadContacts();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Contact Manager - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(33, 150, 243));
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(1000, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Left side - Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Contact Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);

        panel.add(leftPanel, BorderLayout.WEST);

        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        rightPanel.add(userLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setForeground(new Color(33, 150, 243));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> handleLogout());
        rightPanel.add(logoutButton);

        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorderPainted(false);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Filter and search panel
        JPanel filterPanel = createFilterPanel();
        panel.add(filterPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = createTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(15, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Left side - Search and filters
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        // Search field
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        leftPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilters();
            }
        });
        leftPanel.add(searchField);

        // Category filter
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        leftPanel.add(categoryLabel);

        categoryFilter = new JComboBox<>(new String[]{"All", "Friend", "Family", "School", "Work"});
        categoryFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryFilter.setBackground(Color.WHITE);
        categoryFilter.addActionListener(e -> applyFilters());
        leftPanel.add(categoryFilter);

        // Favorite filter
        favoriteFilter = new JCheckBox("Favorites Only");
        favoriteFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        favoriteFilter.setOpaque(false);
        favoriteFilter.addActionListener(e -> applyFilters());
        leftPanel.add(favoriteFilter);

        panel.add(leftPanel, BorderLayout.WEST);

        // Right side - Add button
        JButton addButton = new JButton("+ Add Contact");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> openAddContactView());

        addButton.setOpaque(true);
        addButton.setContentAreaFilled(true);
        addButton.setBorderPainted(false);

        addButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(new Color(67, 160, 71));
            }
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(new Color(76, 175, 80));
            }
        });

        panel.add(addButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Table
        String[] columnNames = {"Name", "Category", "Favorite"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        contactTable = new JTable(tableModel);
        contactTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contactTable.setRowHeight(45);
        contactTable.setSelectionBackground(new Color(227, 242, 253));
        contactTable.setSelectionForeground(Color.BLACK);
        contactTable.setShowGrid(false);
        contactTable.setIntercellSpacing(new Dimension(0, 0));
        contactTable.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Header styling
        contactTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        contactTable.getTableHeader().setBackground(new Color(250, 250, 250));
        contactTable.getTableHeader().setForeground(new Color(66, 66, 66));
        contactTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(224, 224, 224)));
        contactTable.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Column widths
        contactTable.getColumnModel().getColumn(0).setPreferredWidth(400);
        contactTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        contactTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Double click listener
        contactTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = contactTable.getSelectedRow();
                    if (row != -1) {
                        openContactDetailView(row);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadContacts() {
        tableModel.setRowCount(0);
        List<Contact> contacts = contactController.getAllContacts();

        for (Contact contact : contacts) {
            Object[] row = {
                    contact.getName(),
                    contact.getCategory(),
                    contact.isFavorite() ? "★ Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }

    private void applyFilters() {
        List<Contact> allContacts = contactController.getAllContacts();
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        boolean favoritesOnly = favoriteFilter.isSelected();

        tableModel.setRowCount(0);

        for (Contact contact : allContacts) {
            // Apply search filter
            boolean matchesSearch = searchText.isEmpty() ||
                    contact.getName().toLowerCase().contains(searchText) ||
                    contact.getPhone().toLowerCase().contains(searchText);

            // Apply category filter
            boolean matchesCategory = selectedCategory.equals("All") ||
                    contact.getCategory().equalsIgnoreCase(selectedCategory);

            // Apply favorite filter
            boolean matchesFavorite = !favoritesOnly || contact.isFavorite();

            if (matchesSearch && matchesCategory && matchesFavorite) {
                Object[] row = {
                        contact.getName(),
                        contact.getCategory(),
                        contact.isFavorite() ? "★ Yes" : "No"
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openAddContactView() {
        AddContactView addView = new AddContactView(this, contactController);
        setVisible(false); // Sembunyikan dashboard
        addView.setVisible(true);
    }

    private void openContactDetailView(int row) {
        List<Contact> contacts = contactController.getAllContacts();
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        boolean favoritesOnly = favoriteFilter.isSelected();

        // Find the actual contact based on filters
        int actualIndex = 0;
        for (Contact contact : contacts) {
            boolean matchesSearch = searchText.isEmpty() ||
                    contact.getName().toLowerCase().contains(searchText) ||
                    contact.getPhone().toLowerCase().contains(searchText);
            boolean matchesCategory = selectedCategory.equals("All") ||
                    contact.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesFavorite = !favoritesOnly || contact.isFavorite();

            if (matchesSearch && matchesCategory && matchesFavorite) {
                if (actualIndex == row) {
                    ContactDetailView detailView = new ContactDetailView(this, contactController, contact);
                    setVisible(false); // Sembunyikan dashboard
                    detailView.setVisible(true);
                    break;
                }
                actualIndex++;
            }
        }
    }

    public void refreshTable() {
        applyFilters();
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                new LoginView().setVisible(true);
                dispose();
            });
        }
    }
}