package main.models;

import java.time.LocalDate;

/**
 * Represents a grade record for a student in a course.
 * Links students to courses with assessment scores.
 */
public class Grade {
    private String studentId;
    private String courseCode;
    private Assessment assessment;
    private double score; // 0-100
    private LocalDate dateRecorded;
    private String remarks;
    
    public Grade(String studentId, String courseCode, Assessment assessment, 
                 double score, LocalDate dateRecorded) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.assessment = assessment;
        this.score = score;
        this.dateRecorded = dateRecorded;
        this.remarks = "";
    }
    
    // Constructor for CSV loading
    public Grade(String studentId, String courseCode, String assessmentType, 
                 double weight, double score, String dateRecorded, String remarks) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.assessment = new Assessment(assessmentType, weight);
        this.score = score;
        this.dateRecorded = LocalDate.parse(dateRecorded);
        this.remarks = remarks;
    }
    
    // Getters
    public String getStudentId() {
        return studentId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public Assessment getAssessment() {
        return assessment;
    }
    
    public double getScore() {
        return score;
    }
    
    public LocalDate getDateRecorded() {
        return dateRecorded;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    // Setters
    public void setScore(double score) {
        this.score = Math.max(0, Math.min(100, score)); // Clamp to 0-100
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    // Calculate weighted score contribution
    public double getWeightedScore() {
        return score * assessment.getWeight();
    }
    
    // Get letter grade for this score
    public String getLetterGrade() {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }
    
    // Get grade point for GPA calculation (4.0 scale)
    public double getGradePoint() {
        if (score >= 90) return 4.0;
        else if (score >= 80) return 3.0;
        else if (score >= 70) return 2.0;
        else if (score >= 60) return 1.0;
        else return 0.0;
    }
    
    // CSV format: studentId,courseCode,assessmentType,weight,score,dateRecorded,remarks
    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%.2f,%s,%s",
            studentId, courseCode, assessment.getType(), assessment.getWeight(),
            score, dateRecorded, remarks.replace(",", ";")); // Escape commas in remarks
    }
    
    public static Grade fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 7);
        String remarks = parts.length > 6 ? parts[6].replace(";", ",") : "";
        
        return new Grade(
            parts[0], // studentId
            parts[1], // courseCode
            parts[2], // assessmentType
            Double.parseDouble(parts[3]), // weight
            Double.parseDouble(parts[4]), // score
            parts[5], // dateRecorded
            remarks
        );
    }
    
    @Override
    public String toString() {
        return String.format("Grade[Student=%s, Course=%s, Assessment=%s, Score=%.2f, Letter=%s]",
            studentId, courseCode, assessment.getType(), score, getLetterGrade());
    }
    
    public String getDetailedString() {
        return String.format("Student: %s | Course: %s | %s (%.0f%%) | Score: %.2f/100 | Grade: %s | Date: %s",
            studentId, courseCode, assessment.getType(), assessment.getWeight() * 100, 
            score, getLetterGrade(), dateRecorded);
    }
}
