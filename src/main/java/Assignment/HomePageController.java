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
    public void setUsername(String username) {
        this.username=username;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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


    public void loadFitnessGoalToHome() {
        File file = new File("src/main/java/Assignment/File IO/" + username + "_data/fitnessGoals.txt");

        try (Scanner scanner = new Scanner(file)) {

            initialWeightHome.setText("0.0");
            currentWeightHome.setText("0.0");
            targetWeightHome.setText("0.0");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    if (parts.length > 1) {
                        if (line.contains("Initial Weight")) {
                            initialWeightHome.setText(parts[1].trim());
                        } else if (line.contains("Current Weight")) {
                            currentWeightHome.setText(parts[1].trim());
                        } else if (line.contains("Target Weight")) {
                            targetWeightHome.setText(parts[1].trim());
                        }
                    }
                }
            }
            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }

    }

    public void loadExerciseToHome() {
        File file = new File("src/main/java/Assignment/File IO/" + username + "_data/exerciseLog.txt");

        try (Scanner scanner = new Scanner(file)) {

            exerciseNumberHome.setText("0.0");
            durationHome.setText("0.0");
            exerciseCalorieHome.setText("0.0");

            while (scanner.hasNextLine()) {
                String line=scanner.nextLine();

                if (line.contains(": ")){
                    String[] parts = line.split(": ");
                    if (parts.length > 1){
                        if (line.contains("Exercise Number")) {
                            exerciseNumberHome.setText(parts[1].trim());
                        } else if (line.contains("Duration")) {
                            durationHome.setText(parts[1].trim());
                        } else if (line.contains("Exercise Calories")) {
                            exerciseCalorieHome.setText(parts[1].trim());
                        }
                    }
                }
            }

            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }

    }

    public void loadNutritionToHome() {
        File file = new File("src/main/java/Assignment/File IO/" + username + "_data/nutrition.txt");

        try (Scanner scanner = new Scanner(file)) {
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
            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }

    }

    public void loadUsernameToHome() {
        welcomeHome.setText(username);
    }


    public static void main (String[]args){
        launch(args);
    }
}
