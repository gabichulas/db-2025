package com.triplog.ui;

import com.triplog.dao.DAOException;
import com.triplog.dao.UsuarioDAO;
import com.triplog.dao.impl.UsuarioDAOHibernate;
import com.triplog.model.Usuario;
import com.triplog.util.GeneralUtils;
import com.triplog.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LoginWindow extends JFrame {
    UsuarioDAOHibernate dao = new UsuarioDAOHibernate() {
        @Override
        public List<Usuario> findByGasto(Long idGasto) throws DAOException {
            return List.of();
        }
    };
    public LoginWindow() {
        setTitle("Triplog - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/triplog.png")));
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Título


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
                System.out.println("Inicio de acción de login");
                String username = userField.getText().trim();
                char[] passwordChars = passField.getPassword();

                try {
                    System.out.println("Usuario ingresado: " + username);

                    // Paso 1: Buscar usuario
                    System.out.println("Buscando usuario en BD...");
                    Optional<Usuario> usuarioOpt = dao.findByEmail(username);

                    if (usuarioOpt.isEmpty()) {
                        System.out.println("Usuario no encontrado");
                        JOptionPane.showMessageDialog(null,
                                """
                                        Credenciales inválidas. Asegúrese que:
                                        - El email sea correcto y tenga formato válido (usuario@dominio.com)
                                        """,
                                "Error de validación",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Paso 2: Verificar contraseña
                    String password = new String(passwordChars);
                    System.out.println("Verificando contraseña...");

                    if (dao.verifyUsuario(username, password)) {
                        System.out.println("Contraseña válida");
                        // Paso 3: Establecer sesión
                        SessionManager.getInstance().login(usuarioOpt.get());
                        System.out.println("Sesión iniciada para: " +
                                SessionManager.getInstance().getLoggedUser().getEmail());

                        dispose();
                        new MainWindow().setVisible(true);
                    } else {

                        JOptionPane.showMessageDialog(null,
                                """
                                        Credenciales inválidas. Asegúrese que:
                                        - La contraseña sea correcta y tenga al menos 6 caracteres""",
                                "Error de validación",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception | DAOException ex) {
                    System.out.println("EXCEPCIÓN: " + ex.getClass().getName());
                    System.out.println("Mensaje: " + ex.getMessage());
                    ex.printStackTrace();
                    // Mostrar error
                } finally {
                    Arrays.fill(passwordChars, '\0');
                    System.out.println("Limpieza completada");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new RegisterDialog(LoginWindow.this).setVisible(true);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formPanel.add(userPanel);
        formPanel.add(passPanel);
        formPanel.add(buttonPanel);


        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(logoLabel, BorderLayout.CENTER);


        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}