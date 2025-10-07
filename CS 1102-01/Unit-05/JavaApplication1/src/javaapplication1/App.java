/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication1;

import javaapplication1.service.BankAccount;
import javaapplication1.service.Person;

/**
 *
 * @author mk
 */
public class App {

    public static void main(String[] args) {
        // Testing Person class
        System.out.println("=== Testing Person Class ===");
        Person person = new Person("Alice", 25);
        person.displayInfo(); // Output: Name: Alice, Age: 25
        
        person.setAge(30);
        person.displayInfo(); // Output: Name: Alice, Age: 30
        
        // This would throw an exception: person.setAge(-5);
        System.out.println("Is Adult? " + person.isAdult()); // Output: true

        // Testing BankAccount class
        System.out.println("\n=== Testing BankAccount Class ===");
        BankAccount account = new BankAccount("12345", "Bob", 1000.0);
        account.displayAccountInfo(); // Output: Account: 12345, Owner: Bob, Balance: $1000.0
        
        account.deposit(500.0); // Output: Deposited: $500.0. New balance: $1500.0
        account.withdraw(200.0); // Output: Withdrew: $200.0. New balance: $1300.0
        
        // This would throw an exception: account.withdraw(2000.0); (insufficient funds)
        // Direct access is impossible: account.balance = -100; (compilation error)
    }
}