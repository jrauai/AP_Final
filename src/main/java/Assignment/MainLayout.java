package Assignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

//SideBar
public class MainLayout {

    @FXML
    private StackPane mainContentPane;

    @FXML
    private Button homeButton;

    @FXML
    private Button dailyActivitiesButton;

    @FXML
    private Button stepsButton;

    @FXML
    private Button workoutButton;

    @FXML
    private Button nutritionButton;

    @FXML
    private Button fitnessGoalButton;

    @FXML
    private Button SettingsButton;

    @FXML
    private Button logOutButton;

    @FXML
    public void initialize() {
        loadHomePage();
    }

    @FXML
    public void loadHomePage() {
        HomePageController controller = loadPage("/Assignment/HomePage.fxml");
        highlightButton(homeButton);
        if (controller != null){
            controller.loadFitnessGoalToHome();
            controller.loadExerciseToHome();
            controller.loadNutritionToHome();
            controller.loadUsernameToHome();
        }
    }

    @FXML
    public void loadFitnessGoalPage() {
        FitnessGoalController controller = loadPage("/Assignment/FitnessGoal.fxml");
        highlightButton(fitnessGoalButton);
        if (controller != null){
            controller.loadFitnessGoalsFromFile();
            controller.calculateWeightProgress();
        }

    }

    @FXML
    public void loadProfilePage() {
        ProfileController controller = loadPage("/Assignment/Profile.fxml");
        highlightButton(SettingsButton);
    }

    public <T> T loadPage(String fxmlPath) {
        T controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node page = loader.load();
            mainContentPane.getChildren().setAll(page);
            controller = loader.getController();
            System.out.println("Page loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return controller;
    }

    private void highlightButton(Button selectedButton) {
        homeButton.setStyle("-fx-background-color: transparent;");
        dailyActivitiesButton.setStyle("-fx-background-color: transparent;");
        stepsButton.setStyle("-fx-background-color: transparent;");
        workoutButton.setStyle("-fx-background-color: transparent;");
        nutritionButton.setStyle("-fx-background-color: transparent;");
        fitnessGoalButton.setStyle("-fx-background-color: transparent;");
        SettingsButton.setStyle("-fx-background-color: transparent;");
        logOutButton.setStyle("-fx-background-color: transparent;");

        selectedButton.setStyle("-fx-background-color: #d0d0d0;");

    }
}