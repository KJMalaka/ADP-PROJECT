/*
 * Admin GUI with Direct Database Connection (No Socket/Server)
 */
package za.ac.cput.firstpage;

import dao.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

/**
 * @author phelo
 */
public class Admin extends JFrame implements ActionListener {
    
    // DAO objects for database operations
    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;
    
    private JPanel headerPnl, mainPnl, btnPnl;
    private JLabel titleLbl;
    
    // Add Course Components
    private JTextField txtCourseCode, txtCourseName, txtCredits;
    private JButton btnAddCourse;
    
    // Add Student Components
    private JTextField txtStudentNum, txtStudentName, txtStudentEmail;
    private JButton btnAddStudent;
    
    // View Enrollments Components
    private JTextField txtViewCourseCode, txtViewStudentNum;
    private JButton btnViewStudentsByCourse, btnViewCoursesByStudent;
    private JTextArea displayArea;
    
    private JButton btnLogout;
    
    public Admin() {
        super("Admin Portal");
        initializeDAOs();
        initComponents();
    }
    
    /**
     * Initialize DAO objects
     */
    private void initializeDAOs() {
        courseDAO = new CourseDAO();
        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();
        System.out.println("✓ Admin portal initialized with database connection");
    }
    
    private void initComponents() {
        this.setLayout(new BorderLayout());
        
        // Header
        headerPnl = new JPanel();
        headerPnl.setBackground(new Color(220, 53, 69));
        titleLbl = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 24));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPnl.add(titleLbl);
        
        // Main Panel with Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Course", createAddCoursePanel());
        tabbedPane.addTab("Add Student", createAddStudentPanel());
        tabbedPane.addTab("View Enrollments", createViewEnrollmentsPanel());
        
        // Logout Panel
        btnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(108, 117, 125));
        btnLogout.setForeground(Color.red);
        btnLogout.addActionListener(this);
        btnPnl.add(btnLogout);
        btnPnl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        this.add(headerPnl, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(btnPnl, BorderLayout.SOUTH);
    }
    
    private JPanel createAddCoursePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 20, 100));
        
        centerPanel.add(new JLabel("Course Code:"));
        txtCourseCode = new JTextField(20);
        centerPanel.add(txtCourseCode);
        
        centerPanel.add(new JLabel("Course Name:"));
        txtCourseName = new JTextField(20);
        centerPanel.add(txtCourseName);
        
        centerPanel.add(new JLabel("Credits:"));
        txtCredits = new JTextField(20);
        centerPanel.add(txtCredits);
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        btnAddCourse = new JButton("Add Course");
        btnAddCourse.setBackground(new Color(220, 53, 69));
        btnAddCourse.setForeground(Color.blue);
        btnAddCourse.addActionListener(this);
        southPanel.add(btnAddCourse);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createAddStudentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 20, 100));
        
        centerPanel.add(new JLabel("Student Number:"));
        txtStudentNum = new JTextField(20);
        centerPanel.add(txtStudentNum);
        
        centerPanel.add(new JLabel("Student Name:"));
        txtStudentName = new JTextField(20);
        centerPanel.add(txtStudentName);
        
        centerPanel.add(new JLabel("Email:"));
        txtStudentEmail = new JTextField(20);
        centerPanel.add(txtStudentEmail);
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        btnAddStudent = new JButton("Add Student");
        btnAddStudent.setBackground(new Color(220, 53, 69));
        btnAddStudent.setForeground(Color.blue);
        btnAddStudent.addActionListener(this);
        southPanel.add(btnAddStudent);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createViewEnrollmentsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // North panel for inputs
        JPanel northPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        northPanel.add(new JLabel("Course Code:"));
        txtViewCourseCode = new JTextField();
        northPanel.add(txtViewCourseCode);
        
        btnViewStudentsByCourse = new JButton("View Students in Course");
        btnViewStudentsByCourse.addActionListener(this);
        northPanel.add(new JLabel(""));
        northPanel.add(btnViewStudentsByCourse);
        
        northPanel.add(new JLabel("Student Number:"));
        txtViewStudentNum = new JTextField();
        northPanel.add(txtViewStudentNum);
        
        btnViewCoursesByStudent = new JButton("View Student's Courses");
        btnViewCoursesByStudent.addActionListener(this);
        northPanel.add(new JLabel(""));
        northPanel.add(btnViewCoursesByStudent);
        
        // Center panel for display area
        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddCourse) {
            addCourse();
        } else if (e.getSource() == btnAddStudent) {
            addStudent();
        } else if (e.getSource() == btnViewStudentsByCourse) {
            viewStudentsByCourse();
        } else if (e.getSource() == btnViewCoursesByStudent) {
            viewCoursesByStudent();
        } else if (e.getSource() == btnLogout) {
            logout();
        }
    }
    
    /**
     * Add a new course to the database
     */
    private void addCourse() {
        String code = txtCourseCode.getText().trim();
        String name = txtCourseName.getText().trim();
        String creditsStr = txtCredits.getText().trim();
        
        // Validation
        if (code.isEmpty() || name.isEmpty() || creditsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill all fields", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate credits is a number
        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
            if (credits <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Credits must be a positive number", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Credits must be a valid number", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if course already exists
        if (courseDAO.courseExists(code)) {
            JOptionPane.showMessageDialog(this, 
                "Course code '" + code + "' already exists!", 
                "Duplicate Course", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Add to database
        boolean success = courseDAO.addCourse(code, name, credits);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Course added successfully!\n\nCourse Code: " + code + "\nCourse Name: " + name + "\nCredits: " + credits, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            txtCourseCode.setText("");
            txtCourseName.setText("");
            txtCredits.setText("");
            txtCourseCode.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add course. Please check the console for errors.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Add a new student to the database
     */
    private void addStudent() {
        String num = txtStudentNum.getText().trim();
        String name = txtStudentName.getText().trim();
        String email = txtStudentEmail.getText().trim();
        
        // Validation
        if (num.isEmpty() || name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill all fields", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Basic email validation
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if student already exists
        if (studentDAO.studentExists(num)) {
            JOptionPane.showMessageDialog(this, 
                "Student number '" + num + "' already exists!", 
                "Duplicate Student", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Add to database with default PIN
        String defaultPin = "1111";
        boolean success = studentDAO.addStudent(num, name, email, defaultPin);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Student added successfully!\n\nStudent Number: " + num + 
                "\nStudent Name: " + name + 
                "\nEmail: " + email + 
                "\nDefault PIN: " + defaultPin, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            txtStudentNum.setText("");
            txtStudentName.setText("");
            txtStudentEmail.setText("");
            txtStudentNum.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add student. Please check the console for errors.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * View all students enrolled in a specific course
     */
    private void viewStudentsByCourse() {
        String courseCode = txtViewCourseCode.getText().trim();
        
        if (courseCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a course code", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if course exists
        if (!courseDAO.courseExists(courseCode)) {
            displayArea.setText("Course '" + courseCode + "' does not exist in the database.");
            return;
        }
        
        // Get students enrolled in this course
        List<String[]> students = enrollmentDAO.getStudentsByCourse(courseCode);
        
        // Display results
        StringBuilder result = new StringBuilder();
        result.append("═══════════════════════════════════════════════════════════════════════════════\n");
        result.append("                    STUDENTS ENROLLED IN ").append(courseCode).append("\n");
        result.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
        
        if (students.isEmpty()) {
            result.append("No students are currently enrolled in this course.\n");
        } else {
            result.append(String.format("%-15s %-25s %-30s %s\n", 
                    "Student #", "Name", "Email", "Enrollment Date"));
            result.append("───────────────────────────────────────────────────────────────────────────────\n");
            
            for (String[] student : students) {
                result.append(String.format("%-15s %-25s %-30s %s\n",
                        student[0], student[1], student[2], student[3]));
            }
            
            result.append("───────────────────────────────────────────────────────────────────────────────\n");
            result.append("Total Students: ").append(students.size()).append("\n");
        }
        
        result.append("═══════════════════════════════════════════════════════════════════════════════\n");
        displayArea.setText(result.toString());
    }
    
    /**
     * View all courses a specific student is enrolled in
     */
    private void viewCoursesByStudent() {
        String studentNum = txtViewStudentNum.getText().trim();
        
        if (studentNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a student number", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if student exists
        if (!studentDAO.studentExists(studentNum)) {
            displayArea.setText("Student '" + studentNum + "' does not exist in the database.");
            return;
        }
        
        // Get student info
        String[] studentInfo = studentDAO.getStudentByNumber(studentNum);
        
        // Get courses for this student
        List<String[]> courses = enrollmentDAO.getCoursesByStudent(studentNum);
        
        // Display results
        StringBuilder result = new StringBuilder();
        result.append("═══════════════════════════════════════════════════════════════════════════════\n");
        result.append("                    COURSES FOR STUDENT ").append(studentNum).append("\n");
        if (studentInfo != null) {
            result.append("                    ").append(studentInfo[1]).append("\n");
        }
        result.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
        
        if (courses.isEmpty()) {
            result.append("This student is not currently enrolled in any courses.\n");
        } else {
            result.append(String.format("%-12s %-35s %-8s %s\n", 
                    "Course Code", "Course Name", "Credits", "Enrollment Date"));
            result.append("───────────────────────────────────────────────────────────────────────────────\n");
            
            int totalCredits = 0;
            for (String[] course : courses) {
                result.append(String.format("%-12s %-35s %-8s %s\n",
                        course[0], course[1], course[2], course[3]));
                totalCredits += Integer.parseInt(course[2]);
            }
            
            result.append("───────────────────────────────────────────────────────────────────────────────\n");
            result.append("Total Courses: ").append(courses.size()).append("\n");
            result.append("Total Credits: ").append(totalCredits).append("\n");
        }
        
        result.append("═══════════════════════════════════════════════════════════════════════════════\n");
        displayArea.setText(result.toString());
    }
    
    /**
     * Logout and return to login page
     */
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            System.out.println("Admin logged out");
            
            FirstPage fpGui = new FirstPage();
            fpGui.setDefaultCloseOperation(EXIT_ON_CLOSE);
            fpGui.setSize(450, 350);
            fpGui.setVisible(true);
            fpGui.setLocationRelativeTo(null);
            dispose();
        }
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Admin adGui = new Admin();
        adGui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        adGui.setSize(700, 600);
        adGui.setVisible(true);
        adGui.setLocationRelativeTo(null);
    }
}