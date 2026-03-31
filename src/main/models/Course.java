package main.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a course in the grade management system.
 * Stores course information including code, name, credits, and enrolled students.
 */
public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private int maxCapacity;
    private List<String> enrolledStudents; // List of student IDs
    
    public Course(String courseCode, String courseName, int credits, int maxCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new ArrayList<>();
    }
    
    // Getters
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public List<String> getEnrolledStudents() {
        return enrolledStudents;
    }
    
    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }
    
    // Setters
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    // Student enrollment management
    public boolean addStudent(String studentId) {
        if (enrolledStudents.size() >= maxCapacity) {
            return false; // Course is full
        }
        if (!enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            return true;
        }
        return false; // Student already enrolled
    }
    
    public void removeStudent(String studentId) {
        enrolledStudents.remove(studentId);
    }
    
    public boolean hasStudent(String studentId) {
        return enrolledStudents.contains(studentId);
    }
    
    public boolean isFull() {
        return enrolledStudents.size() >= maxCapacity;
    }
    
    // CSV format: courseCode,courseName,credits,maxCapacity,studentId1;studentId2;...
    public String toCSV() {
        StringBuilder students = new StringBuilder();
        for (int i = 0; i < enrolledStudents.size(); i++) {
            students.append(enrolledStudents.get(i));
            if (i < enrolledStudents.size() - 1) {
                students.append(";");
            }
        }
        return String.format("%s,%s,%d,%d,%s",
            courseCode, courseName, credits, maxCapacity, students.toString());
    }
    
    public static Course fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 5);
        Course course = new Course(
            parts[0], 
            parts[1], 
            Integer.parseInt(parts[2]), 
            Integer.parseInt(parts[3])
        );
        
        if (parts.length > 4 && !parts[4].trim().isEmpty()) {
            String[] students = parts[4].split(";");
            for (String student : students) {
                course.addStudent(student.trim());
            }
        }
        
        return course;
    }
    
    @Override
    public String toString() {
        return String.format("Course[Code=%s, Name=%s, Credits=%d, Enrolled=%d/%d]",
            courseCode, courseName, credits, enrolledStudents.size(), maxCapacity);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseCode.equals(course.courseCode);
    }
    
    @Override
    public int hashCode() {
        return courseCode.hashCode();
    }
}
