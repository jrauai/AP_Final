package Assignment;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyActivitiesController {

    @FXML
    private BarChart<String, Number> activeTimeChart;

    @FXML
    private BarChart<String, Number> activityCaloriesChart;

    @FXML
    private ComboBox<String> timeFilterComboBox;

    @FXML
    private Label dailySummaryLabel;

    private static final String CSV_FILE_PATH = "src/main/resources/dailyActivity_merged.csv";

    @FXML
    public void initialize() {
        timeFilterComboBox.getItems().addAll("Today", "This Week", "This Month");
        timeFilterComboBox.setValue("Today");
        timeFilterComboBox.setOnAction(event -> updateCharts());
        updateCharts();
    }

    private void updateCharts() {
        List<ExerciseLog> logs = readLogsFromCSV(CSV_FILE_PATH);
        populateCharts(logs);
    }

    private List<ExerciseLog> readLogsFromCSV(String filePath) {
        List<ExerciseLog> logs = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;
            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false; // Skip the header row
                    continue;
                }
                String[] values = line.split(","); // Adjust if necessary
                if (values.length >= 15) {
                    String activityDate = values[1];
                    int totalSteps = Integer.parseInt(values[2]);
                    double totalDistance = Double.parseDouble(values[3]);
                    int veryActiveMinutes = Integer.parseInt(values[10]);
                    int calories = Integer.parseInt(values[14]);

                    // Convert ActivityDate to timestamp
                    Date date = dateFormat.parse(activityDate);
                    logs.add(new ExerciseLog(activityDate, totalSteps, totalDistance, veryActiveMinutes, calories));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }



    private void populateCharts(List<ExerciseLog> logs) {
        activeTimeChart.getData().clear();
        activityCaloriesChart.getData().clear();

        XYChart.Series<String, Number> activeTimeSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> caloriesSeries = new XYChart.Series<>();

        double totalDuration = 0;
        double totalCalories = 0;

        for (ExerciseLog log : logs) {
            activeTimeSeries.getData().add(new XYChart.Data<>(log.getExerciseType(), log.getDuration()));
            caloriesSeries.getData().add(new XYChart.Data<>(log.getExerciseType(), log.getCalories()));
            totalDuration += log.getDuration();
            totalCalories += log.getCalories();
        }

        activeTimeChart.getData().add(activeTimeSeries);
        activityCaloriesChart.getData().add(caloriesSeries);

        dailySummaryLabel.setText(String.format("Total Duration: %.2f mins | Total Calories: %.2f", totalDuration, totalCalories));
    }

    public static class ExerciseLog {
        private final String exerciseType;
        private final double duration;
        private final double calories;
        private final long timestamp;

        public ExerciseLog(String exerciseType, double duration, double calories, long timestamp, int i) {
            this.exerciseType = exerciseType;
            this.duration = duration;
            this.calories = calories;
            this.timestamp = timestamp;
        }

        public String getExerciseType() {
            return exerciseType;
        }

        public double getDuration() {
            return duration;
        }

        public double getCalories() {
            return calories;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
