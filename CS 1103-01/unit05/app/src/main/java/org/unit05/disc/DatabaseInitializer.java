package org.unit05.disc;

/**
 *
 * @author mk
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL_WITHOUT_DB = "jdbc:mariadb://192.168.77.32:3306/?createDatabaseIfNotExist=true";
    private static final String DB_URL_WITH_DB = "jdbc:mariadb://192.168.77.32:3306/test_unit05";
    private static final String USER = "root";
    private static final String PASSWORD = "test123456";

    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS test_unit05";
    private static final String USE_DATABASE = "USE test_unit05";

    private static final String CREATE_TABLE_EMPLOYEES = """
        CREATE TABLE IF NOT EXISTS employees (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            position VARCHAR(255),
            salary DOUBLE
        )""";

    /**
     * Inicializa o banco de dados e a tabela necessária.
     * Deve ser chamado uma vez no início do programa (ex: no main).
     */
    public static void initialize() {
        try {
            // 1. Conecta sem especificar o DB para criar o banco se necessário
            try (Connection conn = java.sql.DriverManager.getConnection(DB_URL_WITHOUT_DB, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate(CREATE_DATABASE);
                stmt.executeUpdate(USE_DATABASE);
                System.out.println("Banco de dados 'test_unit05' garantido (criado se não existia).");
            }

            // 2. Conecta ao banco específico e cria a tabela se não existir
            try (Connection conn = java.sql.DriverManager.getConnection(DB_URL_WITH_DB, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate(CREATE_TABLE_EMPLOYEES);
                System.out.println("Tabela 'employees' criada ou já existe.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se uma tabela existe (opcional, para uso futuro)
     */
    public static boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }
}