/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit06.vehicle.models;

import unit06.vehicle.interfaces.MotorVehicle;
import unit06.vehicle.interfaces.Vehicle;

/**
 *
 * @author mk
 */
public class Motorcycle implements Vehicle, MotorVehicle {
    private String make;
    private String model;
    private int year;
    private int numberOfWheels;
    private String type;

    public Motorcycle(String make, String model, int year, int numberOfWheels, String type) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.numberOfWheels = numberOfWheels;
        this.type = type;
    }

    // Implementing Vehicle methods
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    // Implementing MotorVehicle methods
    public void setNumberOfWheels(int wheels) { this.numberOfWheels = wheels; }
    public int getNumberOfWheels() { return numberOfWheels; }

    public void setMotorcycleType(String type) { this.type = type; }
    public String getMotorcycleType() { return type; }

    @Override
    public String toString() {
        return "Motorcycle [Make=" + make + ", Model=" + model + ", Year=" + year +
               ", Wheels=" + numberOfWheels + ", Type=" + type + "]";
    }
}

