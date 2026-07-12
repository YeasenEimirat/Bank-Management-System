package com.bankmanagement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public final class FileStorage {
    private FileStorage() {}

    public static List<String> readAllLines(String fileName) {
        try {
            Path path = Path.of(fileName);
            if (!Files.exists(path)) {
                if (path.getParent() != null) Files.createDirectories(path.getParent());
                Files.createFile(path);
                return new ArrayList<>();
            }
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read file: " + fileName, ex);
        }
    }

    public static void writeAllLines(String fileName, List<String> lines) {
        try {
            Path path = Path.of(fileName);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            Files.write(path, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot write file: " + fileName, ex);
        }
    }

    public static void appendLine(String fileName, String line) {
        try {
            Path path = Path.of(fileName);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            Files.writeString(path, line + System.lineSeparator(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot append to file: " + fileName, ex);
        }
    }
}
