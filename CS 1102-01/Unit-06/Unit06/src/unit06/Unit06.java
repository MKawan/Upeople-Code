package unit06;
/**
 *
 * @author mk
 */
 class Vehicle {
    public void startEngine() {
        System.out.println("Engine starting...");
    }
}

class Car extends Vehicle {
    @Override
    public void startEngine() {
        System.out.println("Car engine roaring with fuel injection.");
    }
}

class Motorcycle extends Vehicle {
    @Override
    public void startEngine() {
        System.out.println("Motorcycle engine kicking in with a rev.");
    }
}
public class Unit06 {
    public static void main(String[] args) {
        Vehicle[] vehicles = {new Car(), new Motorcycle()};
        for (Vehicle v : vehicles) {
            v.startEngine(); // Dynamic binding selects correct method
        }
    }
}