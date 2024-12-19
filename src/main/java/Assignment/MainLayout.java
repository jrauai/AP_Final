package Assignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

//SideBar
public class MainLayout{

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
    private Button searchButton;

    @FXML
    private Button logOutButton;

    private String username;
    private String userEmail;


    private String sanitizeEmail(String email) {
        if (email == null) return "default_email";
        return email.replaceAll("[^a-zA-Z0-9]", "_");
    }

    public void setUserEmail(String email) {
        if (email == null || email.isEmpty()) {
            System.err.println("Error: User email is null or empty!");
            this.userEmail = "default_email";
        } else {
            this.userEmail = email;
        }

        try {
            FileManager.ensureUserDirectoryExists(this.userEmail);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadUserData();
        loadHomePage();
    }

    private void loadUserData() {
        if (userEmail == null || userEmail.isEmpty()) {
            System.err.println("User email is null or empty!");
            return;
        }

        String sanitizedEmail = sanitizeEmail(userEmail);
        Path userDirectory = Paths.get("src", "main", "java", "Assignment", "File IO", sanitizedEmail  + "_data");

        if (!Files.exists(userDirectory)) {
            System.err.println("User directory does not exist: " + userDirectory.toAbsolutePath());
            return;
        }

        Path registrationFile = userDirectory.resolve(sanitizedEmail  + "_registration.txt");
        try (BufferedReader reader = Files.newBufferedReader(registrationFile)) {
            String nameLine = reader.readLine();
            String emailLine = reader.readLine();
            String passwordLine = reader.readLine();

            if (nameLine != null && nameLine.startsWith("Name: ")) {
                this.username = nameLine.split(": ")[1].trim();
                System.out.println("Loaded username: " + this.username); // Debugging
            }

            // Update the HomePage with the username
            loadHomePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadHomePage() {
        HomePageController controller = loadPage("/Assignment/HomePage.fxml");
        highlightButton(homeButton);
        if (controller != null) {
            controller.setUserEmail(userEmail); // Pass the email, not username
            controller.loadFitnessGoalToHome();
            controller.loadExerciseToHome();
            controller.loadNutritionToHome();
            controller.loadUsernameToHome();
        }
    }

    @FXML
    public void loadDailyActivities() {
        DailyActivitiesController controller = loadPage("/Assignment/DailyActivities.fxml");
        highlightButton(dailyActivitiesButton);
    }

    @FXML
    public void loadExerciseLog() {
        ExerciseLogController controller = loadPage("/Assignment/ExerciseLogView.fxml");
        highlightButton(workoutButton);
    }

    @FXML
    public void loadFitnessGoalPage() {
        if (userEmail == null || userEmail.isEmpty()) {
            System.err.println("Error: User email is not initialized. Cannot load FitnessGoal page.");
            return;
        }

        FitnessGoalController controller = loadPage("/Assignment/FitnessGoal.fxml");
        highlightButton(fitnessGoalButton);

        if (controller != null) {
            String sanitizedEmail = sanitizeEmail(userEmail);
            controller.setUsername(sanitizedEmail); // Use sanitized email instead of username
        } else {
            System.err.println("Error: Failed to load FitnessGoalController.");
        }
    }





    public void loadStepsPage() {
        StepsController controller = loadPage("/Assignment/StepsPage.fxml");
        highlightButton(stepsButton);

//        if (controller != null) {
//            controller.initialize();
//        }
    }

    @FXML
    public void loadProfilePage() {
        Assignment.ProfileController controller = loadPage("/Assignment/Profile.fxml");
        highlightButton(SettingsButton);
    }

    @FXML
    public void loadNutritionPage() {
        NutritionController controller = loadPage("/Assignment/nutrition-view.fxml");
        highlightButton(nutritionButton);

        if (controller != null) {
            controller.setUsername(userEmail);
        } else {
            System.err.println("Error: Failed to load NutritionController.");
        }
    }

    @FXML
    public void loadSearchPage() {
       SearchController controller = loadPage("/Assignment/Search.fxml");
       highlightButton(searchButton);

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
        searchButton.setStyle("-fx-background-color: transparent;");
        logOutButton.setStyle("-fx-background-color: transparent;");


        selectedButton.setStyle("-fx-background-color: #d0d0d0;");

    }
}