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

    public SelectTripPanel(TripSelectionListener listener) throws DAOException {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton newButton = new JButton("Nuevo Viaje");
        JButton refreshButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Eliminar");

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewTripDialog dialog = new NewTripDialog((JFrame) SwingUtilities.getWindowAncestor(SelectTripPanel.this));
                dialog.setVisible(true);
            }
        });

        buttonPanel.add(newButton);
        buttonPanel.add(refreshButton);

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
