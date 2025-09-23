package App;

import App.StaticClass.Bicycle;

public class App {
    public static void main(String[] args) {
        // Displays the initial number of bicycles (static member)
        System.out.println("Número inicial de bicicletas: " + Bicycle.getNumberOfBicycles());

        // Creating Bicycle instances
        Bicycle bike1 = new Bicycle(101);
        Bicycle bike2 = new Bicycle(102);
        Bicycle bike3 = new Bicycle(103);

        // Displays the ID of each bicycle (instance member)
        System.out.println("ID da bike1: " + bike1.getId());
        System.out.println("ID da bike2: " + bike2.getId());
        System.out.println("ID da bike3: " + bike3.getId());

        // Displays the total number of bicycles after creation (static member)
        System.out.println("Número total de bicicletas criadas: " + Bicycle.getNumberOfBicycles());

        // Demonstrates that the static member is shared
        System.out.println("Criando mais uma bicicleta...");
        Bicycle bike4 = new Bicycle(104);
        System.out.println("Novo total de bicicletas: " + Bicycle.getNumberOfBicycles());
        
        // Demonstrates access to instance members in a practical context
        System.out.println("Verificando o ID da última bicicleta criada: " + bike4.getId());
    }
}