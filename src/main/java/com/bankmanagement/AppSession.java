package com.bankmanagement;

public final class AppSession {
    private AppSession() {}
    public static User currentUser = User.emptyUser();
}
