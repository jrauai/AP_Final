package Assignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExerciseLogController {

    @FXML
    private TextField exerciseTypeField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField caloriesField;

    @FXML
    private Label statusLabel;

    private List<ExerciseLog> exerciseLogs = new ArrayList<>();

    @FXML
    public void logExercise() {

        try{
            String exerciseType = exerciseTypeField.getText();
            double duration = Double.parseDouble(durationField.getText());
            double calories = Integer.parseInt(caloriesField.getText());
            String logID = generateLogID();
            ExerciseLog log = new ExerciseLog(logID, exerciseType, duration, calories, new Date());
            exerciseLogs.add(log);
            log.displayLogDetails();
        }catch(NumberFormatException e){
            statusLabel.setText("Please enter a valid number");
        }


    }

    private String generateLogID() {
        return "LOG" + (exerciseLogs.size() + 1);
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }
}