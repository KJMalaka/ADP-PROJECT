package dao;

import Connection.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Course table
 * Handles all CRUD operations for courses
 */
public class CourseDAO {
    
    /**
     * CREATE - Add a new course to the database
     * @param courseCode
     * @param courseName
     * @param credits
     * @return true if successful, false otherwise
     */
    public boolean addCourse(String courseCode, String courseName, int credits) {
        String sql = "INSERT INTO Courses (courseCode, courseName, credits) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            pstmt.setString(2, courseName);
            pstmt.setInt(3, credits);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Course added: " + courseCode);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error adding course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * READ - Get all courses from the database
     * @return List of String arrays [courseCode, courseName, credits]
     */
    public List<String[]> getAllCourses() {
        List<String[]> courses = new ArrayList<>();
        String sql = "SELECT courseCode, courseName, credits FROM Courses ORDER BY courseCode";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String[] course = new String[3];
                course[0] = rs.getString("courseCode");
                course[1] = rs.getString("courseName");
                course[2] = String.valueOf(rs.getInt("credits"));
                courses.add(course);
            }
            
            System.out.println("✓ Retrieved " + courses.size() + " courses");
            
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving courses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }
    
    /**
     * READ - Get a specific course by code
     * @param courseCode
     * @return String array [courseCode, courseName, credits] or null if not found
     */
    public String[] getCourseByCode(String courseCode) {
        String sql = "SELECT courseCode, courseName, credits FROM Courses WHERE courseCode = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String[] course = new String[3];
                course[0] = rs.getString("courseCode");
                course[1] = rs.getString("courseName");
                course[2] = String.valueOf(rs.getInt("credits"));
                return course;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting course: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * UPDATE - Update an existing course
     * @param courseCode
     * @param courseName
     * @param credits
     * @return true if successful, false otherwise
     */
    public boolean updateCourse(String courseCode, String courseName, int credits) {
        String sql = "UPDATE Courses SET courseName = ?, credits = ? WHERE courseCode = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseName);
            pstmt.setInt(2, credits);
            pstmt.setString(3, courseCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Course updated: " + courseCode);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error updating course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * DELETE - Delete a course
     * @param courseCode
     * @return true if successful, false otherwise
     */
    public boolean deleteCourse(String courseCode) {
        String sql = "DELETE FROM Courses WHERE courseCode = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Course deleted: " + courseCode);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error deleting course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if a course exists
     * @param courseCode
     * @return true if exists, false otherwise
     */
    public boolean courseExists(String courseCode) {
        String sql = "SELECT COUNT(*) FROM Courses WHERE courseCode = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error checking course existence: " + e.getMessage());
        }
        return false;
    }
}