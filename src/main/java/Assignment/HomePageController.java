package Assignment;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_Final\\src\\main\\java\\Assignment\\File IO\\fitnessGoals.txt");
        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNext()) {
                initialWeightHome.setText(scanner.nextLine().split(": ")[1]);
            }

            if (scanner.hasNextLine()) {
                currentWeightHome.setText(scanner.nextLine().split(": ")[1]);
            }
            if (scanner.hasNextLine()) {
                targetWeightHome.setText(scanner.nextLine().split(": ")[1]);
            }
            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }

    }

    public void loadExerciseToHome() {

        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_Final\\src\\main\\java\\Assignment\\File IO\\exerciseHome.txt");
        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNext()) {
                exerciseNumberHome.setText(scanner.nextLine().split(": ")[1]);
            }

            if (scanner.hasNextLine()) {
                durationHome.setText(scanner.nextLine().split(": ")[1]);
            }
            if (scanner.hasNextLine()) {
                exerciseCalorieHome.setText(scanner.nextLine().split(": ")[1]);
            }
            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }

    }

    public void loadNutritionToHome() {

        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_Final\\src\\main\\java\\Assignment\\File IO\\foodHome.txt");
        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNext()) {
                foodCalorieHome.setText(scanner.nextLine().split(": ")[1]);
            }
            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }

    }

    public void loadUsernameToHome() {

        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_Final\\src\\main\\java\\Assignment\\File IO\\usernameHome.txt");
        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNext()) {
                welcomeHome.setText(scanner.nextLine().split(": ")[1]);
            }
            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }

    }


    public static void main (String[]args){
        launch(args);
    }
}
