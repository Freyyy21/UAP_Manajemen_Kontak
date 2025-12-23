package com.uap.view;

import com.uap.model.Contact;

import javax.swing.*;
import java.awt.*;

public class ContactDetailDialog extends JDialog {

    public ContactDetailDialog(JFrame parent, Contact c) {
        super(parent, "Contact Detail", true);
        setSize(360, 300);
        setLocationRelativeTo(parent);

        JPanel root = new JPanel(new GridLayout(6, 2, 10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        root.add(label("Name"));
        root.add(value(c.getName()));

        root.add(label("Phone"));
        root.add(value(c.getPhone()));

        root.add(label("Email"));
        root.add(value(c.getEmail()));

        root.add(label("Category"));
        root.add(value(c.getCategory()));

        root.add(label("Favorite"));
        root.add(value(c.isFavorite() ? "Yes" : "No"));

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        root.add(new JLabel());
        root.add(close);

        add(root);
        setVisible(true);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }

    private JLabel value(String text) {
        JLabel l = new JLabel(text == null ? "-" : text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }
}
