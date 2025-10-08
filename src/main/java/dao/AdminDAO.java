package dao;

import Connection.DBConnection;
import java.sql.*;

/**
 * Data Access Object for Admin table
 * Handles admin authentication and management
 */
public class AdminDAO {
    
    /**
     * Authenticate admin login
     * @param adminId
     * @param pin
     * @return true if credentials are valid, false otherwise
     */
    public boolean authenticateAdmin(String adminId, String pin) {
        String sql = "SELECT COUNT(*) FROM Admins WHERE adminId = ? AND pin = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, adminId);
            pstmt.setString(2, pin);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                boolean authenticated = rs.getInt(1) > 0;
                if (authenticated) {
                    System.out.println("✓ Admin authenticated: " + adminId);
                } else {
                    System.out.println("✗ Admin authentication failed for: " + adminId);
                }
                return authenticated;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error authenticating admin: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Add a new admin
     * @param adminId
     * @param adminName
     * @param pin
     * @return true if successful, false otherwise
     */
    public boolean addAdmin(String adminId, String adminName, String pin) {
        String sql = "INSERT INTO Admins (adminId, adminName, pin) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, adminId);
            pstmt.setString(2, adminName);
            pstmt.setString(3, pin);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Admin added: " + adminId);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error adding admin: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if an admin exists
     * @param adminId
     * @return true if exists, false otherwise
     */
    public boolean adminExists(String adminId) {
        String sql = "SELECT COUNT(*) FROM Admins WHERE adminId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, adminId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error checking admin existence: " + e.getMessage());
        }
        return false;
    }
}