package main.managers;

import main.models.Grade;
import main.models.Assessment;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all grade-related operations.
 * Uses ArrayList for grade storage with flexible filtering.
 */
public class GradeManager {
    private List<Grade> grades;
    
    public GradeManager() {
        this.grades = new ArrayList<>();
    }
    
    // Create
    public boolean addGrade(String studentId, String courseCode, Assessment assessment, 
                           double score, LocalDate dateRecorded) {
        Grade grade = new Grade(studentId, courseCode, assessment, score, dateRecorded);
        grades.add(grade);
        return true;
    }
    
    public boolean addGrade(Grade grade) {
        grades.add(grade);
        return true;
    }
    
    // Read - Get all grades
    public List<Grade> getAllGrades() {
        return new ArrayList<>(grades);
    }
    
    // Read - Filter by student
    public List<Grade> getGradesByStudent(String studentId) {
        return grades.stream()
                .filter(g -> g.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }
    
    public List<Grade> getGradesByStudentAndCourse(String studentId, String courseCode) {
        return grades.stream()
                .filter(g -> g.getStudentId().equals(studentId) && 
                            g.getCourseCode().equals(courseCode))
                .collect(Collectors.toList());
    }
    
    // Read - Filter by course
    public List<Grade> getGradesByCourse(String courseCode) {
        return grades.stream()
                .filter(g -> g.getCourseCode().equals(courseCode))
                .collect(Collectors.toList());
    }
    
    // Read - Filter by assessment type
    public List<Grade> getGradesByAssessmentType(String assessmentType) {
        return grades.stream()
                .filter(g -> g.getAssessment().getType().equalsIgnoreCase(assessmentType))
                .collect(Collectors.toList());
    }
    
    // Update
    public boolean updateGrade(String studentId, String courseCode, String assessmentType, 
                              double newScore) {
        Optional<Grade> gradeOpt = grades.stream()
                .filter(g -> g.getStudentId().equals(studentId) && 
                            g.getCourseCode().equals(courseCode) &&
                            g.getAssessment().getType().equalsIgnoreCase(assessmentType))
                .findFirst();
        
        if (gradeOpt.isPresent()) {
            gradeOpt.get().setScore(newScore);
            return true;
        }
        return false;
    }
    
    // Delete
    public boolean removeGrade(String studentId, String courseCode, String assessmentType) {
        return grades.removeIf(g -> 
            g.getStudentId().equals(studentId) && 
            g.getCourseCode().equals(courseCode) &&
            g.getAssessment().getType().equalsIgnoreCase(assessmentType)
        );
    }
    
    public boolean removeAllGradesForStudent(String studentId) {
        return grades.removeIf(g -> g.getStudentId().equals(studentId));
    }
    
    public boolean removeAllGradesForCourse(String courseCode) {
        return grades.removeIf(g -> g.getCourseCode().equals(courseCode));
    }
    
    // Calculate final grade for a student in a course (weighted average)
    public double calculateFinalGrade(String studentId, String courseCode) {
        List<Grade> courseGrades = getGradesByStudentAndCourse(studentId, courseCode);
        
        if (courseGrades.isEmpty()) {
            return -1.0; // No grades found
        }
        
        double totalWeightedScore = courseGrades.stream()
                .mapToDouble(Grade::getWeightedScore)
                .sum();
        
        double totalWeight = courseGrades.stream()
                .mapToDouble(g -> g.getAssessment().getWeight())
                .sum();
        
        if (totalWeight == 0) {
            return 0.0;
        }
        
        // Normalize to 100 if weights don't sum to 1.0
        return (totalWeightedScore / totalWeight) * 100;
    }
    
    // Get letter grade for final score
    public String getFinalLetterGrade(String studentId, String courseCode) {
        double finalScore = calculateFinalGrade(studentId, courseCode);
        if (finalScore < 0) return "N/A";
        
        if (finalScore >= 90) return "A";
        else if (finalScore >= 80) return "B";
        else if (finalScore >= 70) return "C";
        else if (finalScore >= 60) return "D";
        else return "F";
    }
    
    // Get grade point for GPA calculation
    public double getGradePoint(String letterGrade) {
        switch (letterGrade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
    
    // Calculate student GPA across all courses
    public double calculateStudentGPA(String studentId, List<String> courseCodes) {
        if (courseCodes.isEmpty()) {
            return 0.0;
        }
        
        double totalGradePoints = 0.0;
        int courseCount = 0;
        
        for (String courseCode : courseCodes) {
            String letterGrade = getFinalLetterGrade(studentId, courseCode);
            if (!letterGrade.equals("N/A")) {
                totalGradePoints += getGradePoint(letterGrade);
                courseCount++;
            }
        }
        
        return courseCount > 0 ? totalGradePoints / courseCount : 0.0;
    }
    
    // Calculate course average
    public double calculateCourseAverage(String courseCode) {
        List<Grade> courseGrades = getGradesByCourse(courseCode);
        
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        
        // Get unique students in the course
        Set<String> students = courseGrades.stream()
                .map(Grade::getStudentId)
                .collect(Collectors.toSet());
        
        double totalFinalGrades = 0.0;
        int studentCount = 0;
        
        for (String studentId : students) {
            double finalGrade = calculateFinalGrade(studentId, courseCode);
            if (finalGrade >= 0) {
                totalFinalGrades += finalGrade;
                studentCount++;
            }
        }
        
        return studentCount > 0 ? totalFinalGrades / studentCount : 0.0;
    }
    
    // Get grade distribution for a course
    public Map<String, Integer> getGradeDistribution(String courseCode) {
        Map<String, Integer> distribution = new TreeMap<>();
        distribution.put("A", 0);
        distribution.put("B", 0);
        distribution.put("C", 0);
        distribution.put("D", 0);
        distribution.put("F", 0);
        
        List<Grade> courseGrades = getGradesByCourse(courseCode);
        Set<String> students = courseGrades.stream()
                .map(Grade::getStudentId)
                .collect(Collectors.toSet());
        
        for (String studentId : students) {
            String letterGrade = getFinalLetterGrade(studentId, courseCode);
            if (!letterGrade.equals("N/A")) {
                distribution.put(letterGrade, distribution.get(letterGrade) + 1);
            }
        }
        
        return distribution;
    }
    
    // Statistics
    public int getTotalGrades() {
        return grades.size();
    }
    
    public double getHighestScore(String courseCode) {
        return getGradesByCourse(courseCode).stream()
                .mapToDouble(Grade::getScore)
                .max()
                .orElse(0.0);
    }
    
    public double getLowestScore(String courseCode) {
        return getGradesByCourse(courseCode).stream()
                .mapToDouble(Grade::getScore)
                .min()
                .orElse(0.0);
    }
    
    // Bulk operations
    public void clear() {
        grades.clear();
    }
    
    @Override
    public String toString() {
        return String.format("GradeManager[Total Grades: %d]", grades.size());
    }
}
