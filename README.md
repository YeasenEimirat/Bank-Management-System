<div align="center">

# Bank Management System

## Java OOP Application with JavaFX and Console Interfaces

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-blue)
![Maven](https://img.shields.io/badge/Maven-Project-C71A36?logo=apachemaven)
![OOP](https://img.shields.io/badge/Design-Object--Oriented-success)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen)

A complete academic banking application that demonstrates Object-Oriented Programming concepts, text-file persistence, user permissions, banking transactions, activity logs, currency exchange, and a graphical JavaFX interface.

</div>

---

## Project Overview

The **Bank Management System** is a Java application that simulates the essential operations of a real-world bank. It allows authorized users to manage clients and system users, process banking transactions, review activity logs, and perform currency conversion operations.

The project provides two interfaces that share the same business classes and data files:

- **JavaFX GUI** for graphical interaction.
- **Console UI** as an additional command-line version.

> **Academic Notice:** This project was developed for learning and course assessment purposes. It is not intended for production banking use.

---

## Application Preview

<p align="center">
  <img src="https://github.com/user-attachments/assets/30e2c3e4-a8f2-4808-9a21-7ef06ede1c76"
       alt="Bank Management System Login Screen"
       width="850">
</p>

---

## Main Features

- Login system with input validation and a maximum of three attempts.
- Password encryption and decryption for stored users.
- User permissions using bitwise permission values.
- Client management: list, add, edit, delete, refresh, and search.
- User management: list, add, edit, delete, and permission control.
- Banking transactions: deposit, withdraw, and transfer.
- Transfer log saved in a readable text file.
- Login register saved in a readable text file.
- Currency management: search, update rates, and convert currencies.
- JavaFX dashboard with summary cards.
- File handling using `.txt` files in the `data` directory.
- Error handling and input validation.
- Shared business logic between the JavaFX and Console versions.

---

## Screenshots

### Login Screen

<p align="center">
  <img src="https://github.com/user-attachments/assets/30e2c3e4-a8f2-4808-9a21-7ef06ede1c76"
       alt="Login Screen"
       width="850">
</p>

### Dashboard

<p align="center">
  <img src="https://github.com/user-attachments/assets/c684bca8-4fe5-4089-9acf-e2acfa838c9b" 
       alt="Dashboard"
       width="900">
 

</p>

### Client Management

<p align="center">
  <img src="https://github.com/user-attachments/assets/40d98421-27b2-4d48-99f4-fd0442cfa8c2"
       alt="Client Management"
       width="900">
</p>

### User Management

<p align="center">
  <img src="https://github.com/user-attachments/assets/3a0cdaab-6bb8-48b0-bdad-07f6ab114663"
       alt="User Management"
       width="900">
</p>

### Deposit Transaction

<p align="center">
  <img src="https://github.com/user-attachments/assets/55020488-662e-41da-b103-865d5791e510"
       alt="Deposit Transaction"
       width="900">
</p>

### Currency Exchange

<p align="center">
  <img src="https://github.com/user-attachments/assets/8c0a218d-df7d-42b8-9e0c-3a2297676f5f"
       alt="Currency Exchange"
       width="900">
</p>

### Login Register

<p align="center">
  <img src="https://github.com/user-attachments/assets/a49a05b8-0c07-46cb-a81a-4ec974977c04"
       alt="Login Register"
       width="900">
</p>

---

## OOP Concepts Demonstrated

| Concept | Implementation |
|---|---|
| **Classes and Objects** | The project uses meaningful classes such as `User`, `BankClient`, `Currency`, `Screens`, and `BankFxApp`. |
| **Encapsulation** | Fields are private or protected and are accessed through methods, getters, and setters. |
| **Inheritance** | `User` and `BankClient` inherit shared personal information from `Person`. |
| **Abstraction** | `Person` is an abstract class containing the abstract method `getRoleDescription()`. |
| **Polymorphism** | `User` and `BankClient` provide different implementations of `getRoleDescription()`. |
| **Composition / Usage** | UI classes use business and helper classes such as `FileStorage`, `Permission`, `Util`, and `AppSession`. |
| **Enumeration** | `SaveResult` represents the possible results of save operations. |

---

## UML Documentation

The complete UML report includes the core class diagram and the UI/supporting classes diagram.

- [View or Download the Complete UML Report](https://github.com/user-attachments/files/29938351/Bank_Management_System_UML_Report_Ready.pdf)

---

## Main Classes

| Class | Responsibility |
|---|---|
| `Main` | Entry point for the Console version. |
| `MainFX` | Entry point for the JavaFX version. |
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

---

## Technologies Used

- Java 17+
- JavaFX 21.0.2
- Apache Maven
- Object-Oriented Programming
- Text-file persistence
- Git and GitHub

---

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
│   └── images/
│       └── dashboard.jpg
├── src/main/java/com/bankmanagement/
│   ├── fx/
│   │   └── BankFxApp.java
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

---

## Data Persistence

The application stores and loads data from the `data` directory. The information remains available after the program is closed.

| File | Stored Data |
|---|---|
| `BClients.txt` | Client and bank account records. |
| `BUsers.txt` | User credentials and permissions. |
| `Currencies.txt` | Currency names, codes, and exchange rates. |
| `LoginRegister.txt` | Login activity records. |
| `TransferLog.txt` | Money transfer records. |

> Do not delete or rename the `data` folder while running the project.

---

## Requirements

Before running the application, install:

- **JDK 17 or later**
- **Apache Maven**
- An IDE such as IntelliJ IDEA, Eclipse, or NetBeans is optional.

---

## How to Run the Project

### JavaFX Version with Maven

Open a terminal in the project root directory and run:

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

---

## Test Accounts

| Username | Password |
|---|---|
| `User1` | `9898` |
| `User2` | `1234` |
| `w` | `w` |

> These accounts are included for demonstration and academic testing only.

---
## Team Members

| Name | Role |
|---|---|
| **Yaseen Hani Abdul-Majeed Eimirat** | Team Leader / Tech Lead |
| **Abdullah Abu Amra** | Team Member |
| **Sohaib Abu Al-Roos** | Team Member |
| **Mohammad Al-Habbash** | Team Member |
> Add the student ID for each team member before the final course submission.

---

## AI Usage Declaration

ChatGPT was used as a learning assistant to review the project structure, improve OOP concept coverage, support debugging, prepare documentation, and refine the JavaFX version. The team understands the submitted code and is prepared to explain it during the oral defense.

---

## Video Presentation

Add the public or unlisted presentation link before submission:

```text
Presentation Link: ADD_VIDEO_LINK_HERE
```




---

<div align="center">

**Developed as an Object-Oriented Programming course project.**

</div>
