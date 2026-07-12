package com.bankmanagement.fx;

import com.bankmanagement.*;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

/**
 * JavaFX GUI for the Bank Management System.
 *
 * This class keeps the original console project untouched and uses the existing
 * business classes: User, BankClient, Currency, Permission, and FileStorage.
 */
public class BankFxApp extends Application {
    private Stage primaryStage;
    private TableView<BankClient> clientsTable;
    private TableView<User> usersTable;
    private TableView<Currency> currenciesTable;
    private TableView<String[]> loginRegisterTable;
    private TableView<String[]> transferLogTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Bank Management System");
        showLoginScene();
    }

    private void showLoginScene() {
        Label title = new Label("Bank Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#12355B"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(320);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(320);

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#B00020"));

        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setMinWidth(120);
        loginButton.setStyle("-fx-background-color: #12355B; -fx-text-fill: white; -fx-font-weight: bold;");

        final int[] failedCount = {0};
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password.");
                return;
            }

            User user = User.find(username, password);
            if (user.isEmpty()) {
                failedCount[0]++;
                int remaining = 3 - failedCount[0];
                if (remaining <= 0) {
                    messageLabel.setText("You are locked after 3 failed trials.");
                    loginButton.setDisable(true);
                    usernameField.setDisable(true);
                    passwordField.setDisable(true);
                } else {
                    messageLabel.setText("Invalid username/password. Trials left: " + remaining);
                }
                return;
            }

            AppSession.currentUser = user;
            user.registerLogin();
            showMainScene();
        });

        VBox card = new VBox(14, title, usernameField, passwordField, loginButton, messageLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(35));
        card.setMaxWidth(430);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 18, 0.15, 0, 4);");

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #EAF2F8, #D6EAF8);");
        Scene scene = new Scene(root, 950, 620);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainScene() {
        BorderPane root = new BorderPane();
        root.setTop(buildTopBar());
        root.setCenter(buildMainTabs());
        root.setStyle("-fx-background-color: #F6F8FB;");

        Scene scene = new Scene(root, 1150, 720);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private Node buildTopBar() {
        Label title = new Label("Bank Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        Label userInfo = new Label("Logged in as: " + AppSession.currentUser.getUserName());
        userInfo.setTextFill(Color.web("#DCE6F2"));
        userInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 13));

        VBox titleBox = new VBox(2, title, userInfo);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            AppSession.currentUser = User.emptyUser();
            showLoginScene();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bar = new HBox(15, titleBox, spacer, logoutButton);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 20, 14, 20));
        bar.setStyle("-fx-background-color: #12355B;");
        return bar;
    }

    private TabPane buildMainTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().add(new Tab("Dashboard", buildDashboardTab()));
        tabPane.getTabs().add(new Tab("Clients", buildClientsTab()));
        tabPane.getTabs().add(new Tab("Transactions", buildTransactionsTab()));
        tabPane.getTabs().add(new Tab("Users", buildUsersTab()));
        tabPane.getTabs().add(new Tab("Login Register", buildLoginRegisterTab()));
        tabPane.getTabs().add(new Tab("Currency Exchange", buildCurrencyTab()));
        return tabPane;
    }

    private Node buildDashboardTab() {
        int clientCount = BankClient.loadAllClients().size();
        int userCount = User.loadAllUsers().size();
        int currencyCount = Currency.loadAllCurrencies().size();
        int loginCount = User.loadLoginRegisterRecords().size();

        HBox cards = new HBox(18,
                dashboardCard("Clients", String.valueOf(clientCount)),
                dashboardCard("Users", String.valueOf(userCount)),
                dashboardCard("Currencies", String.valueOf(currencyCount)),
                dashboardCard("Login Records", String.valueOf(loginCount))
        );
        cards.setAlignment(Pos.CENTER);

        VBox root = new VBox(cards);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        return root;
    }

    private VBox dashboardCard(String title, String value) {
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 34));
        valueLabel.setTextFill(Color.web("#12355B"));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        titleLabel.setTextFill(Color.web("#5C677D"));

        VBox card = new VBox(8, valueLabel, titleLabel);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(190, 120);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0.15, 0, 3);");
        return card;
    }

    private Node buildClientsTab() {
        clientsTable = new TableView<>();
        clientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<BankClient, String> accountCol = new TableColumn<>("Account No");
        accountCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        TableColumn<BankClient, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<BankClient, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<BankClient, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<BankClient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<BankClient, String> pinCol = new TableColumn<>("PIN");
        pinCol.setCellValueFactory(new PropertyValueFactory<>("pinCode"));
        TableColumn<BankClient, Number> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAccountBalance()));

        clientsTable.getColumns().addAll(accountCol, firstNameCol, lastNameCol, emailCol, phoneCol, pinCol, balanceCol);
        refreshClientsTable();

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshClientsTable());

        Button addButton = new Button("Add Client");
        addButton.setDisable(!has(Permission.ADD_NEW_CLIENT));
        addButton.setOnAction(e -> showClientDialog(null));

        Button editButton = new Button("Edit Client");
        editButton.setDisable(!has(Permission.UPDATE_CLIENT));
        editButton.setOnAction(e -> {
            BankClient client = selectedClient();
            if (client != null) showClientDialog(client);
        });

        Button deleteButton = new Button("Delete Client");
        deleteButton.setDisable(!has(Permission.DELETE_CLIENT));
        deleteButton.setOnAction(e -> deleteSelectedClient());

        TextField searchField = new TextField();
        searchField.setPromptText("Find by account number");
        Button findButton = new Button("Find");
        findButton.setDisable(!has(Permission.FIND_CLIENT));
        findButton.setOnAction(e -> findClientInTable(searchField.getText().trim()));

        HBox toolbar = new HBox(10, refreshButton, addButton, editButton, deleteButton, new Separator(), searchField, findButton);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10));

        VBox root = new VBox(10, toolbar, clientsTable);
        root.setPadding(new Insets(15));
        VBox.setVgrow(clientsTable, Priority.ALWAYS);
        return root;
    }

    private void refreshClientsTable() {
        clientsTable.setItems(FXCollections.observableArrayList(BankClient.loadAllClients()));
    }

    private BankClient selectedClient() {
        BankClient client = clientsTable.getSelectionModel().getSelectedItem();
        if (client == null) showWarning("Please select a client first.");
        return client;
    }

    private void findClientInTable(String accountNumber) {
        if (accountNumber.isBlank()) {
            refreshClientsTable();
            return;
        }
        BankClient client = BankClient.find(accountNumber);
        if (client.isEmpty()) {
            showWarning("Client not found.");
            return;
        }
        clientsTable.setItems(FXCollections.observableArrayList(client));
    }

    private void showClientDialog(BankClient existingClient) {
        boolean editMode = existingClient != null;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(editMode ? "Edit Client" : "Add New Client");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField accountField = new TextField(editMode ? existingClient.getAccountNumber() : "");
        accountField.setDisable(editMode);
        TextField firstNameField = new TextField(editMode ? existingClient.getFirstName() : "");
        TextField lastNameField = new TextField(editMode ? existingClient.getLastName() : "");
        TextField emailField = new TextField(editMode ? existingClient.getEmail() : "");
        TextField phoneField = new TextField(editMode ? existingClient.getPhone() : "");
        TextField pinField = new TextField(editMode ? existingClient.getPinCode() : "");
        TextField balanceField = new TextField(editMode ? Util.money(existingClient.getAccountBalance()) : "0");

        GridPane grid = formGrid();
        grid.addRow(0, new Label("Account Number:"), accountField);
        grid.addRow(1, new Label("First Name:"), firstNameField);
        grid.addRow(2, new Label("Last Name:"), lastNameField);
        grid.addRow(3, new Label("Email:"), emailField);
        grid.addRow(4, new Label("Phone:"), phoneField);
        grid.addRow(5, new Label("PIN Code:"), pinField);
        grid.addRow(6, new Label("Balance:"), balanceField);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        String accountNumber = accountField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pin = pinField.getText().trim();
        double balance;

        if (accountNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || pin.isEmpty()) {
            showWarning("Account number, first name, last name, and PIN are required.");
            return;
        }

        try {
            balance = Double.parseDouble(balanceField.getText().trim());
            if (balance < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showWarning("Balance must be a valid positive number.");
            return;
        }

        BankClient client = editMode ? existingClient : BankClient.getAddNewClientObject(accountNumber);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhone(phone);
        client.setPinCode(pin);
        client.setAccountBalance(balance);

        SaveResult saveResult = client.save();
        if (saveResult == SaveResult.FAILED_ALREADY_EXISTS) {
            showWarning("This account number already exists.");
            return;
        }

        refreshClientsTable();
        showInfo("Client saved successfully.");
    }

    private void deleteSelectedClient() {
        BankClient client = selectedClient();
        if (client == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Client");
        confirm.setHeaderText("Delete account " + client.getAccountNumber() + "?");
        confirm.setContentText("This action will remove the client from the data file.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (client.delete()) {
                refreshClientsTable();
                showInfo("Client deleted successfully.");
            } else {
                showWarning("Delete failed.");
            }
        }
    }

    private Node buildTransactionsTab() {
        if (!has(Permission.TRANSACTIONS)) return accessDeniedPane();

        TabPane transactionTabs = new TabPane();
        transactionTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        transactionTabs.getTabs().add(new Tab("Deposit", buildDepositPane()));
        transactionTabs.getTabs().add(new Tab("Withdraw", buildWithdrawPane()));
        transactionTabs.getTabs().add(new Tab("Transfer", buildTransferPane()));
        transactionTabs.getTabs().add(new Tab("Transfer Log", buildTransferLogPane()));
        return transactionTabs;
    }

    private Node buildDepositPane() {
        TextField accountField = new TextField();
        accountField.setPromptText("Account number");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        Label clientInfo = new Label("Enter account number and amount.");

        Button depositButton = new Button("Deposit");
        depositButton.setOnAction(e -> {
            BankClient client = BankClient.find(accountField.getText().trim());
            double amount = readPositiveAmount(amountField);
            if (client.isEmpty() || amount <= 0) {
                showWarning("Invalid account number or amount.");
                return;
            }
            client.deposit(amount);
            clientInfo.setText("New balance for " + client.getAccountNumber() + ": " + Util.money(client.getAccountBalance()));
            showInfo("Deposit completed.");
        });

        return simpleTransactionForm("Deposit Money", accountField, amountField, depositButton, clientInfo);
    }

    private Node buildWithdrawPane() {
        TextField accountField = new TextField();
        accountField.setPromptText("Account number");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        Label clientInfo = new Label("Enter account number and amount.");

        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setOnAction(e -> {
            BankClient client = BankClient.find(accountField.getText().trim());
            double amount = readPositiveAmount(amountField);
            if (client.isEmpty() || amount <= 0) {
                showWarning("Invalid account number or amount.");
                return;
            }
            if (!client.withdraw(amount)) {
                showWarning("Withdraw failed. Check balance.");
                return;
            }
            clientInfo.setText("New balance for " + client.getAccountNumber() + ": " + Util.money(client.getAccountBalance()));
            showInfo("Withdraw completed.");
        });

        return simpleTransactionForm("Withdraw Money", accountField, amountField, withdrawButton, clientInfo);
    }

    private Node buildTransferPane() {
        TextField sourceField = new TextField();
        sourceField.setPromptText("Source account");
        TextField destinationField = new TextField();
        destinationField.setPromptText("Destination account");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        Label resultLabel = new Label("Enter source account, destination account, and amount.");

        Button transferButton = new Button("Transfer");
        transferButton.setOnAction(e -> {
            BankClient source = BankClient.find(sourceField.getText().trim());
            BankClient destination = BankClient.find(destinationField.getText().trim());
            double amount = readPositiveAmount(amountField);

            if (source.isEmpty() || destination.isEmpty() || amount <= 0) {
                showWarning("Invalid accounts or amount.");
                return;
            }
            if (source.getAccountNumber().equalsIgnoreCase(destination.getAccountNumber())) {
                showWarning("Source and destination accounts must be different.");
                return;
            }
            if (!source.transfer(amount, destination, AppSession.currentUser.getUserName())) {
                showWarning("Transfer failed. Check balance.");
                return;
            }

            resultLabel.setText("Transfer completed. Source balance: " + Util.money(source.getAccountBalance()));
            refreshTransferLogTable();
            showInfo("Transfer completed.");
        });

        GridPane grid = formGrid();
        grid.addRow(0, new Label("From Account:"), sourceField);
        grid.addRow(1, new Label("To Account:"), destinationField);
        grid.addRow(2, new Label("Amount:"), amountField);
        grid.add(transferButton, 1, 3);
        grid.add(resultLabel, 1, 4);
        return centeredCard("Transfer Money", grid);
    }

    private Node simpleTransactionForm(String title, TextField accountField, TextField amountField, Button actionButton, Label resultLabel) {
        GridPane grid = formGrid();
        grid.addRow(0, new Label("Account Number:"), accountField);
        grid.addRow(1, new Label("Amount:"), amountField);
        grid.add(actionButton, 1, 2);
        grid.add(resultLabel, 1, 3);
        return centeredCard(title, grid);
    }

    private Node buildTransferLogPane() {
        transferLogTable = new TableView<>();
        transferLogTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        transferLogTable.getColumns().addAll(
                arrayColumn("Date/Time", 0),
                arrayColumn("From", 1),
                arrayColumn("To", 2),
                arrayColumn("Amount", 3),
                arrayColumn("From Balance", 4),
                arrayColumn("To Balance", 5),
                arrayColumn("User", 6)
        );
        refreshTransferLogTable();

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshTransferLogTable());
        VBox root = new VBox(10, refreshButton, transferLogTable);
        root.setPadding(new Insets(15));
        VBox.setVgrow(transferLogTable, Priority.ALWAYS);
        return root;
    }

    private void refreshTransferLogTable() {
        if (transferLogTable != null) {
            transferLogTable.setItems(FXCollections.observableArrayList(BankClient.loadTransferLogRecords()));
        }
    }

    private Node buildUsersTab() {
        if (!has(Permission.MANAGE_USERS)) return accessDeniedPane();

        usersTable = new TableView<>();
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<User, Number> permissionsCol = new TableColumn<>("Permissions");
        permissionsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPermissions()));

        usersTable.getColumns().addAll(usernameCol, firstNameCol, lastNameCol, emailCol, phoneCol, permissionsCol);
        refreshUsersTable();

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshUsersTable());
        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> showUserDialog(null));
        Button editButton = new Button("Edit User");
        editButton.setOnAction(e -> {
            User user = selectedUser();
            if (user != null) showUserDialog(user);
        });
        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> deleteSelectedUser());

        HBox toolbar = new HBox(10, refreshButton, addButton, editButton, deleteButton);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10));

        VBox root = new VBox(10, toolbar, usersTable);
        root.setPadding(new Insets(15));
        VBox.setVgrow(usersTable, Priority.ALWAYS);
        return root;
    }

    private void refreshUsersTable() {
        usersTable.setItems(FXCollections.observableArrayList(User.loadAllUsers()));
    }

    private User selectedUser() {
        User user = usersTable.getSelectionModel().getSelectedItem();
        if (user == null) showWarning("Please select a user first.");
        return user;
    }

    private void showUserDialog(User existingUser) {
        boolean editMode = existingUser != null;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(editMode ? "Edit User" : "Add New User");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField usernameField = new TextField(editMode ? existingUser.getUserName() : "");
        usernameField.setDisable(editMode);
        TextField firstNameField = new TextField(editMode ? existingUser.getFirstName() : "");
        TextField lastNameField = new TextField(editMode ? existingUser.getLastName() : "");
        TextField emailField = new TextField(editMode ? existingUser.getEmail() : "");
        TextField phoneField = new TextField(editMode ? existingUser.getPhone() : "");
        PasswordField passwordField = new PasswordField();
        passwordField.setText(editMode ? existingUser.getPassword() : "");

        CheckBox fullAccess = new CheckBox("Full Access");
        CheckBox listClients = new CheckBox("List Clients");
        CheckBox addClient = new CheckBox("Add Client");
        CheckBox deleteClient = new CheckBox("Delete Client");
        CheckBox updateClient = new CheckBox("Update Client");
        CheckBox findClient = new CheckBox("Find Client");
        CheckBox transactions = new CheckBox("Transactions");
        CheckBox manageUsers = new CheckBox("Manage Users");
        CheckBox loginRegister = new CheckBox("Login Register");
        CheckBox currencyExchange = new CheckBox("Currency Exchange");
        CheckBox[] boxes = {listClients, addClient, deleteClient, updateClient, findClient, transactions, manageUsers, loginRegister, currencyExchange};

        if (editMode) {
            int p = existingUser.getPermissions();
            fullAccess.setSelected(p == Permission.FULL_ACCESS);
            listClients.setSelected(Permission.hasPermission(p, Permission.LIST_CLIENTS));
            addClient.setSelected(Permission.hasPermission(p, Permission.ADD_NEW_CLIENT));
            deleteClient.setSelected(Permission.hasPermission(p, Permission.DELETE_CLIENT));
            updateClient.setSelected(Permission.hasPermission(p, Permission.UPDATE_CLIENT));
            findClient.setSelected(Permission.hasPermission(p, Permission.FIND_CLIENT));
            transactions.setSelected(Permission.hasPermission(p, Permission.TRANSACTIONS));
            manageUsers.setSelected(Permission.hasPermission(p, Permission.MANAGE_USERS));
            loginRegister.setSelected(Permission.hasPermission(p, Permission.SHOW_LOGIN_REGISTER));
            currencyExchange.setSelected(Permission.hasPermission(p, Permission.CURRENCY_EXCHANGE));
        }

        fullAccess.selectedProperty().addListener((obs, oldValue, selected) -> {
            for (CheckBox box : boxes) {
                box.setDisable(selected);
                if (selected) box.setSelected(true);
            }
        });
        if (fullAccess.isSelected()) {
            for (CheckBox box : boxes) box.setDisable(true);
        }

        GridPane permissionGrid = new GridPane();
        permissionGrid.setHgap(12);
        permissionGrid.setVgap(6);
        permissionGrid.add(fullAccess, 0, 0, 2, 1);
        permissionGrid.add(listClients, 0, 1);
        permissionGrid.add(addClient, 1, 1);
        permissionGrid.add(deleteClient, 0, 2);
        permissionGrid.add(updateClient, 1, 2);
        permissionGrid.add(findClient, 0, 3);
        permissionGrid.add(transactions, 1, 3);
        permissionGrid.add(manageUsers, 0, 4);
        permissionGrid.add(loginRegister, 1, 4);
        permissionGrid.add(currencyExchange, 0, 5);

        GridPane grid = formGrid();
        grid.addRow(0, new Label("Username:"), usernameField);
        grid.addRow(1, new Label("First Name:"), firstNameField);
        grid.addRow(2, new Label("Last Name:"), lastNameField);
        grid.addRow(3, new Label("Email:"), emailField);
        grid.addRow(4, new Label("Phone:"), phoneField);
        grid.addRow(5, new Label("Password:"), passwordField);
        grid.addRow(6, new Label("Permissions:"), permissionGrid);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String password = passwordField.getText();
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
            showWarning("Username, first name, last name, and password are required.");
            return;
        }

        int permissions = fullAccess.isSelected() ? Permission.FULL_ACCESS : 0;
        if (!fullAccess.isSelected()) {
            if (listClients.isSelected()) permissions |= Permission.LIST_CLIENTS;
            if (addClient.isSelected()) permissions |= Permission.ADD_NEW_CLIENT;
            if (deleteClient.isSelected()) permissions |= Permission.DELETE_CLIENT;
            if (updateClient.isSelected()) permissions |= Permission.UPDATE_CLIENT;
            if (findClient.isSelected()) permissions |= Permission.FIND_CLIENT;
            if (transactions.isSelected()) permissions |= Permission.TRANSACTIONS;
            if (manageUsers.isSelected()) permissions |= Permission.MANAGE_USERS;
            if (loginRegister.isSelected()) permissions |= Permission.SHOW_LOGIN_REGISTER;
            if (currencyExchange.isSelected()) permissions |= Permission.CURRENCY_EXCHANGE;
        }

        User user = editMode ? existingUser : User.getAddNewUserObject(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(emailField.getText().trim());
        user.setPhone(phoneField.getText().trim());
        user.setPassword(password);
        user.setPermissions(permissions);

        SaveResult saveResult = user.save();
        if (saveResult == SaveResult.FAILED_ALREADY_EXISTS) {
            showWarning("This username already exists.");
            return;
        }
        refreshUsersTable();
        showInfo("User saved successfully.");
    }

    private void deleteSelectedUser() {
        User user = selectedUser();
        if (user == null) return;
        if (user.getUserName().equalsIgnoreCase(AppSession.currentUser.getUserName())) {
            showWarning("You cannot delete the current logged-in user.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete User");
        confirm.setHeaderText("Delete user " + user.getUserName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (user.delete()) {
                refreshUsersTable();
                showInfo("User deleted successfully.");
            } else {
                showWarning("Delete failed.");
            }
        }
    }

    private Node buildLoginRegisterTab() {
        if (!has(Permission.SHOW_LOGIN_REGISTER)) return accessDeniedPane();

        loginRegisterTable = new TableView<>();
        loginRegisterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        loginRegisterTable.getColumns().addAll(
                arrayColumn("Date/Time", 0),
                arrayColumn("Username", 1),
                arrayColumn("Password", 2),
                arrayColumn("Permissions", 3)
        );
        refreshLoginRegisterTable();

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshLoginRegisterTable());

        VBox root = new VBox(10, refreshButton, loginRegisterTable);
        root.setPadding(new Insets(15));
        VBox.setVgrow(loginRegisterTable, Priority.ALWAYS);
        return root;
    }

    private void refreshLoginRegisterTable() {
        loginRegisterTable.setItems(FXCollections.observableArrayList(User.loadLoginRegisterRecords()));
    }

    private Node buildCurrencyTab() {
        if (!has(Permission.CURRENCY_EXCHANGE)) return accessDeniedPane();

        currenciesTable = new TableView<>();
        currenciesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        TableColumn<Currency, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        TableColumn<Currency, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("currencyCode"));
        TableColumn<Currency, String> nameCol = new TableColumn<>("Currency Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("currencyName"));
        TableColumn<Currency, Number> rateCol = new TableColumn<>("Rate");
        rateCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getRate()));
        currenciesTable.getColumns().addAll(countryCol, codeCol, nameCol, rateCol);
        refreshCurrenciesTable();

        TextField searchField = new TextField();
        searchField.setPromptText("Currency code, e.g. USD");
        Button findButton = new Button("Find");
        findButton.setOnAction(e -> {
            String code = searchField.getText().trim();
            if (code.isBlank()) {
                refreshCurrenciesTable();
                return;
            }
            Currency currency = Currency.findByCode(code);
            if (currency.isEmpty()) showWarning("Currency not found.");
            else currenciesTable.setItems(FXCollections.observableArrayList(currency));
        });

        TextField newRateField = new TextField();
        newRateField.setPromptText("New rate");
        Button updateRateButton = new Button("Update Rate");
        updateRateButton.setOnAction(e -> {
            Currency currency = currenciesTable.getSelectionModel().getSelectedItem();
            if (currency == null) {
                showWarning("Please select a currency first.");
                return;
            }
            double rate = readPositiveAmount(newRateField);
            if (rate <= 0) {
                showWarning("Invalid rate.");
                return;
            }
            if (currency.updateRate(rate)) {
                refreshCurrenciesTable();
                showInfo("Rate updated successfully.");
            }
        });

        ComboBox<String> fromCombo = new ComboBox<>();
        ComboBox<String> toCombo = new ComboBox<>();
        loadCurrencyCodes(fromCombo, toCombo);
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        Label convertResult = new Label("Conversion result will appear here.");
        Button convertButton = new Button("Convert");
        convertButton.setOnAction(e -> {
            String from = fromCombo.getValue();
            String to = toCombo.getValue();
            double amount = readPositiveAmount(amountField);
            if (from == null || to == null || amount <= 0) {
                showWarning("Select currencies and enter a valid amount.");
                return;
            }
            Currency fromCurrency = Currency.findByCode(from);
            Currency toCurrency = Currency.findByCode(to);
            double result = fromCurrency.convertToOtherCurrency(amount, toCurrency);
            convertResult.setText(Util.money(amount) + " " + from + " = " + Util.money(result) + " " + to);
        });

        HBox toolbar = new HBox(10, searchField, findButton, new Separator(), newRateField, updateRateButton);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        HBox converter = new HBox(10, new Label("From:"), fromCombo, new Label("To:"), toCombo, amountField, convertButton, convertResult);
        converter.setAlignment(Pos.CENTER_LEFT);
        converter.setPadding(new Insets(8));
        converter.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        VBox root = new VBox(10, toolbar, converter, currenciesTable);
        root.setPadding(new Insets(15));
        VBox.setVgrow(currenciesTable, Priority.ALWAYS);
        return root;
    }

    private void refreshCurrenciesTable() {
        currenciesTable.setItems(FXCollections.observableArrayList(Currency.loadAllCurrencies()));
    }

    private void loadCurrencyCodes(ComboBox<String> fromCombo, ComboBox<String> toCombo) {
        ObservableList<String> codes = FXCollections.observableArrayList();
        for (Currency currency : Currency.loadAllCurrencies()) {
            codes.add(currency.getCurrencyCode());
        }
        fromCombo.setItems(codes);
        toCombo.setItems(FXCollections.observableArrayList(codes));
        if (codes.contains("USD")) fromCombo.setValue("USD");
        if (codes.contains("ILS")) toCombo.setValue("ILS");
        else if (!codes.isEmpty()) toCombo.setValue(codes.get(0));
    }

    private TableColumn<String[], String> arrayColumn(String title, int index) {
        TableColumn<String[], String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> {
            String[] row = data.getValue();
            return new SimpleStringProperty(index < row.length ? row[index] : "");
        });
        return column;
    }

    private GridPane formGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(15));
        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setMinWidth(130);
        ColumnConstraints inputCol = new ColumnConstraints();
        inputCol.setPrefWidth(260);
        grid.getColumnConstraints().addAll(labelCol, inputCol);
        return grid;
    }

    private Node centeredCard(String titleText, Node content) {
        Label title = new Label(titleText);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#12355B"));

        VBox card = new VBox(18, title, content);
        card.setPadding(new Insets(25));
        card.setMaxWidth(560);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0.15, 0, 3);");

        StackPane pane = new StackPane(card);
        pane.setPadding(new Insets(25));
        return pane;
    }

    private Node accessDeniedPane() {
        Label title = new Label("Access Denied");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#B00020"));
        Label message = new Label("You do not have permission to open this screen.");
        VBox box = new VBox(10, title, message);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private boolean has(int permission) {
        return AppSession.currentUser != null && AppSession.currentUser.hasPermission(permission);
    }

    private double readPositiveAmount(TextField field) {
        try {
            double value = Double.parseDouble(field.getText().trim());
            return value > 0 ? value : -1;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
