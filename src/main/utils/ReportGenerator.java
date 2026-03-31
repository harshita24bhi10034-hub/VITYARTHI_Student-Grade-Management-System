package main.utils;

import main.managers.*;
import main.models.*;
import main.utils.GPACalculator.StudentPerformance;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Generates various reports for students, courses, and overall statistics.
 */
public class ReportGenerator {
    private StudentManager studentManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private GPACalculator gpaCalculator;
    private String reportsDirectory;
    
    public ReportGenerator(StudentManager studentManager, CourseManager courseManager,
                          GradeManager gradeManager, String reportsDirectory) {
        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.gradeManager = gradeManager;
        this.gpaCalculator = new GPACalculator(gradeManager, studentManager);
        this.reportsDirectory = reportsDirectory;
        
        // Create reports directory if it doesn't exist
        new File(reportsDirectory).mkdirs();
    }
    
    // ========== STUDENT REPORTS ==========
    
    public String generateStudentReportCard(String studentId) {
        Student student = studentManager.getStudent(studentId);
        if (student == null) {
            return "Student not found!";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════════════\n");
        report.append("                 STUDENT REPORT CARD\n");
        report.append("═══════════════════════════════════════════════════\n\n");
        
        report.append(String.format("Student ID:       %s\n", student.getStudentId()));
        report.append(String.format("Name:             %s\n", student.getName()));
        report.append(String.format("Email:            %s\n", student.getEmail()));
        report.append(String.format("Enrollment Date:  %s\n", student.getEnrollmentDate()));
        report.append("\n");
        
        List<String> courses = student.getEnrolledCourses();
        if (courses.isEmpty()) {
            report.append("No courses enrolled.\n");
        } else {
            report.append("COURSE PERFORMANCE:\n");
            report.append("─────────────────────────────────────────────────\n");
            
            for (String courseCode : courses) {
                Course course = courseManager.getCourse(courseCode);
                String courseName = course != null ? course.getCourseName() : "Unknown";
                
                report.append(String.format("\n%s - %s\n", courseCode, courseName));
                
                List<Grade> courseGrades = gradeManager.getGradesByStudentAndCourse(studentId, courseCode);
                
                if (courseGrades.isEmpty()) {
                    report.append("  No grades recorded.\n");
                } else {
                    for (Grade grade : courseGrades) {
                        report.append(String.format("  %-15s: %5.2f/100 (%s)\n",
                            grade.getAssessment().getType(),
                            grade.getScore(),
                            grade.getLetterGrade()));
                    }
                    
                    double finalGrade = gradeManager.calculateFinalGrade(studentId, courseCode);
                    String letterGrade = gradeManager.getFinalLetterGrade(studentId, courseCode);
                    
                    report.append(String.format("  Final Grade     : %5.2f/100 (%s)\n", 
                        finalGrade, letterGrade));
                }
            }
            
            report.append("\n─────────────────────────────────────────────────\n");
            double gpa = gpaCalculator.calculateStudentGPA(studentId);
            report.append(String.format("\nOverall GPA: %.2f / 4.0 (%s)\n", 
                gpa, gpaCalculator.getGPAClassification(gpa)));
        }
        
        report.append("\n═══════════════════════════════════════════════════\n");
        report.append(String.format("Generated: %s\n", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        
        return report.toString();
    }
    
    // ========== COURSE REPORTS ==========
    
    public String generateCourseReport(String courseCode) {
        Course course = courseManager.getCourse(courseCode);
        if (course == null) {
            return "Course not found!";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════════════\n");
        report.append("                   COURSE REPORT\n");
        report.append("═══════════════════════════════════════════════════\n\n");
        
        report.append(String.format("Course Code:      %s\n", course.getCourseCode()));
        report.append(String.format("Course Name:      %s\n", course.getCourseName()));
        report.append(String.format("Credits:          %d\n", course.getCredits()));
        report.append(String.format("Enrollment:       %d / %d\n", 
            course.getCurrentEnrollment(), course.getMaxCapacity()));
        report.append("\n");
        
        List<String> students = course.getEnrolledStudents();
        if (students.isEmpty()) {
            report.append("No students enrolled.\n");
        } else {
            report.append("STUDENT PERFORMANCE:\n");
            report.append("─────────────────────────────────────────────────\n");
            report.append(String.format("%-12s %-20s %10s %8s\n", 
                "Student ID", "Name", "Final Grade", "Letter"));
            report.append("─────────────────────────────────────────────────\n");
            
            for (String studentId : students) {
                Student student = studentManager.getStudent(studentId);
                String name = student != null ? student.getName() : "Unknown";
                
                double finalGrade = gradeManager.calculateFinalGrade(studentId, courseCode);
                String letterGrade = gradeManager.getFinalLetterGrade(studentId, courseCode);
                
                if (finalGrade >= 0) {
                    report.append(String.format("%-12s %-20s %10.2f %8s\n",
                        studentId, name, finalGrade, letterGrade));
                } else {
                    report.append(String.format("%-12s %-20s %10s %8s\n",
                        studentId, name, "N/A", "N/A"));
                }
            }
            
            report.append("\n");
            double courseAverage = gradeManager.calculateCourseAverage(courseCode);
            report.append(String.format("Course Average: %.2f\n", courseAverage));
            
            Map<String, Integer> distribution = gradeManager.getGradeDistribution(courseCode);
            report.append("\nGrade Distribution:\n");
            for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
                report.append(String.format("  %s: %d\n", entry.getKey(), entry.getValue()));
            }
        }
        
        report.append("\n═══════════════════════════════════════════════════\n");
        report.append(String.format("Generated: %s\n", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        
        return report.toString();
    }
    
    // ========== OVERALL STATISTICS REPORTS ==========
    
    public String generateOverallStatisticsReport() {
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════════════\n");
        report.append("              OVERALL STATISTICS REPORT\n");
        report.append("═══════════════════════════════════════════════════\n\n");
        
        report.append("SYSTEM OVERVIEW:\n");
        report.append(String.format("  Total Students:    %d\n", studentManager.getTotalStudents()));
        report.append(String.format("  Total Courses:     %d\n", courseManager.getTotalCourses()));
        report.append(String.format("  Total Enrollments: %d\n", courseManager.getTotalEnrollments()));
        report.append(String.format("  Total Grades:      %d\n", gradeManager.getTotalGrades()));
        report.append("\n");
        
        double avgGPA = gpaCalculator.calculateAverageGPA();
        report.append(String.format("Average GPA:         %.2f / 4.0\n\n", avgGPA));
        
        report.append("GPA DISTRIBUTION:\n");
        Map<String, Integer> gpaDistribution = gpaCalculator.getGPADistribution();
        for (Map.Entry<String, Integer> entry : gpaDistribution.entrySet()) {
            report.append(String.format("  %-20s: %d\n", entry.getKey(), entry.getValue()));
        }
        report.append("\n");
        
        List<StudentPerformance> topPerformers = gpaCalculator.getTopPerformers(5);
        if (!topPerformers.isEmpty()) {
            report.append("TOP 5 PERFORMERS:\n");
            int rank = 1;
            for (StudentPerformance sp : topPerformers) {
                report.append(String.format("  %d. %s (%.2f)\n", 
                    rank++, sp.getStudentName(), sp.getGpa()));
            }
            report.append("\n");
        }
        
        List<StudentPerformance> atRisk = gpaCalculator.getAtRiskStudents();
        if (!atRisk.isEmpty()) {
            report.append("AT-RISK STUDENTS (GPA < 2.0):\n");
            for (StudentPerformance sp : atRisk) {
                report.append(String.format("  %s: %.2f\n", 
                    sp.getStudentName(), sp.getGpa()));
            }
            report.append("\n");
        }
        
        report.append("═══════════════════════════════════════════════════\n");
        report.append(String.format("Generated: %s\n", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        
        return report.toString();
    }
    
    // ========== EXPORT TO FILE ==========
    
    public boolean exportStudentReport(String studentId) {
        Student student = studentManager.getStudent(studentId);
        if (student == null) {
            return false;
        }
        
        String filename = reportsDirectory + "/student_" + studentId + "_" + 
                         System.currentTimeMillis() + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(generateStudentReportCard(studentId));
            System.out.println("Report exported to: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }
    
    public boolean exportCourseReport(String courseCode) {
        Course course = courseManager.getCourse(courseCode);
        if (course == null) {
            return false;
        }
        
        String filename = reportsDirectory + "/course_" + courseCode + "_" + 
                         System.currentTimeMillis() + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(generateCourseReport(courseCode));
            System.out.println("Report exported to: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }
    
    public boolean exportOverallStatistics() {
        String filename = reportsDirectory + "/overall_statistics_" + 
                         System.currentTimeMillis() + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(generateOverallStatisticsReport());
            System.out.println("Report exported to: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }
}
