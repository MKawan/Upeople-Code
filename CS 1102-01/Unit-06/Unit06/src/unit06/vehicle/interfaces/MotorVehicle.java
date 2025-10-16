/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package unit06.vehicle.interfaces;

/**
 *
 * @author mk
 */
// Interface defining specific behavior for motorcycles.
public interface MotorVehicle {
    void setNumberOfWheels(int wheels);
    int getNumberOfWheels();

    void setMotorcycleType(String type);
    String getMotorcycleType();
}
