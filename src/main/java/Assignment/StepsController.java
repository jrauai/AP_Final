package Assignment;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.util.List;

public class StepsController {

    @FXML
    private Label totalStepsLabel;

    @FXML
    private TextField targetStepsField;

    @FXML
    private Label targetStepsLabel;

    @FXML
    private Label distanceLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private Label floorsLabel;

    @FXML
    private BarChart<String, Number> stepsBarChart;

    @FXML
    private AnchorPane stepsPane;

//    private FitnessServiceHelper fitnessServiceHelper;
//
//    public void initialize() {
//        fitnessServiceHelper = new FitnessServiceHelper();
//        try {
//            fitnessServiceHelper.initializeFitnessService(); // Initialize fitnessService
//            fetchAndDisplayData();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Error initializing FitnessServiceHelper: " + e.getMessage());
//        }
//    }


//    private void fetchAndDisplayData() {
//        try {
//            // Fetch step data from Google Fitness API
//            int totalSteps = fitnessServiceHelper.getTotalSteps();
//            double distance = fitnessServiceHelper.getTotalDistance();
//            double calories = fitnessServiceHelper.getTotalCalories();
//            int floors = fitnessServiceHelper.getTotalFloors();
//
//            // Update UI labels
//            totalStepsLabel.setText(String.valueOf(totalSteps));
//            distanceLabel.setText(String.format("%.2f km", distance));
//            caloriesLabel.setText(String.format("%.2f kcal", calories));
//            floorsLabel.setText(String.valueOf(floors));
//
//            // Fetch and display weekly steps data
//            List<Steps> weeklySteps = fitnessServiceHelper.getWeeklySteps();
//            updateBarChart(weeklySteps);
//
//            // Handle target steps
//            targetStepsLabel.setText("Target: " + targetStepsField.getText() + " steps");
//            targetStepsField.setOnAction(event -> {
//                String target = targetStepsField.getText();
//                targetStepsLabel.setText("Target: " + target + " steps");
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void updateBarChart(List<Steps> weeklySteps) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Steps by Day");

        for (Steps step : weeklySteps) {
            series.getData().add(new XYChart.Data<>(step.getWeek(), step.getStepCount()));
        }

        stepsBarChart.getData().clear();
        stepsBarChart.getData().add(series);
    }
}