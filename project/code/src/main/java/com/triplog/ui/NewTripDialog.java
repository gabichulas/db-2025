package com.triplog.ui;
import com.triplog.dao.DAOException;
import com.triplog.dao.impl.GenericDAOHibernate;
import com.triplog.dao.impl.ParticipaDAOHibernate;
import com.triplog.dao.impl.UsuarioDAOHibernate;
import com.triplog.dao.impl.ViajeDAOHibernate;
import com.triplog.model.Participa;
import com.triplog.model.ParticipaId;
import com.triplog.model.Usuario;
import com.triplog.model.Viaje;
import com.triplog.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

public class NewTripDialog extends JDialog {
    public NewTripDialog(JFrame parent) {
        super(parent, "Nuevo Viaje", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        Usuario actual = SessionManager.getInstance().getLoggedUser();

        UsuarioDAOHibernate dao = new UsuarioDAOHibernate() {
            @Override
            public List<Usuario> findByGasto(Long idGasto) throws DAOException {
                return List.of();
            }
        };

        ParticipaDAOHibernate pdao = new ParticipaDAOHibernate() {
            @Override
            public Participa save(Participa entity) throws DAOException {
                return super.save(entity);
            }
        };

        ViajeDAOHibernate vdao = new ViajeDAOHibernate() {
            @Override
            public List<Viaje> findByLugar(Long idLugar) throws DAOException {
                return List.of();
            }
        };

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 10));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        JLabel nameLabel = new JLabel("Nombre del viaje:");
        JTextField nameField = new JTextField();

        JLabel destinoLabel = new JLabel("Destino del viaje (provincia/ciudad):");
        JTextField destinoField = new JTextField();

        JLabel startLabel = new JLabel("Fecha inicio:");
        JFormattedTextField startField = new JFormattedTextField(dateFormat);

        JLabel endLabel = new JLabel("Fecha fin:");
        JFormattedTextField endField = new JFormattedTextField(dateFormat);

        JLabel participantsLabel = new JLabel("Participantes:");

        DefaultListModel<String> participantsModel = new DefaultListModel<>();
        JList<String> participantsList = new JList<>(participantsModel);
        participantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JButton addParticipantButton = new JButton("Agregar Participante");
        addParticipantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JDialog participantDialog = new JDialog(NewTripDialog.this, "Agregar Participante", true);
                participantDialog.setSize(300, 150);
                participantDialog.setLocationRelativeTo(NewTripDialog.this);

                JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JTextField emailField = new JTextField();
                panel.add(new JLabel("Email del participante:"));
                panel.add(emailField);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton addButton = new JButton("Agregar");
                JButton cancelButton = new JButton("Cancelar");

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent innerEvent) {
                        String email = emailField.getText().trim();
                        try {
                            if (!email.isEmpty() && email.matches("[^@]+@[^@]+\\.[^@]+") && dao.findByEmail(email).isPresent()) {
                                participantsModel.addElement(email);
                                participantDialog.dispose();
                            } else {
                                JOptionPane.showMessageDialog(participantDialog,
                                        "El email es invalido o no corresponde a un usuario registrado",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (DAOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent innerEvent) {
                        participantDialog.dispose();
                    }
                });

                buttonPanel.add(addButton);
                buttonPanel.add(cancelButton);

                participantDialog.add(panel, BorderLayout.CENTER);
                participantDialog.add(buttonPanel, BorderLayout.SOUTH);
                participantDialog.setVisible(true);
            }
        });

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(destinoLabel);
        formPanel.add(destinoField);
        formPanel.add(startLabel);
        formPanel.add(startField);
        formPanel.add(endLabel);
        formPanel.add(endField);
        formPanel.add(participantsLabel);
        formPanel.add(new JScrollPane(participantsList));
        formPanel.add(addParticipantButton);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nameField.getText().trim();
                String destino = destinoField.getText().trim();  // Obtener y limpiar el texto
                LocalDate start = getFechaFromField(startField);
                LocalDate end = getFechaFromField(endField);

                // Validar campos obligatorios
                if (nombre.isEmpty() || destino.isEmpty() || start == null || end == null) {
                    JOptionPane.showMessageDialog(
                            NewTripDialog.this,
                            "Todos los campos son obligatorios",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Validar que la fecha de fin sea posterior a la de inicio
                if (end.isBefore(start)) {
                    JOptionPane.showMessageDialog(
                            NewTripDialog.this,
                            "La fecha de fin debe ser posterior a la de inicio",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                Enumeration<String> emails = participantsModel.elements();
                Set<Participa> participaciones = new HashSet<>();
                Viaje newViaje = new Viaje();
                newViaje.setTitulo(nombre);
                newViaje.setDestino(destino);
                newViaje.setFechaInicio(start);
                newViaje.setFechaFin(end);
                newViaje.setIdCreador(SessionManager.getInstance().getLoggedUser());

                Viaje savedViaje = null;
                try {
                    savedViaje = vdao.save(newViaje);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }

                Participa creador = new Participa();
                ParticipaId creadorId = new ParticipaId();
                creadorId.setIdUsuario(actual.getId());
                creadorId.setIdViaje(savedViaje.getId());
                creador.setIdUsuario(actual);
                creador.setIdViaje(savedViaje);
                creador.setEsOrganizador(true);
                creador.setId(creadorId);
                try {
                    pdao.save(creador);
                } catch (DAOException ex) {
                    throw new RuntimeException(ex);
                }
                participaciones.add(creador);

                while (emails.hasMoreElements()) {
                    String email = emails.nextElement();
                    Optional<Usuario> usuarioOpt;
                    try {
                        usuarioOpt = dao.findByEmail(email);
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Participa participacion = new Participa();
                    ParticipaId participacionId = new ParticipaId();
                    participacionId.setIdViaje(savedViaje.getId());
                    participacionId.setIdUsuario(usuarioOpt.get().getId());
                    participacion.setIdUsuario(usuarioOpt.get());
                    participacion.setIdViaje(savedViaje);
                    participacion.setEsOrganizador(false);
                    participacion.setId(participacionId);
                    try {
                        pdao.save(participacion);
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }

                    participaciones.add(participacion);
                }
                savedViaje.setParticipas(participaciones);
                JOptionPane.showMessageDialog(
                        NewTripDialog.this,
                        "El viaje se ha creado correctamente",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public LocalDate getFechaFromField(JFormattedTextField field) throws IllegalArgumentException {
        Object value = field.getValue();

        switch (value) {
            case null -> throw new IllegalArgumentException("La fecha no puede estar vacía");
            case Date date -> {
                return date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }
            case String s -> {
                try {
                    return LocalDate.parse(s,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy");
                }
            }
            default -> {
            }
        }

        throw new IllegalArgumentException("Tipo de fecha no soportado");
    }
}