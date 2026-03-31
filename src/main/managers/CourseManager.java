package main.managers;

import main.models.Course;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all course-related operations.
 * Uses HashMap for efficient course lookup by code.
 */
public class CourseManager {
    private Map<String, Course> courses; // Key: courseCode
    
    public CourseManager() {
        this.courses = new HashMap<>();
    }
    
    // Create
    public boolean addCourse(String courseCode, String courseName, int credits, int maxCapacity) {
        if (courses.containsKey(courseCode)) {
            return false; // Course already exists
        }
        Course course = new Course(courseCode, courseName, credits, maxCapacity);
        courses.put(courseCode, course);
        return true;
    }
    
    public boolean addCourse(Course course) {
        if (courses.containsKey(course.getCourseCode())) {
            return false;
        }
        courses.put(course.getCourseCode(), course);
        return true;
    }
    
    // Read
    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    public List<Course> getCoursesSortedByCode() {
        return courses.values().stream()
                .sorted(Comparator.comparing(Course::getCourseCode))
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesSortedByName() {
        return courses.values().stream()
                .sorted(Comparator.comparing(Course::getCourseName))
                .collect(Collectors.toList());
    }
    
    public boolean courseExists(String courseCode) {
        return courses.containsKey(courseCode);
    }
    
    // Update
    public boolean updateCourse(String courseCode, String courseName, int credits, int maxCapacity) {
        Course course = courses.get(courseCode);
        if (course == null) {
            return false;
        }
        course.setCourseName(courseName);
        course.setCredits(credits);
        course.setMaxCapacity(maxCapacity);
        return true;
    }
    
    // Delete
    public boolean removeCourse(String courseCode) {
        return courses.remove(courseCode) != null;
    }
    
    // Enrollment management
    public boolean enrollStudent(String courseCode, String studentId) {
        Course course = courses.get(courseCode);
        if (course == null) {
            return false;
        }
        return course.addStudent(studentId);
    }
    
    public boolean dropStudent(String courseCode, String studentId) {
        Course course = courses.get(courseCode);
        if (course == null) {
            return false;
        }
        course.removeStudent(studentId);
        return true;
    }
    
    public List<String> getCourseStudents(String courseCode) {
        Course course = courses.get(courseCode);
        return course != null ? course.getEnrolledStudents() : new ArrayList<>();
    }
    
    public boolean isCourseFull(String courseCode) {
        Course course = courses.get(courseCode);
        return course != null && course.isFull();
    }
    
    // Search operations
    public List<Course> searchByName(String nameQuery) {
        String query = nameQuery.toLowerCase();
        return courses.values().stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }
    
    public List<Course> searchByCode(String codeQuery) {
        String query = codeQuery.toUpperCase();
        return courses.values().stream()
                .filter(c -> c.getCourseCode().toUpperCase().contains(query))
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByCredits(int credits) {
        return courses.values().stream()
                .filter(c -> c.getCredits() == credits)
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesWithAvailableSeats() {
        return courses.values().stream()
                .filter(c -> !c.isFull())
                .collect(Collectors.toList());
    }
    
    // Statistics
    public int getTotalCourses() {
        return courses.size();
    }
    
    public int getTotalEnrollments() {
        return courses.values().stream()
                .mapToInt(Course::getCurrentEnrollment)
                .sum();
    }
    
    public double getAverageEnrollmentPerCourse() {
        if (courses.isEmpty()) return 0.0;
        return (double) getTotalEnrollments() / courses.size();
    }
    
    // Bulk operations
    public void clear() {
        courses.clear();
    }
    
    public Map<String, Course> getCoursesMap() {
        return courses;
    }
    
    @Override
    public String toString() {
        return String.format("CourseManager[Total Courses: %d, Total Enrollments: %d]", 
                           courses.size(), getTotalEnrollments());
    }
}
