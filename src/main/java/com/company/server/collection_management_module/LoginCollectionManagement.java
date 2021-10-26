package com.company.server.collection_management_module;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginCollectionManagement {
    private final HashMap<String, String> loginPasswordMap;

    public LoginCollectionManagement() {
        this.loginPasswordMap = new HashMap<>();
    }

    public boolean isTruePassword(String userName, String password) {
        StringBuilder hexStringPassword = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexStringPassword.append('0');
                hexStringPassword.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return loginPasswordMap.get(userName).equals(hexStringPassword.toString());
    }

    public boolean isRegisteredUser(String userName) {
        return loginPasswordMap.containsKey(userName);
    }

    public void register(String userName, String password) {
        loginPasswordMap.put(userName, password);
    }
}
