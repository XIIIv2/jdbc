package icu.xiii;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnector {

    private final String PROPERTIES_FILE = "db/jdbc.properties";
    private final Properties PROPERTIES = new Properties();

    private String DRIVER;
    private String DB_URL;
    private String DB_NAME;
    private String USERNAME;
    private String PASSWORD;

    private Connection connection = null;

    public DatabaseConnector() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            PROPERTIES.load(DatabaseConnector.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
            DRIVER = PROPERTIES.getProperty("driver");
            DB_URL = PROPERTIES.getProperty("url");
            DB_NAME = PROPERTIES.getProperty("dbName");
            USERNAME = PROPERTIES.getProperty("username");
            PASSWORD = PROPERTIES.getProperty("password");
        } catch (IOException e) {
            System.out.println("Failed to load properties file!\nError: " + e.getMessage());
        }
    }

    public void connect() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL + "/" + DB_NAME, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Driver Error: " + e.getMessage());
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void checkConnection() throws RuntimeException {
        if (connection == null) {
            throw new RuntimeException("No database connection! Call connect() method first.");
        }
    }

    public void initDatabase() {
        checkConnection();
        String query = """
                CREATE TABLE IF NOT EXISTS `employees` (
                  `id` INT NOT NULL AUTO_INCREMENT,
                  `name` VARCHAR(255) NULL,
                  `age` INT NOT NULL DEFAULT 0,
                  `position` VARCHAR(255) NOT NULL,
                  `salary` DECIMAL(16,2) NOT NULL DEFAULT 0,
                  PRIMARY KEY (`id`));
                """;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(query);
            try (ResultSet resultSet = stmt.executeQuery("SELECT COUNT(id) as totalRows FROM `employees`")) {
                resultSet.next();
                if (resultSet.getInt("totalRows") == 0) {
                    stmt.execute("""
                                INSERT INTO `employees` (name, age, position, salary)
                                VALUES ("Bob", 37, "Manager", 2000),
                                        ("Tom", 21, "Seller", 1000),
                                        ("Alice", 18, "Seller", 1000),
                                        ("Susan", 20, "Seller", 1000)
                                """);
                    System.out.println("Database initialized.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
