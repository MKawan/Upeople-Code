package unit.pkg01;
/**
 * @author mk
 */
// Class to represent a student with name and grades
class Aluno {
    String nome;
    double nota1, nota2, nota3;
    // Constructor
    public Aluno(String nome, double nota1, double nota2, double nota3) {
        this.nome = nome;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
    }
    // Method to display student information
    @Override
    public String toString() {
        return "Student: " + nome + ", Grades: " + nota1 + ", " + nota2 + ", " + nota3;
    }
}
public class ForEach {
    public static void main(String[] args) {
        // Initial array of names provided
        String[] nomes = {"Mateus", "Gustavo"};

        // Creating the array of Aluno objects
        Aluno[] alunos = new Aluno[nomes.length];
        // Filling the array with Aluno objects
        // Assigning fictional grades for demonstration
        alunos[0] = new Aluno(nomes[0], 7.5, 8.0, 6.5); // Mateus' grades
        alunos[1] = new Aluno(nomes[1], 9.0, 8.5, 7.0); // Gustavo's grades
        // Displaying student data using foreach
        System.out.println("List of Students and Grades:");
        for (Aluno aluno : alunos) {
            System.out.println(aluno);
        }
    }
}