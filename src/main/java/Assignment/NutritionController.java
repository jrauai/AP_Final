package Assignment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class NutritionController {

    int i = 0;

    @FXML
    protected Label remainingLabel,goalCaloriesLabel, foodLabel, exerciseLabel, progressLabel;

    @FXML
    protected ProgressBar progressBar;

    @FXML
    protected ChoiceBox<String> typeChoiceBox;

    @FXML
    protected TextField foodNameText, caloriesText;

    @FXML
    protected Label breakfastTotalCalories, lunchTotalCalories, dinnerTotalCalories,snacksTotalCalories;

    @FXML
    protected ListView<String> breakfastList, breakfastCals, lunchList, lunchCals, dinnerList, dinnerCals, snacksList, snacksCals;

    private ObservableList<String> breakfast1, breakfast2, lunch1, lunch2,dinner1,dinner2,snacks1,snacks2;

    private String username;

    public void setUsername(String email) {
        if (email == null || email.isEmpty()) {
            System.err.println("Error: Email cannot be null or empty in NutritionController.");
            return;
        }
        this.username = email;
        loadOrInitializeNutrition();
    }

    private void loadOrInitializeNutrition() {
        if (username == null || username.isEmpty()) {
            System.err.println("Error: Email cannot be null or empty in NutritionController.");
            return;
        }
        try {
            FileManager.ensureUserDirectoryExists(username);
            Path filePath = FileManager.getUserFilePath(username, "Nutrition.txt");

            if (Files.exists(filePath)) {
                loadNutritionFromFile(filePath);
            } else {
                initializeNewUserFields();
            }
        } catch (IOException e) {
            System.err.println("Error ensuring user directory exists for nutrition.");
            e.printStackTrace();
        }
    }

    private void loadNutritionFromFile(Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                if (line.startsWith("Breakfast:")) {
                    populateMealData(lines, "Breakfast:", breakfast1, breakfast2);
                } else if (line.startsWith("Lunch:")) {
                    populateMealData(lines, "Lunch:", lunch1, lunch2);
                } else if (line.startsWith("Dinner:")) {
                    populateMealData(lines, "Dinner:", dinner1, dinner2);
                } else if (line.startsWith("Snack:")) {
                    populateMealData(lines, "Snack:", snacks1, snacks2);
                } else if (line.startsWith("GoalCalories:")) {
                    goalCaloriesLabel.setText(line.split(":")[1].trim());
                }
            }
            updateProgressAndLabels();

        } catch (Exception e) {
            showAlert("Error", "Failed to load nutrition data. A new session will be initialized.");
            initializeNewUserFields();
        }
    }

    private void initializeNewUserFields() {
        breakfast1.clear();
        breakfast2.clear();
        lunch1.clear();
        lunch2.clear();
        dinner1.clear();
        dinner2.clear();
        snacks1.clear();
        snacks2.clear();

        goalCaloriesLabel.setText("1500");
        remainingLabel.setText("1500");
        progressBar.setProgress(0);
        currentProgress = 0.0;
    }

    private void populateMealData(List<String> lines, String header, ObservableList<String> foodList, ObservableList<String> calorieList) {
        int startIndex = lines.indexOf(header) + 1;
        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) break;
            String[] parts = line.split(" - ");
            if (parts.length == 2) {
                foodList.add(parts[0].trim());
                calorieList.add(parts[1].replace(" calories", "").trim());
            }
        }
    }

    private double currentProgress = 0.0;

    public void initialize() {
        typeChoiceBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        typeChoiceBox.setValue("Choose");

        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Breakfast".equals(newValue))
                i = 1;
            else if ("Lunch".equals(newValue))
                i = 2;
            else if ("Dinner".equals(newValue))
                i = 3;
            else if ("Snack".equals(newValue))
                i = 4;
        });

        breakfast1 = FXCollections.observableArrayList();
        breakfastList.setItems(breakfast1);
        breakfast2 = FXCollections.observableArrayList();
        breakfastCals.setItems(breakfast2);

        lunch1 = FXCollections.observableArrayList();
        lunchList.setItems(lunch1);
        lunch2 = FXCollections.observableArrayList();
        lunchCals.setItems(lunch2);

        dinner1 = FXCollections.observableArrayList();
        dinnerList.setItems(dinner1);
        dinner2 = FXCollections.observableArrayList();
        dinnerCals.setItems(dinner2);

        snacks1 = FXCollections.observableArrayList();
        snacksList.setItems(snacks1);
        snacks2 = FXCollections.observableArrayList();
        snacksCals.setItems(snacks2);

        caloriesText.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("\\d*")) ? change : null));

        // Temporary
        goalCaloriesLabel.setText("1500");
        remainingLabel.setText(goalCaloriesLabel.getText());
        progressBar.setProgress(0);
        currentProgress = 0.0;
    }

    @FXML
    private void updateProgressBar(double targetProgress) {
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(50), e -> {
            currentProgress = Math.min(currentProgress + 0.01, targetProgress);
            progressBar.setProgress(currentProgress);
            if (currentProgress >= targetProgress) {
                timeline.stop();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void addFoodItem(ObservableList<String> foodList, ObservableList<String> calorieList, TextField foodInput, TextField calorieInput, Label totalCaloriesLabel) {
        String foodName = foodInput.getText().trim();
        String calorieValue = calorieInput.getText().trim();

        if (!foodName.isEmpty() && !calorieValue.isEmpty()) {
            try {
                Integer.parseInt(calorieValue);
                foodList.add(foodName);
                calorieList.add(calorieValue);
                foodInput.clear();
                calorieInput.clear();

                int totalCalories = calorieList.stream()
                        .mapToInt(Integer::parseInt)
                        .sum();
                totalCaloriesLabel.setText(String.valueOf(totalCalories));
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Calories must be a number.");
            }
        }
    }

    private void updateProgressAndLabels() {
        double baseCalories;
        try {
            baseCalories = Double.parseDouble(goalCaloriesLabel.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Goal calories must be a valid number.");
            return;
        }

        int totalCalories = getTotalCalories();

        double remainingCalories = baseCalories - totalCalories;
        remainingLabel.setText(String.valueOf(remainingCalories));
        foodLabel.setText(String.valueOf(totalCalories));

        double progress = Math.min(totalCalories / baseCalories, 1.0);
        updateProgressBar(progress);
        progressLabel.setText(String.format("%.2f%%", progress * 100));
    }

    private int getTotalCalories() {
        return sumCalories(breakfast2) + sumCalories(lunch2) + sumCalories(dinner2) + sumCalories(snacks2);
    }

    private int sumCalories(ObservableList<String> calorieList) {
        return calorieList.stream()
                .mapToInt(cal -> safeParseInt(cal))
                .sum();
    }

    private int safeParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void onAddFoodAction(ActionEvent event) {
        switch (i) {
            case 1:
                addFoodItem(breakfast1, breakfast2, foodNameText, caloriesText, breakfastTotalCalories);
                break;
            case 2:
                addFoodItem(lunch1, lunch2, foodNameText, caloriesText, lunchTotalCalories);
                break;
            case 3:
                addFoodItem(dinner1, dinner2, foodNameText, caloriesText, dinnerTotalCalories);
                break;
            case 4:
                addFoodItem(snacks1, snacks2, foodNameText, caloriesText, snacksTotalCalories);
                break;
        }
        updateProgressAndLabels();
    }
    @FXML
    private void saveNutritionToFile() {
        Path filePath = FileManager.getUserFilePath(username, "Nutrition.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Save goal calories
            writer.write("GoalCalories: " + goalCaloriesLabel.getText() + "\n\n");

            // Save meal data
            saveMealData(writer, "Breakfast", breakfast1, breakfast2);
            saveMealData(writer, "Lunch", lunch1, lunch2);
            saveMealData(writer, "Dinner", dinner1, dinner2);
            saveMealData(writer, "Snack", snacks1, snacks2);

            showAlert("Save Successful", "Nutrition data saved successfully.");
        } catch (IOException e) {
            showAlert("Error", "Failed to save nutrition data.");
        }
    }

    private void saveMealData(BufferedWriter writer, String mealType, ObservableList<String> foodList, ObservableList<String> calorieList) throws IOException {
        writer.write(mealType + ":\n");
        for (int i = 0; i < foodList.size(); i++) {
            writer.write(foodList.get(i) + " - " + calorieList.get(i) + " calories\n");
        }
        writer.write("\n");
    }

    @FXML
    public void onEditFoodAction(ActionEvent event) {
        ObservableList<String> foodList = getSelectedFoodList();
        ObservableList<String> calorieList = getSelectedCalorieList();
        ListView<String> foodListView = getSelectedFoodListView();

        if (foodListView != null && foodListView.getSelectionModel().getSelectedIndex() >= 0) {
            int index = foodListView.getSelectionModel().getSelectedIndex();
            String newFoodName = foodNameText.getText().trim();
            String newCalorieValue = caloriesText.getText().trim();

            if (!newFoodName.isEmpty() && !newCalorieValue.isEmpty() && newCalorieValue.matches("\\d+")) {
                foodList.set(index, newFoodName);
                calorieList.set(index, newCalorieValue);
                updateProgressAndLabels();
            } else {
                showAlert("Edit Failed", "Please provide valid inputs for food name and calories.");
            }
        } else {
            showAlert("Edit Failed", "Please select an item to edit.");
        }
    }

    @FXML
    public void onDeleteFoodAction(ActionEvent event) {
        ObservableList<String> foodList = getSelectedFoodList();
        ObservableList<String> calorieList = getSelectedCalorieList();
        ListView<String> foodListView = getSelectedFoodListView();

        if (foodListView != null && foodListView.getSelectionModel().getSelectedIndex() >= 0) {
            int index = foodListView.getSelectionModel().getSelectedIndex();
            foodList.remove(index);
            calorieList.remove(index);
            updateProgressAndLabels();
        } else {
            showAlert("Delete Failed", "Please select an item to delete.");
        }
    }

    private ObservableList<String> getSelectedFoodList() {
        switch (i) {
            case 1: return breakfast1;
            case 2: return lunch1;
            case 3: return dinner1;
            case 4: return snacks1;
            default: return null;
        }
    }

    private ObservableList<String> getSelectedCalorieList() {
        switch (i) {
            case 1: return breakfast2;
            case 2: return lunch2;
            case 3: return dinner2;
            case 4: return snacks2;
            default: return null;
        }
    }

    private ListView<String> getSelectedFoodListView() {
        switch (i) {
            case 1: return breakfastList;
            case 2: return lunchList;
            case 3: return dinnerList;
            case 4: return snacksList;
            default: return null;
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


}
