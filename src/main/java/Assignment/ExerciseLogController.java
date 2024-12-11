package Assignment;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExerciseLogController {

    @FXML
    private ComboBox<String> exerciseTypeField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField caloriesField;

    @FXML
    private Label statusLabel;

    private final List<ExerciseLog> exerciseLogs = new ArrayList<>();

    private final List<String> exerciseTypes = List.of(
            "Running", "Cycling", "Swimming", "Weight Lifting", "Yoga", "Pilates"
    );

    @FXML
    public void initialize() {
        // Populate the ComboBox with available exercise types
        exerciseTypeField.getItems().addAll(exerciseTypes);
    }

    @FXML
    public void logExercise() {

        try{
            String exerciseType = exerciseTypeField.getValue();
            double duration = Double.parseDouble(durationField.getText());
            double calories = Integer.parseInt(caloriesField.getText());

            String logID = generateLogID();
            ExerciseLog log = new ExerciseLog(logID, exerciseType, duration, calories, new Date());
            exerciseLogs.add(log);
            log.displayLogDetails();

            statusLabel.setText("Exercise logged successfully!");
        }catch(NumberFormatException e){
            statusLabel.setText("Please enter valid numbers for duration and calories.");
        }catch (NullPointerException e) {
            statusLabel.setText("Please select an exercise type.");
        }
    }

    private String generateLogID() {
        return "LOG" + (exerciseLogs.size() + 1);
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }
}