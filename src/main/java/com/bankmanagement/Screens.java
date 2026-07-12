package com.bankmanagement;

import java.util.List;

public class Screens {

    public void start() {
        while (true) {
            boolean loggedIn = showLoginScreen();
            if (!loggedIn) break;
            showMainMenu();
        }
        System.out.println("Goodbye.");
    }

    private boolean showLoginScreen() {
        boolean loginFailed = false;
        int failedCount = 0;

        while (true) {
            ConsoleUI.clear();
            ConsoleUI.header("Login Screen");

            if (loginFailed) {
                failedCount++;
                System.out.println("Invalid Username/Password!");
                System.out.println("You have " + (3 - failedCount) + " trial(s) to login.");
                if (failedCount == 3) {
                    System.out.println("You are locked after 3 failed trials.");
                    return false;
                }
            }

            System.out.print("Enter User Name: ");
            String userName = Input.readString();
            System.out.print("Enter Password : ");
            String password = Input.readString();

            AppSession.currentUser = User.find(userName, password);
            loginFailed = AppSession.currentUser.isEmpty();
            if (!loginFailed) {
                AppSession.currentUser.registerLogin();
                return true;
            }
        }
    }

    private void showMainMenu() {
        while (true) {
            ConsoleUI.clear();
            ConsoleUI.header("Main Menu Screen");
            System.out.println("[1] Show Client List");
            System.out.println("[2] Add New Client");
            System.out.println("[3] Delete Client");
            System.out.println("[4] Update Client Info");
            System.out.println("[5] Find Client");
            System.out.println("[6] Transactions");
            System.out.println("[7] Manage Users");
            System.out.println("[8] Login Register");
            System.out.println("[9] Currency Exchange");
            System.out.println("[10] Logout");
            System.out.print("Choose what do you want to do [1 to 10]: ");
            int choice = Input.readIntBetween(1, 10);

            switch (choice) {
                case 1 -> showClientList();
                case 2 -> addNewClient();
                case 3 -> deleteClient();
                case 4 -> updateClient();
                case 5 -> findClient();
                case 6 -> showTransactionsMenu();
                case 7 -> showManageUsersMenu();
                case 8 -> showLoginRegister();
                case 9 -> showCurrencyExchangeMenu();
                case 10 -> {
                    AppSession.currentUser = User.emptyUser();
                    return;
                }
            }
        }
    }

    private boolean checkAccess(int permission) {
        if (!AppSession.currentUser.hasPermission(permission)) {
            ConsoleUI.accessDenied();
            Input.pause();
            return false;
        }
        return true;
    }

    private void showClientList() {
        if (!checkAccess(Permission.LIST_CLIENTS)) return;
        ConsoleUI.clear();
        ConsoleUI.header("Client List Screen");
        List<BankClient> clients = BankClient.loadAllClients();
        System.out.println("\nClient count: " + clients.size());
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-25s %-15s %-12s %-12s%n", "Account No", "Client Name", "Email", "Phone", "Pin", "Balance");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        for (BankClient client : clients) {
            System.out.printf("%-15s %-20s %-25s %-15s %-12s %-12s%n",
                    client.getAccountNumber(), client.fullName(), client.getEmail(),
                    client.getPhone(), client.getPinCode(), Util.money(client.getAccountBalance()));
        }
        Input.pause();
    }

    private void printClientCard(BankClient client) {
        System.out.println("\nClient Card");
        System.out.println("------------------------------");
        System.out.println("First Name     : " + client.getFirstName());
        System.out.println("Last Name      : " + client.getLastName());
        System.out.println("Full Name      : " + client.fullName());
        System.out.println("Email          : " + client.getEmail());
        System.out.println("Phone          : " + client.getPhone());
        System.out.println("Account Number : " + client.getAccountNumber());
        System.out.println("Pin Code       : " + client.getPinCode());
        System.out.println("Balance        : " + Util.money(client.getAccountBalance()));
        System.out.println("------------------------------");
    }

    private void readClientInfo(BankClient client) {
        System.out.print("Enter First Name: ");
        client.setFirstName(Input.readString());
        System.out.print("Enter Last Name : ");
        client.setLastName(Input.readString());
        System.out.print("Enter Email     : ");
        client.setEmail(Input.readString());
        System.out.print("Enter Phone     : ");
        client.setPhone(Input.readString());
        System.out.print("Enter Pin Code  : ");
        client.setPinCode(Input.readString());
        System.out.print("Enter Balance   : ");
        client.setAccountBalance(Input.readDouble());
    }

    private void addNewClient() {
        if (!checkAccess(Permission.ADD_NEW_CLIENT)) return;
        ConsoleUI.clear();
        ConsoleUI.header("Add New Client Screen");
        System.out.print("Please enter account number: ");
        String accountNumber = Input.readString();
        while (BankClient.exists(accountNumber)) {
            System.out.print("Account number is already used, choose another one: ");
            accountNumber = Input.readString();
        }
        BankClient client = BankClient.getAddNewClientObject(accountNumber);
        readClientInfo(client);
        SaveResult result = client.save();
        if (result == SaveResult.SUCCEEDED) {
            System.out.println("\nClient added successfully.");
            printClientCard(client);
        } else {
            System.out.println("\nClient was not saved.");
        }
        Input.pause();
    }

    private BankClient askForExistingClient(String message) {
        System.out.print(message);
        String accountNumber = Input.readString();
        while (!BankClient.exists(accountNumber)) {
            System.out.println("Client with account number [" + accountNumber + "] does not exist.");
            System.out.print(message);
            accountNumber = Input.readString();
        }
        return BankClient.find(accountNumber);
    }

    private void deleteClient() {
        if (!checkAccess(Permission.DELETE_CLIENT)) return;
        ConsoleUI.clear();
        ConsoleUI.header("Delete Client Screen");
        BankClient client = askForExistingClient("Please enter account number: ");
        printClientCard(client);
        System.out.print("Are you sure you want to delete this client? Y/N: ");
        if (Input.readYesNo()) {
            if (client.delete()) System.out.println("\nClient deleted successfully.");
            else System.out.println("\nDelete failed.");
        } else {
            System.out.println("\nOperation cancelled.");
        }
        Input.pause();
    }

    private void updateClient() {
        if (!checkAccess(Permission.UPDATE_CLIENT)) return;
        ConsoleUI.clear();
        ConsoleUI.header("Update Client Screen");
        BankClient client = askForExistingClient("Please enter account number: ");
        printClientCard(client);
        System.out.print("Are you sure you want to update this client? Y/N: ");
        if (Input.readYesNo()) {
            readClientInfo(client);
            client.save();
            System.out.println("\nClient updated successfully.");
            printClientCard(client);
        } else {
            System.out.println("\nOperation cancelled.");
        }
        Input.pause();
    }

    private void findClient() {
        if (!checkAccess(Permission.FIND_CLIENT)) return;
        ConsoleUI.clear();
        ConsoleUI.header("Find Client Screen");
        System.out.print("Please enter account number: ");
        String accountNumber = Input.readString();
        BankClient client = BankClient.find(accountNumber);
        if (client.isEmpty()) System.out.println("\nClient was not found.");
        else printClientCard(client);
        Input.pause();
    }

    private void showTransactionsMenu() {
        if (!checkAccess(Permission.TRANSACTIONS)) return;
        while (true) {
            ConsoleUI.clear();
            ConsoleUI.header("Transactions Menu Screen");
            System.out.println("[1] Deposit");
            System.out.println("[2] Withdraw");
            System.out.println("[3] Total Balances");
            System.out.println("[4] Transfer");
            System.out.println("[5] Transfer Log");
            System.out.println("[6] Main Menu");
            System.out.print("Choose what do you want to do [1 to 6]: ");
            int choice = Input.readIntBetween(1, 6);
            switch (choice) {
                case 1 -> deposit();
                case 2 -> withdraw();
                case 3 -> showTotalBalances();
                case 4 -> transfer();
                case 5 -> showTransferLog();
                case 6 -> { return; }
            }
        }
    }

    private void deposit() {
        ConsoleUI.clear();
        ConsoleUI.header("Deposit Screen");
        BankClient client = askForExistingClient("Please enter account number: ");
        printClientCard(client);
        System.out.print("Please enter deposit amount: ");
        double amount = Input.readPositiveDouble();
        System.out.print("Are you sure you want to perform this transaction? Y/N: ");
        if (Input.readYesNo()) {
            client.deposit(amount);
            System.out.println("\nAmount deposited successfully. New balance: " + Util.money(client.getAccountBalance()));
        } else {
            System.out.println("\nOperation cancelled.");
        }
        Input.pause();
    }

    private void withdraw() {
        ConsoleUI.clear();
        ConsoleUI.header("Withdraw Screen");
        BankClient client = askForExistingClient("Please enter account number: ");
        printClientCard(client);
        System.out.print("Please enter withdraw amount: ");
        double amount = Input.readPositiveDouble();
        while (amount > client.getAccountBalance()) {
            System.out.println("Insufficient balance! Current balance is: " + Util.money(client.getAccountBalance()));
            System.out.print("Please enter withdraw amount: ");
            amount = Input.readPositiveDouble();
        }
        System.out.print("Are you sure you want to perform this transaction? Y/N: ");
        if (Input.readYesNo()) {
            client.withdraw(amount);
            System.out.println("\nAmount withdrawn successfully. New balance: " + Util.money(client.getAccountBalance()));
        } else {
            System.out.println("\nOperation cancelled.");
        }
        Input.pause();
    }

    private void showTotalBalances() {
        ConsoleUI.clear();
        ConsoleUI.header("Total Balances Screen");
        List<BankClient> clients = BankClient.loadAllClients();
        double total = 0;
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-15s %-25s %-15s%n", "Account No", "Client Name", "Balance");
        System.out.println("--------------------------------------------------------------------------------");
        for (BankClient client : clients) {
            total += client.getAccountBalance();
            System.out.printf("%-15s %-25s %-15s%n", client.getAccountNumber(), client.fullName(), Util.money(client.getAccountBalance()));
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Total balances = " + Util.money(total));
        Input.pause();
    }

    private void transfer() {
        ConsoleUI.clear();
        ConsoleUI.header("Transfer Screen");
        BankClient source = askForExistingClient("Enter source account number: ");
        printClientCard(source);
        BankClient destination = askForExistingClient("Enter destination account number: ");
        while (destination.getAccountNumber().equalsIgnoreCase(source.getAccountNumber())) {
            System.out.println("Destination account cannot be the same as source account.");
            destination = askForExistingClient("Enter destination account number: ");
        }
        printClientCard(destination);
        System.out.print("Enter transfer amount: ");
        double amount = Input.readPositiveDouble();
        while (amount > source.getAccountBalance()) {
            System.out.println("Amount exceeds source balance: " + Util.money(source.getAccountBalance()));
            System.out.print("Enter transfer amount: ");
            amount = Input.readPositiveDouble();
        }
        System.out.print("Are you sure you want to perform this transfer? Y/N: ");
        if (Input.readYesNo()) {
            if (source.transfer(amount, destination, AppSession.currentUser.getUserName())) {
                System.out.println("\nTransfer completed successfully.");
                System.out.println("Source balance: " + Util.money(source.getAccountBalance()));
                System.out.println("Destination balance: " + Util.money(BankClient.find(destination.getAccountNumber()).getAccountBalance()));
            } else {
                System.out.println("\nTransfer failed.");
            }
        } else {
            System.out.println("\nOperation cancelled.");
        }
        Input.pause();
    }

    private void showTransferLog() {
        ConsoleUI.clear();
        ConsoleUI.header("Transfer Log Screen");
        List<String[]> records = BankClient.loadTransferLogRecords();
        System.out.println("\nRecords count: " + records.size());
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-22s %-15s %-15s %-12s %-18s %-18s %-12s%n",
                "Date/Time", "Source", "Destination", "Amount", "Source After", "Dest After", "User");
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        for (String[] r : records) {
            System.out.printf("%-22s %-15s %-15s %-12s %-18s %-18s %-12s%n", r[0], r[1], r[2], r[3], r[4], r[5], r[6]);
        }
        Input.pause();
    }

    private void showManageUsersMenu() {
        if (!checkAccess(Permission.MANAGE_USERS)) return;
        while (true) {
            ConsoleUI.clear();
            ConsoleUI.header("Manage Users Menu Screen");
            System.out.println("[1] List Users");
            System.out.println("[2] Add New User");
            System.out.println("[3] Delete User");
            System.out.println("[4] Update User");
            System.out.println("[5] Find User");
            System.out.println("[6] Main Menu");
            System.out.print("Choose what do you want to do [1 to 6]: ");
            int choice = Input.readIntBetween(1, 6);
            switch (choice) {
                case 1 -> listUsers();
                case 2 -> addNewUser();
                case 3 -> deleteUser();
                case 4 -> updateUser();
                case 5 -> findUser();
                case 6 -> { return; }
            }
        }
    }

    private void listUsers() {
        ConsoleUI.clear();
        ConsoleUI.header("Users List Screen");
        List<User> users = User.loadAllUsers();
        System.out.println("\nUsers count: " + users.size());
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-25s %-15s %-15s %-12s%n", "Username", "Full Name", "Email", "Phone", "Password", "Permissions");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-15s %-20s %-25s %-15s %-15s %-12d%n",
                    user.getUserName(), user.fullName(), user.getEmail(), user.getPhone(),
                    user.getPassword(), user.getPermissions());
        }
        Input.pause();
    }

    private void printUserCard(User user) {
        System.out.println("\nUser Card");
        System.out.println("------------------------------");
        System.out.println("First Name  : " + user.getFirstName());
        System.out.println("Last Name   : " + user.getLastName());
        System.out.println("Full Name   : " + user.fullName());
        System.out.println("Email       : " + user.getEmail());
        System.out.println("Phone       : " + user.getPhone());
        System.out.println("Username    : " + user.getUserName());
        System.out.println("Password    : " + user.getPassword());
        System.out.println("Permissions : " + user.getPermissions());
        System.out.println("------------------------------");
    }

    private int readPermissionsToSet() {
        int permissions = 0;
        System.out.print("Do you want to give full access? Y/N: ");
        if (Input.readYesNo()) return Permission.FULL_ACCESS;

        System.out.println("\nDo you want to give access to:");
        System.out.print("Show client list? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.LIST_CLIENTS;
        System.out.print("Add new client? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.ADD_NEW_CLIENT;
        System.out.print("Delete client? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.DELETE_CLIENT;
        System.out.print("Update client? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.UPDATE_CLIENT;
        System.out.print("Find client? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.FIND_CLIENT;
        System.out.print("Transactions? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.TRANSACTIONS;
        System.out.print("Manage users? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.MANAGE_USERS;
        System.out.print("Show login register? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.SHOW_LOGIN_REGISTER;
        System.out.print("Currency exchange? Y/N: ");
        if (Input.readYesNo()) permissions += Permission.CURRENCY_EXCHANGE;
        return permissions;
    }

    private void readUserInfo(User user) {
        System.out.print("Enter First Name: ");
        user.setFirstName(Input.readString());
        System.out.print("Enter Last Name : ");
        user.setLastName(Input.readString());
        System.out.print("Enter Email     : ");
        user.setEmail(Input.readString());
        System.out.print("Enter Phone     : ");
        user.setPhone(Input.readString());
        System.out.print("Enter Password  : ");
        user.setPassword(Input.readString());
        user.setPermissions(readPermissionsToSet());
    }

    private void addNewUser() {
        ConsoleUI.clear();
        ConsoleUI.header("Add New User Screen");
        System.out.print("Please enter username: ");
        String userName = Input.readString();
        while (User.exists(userName)) {
            System.out.print("Username is already used, choose another one: ");
            userName = Input.readString();
        }
        User user = User.getAddNewUserObject(userName);
        readUserInfo(user);
        SaveResult result = user.save();
        if (result == SaveResult.SUCCEEDED) {
            System.out.println("\nUser added successfully.");
            printUserCard(user);
        } else {
            System.out.println("\nUser was not saved.");
        }
        Input.pause();
    }

    private User askForExistingUser(String message) {
        System.out.print(message);
        String userName = Input.readString();
        while (!User.exists(userName)) {
            System.out.println("User with username [" + userName + "] does not exist.");
            System.out.print(message);
            userName = Input.readString();
        }
        return User.find(userName);
    }

    private void deleteUser() {
        ConsoleUI.clear();
        ConsoleUI.header("Delete User Screen");
        User user = askForExistingUser("Please enter username: ");
        printUserCard(user);
        System.out.print("Are you sure you want to delete this user? Y/N: ");
        if (Input.readYesNo()) {
            if (user.delete()) System.out.println("\nUser deleted successfully.");
            else System.out.println("\nDelete failed.");
        } else {
            System.out.println("\nOperation cancelled.");
        }
        Input.pause();
    }

    private void updateUser() {
        ConsoleUI.clear();
        ConsoleUI.header("Update User Screen");
        User user = askForExistingUser("Please enter username: ");
        printUserCard(user);
        System.out.print("Are you sure you want to update this user? Y/N: ");
        if (Input.readYesNo()) {
            readUserInfo(user);
            user.save();
            System.out.println("\nUser updated successfully.");
            printUserCard(user);
        } else {
            System.out.println("\nOperation cancelled.");
        }
        Input.pause();
    }

    private void findUser() {
        ConsoleUI.clear();
        ConsoleUI.header("Find User Screen");
        System.out.print("Please enter username: ");
        String userName = Input.readString();
        User user = User.find(userName);
        if (user.isEmpty()) System.out.println("\nUser was not found.");
        else printUserCard(user);
        Input.pause();
    }

    private void showLoginRegister() {
        if (!checkAccess(Permission.SHOW_LOGIN_REGISTER)) return;
        ConsoleUI.clear();
        ConsoleUI.header("Login Register Screen");
        List<String[]> records = User.loadLoginRegisterRecords();
        System.out.println("\nRecords count: " + records.size());
        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("%-25s %-15s %-15s %-12s%n", "Date/Time", "Username", "Password", "Permissions");
        System.out.println("----------------------------------------------------------------------------");
        for (String[] r : records) {
            System.out.printf("%-25s %-15s %-15s %-12s%n", r[0], r[1], r[2], r[3]);
        }
        Input.pause();
    }

    private void showCurrencyExchangeMenu() {
        if (!checkAccess(Permission.CURRENCY_EXCHANGE)) return;
        while (true) {
            ConsoleUI.clear();
            ConsoleUI.header("Currency Exchange Menu Screen");
            System.out.println("[1] List Currencies");
            System.out.println("[2] Find Currency By Code");
            System.out.println("[3] Find Currency By Country");
            System.out.println("[4] Update Currency Rate");
            System.out.println("[5] Currency Calculator");
            System.out.println("[6] Main Menu");
            System.out.print("Choose what do you want to do [1 to 6]: ");
            int choice = Input.readIntBetween(1, 6);
            switch (choice) {
                case 1 -> listCurrencies();
                case 2 -> findCurrencyByCode();
                case 3 -> findCurrencyByCountry();
                case 4 -> updateCurrencyRate();
                case 5 -> currencyCalculator();
                case 6 -> { return; }
            }
        }
    }

    private void listCurrencies() {
        ConsoleUI.clear();
        ConsoleUI.header("Currencies List Screen");
        List<Currency> currencies = Currency.loadAllCurrencies();
        System.out.println("\nCurrencies count: " + currencies.size());
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.printf("%-35s %-10s %-30s %-12s%n", "Country", "Code", "Name", "Rate");
        System.out.println("----------------------------------------------------------------------------------------------------");
        for (Currency currency : currencies) {
            System.out.printf("%-35s %-10s %-30s %-12s%n",
                    currency.getCountry(), currency.getCurrencyCode(), currency.getCurrencyName(), Util.fileDouble(currency.getRate()));
        }
        Input.pause();
    }

    private void printCurrencyCard(Currency currency) {
        System.out.println("\nCurrency Card");
        System.out.println("------------------------------");
        System.out.println("Country : " + currency.getCountry());
        System.out.println("Code    : " + currency.getCurrencyCode());
        System.out.println("Name    : " + currency.getCurrencyName());
        System.out.println("Rate    : " + Util.fileDouble(currency.getRate()));
        System.out.println("------------------------------");
    }

    private void findCurrencyByCode() {
        ConsoleUI.clear();
        ConsoleUI.header("Find Currency By Code Screen");
        System.out.print("Enter currency code: ");
        String code = Input.readString();
        Currency currency = Currency.findByCode(code);
        if (currency.isEmpty()) System.out.println("\nCurrency was not found.");
        else printCurrencyCard(currency);
        Input.pause();
    }

    private void findCurrencyByCountry() {
        ConsoleUI.clear();
        ConsoleUI.header("Find Currency By Country Screen");
        System.out.print("Enter country name: ");
        String country = Input.readString();
        Currency currency = Currency.findByCountry(country);
        if (currency.isEmpty()) System.out.println("\nCurrency was not found.");
        else printCurrencyCard(currency);
        Input.pause();
    }

    private Currency askForExistingCurrencyByCode(String message) {
        System.out.print(message);
        String code = Input.readString();
        Currency currency = Currency.findByCode(code);
        while (currency.isEmpty()) {
            System.out.println("Currency with code [" + code + "] does not exist.");
            System.out.print(message);
            code = Input.readString();
            currency = Currency.findByCode(code);
        }
        return currency;
    }

    private void updateCurrencyRate() {
        ConsoleUI.clear();
        ConsoleUI.header("Update Currency Rate Screen");
        Currency currency = askForExistingCurrencyByCode("Enter currency code: ");
        printCurrencyCard(currency);
        System.out.print("Enter new rate: ");
        double newRate = Input.readPositiveDouble();
        if (currency.updateRate(newRate)) System.out.println("\nCurrency rate updated successfully.");
        else System.out.println("\nUpdate failed.");
        printCurrencyCard(currency);
        Input.pause();
    }

    private void currencyCalculator() {
        ConsoleUI.clear();
        ConsoleUI.header("Currency Calculator Screen");
        Currency from = askForExistingCurrencyByCode("Enter source currency code: ");
        Currency to = askForExistingCurrencyByCode("Enter target currency code: ");
        System.out.print("Enter amount to exchange: ");
        double amount = Input.readPositiveDouble();
        double result = from.convertToOtherCurrency(amount, to);
        System.out.println("\n" + Util.money(amount) + " " + from.getCurrencyCode() +
                " = " + Util.money(result) + " " + to.getCurrencyCode());
        Input.pause();
    }
}
