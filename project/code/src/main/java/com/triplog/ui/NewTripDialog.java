package com.triplog.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class NewTripDialog extends JDialog {
    public NewTripDialog(JFrame parent) {
        super(parent, "Nuevo Viaje", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 10));

        JLabel nameLabel = new JLabel("Nombre del viaje:");
        JTextField nameField = new JTextField();

        JLabel startLabel = new JLabel("Fecha inicio:");
        JFormattedTextField startField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));

        JLabel endLabel = new JLabel("Fecha fin:");
        JFormattedTextField endField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));

        JLabel participantsLabel = new JLabel("Participantes:");

        DefaultListModel<String> participantsModel = new DefaultListModel<>();
        participantsModel.addElement("Juan");
        participantsModel.addElement("María");
        participantsModel.addElement("Pedro");

        JList<String> participantsList = new JList<>(participantsModel);
        participantsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(startLabel);
        formPanel.add(startField);
        formPanel.add(endLabel);
        formPanel.add(endField);
        formPanel.add(participantsLabel);
        formPanel.add(new JScrollPane(participantsList));

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