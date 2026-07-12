package com.bankmanagement;

public final class Permission {
    private Permission() {}

    public static final int FULL_ACCESS = -1;
    public static final int LIST_CLIENTS = 1;
    public static final int ADD_NEW_CLIENT = 2;
    public static final int DELETE_CLIENT = 4;
    public static final int UPDATE_CLIENT = 8;
    public static final int FIND_CLIENT = 16;
    public static final int TRANSACTIONS = 32;
    public static final int MANAGE_USERS = 64;
    public static final int SHOW_LOGIN_REGISTER = 128;
    public static final int CURRENCY_EXCHANGE = 256;

    public static boolean hasPermission(int userPermissions, int permission) {
        return userPermissions == FULL_ACCESS || (userPermissions & permission) == permission;
    }
}
