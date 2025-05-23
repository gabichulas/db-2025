package com.triplog;

import com.triplog.ui.LoginWindow;

import javax.swing.*;

public class TravelPlannerUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }
}
