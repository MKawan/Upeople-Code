package br.com.mk;

public class GradeCalculator{

	public static void main(String[] args) {
        double[] grades = new double[5]; // Fixed size for 5 students
        // Initialize grades
        grades[0] = 85.5; grades[1] = 90.0; grades[2] = 78.5; grades[3] = 92.0; grades[4] = 88.0;
        // Calculate average
        double sum = 0;
        for (int i = 0; i < grades.length; i++) {
            sum += grades[i];
        }
        double average = sum / grades.length;
        System.out.println("Class average: " + average);
    }

}
