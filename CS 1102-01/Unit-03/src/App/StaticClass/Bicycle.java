package App.StaticClass;
/**
 *
 * @author mk
 */
public class Bicycle {
    private static int numberOfBicycles = 0;  // Static variable shared by all instances
    private int id;  // Instance variable unique to each bicycle

    public Bicycle(int id) {
        this.id = id;
        numberOfBicycles++;  // Increments the shared counter
    }

    public static int getNumberOfBicycles() {  // Static method to access the class variable
        return numberOfBicycles;
    }

    public int getId() {  // Instance method accessing instance variable
        return id;
    }
}