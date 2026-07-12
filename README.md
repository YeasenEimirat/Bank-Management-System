# Bank Management System - Java OOP + JavaFX

## Project Title
Bank Management System

## Project Description
This project is a Java Object-Oriented Programming application that simulates a real-world bank management system. The system supports client management, banking transactions, user management, permissions, login records, transfer logs, and a currency exchange module. Data is saved and loaded from readable `.txt` files, so the information remains available after closing the program.

The project contains two user interfaces:
- **JavaFX GUI** for graphical interaction and bonus requirements.
- **Console UI** as a backup version using the same business classes and data files.

## Main Features
- Login system with validation and 3 allowed attempts.
- Password encryption/decryption for saved users.
- User permissions using bitwise permission values.
- Client management: list, add, update, delete, and search clients.
- Banking transactions: deposit, withdraw, transfer, and total balances.
- Transfer log saved in a text file.
- Login register saved in a text file.
- Currency exchange: list currencies, find by code/country, update rate, and convert currencies.
- JavaFX graphical screens for login, dashboard, clients, transactions, users, logs, and currencies.
- Input validation and error handling.
- File handling using text files in the `data` folder.

## OOP Concepts Used
- **Classes and Objects:** The project uses several meaningful classes such as `User`, `BankClient`, `Currency`, `Screens`, `BankFxApp`, `FileStorage`, `Input`, and `Permission`.
- **Encapsulation:** Fields are private/protected and accessed through getters and setters.
- **Inheritance:** `User` and `BankClient` inherit shared personal information from `Person`.
- **Abstraction:** `Person` is an abstract class and contains the abstract method `getRoleDescription()`.
- **Polymorphism:** `User` and `BankClient` override `getRoleDescription()` differently.
- **Composition/Usage Relationships:** `Screens` and `BankFxApp` use `User`, `BankClient`, `Currency`, `FileStorage`, `Permission`, and helper classes to run the system.
- **Enum:** `SaveResult` is used to represent save operation results.

## Main Classes
- `Main`: Entry point for the console version.
- `MainFX`: Entry point for the JavaFX version.
- `BankFxApp`: Main JavaFX GUI class.
- `Screens`: Console menus and user interaction flow.
- `Person`: Abstract base class for shared person information.
- `User`: Represents a system user with username, password, and permissions.
- `BankClient`: Represents a bank client with account number, PIN code, and balance.
- `Currency`: Represents currency information and conversion operations.
- `FileStorage`: Reads, writes, and appends data to text files.
- `Input`: Handles console input validation.
- `Permission`: Stores permission constants and permission checking logic.
- `ConsoleUI`: Contains simple console display helpers.
- `Util`: Contains helper methods for encryption, time, and formatting.

## UML Class Diagram Text
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

+-------------+          +---------------+
|  BankFxApp  | -------> | User/Client/  |
| JavaFX GUI  |          | Currency      |
+-------------+          +---------------+
       |
       +---- uses ----> FileStorage, Permission, Util

+-------------+
|   Screens   |  Console UI using the same business classes
+-------------+
```

## How to Run the Project

### Run JavaFX GUI using Maven
```bash
mvn clean javafx:run
```

### Run JavaFX GUI using scripts
Windows:
```bat
run-javafx.bat
```

Linux/Mac:
```bash
./run-javafx.sh
```

### Run Console Version
Windows:
```bat
run.bat
```

Linux/Mac:
```bash
./run.sh
```

## Project Type
JavaFX GUI application with an additional console version.

## File Handling
The project saves and loads data using text files in the `data` folder:
- `BClients.txt`
- `BUsers.txt`
- `Currencies.txt`
- `LoginRegister.txt`
- `TransferLog.txt`

## Test Accounts
- Username: `User1` / Password: `9898`
- Username: `User2` / Password: `1234`
- Username: `w` / Password: `w`

## AI Usage Declaration
I used ChatGPT as a learning assistant to review the project structure, improve OOP concept coverage, add the abstract class requirement, prepare documentation, and improve the JavaFX version. I understand the code and can explain the project during the oral defense.

## Student Names and IDs
Add group member names and IDs here before submission.
