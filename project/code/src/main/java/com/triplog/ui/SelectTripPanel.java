package com.triplog.ui;

import com.triplog.dao.DAOException;
import com.triplog.dao.impl.GenericDAOHibernate;
import com.triplog.dao.impl.ViajeDAOHibernate;
import com.triplog.model.Viaje;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SelectTripPanel extends JPanel {
    public interface TripSelectionListener {
        void onTripSelected(Viaje viaje);
    }

    public SelectTripPanel(TripSelectionListener listener) throws DAOException {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton newButton = new JButton("Nuevo Viaje");
        JButton refreshButton = new JButton("Actualizar");

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
        List<Viaje> viajes = viajeDAOHibernate.findAll();

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
                if (value instanceof Viaje) {
                    Viaje viaje = (Viaje) value;
                    setText(viaje.getTitulo() + " (" + viaje.getFechaInicio() + " - " + viaje.getFechaFin() + ")");
                }
                return this;
            }
        });

        tripsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    listener.onTripSelected(tripsList.getSelectedValue());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tripsList);

        // Panel de detalles
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalles del Viaje"));

        detailsPanel.add(new JLabel("Nombre:"));
        detailsPanel.add(new JLabel(""));
        detailsPanel.add(new JLabel("Fecha Inicio:"));
        detailsPanel.add(new JLabel(""));
        detailsPanel.add(new JLabel("Fecha Fin:"));
        detailsPanel.add(new JLabel(""));
        detailsPanel.add(new JLabel("Participantes:"));
        detailsPanel.add(new JLabel(""));

        // Organizar componentes
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);
    }
}
