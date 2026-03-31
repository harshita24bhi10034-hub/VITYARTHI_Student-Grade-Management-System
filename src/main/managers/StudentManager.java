package main.managers;

import main.models.Student;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all student-related operations.
 * Uses HashMap for efficient student lookup by ID.
 */
public class StudentManager {
    private Map<String, Student> students; // Key: studentId
    
    public StudentManager() {
        this.students = new HashMap<>();
    }
    
    // Create
    public boolean addStudent(String studentId, String name, String email, LocalDate enrollmentDate) {
        if (students.containsKey(studentId)) {
            return false; // Student already exists
        }
        Student student = new Student(studentId, name, email, enrollmentDate);
        students.put(studentId, student);
        return true;
    }
    
    public boolean addStudent(Student student) {
        if (students.containsKey(student.getStudentId())) {
            return false;
        }
        students.put(student.getStudentId(), student);
        return true;
    }
    
    // Read
    public Student getStudent(String studentId) {
        return students.get(studentId);
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    public List<Student> getStudentsSortedByName() {
        return students.values().stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }
    
    public List<Student> getStudentsSortedById() {
        return students.values().stream()
                .sorted(Comparator.comparing(Student::getStudentId))
                .collect(Collectors.toList());
    }
    
    public boolean studentExists(String studentId) {
        return students.containsKey(studentId);
    }
    
    // Update
    public boolean updateStudent(String studentId, String name, String email) {
        Student student = students.get(studentId);
        if (student == null) {
            return false;
        }
        student.setName(name);
        student.setEmail(email);
        return true;
    }
    
    // Delete
    public boolean removeStudent(String studentId) {
        return students.remove(studentId) != null;
    }
    
    // Enrollment management
    public boolean enrollStudentInCourse(String studentId, String courseCode) {
        Student student = students.get(studentId);
        if (student == null) {
            return false;
        }
        student.enrollInCourse(courseCode);
        return true;
    }
    
    public boolean dropStudentFromCourse(String studentId, String courseCode) {
        Student student = students.get(studentId);
        if (student == null) {
            return false;
        }
        student.dropCourse(courseCode);
        return true;
    }
    
    public List<String> getStudentCourses(String studentId) {
        Student student = students.get(studentId);
        return student != null ? student.getEnrolledCourses() : new ArrayList<>();
    }
    
    // Search operations
    public List<Student> searchByName(String nameQuery) {
        String query = nameQuery.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }
    
    public List<Student> searchByEmail(String emailQuery) {
        String query = emailQuery.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getEmail().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }
    
    public List<Student> getStudentsEnrolledInCourse(String courseCode) {
        return students.values().stream()
                .filter(s -> s.isEnrolledIn(courseCode))
                .collect(Collectors.toList());
    }
    
    // Statistics
    public int getTotalStudents() {
        return students.size();
    }
    
    public int getStudentsEnrolledAfter(LocalDate date) {
        return (int) students.values().stream()
                .filter(s -> s.getEnrollmentDate().isAfter(date))
                .count();
    }
    
    // Bulk operations
    public void clear() {
        students.clear();
    }
    
    public Map<String, Student> getStudentsMap() {
        return students;
    }
    
    @Override
    public String toString() {
        return String.format("StudentManager[Total Students: %d]", students.size());
    }
}
