package main.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student in the grade management system.
 * Stores student information including personal details and enrolled courses.
 */
public class Student {
    private String studentId;
    private String name;
    private String email;
    private LocalDate enrollmentDate;
    private List<String> enrolledCourses; // List of course codes
    
    public Student(String studentId, String name, String email, LocalDate enrollmentDate) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
        this.enrolledCourses = new ArrayList<>();
    }
    
    // Constructor for CSV loading
    public Student(String studentId, String name, String email, String enrollmentDate) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.enrollmentDate = LocalDate.parse(enrollmentDate);
        this.enrolledCourses = new ArrayList<>();
    }
    
    // Getters
    public String getStudentId() {
        return studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    // Course management
    public void enrollInCourse(String courseCode) {
        if (!enrolledCourses.contains(courseCode)) {
            enrolledCourses.add(courseCode);
        }
    }
    
    public void dropCourse(String courseCode) {
        enrolledCourses.remove(courseCode);
    }
    
    public boolean isEnrolledIn(String courseCode) {
        return enrolledCourses.contains(courseCode);
    }
    
    // CSV format: studentId,name,email,enrollmentDate,courseCode1;courseCode2;...
    public String toCSV() {
        StringBuilder courses = new StringBuilder();
        for (int i = 0; i < enrolledCourses.size(); i++) {
            courses.append(enrolledCourses.get(i));
            if (i < enrolledCourses.size() - 1) {
                courses.append(";");
            }
        }
        return String.format("%s,%s,%s,%s,%s", 
            studentId, name, email, enrollmentDate, courses.toString());
    }
    
    public static Student fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 5);
        Student student = new Student(parts[0], parts[1], parts[2], parts[3]);
        
        if (parts.length > 4 && !parts[4].trim().isEmpty()) {
            String[] courses = parts[4].split(";");
            for (String course : courses) {
                student.enrollInCourse(course.trim());
            }
        }
        
        return student;
    }
    
    @Override
    public String toString() {
        return String.format("Student[ID=%s, Name=%s, Email=%s, Enrolled=%s, Courses=%d]",
            studentId, name, email, enrollmentDate, enrolledCourses.size());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId.equals(student.studentId);
    }
    
    @Override
    public int hashCode() {
        return studentId.hashCode();
    }
}
