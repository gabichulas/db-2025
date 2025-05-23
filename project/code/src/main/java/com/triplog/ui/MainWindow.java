package com.triplog.ui;

import com.triplog.dao.DAOException;
import com.triplog.model.Viaje;

import javax.swing.*;

class MainWindow extends JFrame {
    private final JTabbedPane tabbedPane;

    public MainWindow() throws DAOException {
        setTitle("Travel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Pestaña de selección de viaje
        tabbedPane.addTab("Seleccionar Viaje", new SelectTripPanel(new SelectTripPanel.TripSelectionListener() {
            @Override
            public void onTripSelected(Viaje viaje) {
                updateTabs(viaje);
            }
        }));

        // Pestañas inicialmente deshabilitadas
        tabbedPane.addTab("Tareas", new JPanel());
        tabbedPane.addTab("Gastos", new JPanel());
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);

        add(tabbedPane);
    }

    private void updateTabs(Viaje viaje) {
        if (viaje != null) {
            tabbedPane.setComponentAt(1, new TasksPanel(viaje));
            tabbedPane.setComponentAt(2, new ExpensesPanel(viaje));
            tabbedPane.setEnabledAt(1, true);
            tabbedPane.setEnabledAt(2, true);
            setTitle("Travel Planner - " + viaje.getTitulo());
        }
    }
}