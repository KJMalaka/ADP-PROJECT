package za.ac.cput.firstpage;

import dao.AdminDAO;
import dao.StudentDAO;



public class TestAuthentication {
    public static void main(String[] args) {
        System.out.println("=== Testing Authentication ===\n");
        
        AdminDAO adminDAO = new AdminDAO();
        System.out.println("Testing Admin: admin / 0000");
        boolean adminAuth = adminDAO.authenticateAdmin("admin", "0000");
        System.out.println("Admin auth result: " + adminAuth);
        
        System.out.println("\n---\n");
        
        
        StudentDAO studentDAO = new StudentDAO();
        System.out.println("Testing Student: 12345 / 1111");
        boolean studentAuth = studentDAO.authenticateStudent("12345", "1111");
        System.out.println("Student auth result: " + studentAuth);
    }
}