package com.triplog.ui;

import com.triplog.dao.DAOException;
import com.triplog.dao.impl.UsuarioDAOHibernate;
import com.triplog.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class RegisterDialog extends JDialog {
    public RegisterDialog(JFrame parent) throws DAOException {
        super(parent, "Registro de Usuario", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        UsuarioDAOHibernate dao = new UsuarioDAOHibernate() {
            @Override
            public List<Usuario> findByGasto(Long idGasto) throws DAOException {
                return List.of();
            }
        };

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 10));

        formPanel.add(new JLabel("Nombre/s y apellido/s:"));
        JTextField nombreField = new JTextField(15);
        formPanel.add(nombreField);

        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(15);
        formPanel.add(emailField);

        formPanel.add(new JLabel("Contraseña:"));
        JPasswordField passwordField = new JPasswordField(15);
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerButton = new JButton("Registrarse");
        JButton cancelButton = new JButton("Cancelar");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 1. Validar campos
                    if (nombreField.getText().trim().isEmpty() ||
                            emailField.getText().trim().isEmpty() ||
                            passwordField.getPassword().length == 0) {
                        JOptionPane.showMessageDialog(null,
                                "Todos los campos son obligatorios",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 2. Validar formato de email
                    if (!emailField.getText().matches("[^@]+@[^@]+\\.[^@]+")) {
                        JOptionPane.showMessageDialog(null,
                                "Ingrese un email válido",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (dao.findByEmail(emailField.getText()).isPresent()) {
                        JOptionPane.showMessageDialog(null,
                                "Ya existe una cuenta registrada con este email. Intente iniciar sesión",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 3. Crear y guardar usuario
                    String nombre = nombreField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());

                    Usuario newUsuario = new Usuario(nombre, email, password);
                    dao.save(newUsuario);

                    JOptionPane.showMessageDialog(null,
                            "Registro exitoso. Por favor, inicie sesion",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    dispose(); // Cerrar diálogo después de registro exitoso

                } catch (DAOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error al registrar: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    // Limpiar contraseña en memoria
                    Arrays.fill(passwordField.getPassword(), '0');
                }
            }
        });
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

    }
}