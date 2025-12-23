package com.uap;

import com.uap.view.ContactManagerView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactManagerView::new);
    }
}
