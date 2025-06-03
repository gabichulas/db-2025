package com.triplog.ui;

import com.triplog.dao.DAOException;
import com.triplog.dao.impl.GenericDAOHibernate;
import com.triplog.dao.impl.UsuarioDAOHibernate;
import com.triplog.dao.impl.ViajeDAOHibernate;
import com.triplog.model.Participa;
import com.triplog.model.Usuario;
import com.triplog.model.Viaje;
import com.triplog.util.SessionManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectTripPanel extends JPanel {
    public interface TripSelectionListener {
        void onTripSelected(Viaje viaje);
    }
    UsuarioDAOHibernate daoHibernate = new UsuarioDAOHibernate() {
        @Override
        public List<Usuario> findByGasto(Long idGasto) throws DAOException {
            return List.of();
        }
    };
    Usuario actual = SessionManager.getInstance().getLoggedUser();
    private JLabel lblNombre = new JLabel();
    private JLabel lblFechaInicio = new JLabel();
    private JLabel lblFechaFin = new JLabel();
    private JLabel lblParticipantes = new JLabel();
    private Viaje viajeActual;

    public SelectTripPanel(TripSelectionListener listener) throws DAOException {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton newButton = new JButton("Nuevo Viaje");
        JButton refreshButton = new JButton("Actualizar Lista");
        JButton editButton = new JButton("Editar Viaje");
        JButton deleteButton = new JButton("Eliminar Viaje");
        JButton logoutButton = new JButton("Cerrar Sesión");

        // Lista de viajes (datos de ejemplo)
        ViajeDAOHibernate viajeDAOHibernate = new ViajeDAOHibernate() {
            @Override
            public List<Viaje> findByLugar(Long idLugar) throws DAOException {
                return List.of();
            }
        };

        List<Viaje> viajes = viajeDAOHibernate.findByParticipante(actual.getId());

        DefaultListModel<Viaje> listModel = new DefaultListModel<>();
        for (Viaje viaje : viajes) {
            listModel.addElement(viaje);
        }

        JList<Viaje> tripsList = new JList<>(listModel);
        tripsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tripsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Viaje viaje) {
                    setText(viaje.getTitulo() + " (" + viaje.getFechaInicio() + " - " + viaje.getFechaFin() + ")");
                }
                return this;
            }
        });

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewTripDialog dialog = new NewTripDialog((JFrame) SwingUtilities.getWindowAncestor(SelectTripPanel.this));
                dialog.setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(actual.getId(), tripsList.getSelectedValue().getIdCreador().getId())) {
                    UpdateTripDialog dialog = null;
                    try {
                        dialog = new UpdateTripDialog((JFrame) SwingUtilities.getWindowAncestor(SelectTripPanel.this), tripsList.getSelectedValue());
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    }
                    dialog.setVisible(true);
            } else {
                    JOptionPane.showMessageDialog(null, "Usted no puede editar este viaje ya que no es el creador", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                List<Viaje> viajesActualizados = viajeDAOHibernate.findByParticipante(actual.getId());
                DefaultListModel<Viaje> model = (DefaultListModel<Viaje>) tripsList.getModel();
                model.clear();  // Limpiar el modelo
                viajesActualizados.forEach(model::addElement);  // Agregar todos los viajes
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar viajes", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tripsList.getSelectedValue() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(
                            null,
                            "¿Estas seguro?",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    // Procesar la respuesta
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            viajeDAOHibernate.delete(tripsList.getSelectedValue());
                            JOptionPane.showMessageDialog(null, "Viaje eliminado correctamente", "Eliminado", JOptionPane.WARNING_MESSAGE);
                        } catch (DAOException ex) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el viaje", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        System.out.println("No :)");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No hay ningun viaje seleccionado", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    SelectTripPanel.this,
                    "¿Estás seguro que deseas cerrar sesión?",
                    "Confirmar cierre de sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Cerrar la sesión actual
                SessionManager.getInstance().logout();

                // Obtener la ventana padre y cerrarla
                Window parentWindow = SwingUtilities.getWindowAncestor(SelectTripPanel.this);
                parentWindow.dispose();

                // Abrir la ventana de login nuevamente
                SwingUtilities.invokeLater(() -> {
                    LoginWindow loginWindow = new LoginWindow();
                    loginWindow.setVisible(true);
                });
            }
        });

        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        tripsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Viaje viajeSeleccionado = tripsList.getSelectedValue();

                    if (viajeSeleccionado != null) {
                        // 1. Obtener datos básicos del viaje
                        String nombres = viajeSeleccionado.getTitulo();
                        LocalDate fechaInicio = viajeSeleccionado.getFechaInicio();
                        LocalDate fechaFin = viajeSeleccionado.getFechaFin();

                        // 1. Obtener todos los participantes
                        List<Usuario> participaciones = null;
                        try {
                            participaciones = daoHibernate.getParticipantesDelViaje(viajeSeleccionado.getId());
                        } catch (DAOException ex) {
                            throw new RuntimeException(ex);
                        }

                        // 3. Formatear nombres para mostrar
                        String nombresParticipantes = participaciones.stream()
                                .map(Usuario::getNombre)
                                .filter(nombre -> nombre != null && !nombre.isEmpty())
                                .collect(Collectors.joining(", "));

                        // 3. Actualizar la interfaz gráfica
                        actualizarPanelDetalles(nombres, fechaInicio, fechaFin, nombresParticipantes);

                        // 4. Notificar al listener si es necesario
                        listener.onTripSelected(viajeSeleccionado);
                    }
                }
            }

            private void actualizarPanelDetalles(String nombre, LocalDate fechaInicio, LocalDate fechaFin, String participantes) {
                // Asumiendo que tienes estos componentes en tu panel
                lblNombre.setText(nombre);
                lblFechaInicio.setText(formatearFecha(fechaInicio));
                lblFechaFin.setText(fechaFin != null ? formatearFecha(fechaFin) : "En curso");
                lblParticipantes.setText(participantes);
            }

            private String formatearFecha(LocalDate fecha) {
                return fecha != null ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "No definida";
            }
        });

        JScrollPane scrollPane = new JScrollPane(tripsList);

        // Panel de detalles
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalles del Viaje"));
            detailsPanel.add(new JLabel("Nombre:"));
            detailsPanel.add(lblNombre);
            detailsPanel.add(new JLabel("Fecha Inicio:"));
            detailsPanel.add(lblFechaInicio);
            detailsPanel.add(new JLabel("Fecha Fin:"));
            detailsPanel.add(lblFechaFin);
            detailsPanel.add(new JLabel("Participantes:"));
            detailsPanel.add(lblParticipantes);

        // Organizar componentes
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);


    }


}
