package main.utils;

import main.managers.GradeManager;
import main.managers.StudentManager;
import main.models.Student;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for GPA calculations and student performance analytics.
 */
public class GPACalculator {
    private GradeManager gradeManager;
    private StudentManager studentManager;
    
    public GPACalculator(GradeManager gradeManager, StudentManager studentManager) {
        this.gradeManager = gradeManager;
        this.studentManager = studentManager;
    }
    
    // Calculate GPA for a single student
    public double calculateStudentGPA(String studentId) {
        Student student = studentManager.getStudent(studentId);
        if (student == null) {
            return -1.0; // Student not found
        }
        
        List<String> courses = student.getEnrolledCourses();
        if (courses.isEmpty()) {
            return 0.0;
        }
        
        return gradeManager.calculateStudentGPA(studentId, courses);
    }
    
    // Get student's GPA with letter grade classification
    public String getGPAClassification(double gpa) {
        if (gpa >= 3.7) return "Excellent";
        else if (gpa >= 3.3) return "Very Good";
        else if (gpa >= 3.0) return "Good";
        else if (gpa >= 2.5) return "Satisfactory";
        else if (gpa >= 2.0) return "Pass";
        else return "At Risk";
    }
    
    // Identify at-risk students (GPA < 2.0)
    public List<StudentPerformance> getAtRiskStudents() {
        List<StudentPerformance> atRisk = new ArrayList<>();
        
        for (Student student : studentManager.getAllStudents()) {
            double gpa = calculateStudentGPA(student.getStudentId());
            if (gpa >= 0 && gpa < 2.0) {
                atRisk.add(new StudentPerformance(
                    student.getStudentId(),
                    student.getName(),
                    gpa
                ));
            }
        }
        
        // Sort by GPA ascending (worst first)
        atRisk.sort(Comparator.comparingDouble(StudentPerformance::getGpa));
        
        return atRisk;
    }
    
    // Get top performing students
    public List<StudentPerformance> getTopPerformers(int limit) {
        List<StudentPerformance> performances = new ArrayList<>();
        
        for (Student student : studentManager.getAllStudents()) {
            double gpa = calculateStudentGPA(student.getStudentId());
            if (gpa > 0) {
                performances.add(new StudentPerformance(
                    student.getStudentId(),
                    student.getName(),
                    gpa
                ));
            }
        }
        
        // Sort by GPA descending (best first)
        performances.sort(Comparator.comparingDouble(StudentPerformance::getGpa).reversed());
        
        // Return top N
        return performances.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // Get class rankings
    public List<StudentPerformance> getClassRankings() {
        List<StudentPerformance> performances = new ArrayList<>();
        
        for (Student student : studentManager.getAllStudents()) {
            double gpa = calculateStudentGPA(student.getStudentId());
            if (gpa > 0) {
                performances.add(new StudentPerformance(
                    student.getStudentId(),
                    student.getName(),
                    gpa
                ));
            }
        }
        
        // Sort by GPA descending
        performances.sort(Comparator.comparingDouble(StudentPerformance::getGpa).reversed());
        
        return performances;
    }
    
    // Calculate average GPA across all students
    public double calculateAverageGPA() {
        List<Double> gpas = new ArrayList<>();
        
        for (Student student : studentManager.getAllStudents()) {
            double gpa = calculateStudentGPA(student.getStudentId());
            if (gpa > 0) {
                gpas.add(gpa);
            }
        }
        
        if (gpas.isEmpty()) {
            return 0.0;
        }
        
        return gpas.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
    
    // Get GPA distribution
    public Map<String, Integer> getGPADistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("4.0 (Excellent)", 0);
        distribution.put("3.0-3.99 (Good)", 0);
        distribution.put("2.0-2.99 (Pass)", 0);
        distribution.put("<2.0 (At Risk)", 0);
        
        for (Student student : studentManager.getAllStudents()) {
            double gpa = calculateStudentGPA(student.getStudentId());
            
            if (gpa >= 4.0) {
                distribution.put("4.0 (Excellent)", distribution.get("4.0 (Excellent)") + 1);
            } else if (gpa >= 3.0) {
                distribution.put("3.0-3.99 (Good)", distribution.get("3.0-3.99 (Good)") + 1);
            } else if (gpa >= 2.0) {
                distribution.put("2.0-2.99 (Pass)", distribution.get("2.0-2.99 (Pass)") + 1);
            } else if (gpa > 0) {
                distribution.put("<2.0 (At Risk)", distribution.get("<2.0 (At Risk)") + 1);
            }
        }
        
        return distribution;
    }
    
    // Inner class for student performance data
    public static class StudentPerformance {
        private String studentId;
        private String studentName;
        private double gpa;
        
        public StudentPerformance(String studentId, String studentName, double gpa) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.gpa = gpa;
        }
        
        public String getStudentId() {
            return studentId;
        }
        
        public String getStudentName() {
            return studentName;
        }
        
        public double getGpa() {
            return gpa;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s): %.2f", studentName, studentId, gpa);
        }
    }
}
