package Assignment;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class HomePageController extends Application {

    @FXML
    private StackPane mainContentPane;

    @FXML
    private Label initialWeightHome;

    @FXML
    private Label currentWeightHome;

    @FXML
    private Label targetWeightHome;

    @FXML
    private Label exerciseNumberHome;

    @FXML
    private Label durationHome;

    @FXML
    private Label exerciseCalorieHome;

    @FXML
    private Label foodCalorieHome;

    @FXML
    private Label welcomeHome;

    private String username;

    public void setUserEmail(String email) {
        if (email == null || email.isEmpty()) {
            System.err.println("Error: User email is null or empty!");
            return;
        }
        this.username = sanitizeEmail(email); // Store sanitized email
    }

    private String sanitizeEmail(String email) {
        return email.replaceAll("[^a-zA-Z0-9]", "_");
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
            AnchorPane root = loader.load();

            primaryStage.setTitle("Fitness App");
            primaryStage.setScene(new Scene(root, 1024, 700));
            primaryStage.centerOnScreen();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Path getFilePath(String filename) {
        if (username == null || username.isEmpty()) {
            System.err.println("Warning: User email not set. Using default.");
            username = "default_email";
        }

        try {
            FileManager.ensureUserDirectoryExists(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileManager.getUserFilePath(username, filename);
    }

    public void loadFitnessGoalToHome() {
        Path filePath = getFilePath("fitnessGoals.txt");
        try (Scanner scanner = new Scanner(filePath.toFile())) {

            initialWeightHome.setText("0.0");
            currentWeightHome.setText("0.0");
            targetWeightHome.setText("0.0");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    if (parts.length > 1) {
                        switch (parts[0].trim()) {
                            case "Initial Weight":
                                initialWeightHome.setText(parts[1].trim());
                                break;
                            case "Current Weight":
                                currentWeightHome.setText(parts[1].trim());
                                break;
                            case "Target Weight":
                                targetWeightHome.setText(parts[1].trim());
                                break;
                        }
                    }
                }
            }
            System.out.println("Fitness goals loaded successfully.");

        } catch (FileNotFoundException e) {
            System.err.println("Error: Fitness goals file not found at " + filePath);
        }
    }

    public void loadExerciseToHome() {
        Path filePath = getFilePath("exerciseLog.txt");
        try (Scanner scanner = new Scanner(filePath.toFile())) {

            exerciseNumberHome.setText("0.0");
            durationHome.setText("0.0");
            exerciseCalorieHome.setText("0.0");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    if (parts.length > 1) {
                        switch (parts[0].trim()) {
                            case "Exercise Number":
                                exerciseNumberHome.setText(parts[1].trim());
                                break;
                            case "Duration":
                                durationHome.setText(parts[1].trim());
                                break;
                            case "Exercise Calories":
                                exerciseCalorieHome.setText(parts[1].trim());
                                break;
                        }
                    }
                }
            }

            System.out.println("Exercise log loaded successfully.");

        } catch (FileNotFoundException e) {
            System.err.println("Error: Exercise log file not found at " + filePath);
        }
    }

    public void loadNutritionToHome() {
        Path filePath = getFilePath("Nutrition.txt");
        try (Scanner scanner = new Scanner(filePath.toFile())) {
            foodCalorieHome.setText("0.0");

            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    if (parts.length > 1) {
                        foodCalorieHome.setText(parts[1].trim());
                    }
                }
            }
            System.out.println("Nutrition data loaded successfully.");

        } catch (FileNotFoundException e) {
            System.err.println("Error: Nutrition file not found at " + filePath);
        }
    }

    public void loadUsernameToHome() {
        Path filePath = getFilePath("userdata.txt");

        try (Scanner scanner = new Scanner(filePath.toFile())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Name: ")) {
                    String extractedName = line.split(": ")[1].trim();
                    welcomeHome.setText(extractedName);
                    System.out.println("Username loaded from file: " + extractedName);
                    return;
                }
            }
            System.err.println("Warning: 'Name' field not found in userdata.txt. Defaulting to 'Guest'.");
            welcomeHome.setText("Guest");
        } catch (FileNotFoundException e) {
            System.err.println("Error: userdata.txt file not found at " + filePath);
            welcomeHome.setText("Guest");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
