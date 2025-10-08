package dao;

import Connection.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Enrollment table
 * Handles all CRUD operations for enrollments
 */
public class EnrollmentDAO {
    
    /**
     * CREATE - Enroll a student in a course
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
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Enrollment created: Student " + studentNumber + " enrolled in " + courseCode);
                return true;
            }
            
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.err.println("✗ Student already enrolled in this course");
            } else {
                System.err.println("✗ Error enrolling student: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * READ - Get all courses a student is enrolled in
     * @param studentNumber
     * @return List of String arrays [courseCode, courseName, credits, enrollmentDate]
     */
    public List<String[]> getCoursesByStudent(String studentNumber) {
        List<String[]> courses = new ArrayList<>();
        String sql = "SELECT c.courseCode, c.courseName, c.credits, e.enrollmentDate " +
                    "FROM Courses c " +
                    "JOIN Enrollments e ON c.courseCode = e.courseCode " +
                    "WHERE e.studentNumber = ? " +
                    "ORDER BY c.courseCode";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] course = new String[4];
                course[0] = rs.getString("courseCode");
                course[1] = rs.getString("courseName");
                course[2] = String.valueOf(rs.getInt("credits"));
                course[3] = rs.getTimestamp("enrollmentDate").toString();
                courses.add(course);
            }
            
            System.out.println("✓ Retrieved " + courses.size() + " courses for student " + studentNumber);
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting student courses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }
    
    /**
     * READ - Get all students enrolled in a course
     * @param courseCode
     * @return List of String arrays [studentNumber, studentName, email, enrollmentDate]
     */
    public List<String[]> getStudentsByCourse(String courseCode) {
        List<String[]> students = new ArrayList<>();
        String sql = "SELECT s.studentNumber, s.studentName, s.email, e.enrollmentDate " +
                    "FROM Students s " +
                    "JOIN Enrollments e ON s.studentNumber = e.studentNumber " +
                    "WHERE e.courseCode = ? " +
                    "ORDER BY s.studentName";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] student = new String[4];
                student[0] = rs.getString("studentNumber");
                student[1] = rs.getString("studentName");
                student[2] = rs.getString("email");
                student[3] = rs.getTimestamp("enrollmentDate").toString();
                students.add(student);
            }
            
            System.out.println("✓ Retrieved " + students.size() + " students for course " + courseCode);
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting course students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }
    
    /**
     * READ - Get all enrollments
     * @return List of String arrays [studentNumber, courseCode, enrollmentDate]
     */
    public List<String[]> getAllEnrollments() {
        List<String[]> enrollments = new ArrayList<>();
        String sql = "SELECT studentNumber, courseCode, enrollmentDate FROM Enrollments ORDER BY enrollmentDate DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String[] enrollment = new String[3];
                enrollment[0] = rs.getString("studentNumber");
                enrollment[1] = rs.getString("courseCode");
                enrollment[2] = rs.getTimestamp("enrollmentDate").toString();
                enrollments.add(enrollment);
            }
            
            System.out.println("✓ Retrieved " + enrollments.size() + " total enrollments");
            
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving enrollments: " + e.getMessage());
            e.printStackTrace();
        }
        return enrollments;
    }
    
    /**
     * DELETE - Remove a student's enrollment from a course
     * @param studentNumber
     * @param courseCode
     * @return true if successful, false otherwise
     */
    public boolean unenrollStudent(String studentNumber, String courseCode) {
        String sql = "DELETE FROM Enrollments WHERE studentNumber = ? AND courseCode = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentNumber);
            pstmt.setString(2, courseCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Enrollment deleted: Student " + studentNumber + " unenrolled from " + courseCode);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error unenrolling student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if a student is already enrolled in a course
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
            System.err.println("✗ Error checking enrollment: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get total credits for a student
     * @param studentNumber
     * @return total credits
     */
    public int getTotalCredits(String studentNumber) {
        String sql = "SELECT SUM(c.credits) as totalCredits " +
                    "FROM Courses c " +
                    "JOIN Enrollments e ON c.courseCode = e.courseCode " +
                    "WHERE e.studentNumber = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("totalCredits");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error calculating credits: " + e.getMessage());
        }
        return 0;
    }
}