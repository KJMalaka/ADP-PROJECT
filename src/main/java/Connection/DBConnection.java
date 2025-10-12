package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database Connection Class
 */
public class DBConnection {

    // Database URL - embedded Derby
    private static final String DATABASE_URL = "jdbc:derby:EnrollmentDB;create=true";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    /**
     * Creates and returns a connection to the Derby database
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Uncomment if you get "No suitable driver" error
            // Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            Connection connection = DriverManager.getConnection(
                    DATABASE_URL,
                    USERNAME,
                    PASSWORD
            );

            // Check if database is set up, if not, create tables and insert data
            setupDatabaseIfNeeded(connection);

            System.out.println("✓ Database connection successful!");
            return connection;

        } catch (Exception e) {
            System.err.println("✗ Database connection error: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Database connection failed: " + e.getMessage());
        }
    }

    private static void setupDatabaseIfNeeded(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeQuery("SELECT 1 FROM Admins");
        } catch (SQLException e) {
            if ("42X05".equals(e.getSQLState())) { // Table does not exist
                setupDatabase(conn);
            } else {
                throw e;
            }
        }
    }

    private static void setupDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creating database tables...");

            // Create Admins table
            stmt.executeUpdate("CREATE TABLE Admins (" +
                    "adminId VARCHAR(50) PRIMARY KEY, " +
                    "adminName VARCHAR(100), " +
                    "pin VARCHAR(10))");
            System.out.println(" Admins table created");

            // Create Students table
            stmt.executeUpdate("CREATE TABLE Students (" +
                    "studentNumber VARCHAR(20) PRIMARY KEY, " +
                    "studentName VARCHAR(100), " +
                    "email VARCHAR(100), " +
                    "pin VARCHAR(10))");
            System.out.println(" Students table created");

            // Create Courses table
            stmt.executeUpdate("CREATE TABLE Courses (" +
                    "courseCode VARCHAR(20) PRIMARY KEY, " +
                    "courseName VARCHAR(100), " +
                    "credits INTEGER)");
            System.out.println(" Courses table created");

            // Create Enrollments table
            stmt.executeUpdate("CREATE TABLE Enrollments (" +
                    "studentNumber VARCHAR(20), " +
                    "courseCode VARCHAR(20), " +
                    "enrollmentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "PRIMARY KEY (studentNumber, courseCode), " +
                    "FOREIGN KEY (studentNumber) REFERENCES Students(studentNumber), " +
                    "FOREIGN KEY (courseCode) REFERENCES Courses(courseCode))");
            System.out.println(" Enrollments table created");

            System.out.println("\nInserting default data...");

            // Insert default admin
            stmt.executeUpdate("INSERT INTO Admins (adminId, adminName, pin) " +
                    "VALUES ('admin', 'Administrator', '0000')");
            System.out.println(" Default admin added (admin/0000)");

            // Insert default student
            stmt.executeUpdate("INSERT INTO Students (studentNumber, studentName, email, pin) " +
                    "VALUES ('12345', 'Test Student', 'test@student.com', '1111')");
            System.out.println("Default student added (12345/1111)");

            // Insert default courses
            stmt.executeUpdate("INSERT INTO Courses (courseCode, courseName, credits) " +
                    "VALUES ('ADP262S', 'APPLICATIONS DEVELOPMENT FUNDAMENTALS 2', 15)");
            stmt.executeUpdate("INSERT INTO Courses (courseCode, courseName, credits) " +
                    "VALUES ('ADP262P', 'APPLICATIONS DEVELOPMENT PRACTICE 2', 15)");
            stmt.executeUpdate("INSERT INTO Courses (courseCode, courseName, credits) " +
                    "VALUES ('CNF262S', 'COMMUNICATIONS NETWORKS FUNDAMENTALS 2', 15)");
            stmt.executeUpdate("INSERT INTO Courses (courseCode, courseName, credits) " +
                    "VALUES ('ICT262S', 'ICT ELECTIVES 2', 15)");
            stmt.executeUpdate("INSERT INTO Courses (courseCode, courseName, credits) " +
                    "VALUES ('IMF262S', 'INFORMATION MANAGEMENT 2', 15)");
            stmt.executeUpdate("INSERT INTO Courses (courseCode, courseName, credits) " +
                    "VALUES ('ISA262S', 'INFORMATION SYSTEMS ANALYSIS', 15)");
            System.out.println(" Default courses added (6 courses)");

            System.out.println("  Database setup completed successfully!        ");
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing database connection...\n");
        try (Connection conn = getConnection()) {
            System.out.println("\n Connection test successful!");
            System.out.println(" Database is ready to use!");
        } catch (SQLException e) {
            System.err.println("\n Connection test failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}