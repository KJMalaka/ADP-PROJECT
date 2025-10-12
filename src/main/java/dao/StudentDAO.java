package dao;

import Connection.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student table
 * Handles all CRUD operations for students
 */
public class StudentDAO {

    /**
     * CREATE - Add a new student to the database
     * @param studentNumber
     * @param studentName
     * @param email
     * @param pin Default pin is "1111"
     * @return true if successful, false otherwise
     */
    public boolean addStudent(String studentNumber, String studentName, String email, String pin) {
        String sql = "INSERT INTO Students (studentNumber, studentName, email, pin) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            pstmt.setString(2, studentName);
            pstmt.setString(3, email);
            pstmt.setString(4, pin);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Student added: " + studentNumber);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error adding student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * READ - Get all students from the database
     * @return List of String arrays [studentNumber, studentName, email]
     */
    public List<String[]> getAllStudents() {
        List<String[]> students = new ArrayList<>();
        String sql = "SELECT studentNumber, studentName, email FROM Students ORDER BY studentName";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] student = new String[3];
                student[0] = rs.getString("studentNumber");
                student[1] = rs.getString("studentName");
                student[2] = rs.getString("email");
                students.add(student);
            }
            System.out.println("✓ Retrieved " + students.size() + " students");
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * READ - Get a specific student by number
     * @param studentNumber
     * @return String array [studentNumber, studentName, email] or null if not found
     */
    public String[] getStudentByNumber(String studentNumber) {
        String sql = "SELECT studentNumber, studentName, email FROM Students WHERE studentNumber = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String[] student = new String[3];
                student[0] = rs.getString("studentNumber");
                student[1] = rs.getString("studentName");
                student[2] = rs.getString("email");
                return student;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting student: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * UPDATE - Update an existing student
     * @param studentNumber
     * @param studentName
     * @param email
     * @return true if successful, false otherwise
     */
    public boolean updateStudent(String studentNumber, String studentName, String email) {
        String sql = "UPDATE Students SET studentName = ?, email = ? WHERE studentNumber = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentName);
            pstmt.setString(2, email);
            pstmt.setString(3, studentNumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Student updated: " + studentNumber);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error updating student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * DELETE - Delete a student
     * @param studentNumber
     * @return true if successful, false otherwise
     */
    public boolean deleteStudent(String studentNumber) {
        String sql = "DELETE FROM Students WHERE studentNumber = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Student deleted: " + studentNumber);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error deleting student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Authenticate student login
     * @param studentNumber
     * @param pin
     * @return true if credentials are valid, false otherwise
     */
    public boolean authenticateStudent(String studentNumber, String pin) {
        String sql = "SELECT COUNT(*) FROM Students WHERE studentNumber = ? AND pin = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error authenticating student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if a student exists
     * @param studentNumber
     * @return true if exists, false otherwise
     */
    public boolean studentExists(String studentNumber) {
        String sql = "SELECT COUNT(*) FROM Students WHERE studentNumber = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error checking student existence: " + e.getMessage());
        }
        return false;
    }

    // ========================= ENROLLMENT METHODS =========================

    /**
     * Check if student is enrolled in a course
     * @param studentNumber
     * @param courseCode
     * @return true if enrolled, false otherwise
     */
    public boolean isEnrolled(String studentNumber, String courseCode) {
        String sql = "SELECT COUNT(*) FROM Enrollments WHERE studentNumber = ? AND courseCode = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            pstmt.setString(2, courseCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Enroll student in a course
     * @param studentNumber
     * @param courseCode
     * @return true if successful, false otherwise
     */
    public boolean enrollStudent(String studentNumber, String courseCode) {
        String sql = "INSERT INTO Enrollments (studentNumber, courseCode) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            pstmt.setString(2, courseCode);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            // If already enrolled, ignore duplicate error
            if ("23505".equals(e.getSQLState())) {
                return false;
            }
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all courses a student is enrolled in
     * @param studentNumber
     * @return List of String arrays [courseCode, courseName, credits, enrollmentDate]
     */
    public List<String[]> getCoursesByStudent(String studentNumber) {
        List<String[]> courses = new ArrayList<>();
        String sql = "SELECT c.courseCode, c.courseName, c.credits, e.enrollmentDate " +
                     "FROM Enrollments e " +
                     "JOIN Courses c ON e.courseCode = c.courseCode " +
                     "WHERE e.studentNumber = ? " +
                     "ORDER BY e.enrollmentDate DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] course = new String[4];
                course[0] = rs.getString("courseCode");
                course[1] = rs.getString("courseName");
                course[2] = String.valueOf(rs.getInt("credits"));
                course[3] = rs.getString("enrollmentDate");
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}