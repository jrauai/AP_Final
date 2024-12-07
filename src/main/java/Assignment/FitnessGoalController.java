package Assignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.Scanner;

public class FitnessGoalController {

    @FXML
    private StackPane mainContentPane;

    @FXML
    private TextField currentWeightField;

    @FXML
    private TextField targetWeightField;

    @FXML
    private TextField chestField;

    @FXML
    private TextField waistField;

    @FXML
    private TextField hipField;

    @FXML
    private TextField targetCalorieField;

    @FXML
    private TextField initialWeightField;

    @FXML
    private ProgressBar goalprogressBar;

    @FXML
    private Label progressLabel;

    private String username;
    public void setUsername(String username) {
        this.username=username;
    }


    //Progress Bar (weight)
    public void calculateWeightProgress(){
        try {
            double currentWeight = Double.parseDouble(currentWeightField.getText().trim());
            double targetWeight = Double.parseDouble(targetWeightField.getText().trim());
            double initialWeight = Double.parseDouble(initialWeightField.getText().trim());
            double weightToLoss = initialWeight - targetWeight;
            double progress = 0;

            if (initialWeight == currentWeight){
                progress = 0;
            }
            else if (currentWeight > targetWeight) {
                progress = Math.abs(initialWeight-currentWeight)/weightToLoss;
            } else{
                progress=1;
            }

            progress=Math.min(1.0, Math.max(0.0, progress));
            goalprogressBar.setProgress(progress);
            progressLabel.setText(String.format("%.0f%% Completed", progress*100));

        }catch (NumberFormatException e){
            System.out.println("Invalid input: Please enter valid numbers for weights.");
        }
    }


    public void saveFitnessGoalsToFile(){
        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_Final\\src\\main\\java\\Assignment\\File IO\\"+username+"_data\\fitnessGoals.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write("Initial Weight: " + initialWeightField.getText());
            writer.newLine();
            writer.write("Current Weight: " + currentWeightField.getText());
            writer.newLine();
            writer.write("Target Weight: " + targetWeightField.getText());
            writer.newLine();
            writer.write("Chest: " + chestField.getText());
            writer.newLine();
            writer.write("Waist: " + waistField.getText());
            writer.newLine();
            writer.write("Hips: " + hipField.getText());
            writer.newLine();
            writer.write("Calories: " +targetCalorieField.getText());
            writer.newLine();
            System.out.println("Goals saved successfully.");

            calculateWeightProgress();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void loadFitnessGoalsFromFile() {
        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_Final\\src\\main\\java\\Assignment\\File IO\\"+username+"_data\\fitnessGoals.txt");

        try (Scanner scanner = new Scanner(file)) {

            initialWeightField.setText("0.0");
            currentWeightField.setText("0.0");
            targetWeightField.setText("0.0");
            chestField.setText("0.0");
            waistField.setText("0.0");
            hipField.setText("0.0");
            targetCalorieField.setText("0.0");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    if (parts.length > 1) {
                        if (line.startsWith("Initial Weight")) {
                            initialWeightField.setText(parts[1].trim());
                        } else if (line.startsWith("Current Weight")) {
                            currentWeightField.setText(parts[1].trim());
                        } else if (line.startsWith("Target Weight")) {
                            targetWeightField.setText(parts[1].trim());
                        } else if (line.startsWith("Chest")) {
                            chestField.setText(parts[1].trim());
                        } else if (line.startsWith("Waist")) {
                            waistField.setText(parts[1].trim());
                        } else if (line.startsWith("Hips")) {
                            hipField.setText(parts[1].trim());
                        } else if (line.startsWith("Calories")) {
                            targetCalorieField.setText(parts[1].trim());
                        }
                    }
                }
            }
            calculateWeightProgress();
            System.out.println("Data loaded successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }
    }


}
