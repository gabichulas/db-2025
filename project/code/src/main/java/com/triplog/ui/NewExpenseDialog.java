package com.triplog.ui;

import com.triplog.model.Viaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class NewExpenseDialog extends JDialog {
    public NewExpenseDialog(JFrame parent, Viaje viaje) {
        super(parent, "Nuevo Gasto para: " + viaje.getTitulo(), true);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 10));

        JLabel descLabel = new JLabel("Descripción:");
        JTextField descField = new JTextField();

        JLabel amountLabel = new JLabel("Monto:");
        JTextField amountField = new JTextField();

        JLabel paidByLabel = new JLabel("Pagado por:");
        JComboBox<String> paidByCombo = new JComboBox<>(new String[]{"Juan", "María", "Pedro"});

        JLabel participantsLabel = new JLabel("Participantes:");
        JList<String> participantsList = new JList<>(new String[]{"Juan", "María", "Pedro"});
        participantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JLabel dateLabel = new JLabel("Fecha:");
        JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));

        formPanel.add(descLabel);
        formPanel.add(descField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(paidByLabel);
        formPanel.add(paidByCombo);
        formPanel.add(participantsLabel);
        formPanel.add(new JScrollPane(participantsList));
        formPanel.add(dateLabel);
        formPanel.add(dateField);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
}
