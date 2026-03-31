package main;

import main.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        try {
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        } catch (Exception e) {
            System.err.println("\nFatal Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("\nThe application has encountered an error and will now exit.");
        }
    }
}
