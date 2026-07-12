package com.bankmanagement;

/**
 * Abstract base class for all people in the system.
 *
 * OOP concepts used here:
 * - Abstraction: this class cannot be created directly.
 * - Inheritance: User and BankClient inherit common personal data from Person.
 * - Polymorphism: each child class gives a different implementation of getRoleDescription().
 */
public abstract class Person {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;

    public Person(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    /**
     * Abstract method: every subclass must describe its role in the system.
     */
    public abstract String getRoleDescription();

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
