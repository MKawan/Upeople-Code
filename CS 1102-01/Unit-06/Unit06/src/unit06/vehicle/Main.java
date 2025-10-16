/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit06.vehicle;

/**
 *
 * @author mk
 */
import java.util.*;
import unit06.vehicle.interfaces.Vehicle;
import unit06.vehicle.models.Car;
import unit06.vehicle.models.Motorcycle;
import unit06.vehicle.models.Truck;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<Vehicle> vehicles = new ArrayList<>();

        try {
            System.out.println("=== Vehicle Information System ===");

            System.out.println("Enter number of vehicles to add:");
            int count = Integer.parseInt(input.nextLine());

            for (int i = 0; i < count; i++) {
                System.out.println("\nSelect Vehicle Type (Car / Motorcycle / Truck):");
                String type = input.nextLine().toLowerCase();

                System.out.print("Make: ");
                String make = input.nextLine();
                System.out.print("Model: ");
                String model = input.nextLine();
                System.out.print("Year: ");
                int year = Integer.parseInt(input.nextLine());

                switch (type) {
                    case "car":
                        System.out.print("Number of Doors: ");
                        int doors = Integer.parseInt(input.nextLine());
                        System.out.print("Fuel Type (Petrol/Diesel/Electric): ");
                        String fuel = input.nextLine();
                        vehicles.add(new Car(make, model, year, doors, fuel));
                        break;

                    case "motorcycle":
                        System.out.print("Number of Wheels: ");
                        int wheels = Integer.parseInt(input.nextLine());
                        System.out.print("Type (Sport/Cruiser/Off-road): ");
                        String motoType = input.nextLine();
                        vehicles.add(new Motorcycle(make, model, year, wheels, motoType));
                        break;

                    case "truck":
                        System.out.print("Cargo Capacity (tons): ");
                        double cargo = Double.parseDouble(input.nextLine());
                        System.out.print("Transmission (Manual/Automatic): ");
                        String trans = input.nextLine();
                        vehicles.add(new Truck(make, model, year, cargo, trans));
                        break;

                    default:
                        System.out.println("Invalid vehicle type. Skipping...");
                        break;
                }
            }

            System.out.println("\n=== Vehicle Details ===");
            for (Vehicle v : vehicles) {
                System.out.println(v.toString());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            input.close();
        }
    }
}
