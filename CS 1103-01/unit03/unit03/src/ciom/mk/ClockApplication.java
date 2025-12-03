package ciom.mk;
/**
 *
 * @author mk
 */
public class ClockApplication {
    private static final Clock clock = new Clock();
    private static volatile boolean running = true;

    public static void main(String[] args) {
        System.out.println("=== Concurrent Digital Clock – Thread Priority Demo ===");
        System.out.println("Press Ctrl+C to stop\n");

        // Background thread – simulates other work in the application
        Thread backgroundThread = new Thread(() -> {
            while (running) {
                clock.getCurrentTimeAndDate();     // dummy work
                try { Thread.sleep(5); } catch (InterruptedException ignored) {}
            }
        }, "Background-Worker");

        // Display thread – responsible for printing the clock every second
        Thread displayThread = new Thread(() -> {
            while (running) {
                System.out.print("\rTime: " + clock.getCurrentTimeAndDate());
                try { Thread.sleep(1000); }          // exactly 1 second
                catch (InterruptedException e) { running = false; }
            }
        }, "Clock-Display");

        // PRIORITY SETTINGS – the core of the demonstration
        backgroundThread.setPriority(2);                    // low priority
        displayThread.setPriority(Thread.MAX_PRIORITY);     // highest priority (10)

        System.out.println("Threads started with different priorities:");
        System.out.println("   " + backgroundThread.getName() + " → priority " + backgroundThread.getPriority());
        System.out.println("   " + displayThread.getName() + " → priority " + displayThread.getPriority() + " (critical)\n");

        backgroundThread.start();
        displayThread.start();

        // Graceful shutdown when user presses Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running = false;
            System.out.println("\n\nClock stopped. Goodbye!");
        }));
    }
}