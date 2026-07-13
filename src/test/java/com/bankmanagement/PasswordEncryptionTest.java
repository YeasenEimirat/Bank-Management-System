package com.bankmanagement;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Dependency-free test for saving and authenticating encrypted user passwords. */
public final class PasswordEncryptionTest {
    private PasswordEncryptionTest() {}

    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Expected data directory");
        System.setProperty("bank.data.dir", args[0]);

        String userName = "EncryptionTestUser";
        String plainPassword = "Pass1234";
        String encryptedPassword = Util.encryptText(plainPassword);

        User oldTestUser = User.find(userName);
        if (!oldTestUser.isEmpty()) oldTestUser.delete();

        User user = User.getAddNewUserObject(userName);
        user.setFirstName("Encryption");
        user.setLastName("Test");
        user.setEmail("encryption@test.local");
        user.setPhone("0000000000");
        user.setPassword(plainPassword);
        user.setPermissions(Permission.LIST_CLIENTS);

        require(user.save() == SaveResult.SUCCEEDED, "New user was not saved");

        List<String> lines = Files.readAllLines(Path.of(Constants.USERS_FILE));
        String savedLine = lines.stream()
                .filter(line -> line.contains(Constants.SEPARATOR + userName + Constants.SEPARATOR))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Saved user record was not found"));

        String[] fields = savedLine.split(java.util.regex.Pattern.quote(Constants.SEPARATOR), -1);
        require(fields.length >= 7, "Saved user record is malformed");
        require(fields[5].equals(encryptedPassword), "Password was not encrypted correctly");
        require(!fields[5].equals(plainPassword), "Plain password was written to the file");
        require(!User.find(userName, plainPassword).isEmpty(), "Login with plain password failed");

        require(User.find(userName).delete(), "Test user cleanup failed");
        System.out.println("Password encryption test passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
