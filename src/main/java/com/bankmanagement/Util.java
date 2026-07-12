package com.bankmanagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Util {
    private Util() {}

    static {
        Locale.setDefault(Locale.US);
    }

    public static String encryptText(String text) {
        return encryptText(text, 2);
    }

    public static String encryptText(String text, int key) {
        if (text == null) return "";
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            encrypted.append((char) (text.charAt(i) + key));
        }
        return encrypted.toString();
    }

    public static String decryptText(String text) {
        return decryptText(text, 2);
    }

    public static String decryptText(String text, int key) {
        if (text == null) return "";
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            decrypted.append((char) (text.charAt(i) - key));
        }
        return decrypted.toString();
    }

    public static String nowString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/yyyy - H:m:s"));
    }

    public static String money(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    public static String fileDouble(double value) {
        return String.format(Locale.US, "%.6f", value);
    }
}
