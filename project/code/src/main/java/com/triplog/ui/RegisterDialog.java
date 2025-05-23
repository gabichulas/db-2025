package com.triplog.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterDialog extends JDialog {
    public RegisterDialog(JFrame parent) {
        super(parent, "Registro de Usuario", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 10));

        formPanel.add(new JLabel("Nombre/s:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Apellido/s:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Email:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Contraseña:"));
        formPanel.add(new JPasswordField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerButton = new JButton("Registrarse");
        JButton cancelButton = new JButton("Cancelar");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra el diálogo
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra el diálogo
            }
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}