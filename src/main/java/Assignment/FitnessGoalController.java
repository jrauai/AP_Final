package Assignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class FitnessGoalController {

    @FXML
    private StackPane mainContentPane;

    @FXML
    private TextField currentWeightField, targetWeightField, chestField, waistField, hipField, targetCalorieField, initialWeightField;

    @FXML
    private ProgressBar goalprogressBar;

    @FXML
    private Label progressLabel;

    private String username;

    public void setUsername(String email) {
        if (email == null || email.isEmpty()) {
            System.err.println("Error: Email cannot be null or empty in FitnessGoalController.");
            return;
        }
        this.username = email;
        loadOrInitializeFitnessGoals();
    }



    private void loadOrInitializeFitnessGoals() {
        if (username == null || username.isEmpty()) {
            System.err.println("Error: User email is not initialized. Cannot load FitnessGoal page.");
            return;

        }

        try {
            FileManager.ensureUserDirectoryExists(username);
            Path filePath = FileManager.getUserFilePath(username, "fitnessGoals.txt");

            if (Files.exists(filePath)) {
                loadFitnessGoalsFromFile(filePath);
            } else {
                initializeNewUserFields();
            }
        } catch (IOException e) {
            System.err.println("Error ensuring user directory exists for fitness goals.");
            e.printStackTrace();
        }
    }

    private void initializeUserFiles(String sanitizedEmail) throws IOException {
        Path fitnessGoalsPath = FileManager.getUserFilePath(sanitizedEmail, "fitnessGoals.txt");
        Path exerciseLogPath = FileManager.getUserFilePath(sanitizedEmail, "exerciseLog.txt");
        Path nutritionPath = FileManager.getUserFilePath(sanitizedEmail, "Nutrition.txt");

        Files.writeString(fitnessGoalsPath, "Initial Weight: 0.0\nCurrent Weight: 0.0\nTarget Weight: 0.0\n");
        Files.writeString(exerciseLogPath, "Exercise Number: 0\nDuration: 0\nExercise Calories: 0\n");
        Files.writeString(nutritionPath, "GoalCalories: 1500\n");
    }


    private void initializeNewUserFields() {
        initialWeightField.setText("");
        currentWeightField.setText("");
        targetWeightField.setText("");
        chestField.setText("");
        waistField.setText("");
        hipField.setText("");
        targetCalorieField.setText("");
        goalprogressBar.setProgress(0.0);
        progressLabel.setText("0% Completed");
    }

    private void loadFitnessGoalsFromFile(Path filePath) {
        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(": ");
                if (parts.length > 1) {
                    switch (parts[0]) {
                        case "Initial Weight": initialWeightField.setText(parts[1].trim()); break;
                        case "Current Weight": currentWeightField.setText(parts[1].trim()); break;
                        case "Target Weight": targetWeightField.setText(parts[1].trim()); break;
                        case "Chest": chestField.setText(parts[1].trim()); break;
                        case "Waist": waistField.setText(parts[1].trim()); break;
                        case "Hips": hipField.setText(parts[1].trim()); break;
                        case "Calories": targetCalorieField.setText(parts[1].trim()); break;
                    }
                }
            }
            calculateWeightProgress();
            System.out.println("Fitness goals loaded successfully.");
        } catch (IOException e) {
            System.err.println("Failed to load fitness goals.");
            e.printStackTrace();
        }
    }

    public void saveFitnessGoalsToFile() {
        Path filePath = FileManager.getUserFilePath(username, "fitnessGoals.txt");

        try {
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write("Initial Weight: " + initialWeightField.getText());
                writer.newLine();
                writer.write("Current Weight: " + currentWeightField.getText());
                writer.newLine();
                writer.write("Target Weight: " + targetWeightField.getText());
                writer.newLine();
                writer.write("Chest: " + chestField.getText());
                writer.newLine();
                writer.write("Waist: " + waistField.getText());
                writer.newLine();
                writer.write("Hips: " + hipField.getText());
                writer.newLine();
                writer.write("Calories: " + targetCalorieField.getText());
                writer.newLine();

                System.out.println("Fitness goals saved successfully.");
                calculateWeightProgress();
            }
        } catch (IOException e) {
            System.err.println("Failed to save fitness goals.");
            e.printStackTrace();
        }
    }

    public void calculateWeightProgress() {
        try {
            double initialWeight = Double.parseDouble(initialWeightField.getText().trim());
            double currentWeight = Double.parseDouble(currentWeightField.getText().trim());
            double targetWeight = Double.parseDouble(targetWeightField.getText().trim());

            double weightToLose = initialWeight - targetWeight;
            double progress = (initialWeight - currentWeight) / weightToLose;

            progress = Math.min(1.0, Math.max(0.0, progress));
            goalprogressBar.setProgress(progress);
            progressLabel.setText(String.format("%.0f%% Completed", progress * 100));
        } catch (NumberFormatException e) {
            goalprogressBar.setProgress(0.0);
            progressLabel.setText("0% Completed");
            System.out.println("Invalid input: Please enter valid numbers for weights.");
        }
    }
}
