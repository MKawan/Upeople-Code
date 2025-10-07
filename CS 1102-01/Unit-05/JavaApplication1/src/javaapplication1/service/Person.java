/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1.service;

/**
 *
 * @author mk
 */
// Person.java
public class Person {
    // Private fields (data hiding)
    private String name;
    private int age;

    // Constructor to initialize the object
    public Person(String name, int age) {
        this.name = name;
        setAge(age); // Use setter for validation
    }

    // Public getter for name
    public String getName() {
        return name;
    }

    // Public setter for name (with basic validation)
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    // Public getter for age
    public int getAge() {
        return age;
    }

    // Public setter for age (with validation to ensure age is positive)
    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }

    // A method that uses the encapsulated data (behavior)
    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }

    // Another method demonstrating internal logic
    public boolean isAdult() {
        return age >= 18;
    }
}