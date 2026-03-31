package main.models;

/**
 * Represents an assessment type with its weight in the final grade.
 * Examples: Midterm (30%), Final (40%), Assignments (20%), Quizzes (10%)
 */
public class Assessment {
    private String type; // e.g., "Midterm", "Final", "Assignment", "Quiz"
    private double weight; // 0.0 to 1.0 (e.g., 0.3 = 30%)
    
    public Assessment(String type, double weight) {
        this.type = type;
        this.weight = Math.max(0.0, Math.min(1.0, weight)); // Clamp to 0-1
    }
    
    // Getters
    public String getType() {
        return type;
    }
    
    public double getWeight() {
        return weight;
    }
    
    // Setters
    public void setType(String type) {
        this.type = type;
    }
    
    public void setWeight(double weight) {
        this.weight = Math.max(0.0, Math.min(1.0, weight));
    }
    
    // Get weight as percentage string
    public String getWeightPercentage() {
        return String.format("%.0f%%", weight * 100);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", type, getWeightPercentage());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Assessment that = (Assessment) obj;
        return type.equals(that.type);
    }
    
    @Override
    public int hashCode() {
        return type.hashCode();
    }
    
    // Common assessment types
    public static Assessment midterm() {
        return new Assessment("Midterm", 0.30);
    }
    
    public static Assessment finalExam() {
        return new Assessment("Final", 0.40);
    }
    
    public static Assessment assignment() {
        return new Assessment("Assignment", 0.20);
    }
    
    public static Assessment quiz() {
        return new Assessment("Quiz", 0.10);
    }
    
    public static Assessment project() {
        return new Assessment("Project", 0.25);
    }
    
    public static Assessment custom(String type, double weight) {
        return new Assessment(type, weight);
    }
}
