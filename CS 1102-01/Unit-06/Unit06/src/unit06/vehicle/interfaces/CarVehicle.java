/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package unit06.vehicle.interfaces;

/**
 *
 * @author mk
 */
// Interface defining specific behavior for cars.
public interface CarVehicle {
    void setNumberOfDoors(int doors);
    int getNumberOfDoors();

    void setFuelType(String fuelType);
    String getFuelType();
}
