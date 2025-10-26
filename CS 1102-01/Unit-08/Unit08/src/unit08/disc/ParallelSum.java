package unit08.disc;
/**
 *
 * @author mk
 */
import java.util.*;
import java.util.concurrent.*; // Required for ExecutorService, Future, etc.
/**
 * Demonstrates parallel processing of a large list using multiple threads.
 * Splits the list into segments and computes partial sums concurrently.
 */
public class ParallelSum {
    public static void main(String[] args) throws Exception {
        
        // Create a large list with 1,000,000 integers (0 to 999,999)
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            list.add(i);
        }
        
        // Define number of threads (can be tuned; here fixed to 4)
        int threads = 4;
        
        // Create a fixed thread pool with the specified number of threads
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        
        // List to hold Future objects that will return partial sums
        List<Future<Long>> futures = new ArrayList<>();
        
        // Calculate segment size for each thread
        int segment = list.size() / threads;
        
        // Submit tasks: each thread sums one segment of the list
        for (int i = 0; i < threads; i++) {
            final int start = i * segment; // Start index for this thread
            final int end = (i == threads - 1) ? list.size() : start + segment; // End index (last thread takes remainder)
            
            // Submit a Callable lambda that returns the partial sum
            futures.add(executor.submit(() -> {
                long sum = 0;
                // Iterate over the assigned segment and accumulate sum
                for (int j = start; j < end; j++) {
                    sum += list.get(j);
                }
                return sum; // Return partial result
            }));
        }
        
        // Aggregate all partial sums into the total
        long total = 0;
        for (Future<Long> future : futures) {
            total += future.get(); // Blocks until the result is available
        }
        
        // Gracefully shut down the executor to free resources
        executor.shutdown();
        
        // Print final result
        System.out.println("Total sum: " + total);
        // Expected: sum from 0 to 999,999 = n(n-1)/2 = 499,999,500,000
    }
}