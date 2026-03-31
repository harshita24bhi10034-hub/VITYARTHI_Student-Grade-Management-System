package main.managers;

import main.models.Student;
import main.models.Course;
import main.models.Grade;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manages file I/O operations for persisting data.
 * Handles CSV reading and writing for students, courses, and grades.
 */
public class FileManager {
    private static FileManager instance;
    private final String dataDirectory;
    private final String studentsFile;
    private final String coursesFile;
    private final String gradesFile;
    
    private FileManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.studentsFile = dataDirectory + "/students.csv";
        this.coursesFile = dataDirectory + "/courses.csv";
        this.gradesFile = dataDirectory + "/grades.csv";
        
        // Create data directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(dataDirectory));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }
    
    // Singleton pattern
    public static FileManager getInstance(String dataDirectory) {
        if (instance == null) {
            instance = new FileManager(dataDirectory);
        }
        return instance;
    }
    
    // ========== SAVE OPERATIONS ==========
    
    public boolean saveStudents(StudentManager studentManager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentsFile))) {
            // Write header
            writer.write("StudentID,Name,Email,EnrollmentDate,EnrolledCourses\n");
            
            // Write student data
            for (Student student : studentManager.getAllStudents()) {
                writer.write(student.toCSV() + "\n");
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveCourses(CourseManager courseManager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(coursesFile))) {
            // Write header
            writer.write("CourseCode,CourseName,Credits,MaxCapacity,EnrolledStudents\n");
            
            // Write course data
            for (Course course : courseManager.getAllCourses()) {
                writer.write(course.toCSV() + "\n");
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveGrades(GradeManager gradeManager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(gradesFile))) {
            // Write header
            writer.write("StudentID,CourseCode,AssessmentType,Weight,Score,DateRecorded,Remarks\n");
            
            // Write grade data
            for (Grade grade : gradeManager.getAllGrades()) {
                writer.write(grade.toCSV() + "\n");
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving grades: " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveAll(StudentManager studentManager, CourseManager courseManager, 
                          GradeManager gradeManager) {
        boolean studentsOk = saveStudents(studentManager);
        boolean coursesOk = saveCourses(courseManager);
        boolean gradesOk = saveGrades(gradeManager);
        
        return studentsOk && coursesOk && gradesOk;
    }
    
    // ========== LOAD OPERATIONS ==========
    
    public boolean loadStudents(StudentManager studentManager) {
        File file = new File(studentsFile);
        if (!file.exists()) {
            System.out.println("No students file found. Starting with empty data.");
            return true;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            int count = 0;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Student student = Student.fromCSV(line);
                    studentManager.addStudent(student);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error parsing student line: " + line);
                    System.err.println("  " + e.getMessage());
                }
            }
            
            System.out.println("Loaded " + count + " students.");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
            return false;
        }
    }
    
    public boolean loadCourses(CourseManager courseManager) {
        File file = new File(coursesFile);
        if (!file.exists()) {
            System.out.println("No courses file found. Starting with empty data.");
            return true;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            int count = 0;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Course course = Course.fromCSV(line);
                    courseManager.addCourse(course);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error parsing course line: " + line);
                    System.err.println("  " + e.getMessage());
                }
            }
            
            System.out.println("Loaded " + count + " courses.");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            return false;
        }
    }
    
    public boolean loadGrades(GradeManager gradeManager) {
        File file = new File(gradesFile);
        if (!file.exists()) {
            System.out.println("No grades file found. Starting with empty data.");
            return true;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            int count = 0;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    Grade grade = Grade.fromCSV(line);
                    gradeManager.addGrade(grade);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error parsing grade line: " + line);
                    System.err.println("  " + e.getMessage());
                }
            }
            
            System.out.println("Loaded " + count + " grades.");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error loading grades: " + e.getMessage());
            return false;
        }
    }
    
    public boolean loadAll(StudentManager studentManager, CourseManager courseManager, 
                          GradeManager gradeManager) {
        boolean studentsOk = loadStudents(studentManager);
        boolean coursesOk = loadCourses(courseManager);
        boolean gradesOk = loadGrades(gradeManager);
        
        return studentsOk && coursesOk && gradesOk;
    }
    
    // ========== UTILITY OPERATIONS ==========
    
    public boolean dataFilesExist() {
        return new File(studentsFile).exists() || 
               new File(coursesFile).exists() || 
               new File(gradesFile).exists();
    }
    
    public void createBackup() {
        String backupDir = dataDirectory + "/backup_" + System.currentTimeMillis();
        try {
            Files.createDirectories(Paths.get(backupDir));
            
            if (new File(studentsFile).exists()) {
                Files.copy(Paths.get(studentsFile), 
                          Paths.get(backupDir + "/students.csv"));
            }
            if (new File(coursesFile).exists()) {
                Files.copy(Paths.get(coursesFile), 
                          Paths.get(backupDir + "/courses.csv"));
            }
            if (new File(gradesFile).exists()) {
                Files.copy(Paths.get(gradesFile), 
                          Paths.get(backupDir + "/grades.csv"));
            }
            
            System.out.println("Backup created at: " + backupDir);
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }
    
    public String getDataDirectory() {
        return dataDirectory;
    }
}
