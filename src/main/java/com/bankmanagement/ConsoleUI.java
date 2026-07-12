package com.bankmanagement;

public final class ConsoleUI {
    private ConsoleUI() {}

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void header(String title) {
        System.out.println("============================================================");
        System.out.println(title);
        System.out.println("User: " + (AppSession.currentUser == null ? "" : AppSession.currentUser.getUserName()));
        System.out.println("============================================================");
    }

    public static void accessDenied() {
        System.out.println("\nAccess denied! You do not have permission to open this screen.");
    }
}
