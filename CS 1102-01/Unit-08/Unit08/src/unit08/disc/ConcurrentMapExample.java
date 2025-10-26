package unit08.disc;
/**
 *
 * @author mk
 */
import java.util.concurrent.*; // Required for concurrent collections and executors
/**
 * Demonstrates safe concurrent insertion into a ConcurrentHashMap.
 * Multiple threads insert key-value pairs without explicit synchronization.
 */
public class ConcurrentMapExample {
    public static void main(String[] args) throws InterruptedException {
        
        // Create a thread-safe hash map (allows concurrent reads/writes)
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        // Create a thread pool with 10 worker threads
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Launch 10 threads, each inserting 1000 unique entries
        for (int i = 0; i < 10; i++) {
            final int id = i; // Capture thread identifier
            
            // Submit a Runnable task (no return value needed)
            executor.execute(() -> {
                // Each thread inserts 1000 entries with unique keys
                for (int j = 0; j < 1000; j++) {
                    String key = "key" + (id * 1000 + j); // Unique key format: key0, key1, ..., key9999
                    map.put(key, j); // Safe concurrent put operation
                }
            });
        }
        
        // Initiate orderly shutdown (no new tasks accepted)
        executor.shutdown();
        
        // Wait for all tasks to complete (up to 10 seconds)
        if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("All threads completed successfully.");
        } else {
            System.out.println("Timeout: some tasks did not finish.");
        }
        
        // Print final size of the map
        System.out.println("Total entries in map: " + map.size());
        // Expected: 10 threads × 1000 entries = 10,000
    }
}