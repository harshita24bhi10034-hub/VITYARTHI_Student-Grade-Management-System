package main.ui;

import main.managers.*;
import main.models.*;
import main.utils.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Console-based user interface for the Student Grade Management System.
 */
public class ConsoleUI {
    private StudentManager studentManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private FileManager fileManager;
    private GPACalculator gpaCalculator;
    private ReportGenerator reportGenerator;
    private Scanner scanner;
    private boolean running;
    
    public ConsoleUI() {
        this.studentManager = new StudentManager();
        this.courseManager = new CourseManager();
        this.gradeManager = new GradeManager();
        this.fileManager = FileManager.getInstance("data");
        this.gpaCalculator = new GPACalculator(gradeManager, studentManager);
        this.reportGenerator = new ReportGenerator(studentManager, courseManager, gradeManager, "reports");
        this.scanner = InputValidator.getScanner();
        this.running = true;
        
        // Load existing data
        loadData();
    }
    
    private void loadData() {
        System.out.println("Loading data...");
        fileManager.loadAll(studentManager, courseManager, gradeManager);
        System.out.println();
    }
    
    private void saveData() {
        System.out.println("Saving data...");
        fileManager.saveAll(studentManager, courseManager, gradeManager);
        System.out.println("Data saved successfully!");
    }
    
    public void start() {
        displayWelcome();
        
        while (running) {
            displayMainMenu();
            int choice = InputValidator.getIntInRange("Enter your choice: ", 0, 9);
            System.out.println();
            
            switch (choice) {
                case 1:
                    studentManagementMenu();
                    break;
                case 2:
                    courseManagementMenu();
                    break;
                case 3:
                    gradeManagementMenu();
                    break;
                case 4:
                    viewReportsMenu();
                    break;
                case 5:
                    viewStatisticsMenu();
                    break;
                case 6:
                    searchMenu();
                    break;
                case 7:
                    saveData();
                    InputValidator.pause();
                    break;
                case 8:
                    loadData();
                    InputValidator.pause();
                    break;
                case 9:
                    aboutSystem();
                    break;
                case 0:
                    exitSystem();
                    break;
            }
        }
    }
    
    private void displayWelcome() {
        clearScreen();
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║                                                   ║");
        System.out.println("║     STUDENT GRADE MANAGEMENT SYSTEM               ║");
        System.out.println("║                                                   ║");
        System.out.println("║     Simplifying Academic Performance Tracking    ║");
        System.out.println("║                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void displayMainMenu() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║                   MAIN MENU                       ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║  1. Student Management                            ║");
        System.out.println("║  2. Course Management                             ║");
        System.out.println("║  3. Grade Management                              ║");
        System.out.println("║  4. View Reports                                  ║");
        System.out.println("║  5. View Statistics                               ║");
        System.out.println("║  6. Search                                        ║");
        System.out.println("║  7. Save Data                                     ║");
        System.out.println("║  8. Reload Data                                   ║");
        System.out.println("║  9. About System                                  ║");
        System.out.println("║  0. Exit                                          ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }
    
    // ========== STUDENT MANAGEMENT ==========
    
    private void studentManagementMenu() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║              STUDENT MANAGEMENT                   ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Add New Student                               ║");
            System.out.println("║  2. View All Students                             ║");
            System.out.println("║  3. View Student Details                          ║");
            System.out.println("║  4. Update Student Information                    ║");
            System.out.println("║  5. Remove Student                                ║");
            System.out.println("║  6. Enroll Student in Course                      ║");
            System.out.println("║  7. Drop Student from Course                      ║");
            System.out.println("║  0. Back to Main Menu                             ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            
            int choice = InputValidator.getIntInRange("Enter your choice: ", 0, 7);
            System.out.println();
            
            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewAllStudents(); break;
                case 3: viewStudentDetails(); break;
                case 4: updateStudent(); break;
                case 5: removeStudent(); break;
                case 6: enrollStudentInCourse(); break;
                case 7: dropStudentFromCourse(); break;
                case 0: return;
            }
        }
    }
    
    private void addStudent() {
        System.out.println("=== Add New Student ===\n");
        
        String studentId = InputValidator.getValidId("Enter Student ID: ");
        
        if (studentManager.studentExists(studentId)) {
            System.out.println("Error: Student with this ID already exists!");
            InputValidator.pause();
            return;
        }
        
        String name = InputValidator.getNonEmptyString("Enter Name: ");
        String email = InputValidator.getValidEmail("Enter Email: ");
        String dateStr = InputValidator.getValidDate("Enter Enrollment Date");
        LocalDate enrollmentDate = LocalDate.parse(dateStr);
        
        if (studentManager.addStudent(studentId, name, email, enrollmentDate)) {
            System.out.println("\n✓ Student added successfully!");
        } else {
            System.out.println("\n✗ Failed to add student.");
        }
        
        InputValidator.pause();
    }
    
    private void viewAllStudents() {
        System.out.println("=== All Students ===\n");
        
        List<Student> students = studentManager.getStudentsSortedByName();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.printf("%-12s %-25s %-30s %-12s %s%n", 
                "Student ID", "Name", "Email", "Enrolled", "Courses");
            System.out.println("─".repeat(95));
            
            for (Student student : students) {
                double gpa = gpaCalculator.calculateStudentGPA(student.getStudentId());
                System.out.printf("%-12s %-25s %-30s %-12s %d (GPA: %.2f)%n",
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getEnrollmentDate(),
                    student.getEnrolledCourses().size(),
                    gpa);
            }
            System.out.println("\nTotal Students: " + students.size());
        }
        
        InputValidator.pause();
    }
    
    private void viewStudentDetails() {
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        
        Student student = studentManager.getStudent(studentId);
        if (student == null) {
            System.out.println("Student not found!");
        } else {
            System.out.println("\n" + reportGenerator.generateStudentReportCard(studentId));
        }
        
        InputValidator.pause();
    }
    
    private void updateStudent() {
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        
        if (!studentManager.studentExists(studentId)) {
            System.out.println("Student not found!");
            InputValidator.pause();
            return;
        }
        
        Student student = studentManager.getStudent(studentId);
        System.out.println("\nCurrent Information:");
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println();
        
        String newName = InputValidator.getNonEmptyString("Enter New Name: ");
        String newEmail = InputValidator.getValidEmail("Enter New Email: ");
        
        if (studentManager.updateStudent(studentId, newName, newEmail)) {
            System.out.println("\n✓ Student updated successfully!");
        } else {
            System.out.println("\n✗ Failed to update student.");
        }
        
        InputValidator.pause();
    }
    
    private void removeStudent() {
        String studentId = InputValidator.getNonEmptyString("Enter Student ID to remove: ");
        
        if (!studentManager.studentExists(studentId)) {
            System.out.println("Student not found!");
            InputValidator.pause();
            return;
        }
        
        Student student = studentManager.getStudent(studentId);
        System.out.println("\nStudent: " + student.getName() + " (" + studentId + ")");
        
        if (InputValidator.getConfirmation("Are you sure you want to remove this student?")) {
            gradeManager.removeAllGradesForStudent(studentId);
            
            for (String courseCode : student.getEnrolledCourses()) {
                courseManager.dropStudent(courseCode, studentId);
            }
            
            if (studentManager.removeStudent(studentId)) {
                System.out.println("\n✓ Student removed successfully!");
            } else {
                System.out.println("\n✗ Failed to remove student.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
        
        InputValidator.pause();
    }
    
    private void enrollStudentInCourse() {
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        
        if (!studentManager.studentExists(studentId)) {
            System.out.println("Student not found!");
            InputValidator.pause();
            return;
        }
        
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ");
        
        if (!courseManager.courseExists(courseCode)) {
            System.out.println("Course not found!");
            InputValidator.pause();
            return;
        }
        
        Course course = courseManager.getCourse(courseCode);
        
        if (course.isFull()) {
            System.out.println("Error: Course is full!");
            InputValidator.pause();
            return;
        }
        
        studentManager.enrollStudentInCourse(studentId, courseCode);
        courseManager.enrollStudent(courseCode, studentId);
        
        System.out.println("\n✓ Student enrolled in course successfully!");
        InputValidator.pause();
    }
    
    private void dropStudentFromCourse() {
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ");
        
        studentManager.dropStudentFromCourse(studentId, courseCode);
        courseManager.dropStudent(courseCode, studentId);
        
        System.out.println("\n✓ Student dropped from course!");
        InputValidator.pause();
    }
    
    // ========== COURSE MANAGEMENT ==========
    
    private void courseManagementMenu() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║              COURSE MANAGEMENT                    ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Add New Course                                ║");
            System.out.println("║  2. View All Courses                              ║");
            System.out.println("║  3. View Course Details                           ║");
            System.out.println("║  4. Update Course Information                     ║");
            System.out.println("║  5. Remove Course                                 ║");
            System.out.println("║  0. Back to Main Menu                             ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            
            int choice = InputValidator.getIntInRange("Enter your choice: ", 0, 5);
            System.out.println();
            
            switch (choice) {
                case 1: addCourse(); break;
                case 2: viewAllCourses(); break;
                case 3: viewCourseDetails(); break;
                case 4: updateCourse(); break;
                case 5: removeCourse(); break;
                case 0: return;
            }
        }
    }
    
    private void addCourse() {
        System.out.println("=== Add New Course ===\n");
        
        String courseCode = InputValidator.getValidId("Enter Course Code: ").toUpperCase();
        
        if (courseManager.courseExists(courseCode)) {
            System.out.println("Error: Course with this code already exists!");
            InputValidator.pause();
            return;
        }
        
        String courseName = InputValidator.getNonEmptyString("Enter Course Name: ");
        int credits = InputValidator.getIntInRange("Enter Credits (1-6): ", 1, 6);
        int maxCapacity = InputValidator.getIntInRange("Enter Max Capacity (1-200): ", 1, 200);
        
        if (courseManager.addCourse(courseCode, courseName, credits, maxCapacity)) {
            System.out.println("\n✓ Course added successfully!");
        } else {
            System.out.println("\n✗ Failed to add course.");
        }
        
        InputValidator.pause();
    }
    
    private void viewAllCourses() {
        System.out.println("=== All Courses ===\n");
        
        List<Course> courses = courseManager.getCoursesSortedByCode();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.printf("%-12s %-35s %-8s %-15s %s%n", 
                "Code", "Name", "Credits", "Enrollment", "Average");
            System.out.println("─".repeat(90));
            
            for (Course course : courses) {
                double avg = gradeManager.calculateCourseAverage(course.getCourseCode());
                System.out.printf("%-12s %-35s %-8d %-15s %.2f%n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getCredits(),
                    course.getCurrentEnrollment() + "/" + course.getMaxCapacity(),
                    avg);
            }
            System.out.println("\nTotal Courses: " + courses.size());
        }
        
        InputValidator.pause();
    }
    
    private void viewCourseDetails() {
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
        
        Course course = courseManager.getCourse(courseCode);
        if (course == null) {
            System.out.println("Course not found!");
        } else {
            System.out.println("\n" + reportGenerator.generateCourseReport(courseCode));
        }
        
        InputValidator.pause();
    }
    
    private void updateCourse() {
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
        
        if (!courseManager.courseExists(courseCode)) {
            System.out.println("Course not found!");
            InputValidator.pause();
            return;
        }
        
        Course course = courseManager.getCourse(courseCode);
        System.out.println("\nCurrent Information:");
        System.out.println("Name: " + course.getCourseName());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Max Capacity: " + course.getMaxCapacity());
        System.out.println();
        
        String newName = InputValidator.getNonEmptyString("Enter New Name: ");
        int newCredits = InputValidator.getIntInRange("Enter New Credits (1-6): ", 1, 6);
        int newCapacity = InputValidator.getIntInRange("Enter New Max Capacity (1-200): ", 1, 200);
        
        if (courseManager.updateCourse(courseCode, newName, newCredits, newCapacity)) {
            System.out.println("\n✓ Course updated successfully!");
        } else {
            System.out.println("\n✗ Failed to update course.");
        }
        
        InputValidator.pause();
    }
    
    private void removeCourse() {
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code to remove: ").toUpperCase();
        
        if (!courseManager.courseExists(courseCode)) {
            System.out.println("Course not found!");
            InputValidator.pause();
            return;
        }
        
        Course course = courseManager.getCourse(courseCode);
        System.out.println("\nCourse: " + course.getCourseName() + " (" + courseCode + ")");
        System.out.println("Enrolled Students: " + course.getCurrentEnrollment());
        
        if (InputValidator.getConfirmation("Are you sure you want to remove this course?")) {
            gradeManager.removeAllGradesForCourse(courseCode);
            
            for (String studentId : course.getEnrolledStudents()) {
                studentManager.dropStudentFromCourse(studentId, courseCode);
            }
            
            if (courseManager.removeCourse(courseCode)) {
                System.out.println("\n✓ Course removed successfully!");
            } else {
                System.out.println("\n✗ Failed to remove course.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
        
        InputValidator.pause();
    }
    
    // ========== GRADE MANAGEMENT ==========
    
    private void gradeManagementMenu() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║              GRADE MANAGEMENT                     ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Add Grade                                     ║");
            System.out.println("║  2. Update Grade                                  ║");
            System.out.println("║  3. View Student Grades                           ║");
            System.out.println("║  4. View Course Grades                            ║");
            System.out.println("║  5. Remove Grade                                  ║");
            System.out.println("║  0. Back to Main Menu                             ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            
            int choice = InputValidator.getIntInRange("Enter your choice: ", 0, 5);
            System.out.println();
            
            switch (choice) {
                case 1: addGrade(); break;
                case 2: updateGrade(); break;
                case 3: viewStudentGrades(); break;
                case 4: viewCourseGrades(); break;
                case 5: removeGrade(); break;
                case 0: return;
            }
        }
    }
    
    private void addGrade() {
        System.out.println("=== Add Grade ===\n");
        
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        
        if (!studentManager.studentExists(studentId)) {
            System.out.println("Student not found!");
            InputValidator.pause();
            return;
        }
        
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
        
        if (!courseManager.courseExists(courseCode)) {
            System.out.println("Course not found!");
            InputValidator.pause();
            return;
        }
        
        System.out.println("\nAssessment Types:");
        System.out.println("1. Midterm (30%)");
        System.out.println("2. Final (40%)");
        System.out.println("3. Assignment (20%)");
        System.out.println("4. Quiz (10%)");
        System.out.println("5. Project (25%)");
        System.out.println("6. Custom");
        
        int assessmentChoice = InputValidator.getIntInRange("Select assessment type: ", 1, 6);
        
        Assessment assessment;
        switch (assessmentChoice) {
            case 1: assessment = Assessment.midterm(); break;
            case 2: assessment = Assessment.finalExam(); break;
            case 3: assessment = Assessment.assignment(); break;
            case 4: assessment = Assessment.quiz(); break;
            case 5: assessment = Assessment.project(); break;
            case 6:
                String type = InputValidator.getNonEmptyString("Enter assessment type: ");
                double weight = InputValidator.getDoubleInRange("Enter weight (0.0-1.0): ", 0.0, 1.0);
                assessment = Assessment.custom(type, weight);
                break;
            default:
                assessment = Assessment.midterm();
        }
        
        double score = InputValidator.getDoubleInRange("Enter score (0-100): ", 0.0, 100.0);
        
        if (gradeManager.addGrade(studentId, courseCode, assessment, score, LocalDate.now())) {
            System.out.println("\n✓ Grade added successfully!");
        } else {
            System.out.println("\n✗ Failed to add grade.");
        }
        
        InputValidator.pause();
    }
    
    private void updateGrade() {
        System.out.println("=== Update Grade ===\n");
        
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
        String assessmentType = InputValidator.getNonEmptyString("Enter Assessment Type: ");
        double newScore = InputValidator.getDoubleInRange("Enter new score (0-100): ", 0.0, 100.0);
        
        if (gradeManager.updateGrade(studentId, courseCode, assessmentType, newScore)) {
            System.out.println("\n✓ Grade updated successfully!");
        } else {
            System.out.println("\n✗ Grade not found.");
        }
        
        InputValidator.pause();
    }
    
    private void viewStudentGrades() {
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        
        List<Grade> grades = gradeManager.getGradesByStudent(studentId);
        
        if (grades.isEmpty()) {
            System.out.println("No grades found for this student.");
        } else {
            System.out.println("\n=== Grades for Student " + studentId + " ===\n");
            for (Grade grade : grades) {
                System.out.println(grade.getDetailedString());
            }
        }
        
        InputValidator.pause();
    }
    
    private void viewCourseGrades() {
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
        
        List<Grade> grades = gradeManager.getGradesByCourse(courseCode);
        
        if (grades.isEmpty()) {
            System.out.println("No grades found for this course.");
        } else {
            System.out.println("\n=== Grades for Course " + courseCode + " ===\n");
            for (Grade grade : grades) {
                System.out.println(grade.getDetailedString());
            }
        }
        
        InputValidator.pause();
    }
    
    private void removeGrade() {
        System.out.println("=== Remove Grade ===\n");
        
        String studentId = InputValidator.getNonEmptyString("Enter Student ID: ");
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
        String assessmentType = InputValidator.getNonEmptyString("Enter Assessment Type: ");
        
        if (InputValidator.getConfirmation("Are you sure you want to remove this grade?")) {
            if (gradeManager.removeGrade(studentId, courseCode, assessmentType)) {
                System.out.println("\n✓ Grade removed successfully!");
            } else {
                System.out.println("\n✗ Grade not found.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
        
        InputValidator.pause();
    }
    
    // ========== REPORTS ==========
    
    private void viewReportsMenu() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║                  VIEW REPORTS                     ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Student Report Card                           ║");
            System.out.println("║  2. Course Report                                 ║");
            System.out.println("║  3. Overall Statistics                            ║");
            System.out.println("║  4. Export Student Report to File                 ║");
            System.out.println("║  5. Export Course Report to File                  ║");
            System.out.println("║  6. Export Statistics to File                     ║");
            System.out.println("║  0. Back to Main Menu                             ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            
            int choice = InputValidator.getIntInRange("Enter your choice: ", 0, 6);
            System.out.println();
            
            switch (choice) {
                case 1:
                    String sid = InputValidator.getNonEmptyString("Enter Student ID: ");
                    System.out.println(reportGenerator.generateStudentReportCard(sid));
                    InputValidator.pause();
                    break;
                case 2:
                    String cid = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
                    System.out.println(reportGenerator.generateCourseReport(cid));
                    InputValidator.pause();
                    break;
                case 3:
                    System.out.println(reportGenerator.generateOverallStatisticsReport());
                    InputValidator.pause();
                    break;
                case 4:
                    sid = InputValidator.getNonEmptyString("Enter Student ID: ");
                    reportGenerator.exportStudentReport(sid);
                    InputValidator.pause();
                    break;
                case 5:
                    cid = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
                    reportGenerator.exportCourseReport(cid);
                    InputValidator.pause();
                    break;
                case 6:
                    reportGenerator.exportOverallStatistics();
                    InputValidator.pause();
                    break;
                case 0:
                    return;
            }
        }
    }
    
    // ========== STATISTICS ==========
    
    private void viewStatisticsMenu() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║                VIEW STATISTICS                    ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Top Performers                                ║");
            System.out.println("║  2. At-Risk Students                              ║");
            System.out.println("║  3. Class Rankings                                ║");
            System.out.println("║  4. GPA Distribution                              ║");
            System.out.println("║  5. Course Statistics                             ║");
            System.out.println("║  0. Back to Main Menu                             ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            
            int choice = InputValidator.getIntInRange("Enter your choice: ", 0, 5);
            System.out.println();
            
            switch (choice) {
                case 1: viewTopPerformers(); break;
                case 2: viewAtRiskStudents(); break;
                case 3: viewClassRankings(); break;
                case 4: viewGPADistribution(); break;
                case 5: viewCourseStatistics(); break;
                case 0: return;
            }
        }
    }
    
    private void viewTopPerformers() {
        int limit = InputValidator.getIntInRange("How many top performers to show? ", 1, 50);
        
        List<GPACalculator.StudentPerformance> topPerformers = gpaCalculator.getTopPerformers(limit);
        
        if (topPerformers.isEmpty()) {
            System.out.println("No student data available.");
        } else {
            System.out.println("\n=== Top " + limit + " Performers ===\n");
            System.out.printf("%-6s %-12s %-25s %8s %15s%n", 
                "Rank", "Student ID", "Name", "GPA", "Classification");
            System.out.println("─".repeat(70));
            
            int rank = 1;
            for (GPACalculator.StudentPerformance sp : topPerformers) {
                System.out.printf("%-6d %-12s %-25s %8.2f %15s%n",
                    rank++, sp.getStudentId(), sp.getStudentName(), 
                    sp.getGpa(), gpaCalculator.getGPAClassification(sp.getGpa()));
            }
        }
        
        InputValidator.pause();
    }
    
    private void viewAtRiskStudents() {
        List<GPACalculator.StudentPerformance> atRisk = gpaCalculator.getAtRiskStudents();
        
        if (atRisk.isEmpty()) {
            System.out.println("No at-risk students found. Great!");
        } else {
            System.out.println("\n=== At-Risk Students (GPA < 2.0) ===\n");
            System.out.printf("%-12s %-25s %8s%n", "Student ID", "Name", "GPA");
            System.out.println("─".repeat(50));
            
            for (GPACalculator.StudentPerformance sp : atRisk) {
                System.out.printf("%-12s %-25s %8.2f%n",
                    sp.getStudentId(), sp.getStudentName(), sp.getGpa());
            }
            System.out.println("\nTotal At-Risk: " + atRisk.size());
        }
        
        InputValidator.pause();
    }
    
    private void viewClassRankings() {
        List<GPACalculator.StudentPerformance> rankings = gpaCalculator.getClassRankings();
        
        if (rankings.isEmpty()) {
            System.out.println("No student data available.");
        } else {
            System.out.println("\n=== Class Rankings ===\n");
            System.out.printf("%-6s %-12s %-25s %8s%n", "Rank", "Student ID", "Name", "GPA");
            System.out.println("─".repeat(55));
            
            int rank = 1;
            for (GPACalculator.StudentPerformance sp : rankings) {
                System.out.printf("%-6d %-12s %-25s %8.2f%n",
                    rank++, sp.getStudentId(), sp.getStudentName(), sp.getGpa());
            }
            
            System.out.println("\nAverage GPA: " + String.format("%.2f", gpaCalculator.calculateAverageGPA()));
        }
        
        InputValidator.pause();
    }
    
    private void viewGPADistribution() {
        Map<String, Integer> distribution = gpaCalculator.getGPADistribution();
        
        System.out.println("\n=== GPA Distribution ===\n");
        
        int total = distribution.values().stream().mapToInt(Integer::intValue).sum();
        
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            int count = entry.getValue();
            double percentage = total > 0 ? (count * 100.0 / total) : 0;
            
            System.out.printf("%-20s: %3d students (%.1f%%)%n", 
                entry.getKey(), count, percentage);
        }
        
        System.out.println("\nTotal Students: " + total);
        System.out.println("Average GPA: " + String.format("%.2f", gpaCalculator.calculateAverageGPA()));
        
        InputValidator.pause();
    }
    
    private void viewCourseStatistics() {
        String courseCode = InputValidator.getNonEmptyString("Enter Course Code: ").toUpperCase();
        
        if (!courseManager.courseExists(courseCode)) {
            System.out.println("Course not found!");
            InputValidator.pause();
            return;
        }
        
        Course course = courseManager.getCourse(courseCode);
        double avg = gradeManager.calculateCourseAverage(courseCode);
        double highest = gradeManager.getHighestScore(courseCode);
        double lowest = gradeManager.getLowestScore(courseCode);
        Map<String, Integer> distribution = gradeManager.getGradeDistribution(courseCode);
        
        System.out.println("\n=== Course Statistics: " + courseCode + " ===\n");
        System.out.println("Course Name:      " + course.getCourseName());
        System.out.println("Enrollment:       " + course.getCurrentEnrollment() + "/" + course.getMaxCapacity());
        System.out.println("Average Grade:    " + String.format("%.2f", avg));
        System.out.println("Highest Score:    " + String.format("%.2f", highest));
        System.out.println("Lowest Score:     " + String.format("%.2f", lowest));
        System.out.println("\nGrade Distribution:");
        
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            System.out.printf("  %s: %d students%n", entry.getKey(), entry.getValue());
        }
        
        InputValidator.pause();
    }
    
    // ========== SEARCH ==========
    
    private void searchMenu() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════╗");
            System.out.println("║                     SEARCH                        ║");
            System.out.println("╠═══════════════════════════════════════════════════╣");
            System.out.println("║  1. Search Students by Name                       ║");
            System.out.println("║  2. Search Students by Email                      ║");
            System.out.println("║  3. Search Courses by Name                        ║");
            System.out.println("║  4. Search Courses by Code                        ║");
            System.out.println("║  0. Back to Main Menu                             ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            
            int choice = InputValidator.getIntInRange("Enter your choice: ", 0, 4);
            System.out.println();
            
            switch (choice) {
                case 1: searchStudentsByName(); break;
                case 2: searchStudentsByEmail(); break;
                case 3: searchCoursesByName(); break;
                case 4: searchCoursesByCode(); break;
                case 0: return;
            }
        }
    }
    
    private void searchStudentsByName() {
        String query = InputValidator.getNonEmptyString("Enter name to search: ");
        List<Student> results = studentManager.searchByName(query);
        
        if (results.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("\nSearch Results:\n");
            for (Student student : results) {
                System.out.println(student);
            }
            System.out.println("\nFound " + results.size() + " student(s).");
        }
        
        InputValidator.pause();
    }
    
    private void searchStudentsByEmail() {
        String query = InputValidator.getNonEmptyString("Enter email to search: ");
        List<Student> results = studentManager.searchByEmail(query);
        
        if (results.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("\nSearch Results:\n");
            for (Student student : results) {
                System.out.println(student);
            }
            System.out.println("\nFound " + results.size() + " student(s).");
        }
        
        InputValidator.pause();
    }
    
    private void searchCoursesByName() {
        String query = InputValidator.getNonEmptyString("Enter course name to search: ");
        List<Course> results = courseManager.searchByName(query);
        
        if (results.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("\nSearch Results:\n");
            for (Course course : results) {
                System.out.println(course);
            }
            System.out.println("\nFound " + results.size() + " course(s).");
        }
        
        InputValidator.pause();
    }
    
    private void searchCoursesByCode() {
        String query = InputValidator.getNonEmptyString("Enter course code to search: ");
        List<Course> results = courseManager.searchByCode(query);
        
        if (results.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("\nSearch Results:\n");
            for (Course course : results) {
                System.out.println(course);
            }
            System.out.println("\nFound " + results.size() + " course(s).");
        }
        
        InputValidator.pause();
    }
    
    // ========== SYSTEM ==========
    
    private void aboutSystem() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║         STUDENT GRADE MANAGEMENT SYSTEM           ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║  Version: 1.0.0                                   ║");
        System.out.println("║                                                   ║");
        System.out.println("║  A comprehensive solution for managing student    ║");
        System.out.println("║  grades, calculating GPAs, and generating         ║");
        System.out.println("║  performance reports.                             ║");
        System.out.println("║                                                   ║");
        System.out.println("║  Features:                                        ║");
        System.out.println("║  • Student & Course Management                    ║");
        System.out.println("║  • Grade Recording & Tracking                     ║");
        System.out.println("║  • GPA Calculation (4.0 scale)                    ║");
        System.out.println("║  • Performance Reports & Analytics                ║");
        System.out.println("║  • At-Risk Student Identification                 ║");
        System.out.println("║  • File-based Data Persistence                    ║");
        System.out.println("║                                                   ║");
        System.out.println("║  Data Storage: ./data/                            ║");
        System.out.println("║  Reports: ./reports/                              ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        
        InputValidator.pause();
    }
    
    private void exitSystem() {
        System.out.println("\nSaving data before exit...");
        saveData();
        System.out.println("\nThank you for using Student Grade Management System!");
        System.out.println("Goodbye!\n");
        running = false;
    }
    
    private void clearScreen() {
        // Simple clear - print multiple newlines
        for (int i = 0; i < 2; i++) {
            System.out.println();
        }
    }
}
