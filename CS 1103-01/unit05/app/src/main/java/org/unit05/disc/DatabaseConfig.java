package org.unit05.disc;

/**
 *
 * @author mk
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mariadb://192.168.77.32:3306/test_unit05";
    private static final String USER = "root";
    private static final String PASSWORD = "test123456"; // Change in production!

    /**
     * Establishes and returns a new database connection.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}