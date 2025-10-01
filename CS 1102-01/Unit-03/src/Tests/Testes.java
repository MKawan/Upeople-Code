/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tests;

/**
 *
 * @author mk
 */
public class Testes {

    public static void main(String[] args) {
        int i = 1;

        while (i <= 5) {

            if (i % 2 == 0) {
                continue;
            }

            System.out.print(i + " ");

            i++;

        }

    }

}
