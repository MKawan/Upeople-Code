package ciom.mk;
/**
 *
 * @author mk
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple, reusable, thread-safe class that provides the current time and date.
 */
public class Clock {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss  dd-MM-yyyy");

    public String getCurrentTimeAndDate() {
        return LocalDateTime.now().format(FORMATTER);
    }
}