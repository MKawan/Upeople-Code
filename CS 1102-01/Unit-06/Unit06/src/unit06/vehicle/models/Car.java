/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit06.vehicle.models;

import unit06.vehicle.interfaces.CarVehicle;
import unit06.vehicle.interfaces.Vehicle;

/**
 *
 * @author mk
 */
public class Car implements Vehicle, CarVehicle {
    private String make;
    private String model;
    private int year;
    private int numberOfDoors;
    private String fuelType;

    public Car(String make, String model, int year, int numberOfDoors, String fuelType) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.numberOfDoors = numberOfDoors;
        this.fuelType = fuelType;
    }

    // Implementing Vehicle methods
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    // Implementing CarVehicle methods
    public void setNumberOfDoors(int doors) { this.numberOfDoors = doors; }
    public int getNumberOfDoors() { return numberOfDoors; }

    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public String getFuelType() { return fuelType; }

    @Override
    public String toString() {
        return "Car [Make=" + make + ", Model=" + model + ", Year=" + year +
               ", Doors=" + numberOfDoors + ", Fuel=" + fuelType + "]";
    }
}
