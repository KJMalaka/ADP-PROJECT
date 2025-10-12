package domain;

import java.io.Serializable;

/**
 * Data Transfer Object for Student data
 * Used for client-server communication with custom serialization
 */
public class StudentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentNumber;
    private String studentName;
    private String email;
    private String pin;

    // Default constructor
    public StudentDTO() {}

    // Constructor with all fields
    public StudentDTO(String studentNumber, String studentName, String email, String pin) {
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.email = email;
        this.pin = pin;
    }

    // Constructor without pin (for safe data transfer)
    public StudentDTO(String studentNumber, String studentName, String email) {
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.email = email;
    }

    // Getters and Setters
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "studentNumber='" + studentNumber + '\'' +
                ", studentName='" + studentName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
