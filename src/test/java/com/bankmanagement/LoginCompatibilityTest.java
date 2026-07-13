package com.bankmanagement;

/** Verifies login for the existing accounts shipped with the project. */
public final class LoginCompatibilityTest {
    private LoginCompatibilityTest() {}

    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Expected data directory");
        System.setProperty("bank.data.dir", args[0]);

        requireLogin("User1", "9898");
        requireLogin("User2", "1234");
        requireLogin("w", "w");
        requireLogin("  User2  ", "1234");
        System.out.println("Existing-user login compatibility test passed.");
    }

    private static void requireLogin(String userName, String password) {
        if (User.find(userName, password).isEmpty()) {
            throw new IllegalStateException("Login failed for existing user: " + userName.trim());
        }
    }
}
