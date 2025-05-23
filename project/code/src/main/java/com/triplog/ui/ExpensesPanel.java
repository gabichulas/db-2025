package com.triplog.ui;

import com.triplog.model.Viaje;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ExpensesPanel extends JPanel {
    public ExpensesPanel(Viaje viaje) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titleLabel = new JLabel("Gastos para: " + viaje.getTitulo(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton newButton = new JButton("Nuevo Gasto");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton settleButton = new JButton("Ajustar Cuentas");

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewExpenseDialog((JFrame) SwingUtilities.getWindowAncestor(ExpensesPanel.this), viaje).setVisible(true);
            }
        });

        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(settleButton);

        // Tabla de gastos (datos de ejemplo)
        String[] columnNames = {"ID", "Descripción", "Monto", "Pagado por", "Participantes", "Fecha"};
        Object[][] data = {
                {1, "Hotel", 300.00, "Juan", "Todos", "12/06/2023"},
                {2, "Cena", 75.50, "María", "Juan, María", "13/06/2023"}
        };

        JTable expensesTable = new JTable(data, columnNames);
        expensesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(expensesTable);

        // Panel de resumen
        JPanel summaryPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Resumen de Gastos"));

        summaryPanel.add(new JLabel("Total gastado:"));
        summaryPanel.add(new JLabel("$375.50", SwingConstants.RIGHT));
        summaryPanel.add(new JLabel("Balance:"));
        summaryPanel.add(new JLabel("Juan debe $25.50 a María", SwingConstants.RIGHT));

        // Organizar componentes
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        add(summaryPanel, BorderLayout.SOUTH);
    }
}