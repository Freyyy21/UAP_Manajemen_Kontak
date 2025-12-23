package com.uap.view;

import com.uap.controller.Service.UserService;
import com.uap.model.Contact;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContactManagerView extends JFrame {

    private final UserService service = new UserService();

    private JTable table;
    private DefaultTableModel model;
    private List<Contact> current;

    // Form fields
    private JTextField tfName, tfPhone, tfEmail;
    private JComboBox<String> cbCategory;
    private JCheckBox cbFavorite;

    // Search & filter
    private JTextField tfSearch;
    private JComboBox<String> filterCategory;
    private JCheckBox filterFavorite;

    public ContactManagerView() {
        applyTheme();

        setTitle("Contact Manager");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        add(buildSearchBar(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        add(buildForm(), BorderLayout.EAST);

        load(service.listContacts());
        setVisible(true);
    }

    /* ================= SEARCH & FILTER ================= */

    private JPanel buildSearchBar() {
        JPanel p = new JPanel(new GridLayout(1, 4, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        tfSearch = new JTextField();
        tfSearch.setBorder(BorderFactory.createTitledBorder("Search Name / Phone"));

        filterCategory = new JComboBox<>(new String[]{
                "All", "Family", "Friend", "Work", "School", "Other"
        });
        filterCategory.setBorder(BorderFactory.createTitledBorder("Category"));

        filterFavorite = new JCheckBox("★ Favorite Only");

        JButton apply = button("Apply Filter", this::applyFilter);

        p.add(tfSearch);
        p.add(filterCategory);
        p.add(filterFavorite);
        p.add(apply);

        return p;
    }

    /* ================= TABLE ================= */

    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new Object[]{"Name", "Phone", "Email", "Category", "★"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));


        table.getSelectionModel().addListSelectionListener(e -> fillForm());

        return new JScrollPane(table);
    }

    /* ================= FORM CRUD ================= */

    private JPanel buildForm() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(320, 0));
        p.setLayout(new GridLayout(0, 1, 10, 10));
        p.setBorder(BorderFactory.createTitledBorder("Contact Detail"));

        tfName = field("Name");
        tfPhone = field("Phone");
        tfEmail = field("Email");

        cbCategory = new JComboBox<>(new String[]{
                "Family", "Friend", "Work", "School", "Other"
        });
        cbCategory.setBorder(BorderFactory.createTitledBorder("Category"));

        cbFavorite = new JCheckBox(" Favorite");

        p.add(tfName);
        p.add(tfPhone);
        p.add(tfEmail);
        p.add(cbCategory);
        p.add(cbFavorite);

        p.add(button("Add Contact", this::add));
        p.add(button("Update Contact", this::update));
        p.add(button("Delete Contact", this::delete));

        return p;
    }

    /* ================= LOGIC ================= */

    private void load(List<Contact> list) {
        current = list;
        model.setRowCount(0);

        for (Contact c : list) {
            model.addRow(new Object[]{
                    c.getName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getCategory(),
                    c.isFavorite() ? "★" : ""
            });
        }
    }

    private void fillForm() {
        int i = table.getSelectedRow();
        if (i < 0) return;

        Contact c = current.get(i);
        tfName.setText(c.getName());
        tfPhone.setText(c.getPhone());
        tfEmail.setText(c.getEmail());
        cbCategory.setSelectedItem(c.getCategory());
        cbFavorite.setSelected(c.isFavorite());
    }

    private void add() {
        try {
            service.createContact(
                    tfName.getText(),
                    tfPhone.getText(),
                    tfEmail.getText(),
                    cbCategory.getSelectedItem().toString(),
                    cbFavorite.isSelected()
            );
            refresh();
        } catch (Exception e) {
            alert(e.getMessage());
        }
    }

    private void update() {
        int i = table.getSelectedRow();
        if (i < 0) return;

        Contact c = current.get(i);
        try {
            service.updateContact(
                    c.getId(),
                    tfName.getText(),
                    tfPhone.getText(),
                    tfEmail.getText(),
                    cbCategory.getSelectedItem().toString(),
                    cbFavorite.isSelected()
            );
            refresh();
        } catch (Exception e) {
            alert(e.getMessage());
        }
    }

    private void delete() {
        int i = table.getSelectedRow();
        if (i < 0) return;

        service.deleteContact(current.get(i).getId());
        refresh();
    }

    private void applyFilter() {
        List<Contact> result = service.searchContacts(tfSearch.getText());

        if (!filterCategory.getSelectedItem().equals("All")) {
            String cat = filterCategory.getSelectedItem().toString();
            result = result.stream()
                    .filter(c -> cat.equals(c.getCategory()))
                    .toList();
        }

        if (filterFavorite.isSelected()) {
            result = result.stream()
                    .filter(Contact::isFavorite)
                    .toList();
        }

        load(result);
    }

    private void refresh() {
        load(service.listContacts());
        clear();
    }

    /* ================= UTIL ================= */

    private JTextField field(String title) {
        JTextField f = new JTextField();
        f.setBorder(BorderFactory.createTitledBorder(title));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return f;
    }

    private JButton button(String text, Runnable r) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(0, 42));
        b.setFocusPainted(false);
        b.addActionListener(e -> r.run());
        return b;
    }

    private void clear() {
        tfName.setText("");
        tfPhone.setText("");
        tfEmail.setText("");
        cbCategory.setSelectedIndex(0);
        cbFavorite.setSelected(false);
    }

    private void alert(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void applyTheme() {
        UIManager.put("Panel.background", new Color(245, 247, 250));
        UIManager.put("Button.background", new Color(59, 130, 246));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 15));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
    }
}
