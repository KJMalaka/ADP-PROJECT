package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Class
 *
 * @author sophiamalapile
 */
public class DBConnection {

    // Database URL - change this based on your setup
    private static final String DATABASE_URL = "jdbc:derby://localhost:1527/Enrollment";
    private static final String USERNAME = "Enrol";
    private static final String PASSWORD = "12345";

    /**
     * Creates and returns a connection to the Derby database
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load Derby JDBC driver
            //Class.forName("org.apache.derby.jdbc.ClientDriver");

            // Create connection
            Connection connection = DriverManager.getConnection(
                    DATABASE_URL,
                    USERNAME,
                    PASSWORD
            );

            System.out.println("Database connection successful!");
            return connection;
            
        } catch (Exception e) {
            System.err.println("Derby JDBC Driver not found!");
            e.printStackTrace();
            throw new SQLException("Driver not found: " + e.getMessage());
        } 

        // } catch (ClassNotFoundException e) {
        //   System.err.println("Derby JDBC Driver not found!");
        // e.printStackTrace();
        //     throw new SQLException("Driver not found: " + e.getMessage());
        // } catch (SQLException e) {
        //    System.err.println("Connection failed: " + e.getMessage());
        //    throw e;
        //  }
    }

    /**
     * Test the database connection
     */
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("✓ Connection test successful!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("✗ Connection test failed!");
            e.printStackTrace();
        }
    }
}
