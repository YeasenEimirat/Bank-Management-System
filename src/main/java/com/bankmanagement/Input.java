package com.bankmanagement;

import java.util.Scanner;

public final class Input {
    private static final Scanner SCANNER = new Scanner(System.in);

    private Input() {}

    public static String readString() {
        if (!SCANNER.hasNextLine()) return "";
        return SCANNER.nextLine().trim();
    }

    public static int readInt() {
        while (true) {
            String text = readString();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                System.out.print("Invalid number, enter again: ");
            }
        }
    }

    public static int readIntBetween(int from, int to) {
        while (true) {
            int number = readInt();
            if (number >= from && number <= to) return number;
            System.out.print("Enter a number between " + from + " and " + to + ": ");
        }
    }

    public static double readDouble() {
        while (true) {
            String text = readString();
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                System.out.print("Invalid number, enter again: ");
            }
        }
    }

    public static double readPositiveDouble() {
        while (true) {
            double number = readDouble();
            if (number > 0) return number;
            System.out.print("Enter a positive number: ");
        }
    }

    public static boolean readYesNo() {
        while (true) {
            String answer = readString();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) return true;
            if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) return false;
            System.out.print("Please enter Y/N: ");
        }
    }

    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        SCANNER.nextLine();
    }
}
