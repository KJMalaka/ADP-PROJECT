/*
 * Login Page with Database Authentication
 */
package za.ac.cput.firstpage;

import dao.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author phelo
 */
public class FirstPage extends JFrame implements ActionListener {
    
    private JPanel radbtnPnl, detailsPnl, btnPnl;
    private JLabel titleLbl, studentLbl, adminLbl, userLbl, passLbl;
    private JTextField userTxt;
    private JPasswordField passTxt;
    private JButton loginBtn, clearBtn;
    private JRadioButton rbtnStudent, rbtnAdmin;
    
    // DAO objects for authentication
    private StudentDAO studentDAO;
    private AdminDAO adminDAO;
    
    private final String STUDENT_ID = "12345";
    private final String STUDENT_PIN = "1111";
    private final String ADMIN_ID = "admin";
    private final String ADMIN_PIN = "0000";

    public FirstPage() {
        super("Login Portal");
        this.setLayout(new BorderLayout());
        initializeDAOs();
        initComponents();
    }
    
    /**
     * Initialize DAO objects
     * this ensures the connection is active before queries are run.
     */
    private void initializeDAOs() {
        studentDAO = new StudentDAO();
        adminDAO = new AdminDAO();
        System.out.println("✓ Login portal initialized with database connection");
    }
    
    private void initComponents() {
        // Title
        titleLbl = new JLabel("Select Your Role:", JLabel.CENTER);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 16));
        titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        // Radio button panel
        radbtnPnl = new JPanel(new FlowLayout());
        radbtnPnl.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        ButtonGroup group = new ButtonGroup();
        rbtnStudent = new JRadioButton();
        rbtnAdmin = new JRadioButton();
        studentLbl = new JLabel("Student");
        adminLbl = new JLabel("Admin");
        
        group.add(rbtnStudent);
        group.add(rbtnAdmin);
        
        radbtnPnl.add(rbtnStudent);
        radbtnPnl.add(studentLbl);
        radbtnPnl.add(rbtnAdmin);
        radbtnPnl.add(adminLbl);

        // Details panel
        detailsPnl = new JPanel(new GridLayout(3, 2, 10, 15));
        detailsPnl.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        userLbl = new JLabel("ID Number:");
        userTxt = new JTextField(15);
        passLbl = new JLabel("Pin:");
        passTxt = new JPasswordField(15);
        
        detailsPnl.add(userLbl);
        detailsPnl.add(userTxt);
        detailsPnl.add(passLbl);
        detailsPnl.add(passTxt);

        // Button panel
        btnPnl = new JPanel(new FlowLayout());
        btnPnl.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.blue);
        
        clearBtn = new JButton("Clear");
        clearBtn.setBackground(new Color(108, 117, 125));
        clearBtn.setForeground(Color.red);
        
        btnPnl.add(loginBtn);
        btnPnl.add(clearBtn);

        // Combine title and radio buttons in north panel
        JPanel northPnl = new JPanel(new BorderLayout());
        northPnl.add(titleLbl, BorderLayout.NORTH);
        northPnl.add(radbtnPnl, BorderLayout.CENTER);

        this.add(northPnl, BorderLayout.NORTH);
        this.add(detailsPnl, BorderLayout.CENTER);
        this.add(btnPnl, BorderLayout.SOUTH);

        // Add action listeners
        rbtnStudent.addActionListener(this);
        rbtnAdmin.addActionListener(this);
        loginBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        
        // Set default selection
        rbtnStudent.setSelected(true);
        updateLabels();
    }

    private void updateLabels() {
        if (rbtnStudent.isSelected()) {
            userLbl.setText("Student Number:");
        } else if (rbtnAdmin.isSelected()) {
            userLbl.setText("Admin ID:");
        }
    }

    private void clearFields() {
        userTxt.setText("");
        passTxt.setText("");
        userTxt.requestFocus();
    }

    /**
     * Authenticate user against database
     * @param userId
     * @param pin
     * @param isStudent
     * @return true if credentials are valid
     */
    private boolean authenticateUser(String userId, String pin, boolean isStudent) {
        
        try {
            if (isStudent) {
                return studentDAO.authenticateStudent(userId, pin);
            } else {
                return adminDAO.authenticateAdmin(userId, pin);
            }
        } catch (Exception e) {
            System.err.println("✗ Authentication error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Database connection error.\nPlease ensure:\n" +
                "1. Derby server is running\n" +
                "2. Database 'EnrollmentDB' exists\n" +
                "3. Tables are created\n\n" +
                "Error: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Radio button clicks handling 
        if (e.getSource() == rbtnStudent || e.getSource() == rbtnAdmin) {
            updateLabels();
        }
        
        // Clear button click
        if (e.getSource() == clearBtn) {
            clearFields();
        }
        
        // Login button click
        if (e.getSource() == loginBtn) {
            handleLogin();
        }
    }
    
    /**
     * Handle login process
     */
    private void handleLogin() {
        // Check if a role is selected
        if (!rbtnStudent.isSelected() && !rbtnAdmin.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a role (Student or Admin)", 
                "Role Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get and validate input
        String userId = userTxt.getText().trim();
        String userPin = new String(passTxt.getPassword()).trim();
        
        if (userId.isEmpty() || userPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields.", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show loading message
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        
        // Authenticate
        boolean isStudent = rbtnStudent.isSelected();
        boolean authenticated = authenticateUser(userId, userPin, isStudent);
        
        // Reset cursor
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        if (authenticated) {
            if (isStudent) {
                // Open student dashboard
                System.out.println("✓ Student login successful: " + userId);
                Student stuGui = new Student(userId);
                stuGui.setDefaultCloseOperation(EXIT_ON_CLOSE);
                stuGui.setSize(800, 600);
                stuGui.setVisible(true);
                stuGui.setLocationRelativeTo(null);
                dispose();
            } else {
                // Open admin dashboard
                System.out.println("✓ Admin login successful: " + userId);
                Admin adGui = new Admin();
                adGui.setDefaultCloseOperation(EXIT_ON_CLOSE);
                adGui.setSize(700, 600);
                adGui.setVisible(true);
                adGui.setLocationRelativeTo(null);
                dispose();
            }
        } else {
            System.out.println("✗ Login failed for: " + userId);
            JOptionPane.showMessageDialog(this, 
                "Invalid credentials. Please try again.\n\n" +
                "If you're a new student, ask admin to add you to the system.\n\n" +
                "Default Test Credentials (if database is set up):\n" +
                "Student - ID: 12345, PIN: 1111\n" +
                "Admin - ID: admin, PIN: 0000", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            clearFields();
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        FirstPage fpGui = new FirstPage();
        fpGui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        fpGui.setSize(450, 350);
        fpGui.setVisible(true);
        fpGui.setLocationRelativeTo(null);
        fpGui.setResizable(false);
    }
}