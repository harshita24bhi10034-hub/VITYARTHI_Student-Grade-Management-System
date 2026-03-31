package main.utils;

import java.util.Scanner;

/**
 * Utility class for validating user input.
 */
public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);
    
    // Validate integer input within range
    public static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    // Validate double input within range
    public static double getDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                double value = Double.parseDouble(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("Please enter a number between %.2f and %.2f.%n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    // Get non-empty string
    public static String getNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }
    
    // Get yes/no confirmation
    public static boolean getConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'.");
            }
        }
    }
    
    // Validate email format
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    // Get valid email
    public static String getValidEmail(String prompt) {
        while (true) {
            String email = getNonEmptyString(prompt);
            
            if (isValidEmail(email)) {
                return email;
            } else {
                System.out.println("Invalid email format. Please try again.");
            }
        }
    }
    
    // Validate date format (YYYY-MM-DD)
    public static boolean isValidDate(String date) {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        return date.matches(dateRegex);
    }
    
    // Get valid date
    public static String getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();
            
            if (isValidDate(date)) {
                try {
                    java.time.LocalDate.parse(date);
                    return date;
                } catch (Exception e) {
                    System.out.println("Invalid date. Please use format YYYY-MM-DD.");
                }
            } else {
                System.out.println("Invalid format. Please use YYYY-MM-DD.");
            }
        }
    }
    
    // Validate ID format (alphanumeric, no spaces)
    public static boolean isValidId(String id) {
        return id.matches("^[A-Za-z0-9]+$");
    }
    
    // Get valid ID
    public static String getValidId(String prompt) {
        while (true) {
            String id = getNonEmptyString(prompt);
            
            if (isValidId(id)) {
                return id;
            } else {
                System.out.println("Invalid ID. Use only letters and numbers (no spaces).");
            }
        }
    }
    
    // Pause and wait for user to press Enter
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // Get scanner instance
    public static Scanner getScanner() {
        return scanner;
    }
}
