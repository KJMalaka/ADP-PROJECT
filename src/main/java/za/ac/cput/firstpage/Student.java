/*
 * Student GUI with Direct Database Connection (No Socket/Server)
 */
package za.ac.cput.firstpage;

import dao.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author phelo
 */
public class Student extends JFrame implements ActionListener {

    private String studentNumber;
    
    // DAO objects for database operations
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    
    private JPanel headerPnl, btnPnl;
    private JLabel titleLbl, welcomeLbl;
    
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    private JButton btnRefreshCourses, btnEnroll, btnViewMyCourses, btnLogout;
    private JTextArea myCourseArea;

    public Student() {
        this("UNKNOWN");
    }
    
    public Student(String studentNumber) {
        super("Student Portal");
        this.studentNumber = studentNumber;
        initializeDAOs();
        initComponents();
        loadAvailableCourses();
    }
    
    /**
     * Initialize DAO objects
     */
    private void initializeDAOs() {
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
        System.out.println("✓ Student portal initialized for student: " + studentNumber);
    }
    
    private void initComponents() {
        this.setLayout(new BorderLayout());
        
        // Header Panel
        headerPnl = new JPanel(new BorderLayout());
        headerPnl.setBackground(new Color(70, 130, 180));
        
        titleLbl = new JLabel("Student Dashboard", JLabel.CENTER);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 24));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        welcomeLbl = new JLabel("Student: " + studentNumber, JLabel.CENTER);
        welcomeLbl.setForeground(Color.WHITE);
        welcomeLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        headerPnl.add(titleLbl, BorderLayout.NORTH);
        headerPnl.add(welcomeLbl, BorderLayout.CENTER);
        
        // Main Panel with Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Available Courses", createAvailableCoursesPanel());
        tabbedPane.addTab("My Courses", createMyCoursesPanel());
        
        // Button Panel
        btnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPnl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.red);
        btnLogout.addActionListener(this);
        btnPnl.add(btnLogout);
        
        this.add(headerPnl, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(btnPnl, BorderLayout.SOUTH);
    }
    
    private JPanel createAvailableCoursesPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Center - Table for courses
        String[] columns = {"Course Code", "Course Name", "Credits", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        coursesTable = new JTable(tableModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        coursesTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        coursesTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        coursesTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        
        // South - Button panel
        JPanel southPanel = new JPanel(new FlowLayout());
        
        btnRefreshCourses = new JButton("Refresh Courses");
        btnRefreshCourses.setBackground(new Color(70, 130, 180));
        btnRefreshCourses.setForeground(Color.blue);
        btnRefreshCourses.addActionListener(this);
        
        btnEnroll = new JButton("Enroll in Selected Course");
        btnEnroll.setBackground(new Color(40, 167, 69));
        btnEnroll.setForeground(Color.blue);
        btnEnroll.addActionListener(this);
        
        southPanel.add(btnRefreshCourses);
        southPanel.add(btnEnroll);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createMyCoursesPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Center - Text area for enrolled courses
        myCourseArea = new JTextArea(20, 50);
        myCourseArea.setEditable(false);
        myCourseArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(myCourseArea);
        
        // South - Button panel
        JPanel southPanel = new JPanel(new FlowLayout());
        
        btnViewMyCourses = new JButton("Refresh My Courses");
        btnViewMyCourses.setBackground(new Color(70, 130, 180));
        btnViewMyCourses.setForeground(Color.blue);
        btnViewMyCourses.addActionListener(this);
        
        southPanel.add(btnViewMyCourses);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Load all available courses from database
     */
    private void loadAvailableCourses() {
        tableModel.setRowCount(0); // Clear existing rows
        
        // Get all courses from database
        List<String[]> allCourses = courseDAO.getAllCourses();
        
        if (allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No courses available in the database.\nPlease contact admin to add courses.",
                "No Courses",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Add each course to the table with enrollment status
        for (String[] course : allCourses) {
            String courseCode = course[0];
            String courseName = course[1];
            String credits = course[2];
            
            // Check if student is already enrolled
            boolean isEnrolled = enrollmentDAO.isEnrolled(studentNumber, courseCode);
            String status = isEnrolled ? "✓ Enrolled" : "Available";
            
            tableModel.addRow(new Object[]{courseCode, courseName, credits, status});
        }
        
        System.out.println("✓ Loaded " + allCourses.size() + " courses");
    }
    
    /**
     * Enroll student in selected course
     */
    private void enrollInCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a course to enroll", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String courseCode = (String) tableModel.getValueAt(selectedRow, 0);
        String courseName = (String) tableModel.getValueAt(selectedRow, 1);
        String status = (String) tableModel.getValueAt(selectedRow, 3);
        
        // Check if already enrolled
        if (status.equals("✓ Enrolled")) {
            JOptionPane.showMessageDialog(this, 
                "You are already enrolled in this course!", 
                "Already Enrolled", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Confirm enrollment
        int confirm = JOptionPane.showConfirmDialog(this,
            "Do you want to enroll in:\n\n" + 
            "Course Code: " + courseCode + "\n" +
            "Course Name: " + courseName + "\n\n" +
            "Click YES to confirm enrollment.",
            "Confirm Enrollment",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Enroll in database
        boolean success = enrollmentDAO.enrollStudent(studentNumber, courseCode);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Successfully enrolled in " + courseName + "!\n\n" +
                "Course Code: " + courseCode + "\n" +
                "You can now view this course in the 'My Courses' tab.", 
                "Enrollment Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the course list to update status
            loadAvailableCourses();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to enroll in course.\n" +
                "This might be because you're already enrolled or there was a database error.\n" +
                "Please check the console for details.", 
                "Enrollment Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * View all courses the student is enrolled in
     */
    private void viewMyCourses() {
        // Get enrolled courses from database
        List<String[]> enrolledCourses = enrollmentDAO.getCoursesByStudent(studentNumber);
        
        // Build display text
        StringBuilder result = new StringBuilder();
        result.append("═══════════════════════════════════════════════════════════════════════════════\n");
        result.append("                    MY ENROLLED COURSES - Student ").append(studentNumber).append("\n");
        result.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
        
        if (enrolledCourses.isEmpty()) {
            result.append("You are not currently enrolled in any courses.\n\n");
            result.append("To enroll:\n");
            result.append("1. Go to the 'Available Courses' tab\n");
            result.append("2. Select a course from the table\n");
            result.append("3. Click 'Enroll in Selected Course'\n");
        } else {
            result.append(String.format("%-12s %-40s %-8s %s\n", 
                    "Course Code", "Course Name", "Credits", "Enrollment Date"));
            result.append("───────────────────────────────────────────────────────────────────────────────\n");
            
            int totalCredits = 0;
            for (String[] course : enrolledCourses) {
                result.append(String.format("%-12s %-40s %-8s %s\n",
                        course[0],  // courseCode
                        course[1],  // courseName
                        course[2],  // credits
                        course[3])); // enrollmentDate
                totalCredits += Integer.parseInt(course[2]);
            }
            
            result.append("───────────────────────────────────────────────────────────────────────────────\n");
            result.append("Total Courses: ").append(enrolledCourses.size()).append("\n");
            result.append("Total Credits: ").append(totalCredits).append("\n");
        }
        
        result.append("═══════════════════════════════════════════════════════════════════════════════\n");
        myCourseArea.setText(result.toString());
        
        System.out.println("✓ Displayed enrolled courses for student " + studentNumber);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefreshCourses) {
            loadAvailableCourses();
            JOptionPane.showMessageDialog(this, 
                "Course list refreshed successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == btnEnroll) {
            enrollInCourse();
        } else if (e.getSource() == btnViewMyCourses) {
            viewMyCourses();
        } else if (e.getSource() == btnLogout) {
            logout();
        }
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
            System.out.println("Student " + studentNumber + " logged out");
            
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
        
        Student stuGui = new Student("12345");
        stuGui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        stuGui.setSize(800, 600);
        stuGui.setVisible(true);
        stuGui.setLocationRelativeTo(null);
    }
}