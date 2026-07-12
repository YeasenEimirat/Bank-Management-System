# Bank Management System - Project Report Draft

## 1. Project Description
Bank Management System is a Java Object-Oriented Programming project that represents a real-world banking system. The project allows system users to manage bank clients, perform financial transactions, manage users and permissions, view login history, view transfer logs, and use currency exchange features. The system uses text files to store and reload data after closing the program.

The project includes a JavaFX graphical user interface and a console version. Both versions use the same core OOP classes and the same data files.

## 2. Main Features
- Login with username and password.
- Client management: add, update, delete, search, and list clients.
- Transactions: deposit, withdraw, transfer, and total balances.
- Transfer log saved in a text file.
- User management with permissions.
- Login register saved in a text file.
- Currency exchange module.
- JavaFX GUI screens for the main operations.
- Input validation and file error handling.

## 3. OOP Concepts Used

### Classes and Objects
The project contains meaningful classes such as `User`, `BankClient`, `Currency`, `Person`, `FileStorage`, `Permission`, `Screens`, and `BankFxApp`. Each class has a clear responsibility.

### Encapsulation
Important fields are private or protected. Access to data is controlled using getters, setters, and methods such as `save()`, `delete()`, `deposit()`, and `withdraw()`.

### Inheritance
`User` and `BankClient` inherit from the `Person` class because both share common personal information such as first name, last name, email, and phone.

### Abstraction
`Person` was changed into an abstract class. It contains the abstract method `getRoleDescription()`, which forces every child class to describe its role in the system.

### Polymorphism
`User` and `BankClient` override `getRoleDescription()` differently. This shows that the same method name can have different behavior based on the object type.

### Composition / Usage Relationships
The UI classes use the business classes. For example, `BankFxApp` uses `User`, `BankClient`, `Currency`, `Permission`, and `FileStorage` to perform the system operations.

## 4. Main Classes

### Person
An abstract base class that stores shared person data and defines the abstract method `getRoleDescription()`.

### User
Represents a system user. It stores username, password, and permissions. It also supports login checking, saving, deleting, and login registration.

### BankClient
Represents a bank client. It stores account number, PIN code, and account balance. It supports deposit, withdraw, transfer, saving, deleting, and searching.

### Currency
Represents a currency and supports searching, updating rates, and currency conversion.

### FileStorage
Handles reading, writing, and appending data to text files.

### Permission
Contains permission constants and checks if a user has a specific permission.

### BankFxApp
Contains the JavaFX graphical user interface for login, dashboard, clients, transactions, users, logs, and currencies.

### Screens
Contains the console version screens and menus.

## 5. UML Class Diagram Text
```text
                 +----------------------+
                 |   abstract Person    |
                 +----------------------+
                 | - firstName          |
                 | - lastName           |
                 | - email              |
                 | - phone              |
                 +----------------------+
                 | + fullName()         |
                 | + getRoleDescription() abstract |
                 +----------^-----------+
                            |
              +-------------+-------------+
              |                           |
+-------------------------+   +---------------------------+
|          User           |   |        BankClient          |
+-------------------------+   +---------------------------+
| - userName              |   | - accountNumber           |
| - password              |   | - pinCode                 |
| - permissions           |   | - accountBalance          |
+-------------------------+   +---------------------------+
| + save()                |   | + save()                  |
| + delete()              |   | + delete()                |
| + hasPermission()       |   | + deposit()               |
| + registerLogin()       |   | + withdraw()              |
| + getRoleDescription()  |   | + transfer()              |
+-------------------------+   | + getRoleDescription()    |
                              +---------------------------+

BankFxApp uses User, BankClient, Currency, FileStorage, Permission, and Util.
Screens uses the same classes for the console version.
```

## 6. File Handling
The project uses readable text files in the `data` folder:

- `BClients.txt` stores bank clients.
- `BUsers.txt` stores system users.
- `Currencies.txt` stores currency data.
- `LoginRegister.txt` stores login records.
- `TransferLog.txt` stores transfer operations.

The project uses file reading, writing, and appending methods to make sure data is not lost after closing the program.

## 7. JavaFX Screenshots Section
Add screenshots of:

1. Login screen.
2. Main dashboard.
3. Clients screen.
4. Add/update client operation.
5. Deposit/withdraw/transfer screen.
6. Users screen.
7. Transfer log screen.
8. Currency exchange screen.

## 8. Video Presentation Link
Add the YouTube unlisted or Google Drive link here:

`PASTE_VIDEO_LINK_HERE`

## 9. GitHub Repository Link
Add the public GitHub repository link here:

`PASTE_GITHUB_LINK_HERE`

## 10. Challenges
One challenge was organizing the project into multiple classes instead of writing all logic in one menu class. Another challenge was making the same business classes work with both the console version and the JavaFX GUI. File handling also needed careful formatting to save and load the data correctly.

## 11. AI Usage Declaration
I used ChatGPT as a learning assistant to review the project structure, improve OOP concept coverage, add the abstract class requirement, prepare documentation, and improve the JavaFX version. I understand the code and can explain the project during the oral defense.
