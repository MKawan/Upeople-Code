package br.com.mk;

import java.util.ArrayList;

public class StockAnalyzer {

    /**
     * Calculates the average stock price from the given array of prices.
     * @param prices An array of integer stock prices.
     * @return The average price as a double.
     */
    public static double calculateAveragePrice(int[] prices) {
        double sum = 0;
        for (int price : prices) {
            sum += price;
        }
        return sum / prices.length;
    }

    /**
     * Finds the maximum stock price from the given array of prices.
     * @param prices An array of integer stock prices.
     * @return The maximum price.
     */
    public static int findMaximumPrice(int[] prices) {
        int max = prices[0];
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > max) {
                max = prices[i];
            }
        }
        return max;
    }

    /**
     * Counts the occurrences of a specific target price in the array.
     * @param prices An array of integer stock prices.
     * @param target The target price to count.
     * @return The number of times the target appears.
     */
    public static int countOccurrences(int[] prices, int target) {
        int count = 0;
        for (int price : prices) {
            if (price == target) {
                count++;
            }
        }
        return count;
    }

    /**
     * Computes the cumulative sum of stock prices from the given ArrayList.
     * @param prices An ArrayList of integer stock prices.
     * @return A new ArrayList containing cumulative sums.
     */
    public static ArrayList<Integer> computeCumulativeSum(ArrayList<Integer> prices) {
        ArrayList<Integer> cumulativeSums = new ArrayList<>();
        int sum = 0;
        for (int price : prices) {
            sum += price;
            cumulativeSums.add(sum);
        }
        return cumulativeSums;
    }

    public static void main(String[] args) {
        // Sample data: 10 days of stock prices
        int[] stockPricesArray = {100, 102, 99, 105, 103, 101, 98, 107, 110, 108};
        
        // Create ArrayList from the array for the cumulative sum method
        ArrayList<Integer> stockPricesList = new ArrayList<>();
        for (int price : stockPricesArray) {
            stockPricesList.add(price);
        }

        // Task 1: Calculate average
        double average = calculateAveragePrice(stockPricesArray);
        System.out.println("Average Stock Price: " + average);

        // Task 2: Find maximum
        int maxPrice = findMaximumPrice(stockPricesArray);
        System.out.println("Maximum Stock Price: " + maxPrice);

        // Task 3: Count occurrences of a target (e.g., 100)
        int target = 100;
        int occurrences = countOccurrences(stockPricesArray, target);
        System.out.println("Occurrences of " + target + ": " + occurrences);

        // Task 4: Compute cumulative sum
        ArrayList<Integer> cumulativeSums = computeCumulativeSum(stockPricesList);
        System.out.println("Cumulative Sums: " + cumulativeSums);
    }
}