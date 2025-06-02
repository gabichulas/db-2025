package com.triplog.util;

import com.triplog.model.Usuario;

public class SessionManager {
    private static SessionManager instance;
    private Usuario loggedUser;

    private SessionManager() {} // Constructor privado

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(Usuario usuario) {
        this.loggedUser = usuario;
    }

    public void logout() {
        this.loggedUser = null;
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public boolean isLoggedIn() {
        return loggedUser != null;
    }
}
