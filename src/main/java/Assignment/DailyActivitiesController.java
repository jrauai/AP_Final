package Assignment;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

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
    public void initialize() {
        // Active Time Data
        XYChart.Series<String, Number> activeTimeSeries = new XYChart.Series<>();
        activeTimeSeries.getData().add(new XYChart.Data<>("12 am", 0.5));
        activeTimeSeries.getData().add(new XYChart.Data<>("6 am", 0.2));
        activeTimeSeries.getData().add(new XYChart.Data<>("12 pm", 3.0));
        activeTimeSeries.getData().add(new XYChart.Data<>("6 pm", 1.0));
        activeTimeChart.getData().add(activeTimeSeries);
        activeTimeChart.setLegendVisible(false);

        // Activity Calories Data
        XYChart.Series<String, Number> caloriesSeries = new XYChart.Series<>();
        caloriesSeries.getData().add(new XYChart.Data<>("12 am", 50));
        caloriesSeries.getData().add(new XYChart.Data<>("6 am", 100));
        caloriesSeries.getData().add(new XYChart.Data<>("12 pm", 500));
        caloriesSeries.getData().add(new XYChart.Data<>("6 pm", 200));
        activityCaloriesChart.getData().add(caloriesSeries);
        activityCaloriesChart.setLegendVisible(false);

        // Style customization
        customizeCharts();
    }

    private void customizeCharts() {
        activeTimeChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        activityCaloriesChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
    }
}
