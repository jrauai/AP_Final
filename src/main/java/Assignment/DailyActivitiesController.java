package Assignment;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import java.util.List;

public class DailyActivitiesController {

    @FXML
    private BarChart<String, Number> activeTimeChart;

    @FXML
    private BarChart<String, Number> activityCaloriesChart;

    @FXML
    private CategoryAxis timeXAxis;

    @FXML
    private NumberAxis timeYAxis;

    @FXML
    private CategoryAxis caloriesXAxis;

    @FXML
    private NumberAxis caloriesYAxis;

    @FXML
    private ComboBox<String> timeFilterComboBox;

    @FXML
    private Label dailySummaryLabel;

    private final ExerciseLogController exerciseLogController = new ExerciseLogController();

    @FXML
    public void initialize() {
        // Populate time filter options
        timeFilterComboBox.getItems().addAll("Today", "This Week", "This Month");
        timeFilterComboBox.setValue("Today");

        timeFilterComboBox.setOnAction(event -> updateCharts());

        // Load initial data
        updateCharts();
    }

    private void updateCharts() {
        String selectedFilter = timeFilterComboBox.getValue();

        // Clear existing data
        activeTimeChart.getData().clear();
        activityCaloriesChart.getData().clear();

        // Retrieve exercise logs based on the selected filter
        List<ExerciseLog> filteredLogs = exerciseLogController.getExerciseLogsByFilter(selectedFilter);

        XYChart.Series<String, Number> activeTimeSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> caloriesSeries = new XYChart.Series<>();

        double totalActiveTime = 0;
        double totalCaloriesBurned = 0;

        for (ExerciseLog log : filteredLogs) {
            activeTimeSeries.getData().add(new XYChart.Data<>(log.getExerciseType(), log.getDuration()));
            caloriesSeries.getData().add(new XYChart.Data<>(log.getExerciseType(), log.getCaloriesBurned()));
            totalActiveTime += log.getDuration();
            totalCaloriesBurned += log.getCaloriesBurned();
        }

        activeTimeChart.getData().add(activeTimeSeries);
        activityCaloriesChart.getData().add(caloriesSeries);

        // Update daily summary
        dailySummaryLabel.setText(String.format("Total Active Time: %.1f minutes\nTotal Calories Burned: %.1f", totalActiveTime, totalCaloriesBurned));
    }

    private void customizeCharts() {
        activeTimeChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        activityCaloriesChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
    }
}
