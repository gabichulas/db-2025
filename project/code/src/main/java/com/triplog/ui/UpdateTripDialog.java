package com.triplog.ui;
import com.triplog.dao.DAOException;
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
import java.util.stream.Collectors;

public class UpdateTripDialog extends JDialog {
    private Viaje viaje;  // Viaje que estamos editando

    public UpdateTripDialog(JFrame parent, Viaje viaje) throws DAOException {
        super(parent, "Editar Viaje", true);
        this.viaje = viaje;
        setSize(500, 400);
        setLocationRelativeTo(parent);

        Usuario actual = SessionManager.getInstance().getLoggedUser();

        // DAOs (igual que en NewTripDialog)
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

        // Campos del formulario
        JLabel nameLabel = new JLabel("Nombre del viaje:");
        JTextField nameField = new JTextField(viaje.getTitulo());  // Cargar valor existente

        JLabel destinoLabel = new JLabel("Destino del viaje (provincia/ciudad):");
        JTextField destinoField = new JTextField(viaje.getDestino());  // Cargar valor existente

        JLabel startLabel = new JLabel("Fecha inicio:");
        JFormattedTextField startField = new JFormattedTextField(dateFormat);
        startField.setValue(Date.from(viaje.getFechaInicio().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        JLabel endLabel = new JLabel("Fecha fin:");
        JFormattedTextField endField = new JFormattedTextField(dateFormat);
        if (viaje.getFechaFin() != null) {
            endField.setValue(Date.from(viaje.getFechaFin().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        JLabel participantsLabel = new JLabel("Participantes:");
        DefaultListModel<String> participantsModel = new DefaultListModel<>();
        JList<String> participantsList = new JList<>(participantsModel);
        participantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        List<Usuario> participaciones = dao.getParticipantesDelViaje(viaje.getId());
        // Cargar participantes existentes (excluyendo al creador)
        participaciones.stream()
                .filter(p -> !p.equals(actual))
                .map(Usuario::getEmail)
                .forEach(participantsModel::addElement);
        participantsModel.removeElement(actual.getEmail());
        // Botón para agregar participantes (igual que en NewTripDialog)
        JButton addParticipantButton = new JButton("Agregar Participante");
        addParticipantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JDialog participantDialog = new JDialog(UpdateTripDialog.this, "Agregar Participante", true);
                participantDialog.setSize(300, 150);
                participantDialog.setLocationRelativeTo(UpdateTripDialog.this);

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

        // Añadir componentes al formulario
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
        JButton saveButton = new JButton("Guardar Cambios");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            String nombre = nameField.getText().trim();
            String destino = destinoField.getText().trim();
            LocalDate start = getFechaFromField(startField);
            LocalDate end = getFechaFromField(endField);

            // Validaciones (se mantienen igual)
            if (nombre.isEmpty() || destino.isEmpty() || start == null || end == null) {
                JOptionPane.showMessageDialog(
                        UpdateTripDialog.this,
                        "Todos los campos son obligatorios",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(
                        UpdateTripDialog.this,
                        "La fecha de fin debe ser posterior a la de inicio",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                // 1. Actualizar los datos básicos del viaje
                viaje.setTitulo(nombre);
                viaje.setDestino(destino);
                viaje.setFechaInicio(start);
                viaje.setFechaFin(end);

                // 2. Actualizar las participaciones (primero eliminaciones, luego inserciones)
                updateParticipantes(viaje, participantsModel, dao, pdao, actual);

                // 3. Guardar el viaje actualizado (una sola vez)
                vdao.update(viaje);

                JOptionPane.showMessageDialog(
                        UpdateTripDialog.this,
                        "Viaje actualizado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } catch (Exception | DAOException ex) {
                JOptionPane.showMessageDialog(
                        UpdateTripDialog.this,
                        "Error al actualizar el viaje: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void updateParticipantes(Viaje viaje, DefaultListModel<String> participantsModel,
                                     UsuarioDAOHibernate dao, ParticipaDAOHibernate pdao,
                                     Usuario actual) throws DAOException {
        try {
            // 1. Obtener TODOS los participantes actuales desde la BD (frescos)
            List<Participa> participacionesActuales = pdao.findByViaje(viaje.getId());
            Set<String> emailsActualesBD = participacionesActuales.stream()
                    .map(p -> p.getIdUsuario().getEmail())
                    .collect(Collectors.toSet());

            // 2. Obtener emails seleccionados en la UI (sin el creador)
            Set<String> emailsSeleccionadosUI = Collections.list(participantsModel.elements())
                    .stream()
                    .collect(Collectors.toSet());

            // 3. El creador siempre debe estar incluido
            emailsSeleccionadosUI.add(actual.getEmail());

            // 4. Identificar REALMENTE qué hay que eliminar
            List<Participa> paraEliminar = participacionesActuales.stream()
                    .filter(p -> !p.getIdUsuario().equals(actual)) // Nunca eliminar al creador
                    .filter(p -> !emailsSeleccionadosUI.contains(p.getIdUsuario().getEmail()))
                    .collect(Collectors.toList());

            // 5. Identificar REALMENTE qué hay que agregar
            Set<String> paraAgregar = emailsSeleccionadosUI.stream()
                    .filter(email -> !emailsActualesBD.contains(email))
                    .collect(Collectors.toSet());

            // 6. Procesar eliminaciones primero
            for (Participa p : paraEliminar) {
                pdao.delete(p);
                viaje.getParticipas().remove(p);
            }

            // 7. Procesar nuevas participaciones
            for (String email : paraAgregar) {
                Usuario usuario = dao.findByEmail(email)
                        .orElseThrow(() -> new Exception("Usuario no encontrado: " + email));

                // Verificación EXTRA para asegurar que no existe
                boolean yaExiste = viaje.getParticipas().stream()
                        .anyMatch(p -> p.getIdUsuario().getEmail().equals(email));

                if (!yaExiste) {
                    Participa nueva = new Participa();
                    ParticipaId id = new ParticipaId();
                    id.setIdViaje(viaje.getId());
                    id.setIdUsuario(usuario.getId());

                    nueva.setId(id);
                    nueva.setIdViaje(viaje);
                    nueva.setIdUsuario(usuario);
                    nueva.setEsOrganizador(email.equals(actual.getEmail()));

                    pdao.save(nueva);
                    viaje.getParticipas().add(nueva);
                }
            }

        } catch (Exception e) {
            throw new DAOException("Error actualizando participantes: " + e.getMessage(), e);
        }
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