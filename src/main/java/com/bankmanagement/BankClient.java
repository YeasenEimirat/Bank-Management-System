package com.bankmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BankClient extends Person {
    private enum Mode { EMPTY, UPDATE, ADD_NEW }

    private Mode mode;
    private String accountNumber;
    private String pinCode;
    private double accountBalance;
    private boolean markedForDelete;

    private BankClient(Mode mode, String firstName, String lastName, String email,
                       String phone, String accountNumber, String pinCode, double accountBalance) {
        super(firstName, lastName, email, phone);
        this.mode = mode;
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
        this.accountBalance = accountBalance;
    }

    public static BankClient emptyClient() {
        return new BankClient(Mode.EMPTY, "", "", "", "", "", "", 0);
    }

    public static BankClient getAddNewClientObject(String accountNumber) {
        return new BankClient(Mode.ADD_NEW, "", "", "", "", accountNumber, "", 0);
    }

    public boolean isEmpty() {
        return mode == Mode.EMPTY;
    }

    public boolean markedForDelete() {
        return markedForDelete;
    }

    private static BankClient fromLine(String line) {
        String[] parts = line.split(java.util.regex.Pattern.quote(Constants.SEPARATOR), -1);
        if (parts.length < 7) return emptyClient();
        return new BankClient(Mode.UPDATE,
                parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],
                Double.parseDouble(parts[6].trim()));
    }

    private String toLine() {
        return firstName + Constants.SEPARATOR +
                lastName + Constants.SEPARATOR +
                email + Constants.SEPARATOR +
                phone + Constants.SEPARATOR +
                accountNumber + Constants.SEPARATOR +
                pinCode + Constants.SEPARATOR +
                Util.fileDouble(accountBalance);
    }

    public static List<BankClient> loadAllClients() {
        List<BankClient> clients = new ArrayList<>();
        for (String line : FileStorage.readAllLines(Constants.CLIENTS_FILE)) {
            if (!line.isBlank()) clients.add(fromLine(line));
        }
        return clients;
    }

    private static void saveAllClients(List<BankClient> clients) {
        List<String> lines = clients.stream()
                .filter(client -> !client.markedForDelete())
                .map(BankClient::toLine)
                .collect(Collectors.toList());
        FileStorage.writeAllLines(Constants.CLIENTS_FILE, lines);
    }

    public static BankClient find(String accountNumber) {
        for (BankClient client : loadAllClients()) {
            if (client.accountNumber.equalsIgnoreCase(accountNumber)) return client;
        }
        return emptyClient();
    }

    public static BankClient find(String accountNumber, String pinCode) {
        for (BankClient client : loadAllClients()) {
            if (client.accountNumber.equalsIgnoreCase(accountNumber) && client.pinCode.equals(pinCode)) return client;
        }
        return emptyClient();
    }

    public static boolean exists(String accountNumber) {
        return !find(accountNumber).isEmpty();
    }

    private void update() {
        List<BankClient> clients = loadAllClients();
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).accountNumber.equalsIgnoreCase(this.accountNumber)) {
                clients.set(i, this);
                break;
            }
        }
        saveAllClients(clients);
    }

    private void addNew() {
        FileStorage.appendLine(Constants.CLIENTS_FILE, toLine());
    }

    public SaveResult save() {
        if (isEmpty()) return SaveResult.FAILED_EMPTY_OBJECT;
        if (mode == Mode.ADD_NEW) {
            if (exists(accountNumber)) return SaveResult.FAILED_ALREADY_EXISTS;
            addNew();
            mode = Mode.UPDATE;
            return SaveResult.SUCCEEDED;
        }
        update();
        return SaveResult.SUCCEEDED;
    }

    public boolean delete() {
        if (isEmpty()) return false;
        List<BankClient> clients = loadAllClients();
        boolean deleted = false;
        for (BankClient client : clients) {
            if (client.accountNumber.equalsIgnoreCase(this.accountNumber)) {
                client.markedForDelete = true;
                deleted = true;
                break;
            }
        }
        saveAllClients(clients);
        this.mode = Mode.EMPTY;
        return deleted;
    }

    public void deposit(double amount) {
        if (amount <= 0) return;
        this.accountBalance += amount;
        update();
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > this.accountBalance) return false;
        this.accountBalance -= amount;
        update();
        return true;
    }

    public boolean transfer(double amount, BankClient destinationClient, String userName) {
        if (destinationClient == null || destinationClient.isEmpty()) return false;
        if (amount <= 0 || amount > this.accountBalance) return false;

        if (!withdraw(amount)) return false;
        destinationClient = find(destinationClient.accountNumber);
        destinationClient.deposit(amount);

        String line = Util.nowString() + Constants.SEPARATOR +
                this.accountNumber + Constants.SEPARATOR +
                destinationClient.accountNumber + Constants.SEPARATOR +
                Util.fileDouble(amount) + Constants.SEPARATOR +
                Util.fileDouble(this.accountBalance) + Constants.SEPARATOR +
                Util.fileDouble(destinationClient.accountBalance) + Constants.SEPARATOR +
                userName;
        FileStorage.appendLine(Constants.TRANSFER_LOG_FILE, line);
        return true;
    }

    public static List<String[]> loadTransferLogRecords() {
        List<String[]> records = new ArrayList<>();
        for (String line : FileStorage.readAllLines(Constants.TRANSFER_LOG_FILE)) {
            if (line.isBlank()) continue;
            String[] p = line.split(java.util.regex.Pattern.quote(Constants.SEPARATOR), -1);
            if (p.length >= 7) records.add(p);
        }
        return records;
    }


    @Override
    public String getRoleDescription() {
        return "Bank Client - owns a bank account and can perform deposits, withdrawals, and transfers";
    }

    public String getAccountNumber() { return accountNumber; }
    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }
    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }
}
