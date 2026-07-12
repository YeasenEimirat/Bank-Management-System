package com.bankmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User extends Person {
    private enum Mode { EMPTY, UPDATE, ADD_NEW }

    private Mode mode;
    private String userName;
    private String password;
    private String storedPassword;
    private int permissions;
    private boolean markedForDelete;

    private User(Mode mode, String firstName, String lastName, String email,
                 String phone, String userName, String password, int permissions) {
        this(mode, firstName, lastName, email, phone, userName, password,
                Util.encryptText(password), permissions);
    }

    private User(Mode mode, String firstName, String lastName, String email,
                 String phone, String userName, String password, String storedPassword, int permissions) {
        super(firstName, lastName, email, phone);
        this.mode = mode;
        this.userName = userName;
        this.password = password;
        this.storedPassword = storedPassword;
        this.permissions = permissions;
    }

    public static User emptyUser() {
        return new User(Mode.EMPTY, "", "", "", "", "", "", 0);
    }

    public static User getAddNewUserObject(String userName) {
        return new User(Mode.ADD_NEW, "", "", "", "", userName, "", 0);
    }

    public boolean isEmpty() {
        return mode == Mode.EMPTY;
    }

    public boolean markedForDelete() {
        return markedForDelete;
    }

    private static User fromLine(String line) {
        String[] parts = line.split(java.util.regex.Pattern.quote(Constants.SEPARATOR), -1);
        if (parts.length < 7) return emptyUser();
        String storedPassword = parts[5];
        return new User(Mode.UPDATE,
                parts[0], parts[1], parts[2], parts[3], parts[4],
                Util.decryptText(storedPassword), storedPassword, Integer.parseInt(parts[6].trim()));
    }

    private String toLine() {
        return firstName + Constants.SEPARATOR +
                lastName + Constants.SEPARATOR +
                email + Constants.SEPARATOR +
                phone + Constants.SEPARATOR +
                userName + Constants.SEPARATOR +
                Util.encryptText(password) + Constants.SEPARATOR +
                permissions;
    }

    public static List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();
        for (String line : FileStorage.readAllLines(Constants.USERS_FILE)) {
            if (!line.isBlank()) users.add(fromLine(line));
        }
        return users;
    }

    private static void saveAllUsers(List<User> users) {
        List<String> lines = users.stream()
                .filter(user -> !user.markedForDelete())
                .map(User::toLine)
                .collect(Collectors.toList());
        FileStorage.writeAllLines(Constants.USERS_FILE, lines);
    }

    public static User find(String userName) {
        for (User user : loadAllUsers()) {
            if (user.userName.equalsIgnoreCase(userName)) {
                return user;
            }
        }
        return emptyUser();
    }

    public static User find(String userName, String password) {
        for (User user : loadAllUsers()) {
            boolean sameUserName = user.userName.equalsIgnoreCase(userName);
            boolean passwordMatches = user.password.equals(password)
                    || user.storedPassword.equals(password)
                    || user.storedPassword.equals(Util.encryptText(password));

            if (sameUserName && passwordMatches) {
                return user;
            }
        }
        return emptyUser();
    }

    public static boolean exists(String userName) {
        return !find(userName).isEmpty();
    }

    private void update() {
        List<User> users = loadAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).userName.equalsIgnoreCase(this.userName)) {
                users.set(i, this);
                break;
            }
        }
        saveAllUsers(users);
    }

    private void addNew() {
        FileStorage.appendLine(Constants.USERS_FILE, toLine());
    }

    public SaveResult save() {
        if (isEmpty()) return SaveResult.FAILED_EMPTY_OBJECT;
        if (mode == Mode.ADD_NEW) {
            if (exists(userName)) return SaveResult.FAILED_ALREADY_EXISTS;
            addNew();
            mode = Mode.UPDATE;
            return SaveResult.SUCCEEDED;
        }
        update();
        return SaveResult.SUCCEEDED;
    }

    public boolean delete() {
        if (isEmpty()) return false;
        List<User> users = loadAllUsers();
        boolean deleted = false;
        for (User user : users) {
            if (user.userName.equalsIgnoreCase(this.userName)) {
                user.markedForDelete = true;
                deleted = true;
                break;
            }
        }
        saveAllUsers(users);
        this.mode = Mode.EMPTY;
        return deleted;
    }

    public void registerLogin() {
        String line = Util.nowString() + Constants.SEPARATOR + userName + Constants.SEPARATOR +
                Util.encryptText(password) + Constants.SEPARATOR + permissions;
        FileStorage.appendLine(Constants.LOGIN_REGISTER_FILE, line);
    }

    public boolean hasPermission(int permission) {
        return Permission.hasPermission(permissions, permission);
    }

    public static List<String[]> loadLoginRegisterRecords() {
        List<String[]> records = new ArrayList<>();
        for (String line : FileStorage.readAllLines(Constants.LOGIN_REGISTER_FILE)) {
            if (line.isBlank()) continue;
            String[] p = line.split(java.util.regex.Pattern.quote(Constants.SEPARATOR), -1);
            if (p.length >= 4) {
                records.add(new String[]{p[0], p[1], Util.decryptText(p[2]), p[3]});
            }
        }
        return records;
    }


    @Override
    public String getRoleDescription() {
        return "System User - logs in and controls screens according to assigned permissions";
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
        this.storedPassword = Util.encryptText(password);
    }
    public int getPermissions() { return permissions; }
    public void setPermissions(int permissions) { this.permissions = permissions; }
}
