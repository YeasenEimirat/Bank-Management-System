package com.bankmanagement;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Constants {
    private Constants() {}
    public static final String SEPARATOR = "#//#";
    public static final String DATA_DIR = resolveDataDir();
    public static final String CLIENTS_FILE = DATA_DIR + "/BClients.txt";
    public static final String USERS_FILE = DATA_DIR + "/BUsers.txt";
    public static final String CURRENCIES_FILE = DATA_DIR + "/Currencies.txt";
    public static final String LOGIN_REGISTER_FILE = DATA_DIR + "/LoginRegister.txt";
    public static final String TRANSFER_LOG_FILE = DATA_DIR + "/TransferLog.txt";

    private static String resolveDataDir() {
        String customDataDir = System.getProperty("bank.data.dir");
        if (customDataDir != null && !customDataDir.isBlank()) {
            return customDataDir;
        }

        List<Path> rootsToSearch = new ArrayList<>();
        rootsToSearch.add(Path.of("").toAbsolutePath().normalize());

        try {
            URI codeLocation = Constants.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            rootsToSearch.add(Path.of(codeLocation).toAbsolutePath().normalize());
        } catch (Exception ignored) {
            // Fallback to current working directory.
        }

        for (Path root : rootsToSearch) {
            for (Path directory = root; directory != null; directory = directory.getParent()) {
                Path directData = directory.resolve("data");
                if (Files.exists(directData.resolve("BUsers.txt"))) {
                    return directData.toString();
                }

                Path projectData = directory.resolve("BankManagementSystemJava").resolve("data");
                if (Files.exists(projectData.resolve("BUsers.txt"))) {
                    return projectData.toString();
                }
            }
        }

        return "data";
    }
}
