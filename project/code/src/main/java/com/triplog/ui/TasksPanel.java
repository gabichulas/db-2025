package com.triplog.ui;

import com.triplog.model.Tarea;
import com.triplog.model.Viaje;
import com.triplog.util.GeneralUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TasksPanel extends JPanel {
    public TasksPanel(Viaje viaje) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GeneralUtils utils = new GeneralUtils();
        utils.setFavicon();
        // Título
        JLabel titleLabel = new JLabel("Tareas para: " + viaje.getTitulo(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton newButton = new JButton("Nueva Tarea");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton completeButton = new JButton("Marcar Completada");

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewTaskDialog((JFrame) SwingUtilities.getWindowAncestor(TasksPanel.this), viaje).setVisible(true);
            }
        });

        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);

        // Tabla de tareas (datos de ejemplo)
        String[] columnNames = {"ID", "Descripción", "Asignada a", "Fecha Límite", "Completada"};
        Object[][] data = {
                {1, "Reservar hotel", "Juan", "10/06/2023", false},
                {2, "Comprar boletos", "María", "05/06/2023", true}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
        };

        JTable tasksTable = new JTable(model);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tasksTable);

        // Organizar componentes
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
}
