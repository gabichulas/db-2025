package com.triplog.ui;

import com.triplog.model.Viaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class NewTaskDialog extends JDialog {
    public NewTaskDialog(JFrame parent, Viaje viaje) {
        super(parent, "Nueva Tarea para: " + viaje.getTitulo(), true);
        setSize(450, 300);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 10));

        JLabel descLabel = new JLabel("Descripción:");
        JTextField descField = new JTextField();

        JLabel assignLabel = new JLabel("Asignar a:");
        JComboBox<String> assignCombo = new JComboBox<>(new String[]{"Juan", "María", "Pedro"});

        JLabel dateLabel = new JLabel("Fecha límite:");
        JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));

        formPanel.add(descLabel);
        formPanel.add(descField);
        formPanel.add(assignLabel);
        formPanel.add(assignCombo);
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
