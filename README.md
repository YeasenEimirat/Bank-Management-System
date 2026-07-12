<div align="center">

# Bank Management System

### Java OOP application with JavaFX and Console interfaces

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-blue)
![Maven](https://img.shields.io/badge/Maven-Project-C71A36?logo=apachemaven)
![OOP](https://img.shields.io/badge/Design-Object--Oriented-success)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen)

A complete academic banking application that demonstrates core Object-Oriented Programming concepts, persistent text-file storage, user permissions, banking transactions, and a graphical JavaFX interface.

</div>

---

## Overview

The **Bank Management System** is a Java application that simulates the essential operations of a real-world bank. It provides tools for managing clients and users, processing deposits, withdrawals, and transfers, viewing activity logs, and converting currencies.

The project contains two interfaces that use the same business classes and data files:

- **JavaFX GUI** for graphical interaction.
- **Console UI** as an additional command-line version.

> **Academic notice:** This project was developed for learning and course assessment. It is not intended for use as a production banking system.

## Application Preview

<p align="center">
  <img src="docs/images/dashboard.jpg" alt="Bank Management System dashboard" width="900">
</p>

## Main Features

- Secure login flow with input validation and a maximum of three attempts.
- User management with role-based permissions using bitwise values.
- Client management: list, add, edit, delete, refresh, and search.
- Banking transactions: deposit, withdraw, and transfer funds.
- Persistent transfer and login records.
- Currency management: search, rate update, and currency conversion.
- JavaFX dashboard with summary cards for clients, users, currencies, and login records.
- File handling through readable `.txt` files.
- Error handling and validation for user input and financial operations.
- Shared business logic between the JavaFX and Console versions.

## Screenshots

### Login and Dashboard

| Login Screen | Dashboard |
|---|---|
| <img src="docs/images/login-screen.jpg" alt="Login screen" width="100%"> | <img src="docs/images/dashboard.jpg" alt="Dashboard" width="100%"> |

### Clients and Users

| Client Management | User Management |
|---|---|
| <img src="docs/images/clients-management.jpg" alt="Client management" width="100%"> | <img src="docs/images/users-management.jpg" alt="User management" width="100%"> |

### Transactions and Currency Exchange

| Deposit Transaction | Currency Exchange |
|---|---|
| <img src="docs/images/deposit-transaction.jpg" alt="Deposit transaction" width="100%"> | <img src="docs/images/currency-exchange.jpg" alt="Currency exchange" width="100%"> |

### Login Register

<p align="center">
  <img src="docs/images/login-register.jpg" alt="Login register" width="900">
</p>

## OOP Concepts Demonstrated

| Concept | Implementation |
|---|---|
| **Classes and Objects** | The system uses meaningful classes such as `User`, `BankClient`, `Currency`, `Screens`, and `BankFxApp`. |
| **Encapsulation** | Data fields are private or protected and accessed through methods, getters, and setters. |
| **Inheritance** | `User` and `BankClient` inherit shared personal data and behavior from `Person`. |
| **Abstraction** | `Person` is an abstract class containing the abstract method `getRoleDescription()`. |
| **Polymorphism** | `User` and `BankClient` provide different implementations of `getRoleDescription()`. |
| **Composition / Usage** | UI classes use business and helper classes such as `FileStorage`, `Permission`, `Util`, and `AppSession`. |
| **Enumeration** | `SaveResult` represents the possible results of save operations. |

## UML Diagrams

### Core Class Diagram

<p align="center">
  <img src="docs/uml/core-class-diagram.png" alt="Core UML class diagram" width="1000">
</p>

### UI and Supporting Classes

<p align="center">
  <img src="docs/uml/ui-supporting-classes.png" alt="UI and supporting classes UML diagram" width="850">
</p>

A printable two-page UML report is also available here:

- [`Bank_Management_System_UML_Report.pdf`](docs/uml/Bank_Management_System_UML_Report.pdf)

## Main Classes

| Class | Responsibility |
|---|---|
| `Main` | Entry point for the Console version. |
| `MainFX` | Additional entry point for the JavaFX version. |
| `BankFxApp` | Builds and controls the JavaFX interface. |
| `Screens` | Handles Console menus and user interaction. |
| `Person` | Abstract base class for shared personal information. |
| `User` | Represents system users, credentials, and permissions. |
| `BankClient` | Represents bank clients, accounts, balances, and transactions. |
| `Currency` | Handles currency lookup, rate updates, and conversion. |
| `FileStorage` | Reads, writes, and appends text-file data. |
| `Permission` | Stores and checks permission constants. |
| `AppSession` | Stores the currently authenticated user. |
| `Input` | Provides validated Console input. |
| `ConsoleUI` | Provides Console formatting helpers. |
| `Util` | Provides encryption, date, and formatting utilities. |
| `SaveResult` | Represents the result of save operations. |

## Technologies

- Java 17+
- JavaFX 21.0.2
- Apache Maven
- Object-Oriented Programming
- Text-file persistence
- Git and GitHub

## Project Structure

```text
BankManagementSystemJava/
├── data/
│   ├── BClients.txt
│   ├── BUsers.txt
│   ├── Currencies.txt
│   ├── LoginRegister.txt
│   └── TransferLog.txt
├── docs/
│   ├── images/
│   └── uml/
├── src/main/java/com/bankmanagement/
│   ├── fx/BankFxApp.java
│   ├── AppSession.java
│   ├── BankClient.java
│   ├── ConsoleUI.java
│   ├── Constants.java
│   ├── Currency.java
│   ├── FileStorage.java
│   ├── Input.java
│   ├── Main.java
│   ├── MainFX.java
│   ├── Permission.java
│   ├── Person.java
│   ├── SaveResult.java
│   ├── Screens.java
│   ├── User.java
│   └── Util.java
├── pom.xml
├── run-javafx.bat
├── run-javafx.sh
├── run.bat
├── run.sh
└── README.md
```

## Data Persistence

The application stores and loads its data from the `data` directory. Information remains available after the program is closed.

| File | Stored Data |
|---|---|
| `BClients.txt` | Client and account records. |
| `BUsers.txt` | User credentials and permissions. |
| `Currencies.txt` | Currency names, codes, and exchange rates. |
| `LoginRegister.txt` | Login activity records. |
| `TransferLog.txt` | Money transfer records. |

Do not delete or rename the `data` folder while running the project.

## Requirements

Before running the application, install:

- **JDK 17 or later**
- **Apache Maven**
- An IDE such as IntelliJ IDEA, Eclipse, or NetBeans (optional)

## Running the Project

### JavaFX Version with Maven

From the project root directory, run:

```bash
mvn clean javafx:run
```

### JavaFX Version with Scripts

On Windows:

```bat
run-javafx.bat
```

On Linux or macOS:

```bash
chmod +x run-javafx.sh
./run-javafx.sh
```

### Console Version

On Windows:

```bat
run.bat
```

On Linux or macOS:

```bash
chmod +x run.sh
./run.sh
```

## Test Accounts

| Username | Password |
|---|---|
| `User1` | `9898` |
| `User2` | `1234` |
| `w` | `w` |

These accounts are included for demonstration and academic testing only.

## Team Members

| Name | Role |
|---|---|
| **Yaseen Eimirat** | Tech Lead |
| **Abda** | Team Member |

> Add the student ID for each team member before the final course submission.

## AI Usage Declaration

ChatGPT was used as a learning assistant to review the project structure, improve OOP concept coverage, support debugging, prepare documentation, and refine the JavaFX version. The team understands the submitted code and is prepared to explain it during the oral defense.

## Video Presentation

Add the public or unlisted presentation link here before submission:

```text
Presentation Link: ADD_VIDEO_LINK_HERE
```

## Course Submission Notes

Before submitting the repository, confirm that:

- The repository is public.
- Team member names and student IDs are complete.
- The project report is included in PDF or Word format.
- UML diagrams and program screenshots are included.
- The video presentation link is added.
- Every team member can explain their contribution and the main code components.

---

<div align="center">

**Developed as an Object-Oriented Programming course project.**

</div>
