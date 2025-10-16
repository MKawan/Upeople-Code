/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit06.vehicle.models;

import unit06.vehicle.interfaces.TruckVehicle;
import unit06.vehicle.interfaces.Vehicle;

/**
 *
 * @author mk
 */
public class Truck implements Vehicle, TruckVehicle {
    private String make;
    private String model;
    private int year;
    private double cargoCapacity;
    private String transmission;

    public Truck(String make, String model, int year, double cargoCapacity, String transmission) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.cargoCapacity = cargoCapacity;
        this.transmission = transmission;
    }

    // Implementing Vehicle methods
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    // Implementing TruckVehicle methods
    public void setCargoCapacity(double tons) { this.cargoCapacity = tons; }
    public double getCargoCapacity() { return cargoCapacity; }

    public void setTransmissionType(String transmission) { this.transmission = transmission; }
    public String getTransmissionType() { return transmission; }

    @Override
    public String toString() {
        return "Truck [Make=" + make + ", Model=" + model + ", Year=" + year +
               ", Cargo=" + cargoCapacity + " tons, Transmission=" + transmission + "]";
    }
}
