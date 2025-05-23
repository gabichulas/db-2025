package com.triplog.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Número de rondas de hashing (10-12 es un buen balance)
    private static final int ROUNDS = 12;

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(ROUNDS));
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}