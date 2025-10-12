package domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Data Transfer Object for Enrollment data
 * Used for client-server communication with custom serialization
 */
public class EnrollmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentNumber;
    private String courseCode;
    private Timestamp enrollmentDate;

    // Default constructor
    public EnrollmentDTO() {}

    // Constructor with all fields
    public EnrollmentDTO(String studentNumber, String courseCode, Timestamp enrollmentDate) {
        this.studentNumber = studentNumber;
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
    }

    // Getters and Setters
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "EnrollmentDTO{" +
                "studentNumber='" + studentNumber + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
