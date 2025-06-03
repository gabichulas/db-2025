package com.triplog.util;

import javax.swing.*;
import java.util.Objects;

public class GeneralUtils extends JFrame{
    public void setFavicon() {
        try {
            // Cargar el icono desde recursos
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/favicon.ico")));

            // Establecer como icono de la ventana
            setIconImage(icon.getImage());

        } catch (Exception e) {
            System.err.println("No se pudo cargar el favicon: " + e.getMessage());
            // Opcional: Usar un icono por defecto
            // setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/default-icon.png")));
        }
    }
}
