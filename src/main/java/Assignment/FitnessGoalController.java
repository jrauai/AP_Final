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
    @FXML
    public void saveFitnessGoalsToFile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\User\\IdeaProjects\\AP_G5\\src\\main\\java\\Assignment\\fitnessGoals.txt"))){
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
        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_Final\\src\\main\\java\\Assignment\\File IO\\fitnessGoals.txt");

        try (Scanner scanner = new Scanner(file)) {

            if(scanner.hasNext()){
                initialWeightField.setText(scanner.nextLine().split(": ")[1]);
            }

            if (scanner.hasNextLine()) {
                currentWeightField.setText(scanner.nextLine().split(": ")[1]);
            }
            if (scanner.hasNextLine()) {
                targetWeightField.setText(scanner.nextLine().split(": ")[1]);
            }
            if (scanner.hasNextLine()) {
                chestField.setText(scanner.nextLine().split(": ")[1]);
            }
            if (scanner.hasNextLine()) {
                waistField.setText(scanner.nextLine().split(": ")[1]);
            }
            if (scanner.hasNextLine()) {
                hipField.setText(scanner.nextLine().split(": ")[1]);
            }
            if (scanner.hasNextLine()) {
                targetCalorieField.setText(scanner.nextLine().split(": ")[1]);
            }

            System.out.println("Data loaded successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: Fitness goals file not found.");
        }
    }
}
