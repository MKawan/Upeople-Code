/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1.service;

/**
 *
 * @author mk
 */
// BankAccount.java
public class BankAccount {
    // Private fields for security
    private String accountNumber;
    private double balance;
    private String ownerName;

    // Constructor
    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        setBalance(initialBalance); // Use setter for validation
    }

    // Getter for account number (read-only, no setter for security)
    public String getAccountNumber() {
        return accountNumber;
    }

    // Getter for owner name
    public String getOwnerName() {
        return ownerName;
    }

    // Setter for owner name (rarely changed, but with validation)
    public void setOwnerName(String ownerName) {
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            this.ownerName = ownerName;
        } else {
            throw new IllegalArgumentException("Owner name cannot be null or empty");
        }
    }

    // Getter for balance (read-only to prevent direct tampering)
    public double getBalance() {
        return balance;
    }

    // Private setter for balance (only used internally for validation)
    private void setBalance(double balance) {
        if (balance >= 0) {
            this.balance = balance;
        } else {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }

    // Encapsulated method for deposit
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount + ". New balance: $" + balance);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    // Encapsulated method for withdrawal (prevents overdraft)
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: $" + amount + ". New balance: $" + balance);
        } else if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
    }

    // Method to display account info (controlled access)
    public void displayAccountInfo() {
        System.out.println("Account: " + accountNumber + ", Owner: " + ownerName + ", Balance: $" + balance);
    }
}