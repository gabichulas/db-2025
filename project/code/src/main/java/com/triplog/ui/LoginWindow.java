package com.triplog.ui;

import com.triplog.dao.DAOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    public LoginWindow() {
        setTitle("Travel Planner - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titleLabel = new JLabel("Triplog", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 5, 10));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("Email:"));
        JTextField userField = new JTextField(15);
        userPanel.add(userField);

        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(new JLabel("Contraseña:"));
        JPasswordField passField = new JPasswordField(15);
        passPanel.add(passField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Iniciar Sesión");
        JButton registerButton = new JButton("Registrarse");

        // Acciones de los botones
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana de login
                try {
                    new MainWindow().setVisible(true); // Abre la ventana principal
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterDialog(LoginWindow.this).setVisible(true);
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formPanel.add(userPanel);
        formPanel.add(passPanel);
        formPanel.add(buttonPanel);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
}