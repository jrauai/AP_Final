package Assignment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NutritionController {

    int i=0;

    @FXML
    protected Label remainingLabel;
    @FXML
    protected Label goalCaloriesLabel;
    @FXML
    protected Label foodLabel;
    @FXML
    protected Label exerciseLabel;
    @FXML
    protected ProgressBar progressBar;
    @FXML
    protected Label progressLabel;

    @FXML
    protected ChoiceBox<String> typeChoiceBox;
    @FXML
    protected TextField foodNameText;
    @FXML
    protected TextField caloriesText;

    @FXML
    protected Label breakfastTotalCalories;
    @FXML
    protected Label lunchTotalCalories;
    @FXML
    protected Label dinnerTotalCalories;
    @FXML
    protected Label snacksTotalCalories;
    @FXML protected ListView<String> breakfastList;@FXML protected ListView<String> breakfastCals;
    @FXML protected ListView<String> lunchList;@FXML protected ListView<String> lunchCals;
    @FXML protected ListView<String> dinnerList;@FXML protected ListView<String> dinnerCals;
    @FXML protected ListView<String> snacksList;@FXML protected ListView<String> snacksCals;

    private ObservableList<String> breakfast1;
    private ObservableList<String> breakfast2;
    private ObservableList<String> lunch1;
    private ObservableList<String> lunch2;
    private ObservableList<String> dinner1;
    private ObservableList<String> dinner2;
    private ObservableList<String> snacks1;
    private ObservableList<String> snacks2;

    private double currentProgress = 0.0;

    public void initialize() {
        typeChoiceBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        typeChoiceBox.setValue("Choose");

        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue=="Breakfast")
                i=1;
            else if (newValue=="Lunch")
                i=2;
            else if (newValue=="Dinner")
                i=3;
            else if (newValue=="Snack")
                i=4;
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

        //temporary
        goalCaloriesLabel.setText("1500");
        remainingLabel.setText(goalCaloriesLabel.getText());
        progressBar.setProgress(0);
    }

    @FXML
    private void updateProgressBar(double targetProgress) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (currentProgress < targetProgress) {
                    currentProgress += 0.01; // Increment the progress
                    Platform.runLater(() -> progressBar.setProgress(currentProgress));
                    Thread.sleep(100); // Control the speed of the animation
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    public void onAddFoodAction(ActionEvent event) {

        int cal = Integer.parseInt(caloriesText.getText());
        double base = Double.parseDouble(goalCaloriesLabel.getText());
        double food=0;
        int bTotal = 0, lTotal = 0, dTotal = 0, sTotal = 0;

        if (cal > 0) {
            switch (i){
                case 1:
                    String input1 = foodNameText.getText().trim();
                    if (!input1.isEmpty()) {
                        breakfast1.add(input1);
                        foodNameText.clear();
                    }

                    String input2 = caloriesText.getText().trim();
                    if (!input2.isEmpty()) {
                        breakfast2.add(input2);
                        caloriesText.clear();
                    }

                    for (String calorie : breakfast2) {
                        bTotal += Integer.parseInt(calorie);
                    }
                    breakfastTotalCalories.setText(String.valueOf(bTotal));
                    break;
                case 2:
                    String input3 = foodNameText.getText().trim();
                    if (!input3.isEmpty()) {
                        lunch1.add(input3);
                        foodNameText.clear();
                    }

                    String input4 = caloriesText.getText().trim();
                    if (!input4.isEmpty()) {
                        lunch2.add(input4);
                        caloriesText.clear();
                    }

                    for (String calorie : lunch2) {
                        lTotal += Integer.parseInt(calorie);
                    }
                    lunchTotalCalories.setText(String.valueOf(lTotal));
                    break;
                case 3:
                    String input5 = foodNameText.getText().trim();
                    if (!input5.isEmpty()) {
                        dinner1.add(input5);
                        foodNameText.clear();
                    }

                    String input6 = caloriesText.getText().trim();
                    if (!input6.isEmpty()) {
                        dinner2.add(input6);
                        caloriesText.clear();
                    }

                    for (String calorie : dinner2) {
                        dTotal += Integer.parseInt(calorie);
                    }
                    dinnerTotalCalories.setText(String.valueOf(dTotal));
                    break;
                case 4:
                    String input7 = foodNameText.getText().trim();
                    if (!input7.isEmpty()) {
                        snacks1.add(input7);
                        foodNameText.clear();
                    }

                    String input8 = caloriesText.getText().trim();
                    if (!input8.isEmpty()) {
                        snacks2.add(input8);
                        caloriesText.clear();
                    }

                    for (String calorie : snacks2) {
                        sTotal += Integer.parseInt(calorie);
                    }
                    snacksTotalCalories.setText(String.valueOf(sTotal));
                    break;
            }
        }

        double remaining = base - bTotal - lTotal - dTotal - sTotal;
        food = bTotal + lTotal + dTotal + sTotal;
        foodLabel.setText(String.valueOf(food));
        remainingLabel.setText(String.valueOf(remaining));

        double progress = food/base;
        if (progress > 1)
            progress = 1;
        updateProgressBar(progress);
        progressLabel.setText((progress*100) + "%");

    }

}
