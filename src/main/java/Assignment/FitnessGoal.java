package Assignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.Scanner;

public class FitnessGoal {

    private double currentWeight;
    private double targetWeight;
    private double chest;
    private double waist;
    private double hip;
    private double calorieGoal;
    private double goalProgress;


    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public double getChest() {
        return chest;
    }

    public void setChest(double chest) {
        this.chest = chest;
    }

    public double getWaist() {
        return waist;
    }

    public void setWaist(double waist) {
        this.waist = waist;
    }

    public double getHip() {
        return hip;
    }

    public void setHip(double hip) {
        this.hip = hip;
    }

    public double getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(double calorieGoal) {
        this.calorieGoal = calorieGoal;
    }


    public double getGoalProgress() {
        return goalProgress;
    }

    public void setGoalProgress(double goalProgress) {
        this.goalProgress = goalProgress;
    }

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
    private ProgressBar goalprogressBar;

    @FXML
    private Label progressLabel;


    //Progress Bar (weight)
    public void calculateWeightProgress(){
        try {
            double currentWeight = Double.parseDouble(currentWeightField.getText().trim());
            double targetWeight = Double.parseDouble(targetWeightField.getText().trim());
            double weightToLoss = currentWeight - targetWeight;
            double progress = 0;

            if (currentWeight > targetWeight) {
                progress = 1-(Math.abs(weightToLoss)/150);
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

    @Override
    public String toString() {
        return "FitnessGoal{" +
                "goalProgress=" + goalProgress +
                ", calorieGoal=" + calorieGoal +
                ", hip=" + hip +
                ", waist=" + waist +
                ", chest=" + chest +
                ", targetWeight=" + targetWeight +
                ", currentWeight=" + currentWeight +
                '}';
    }

    @FXML
    public void saveFitnessGoalsToFile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\User\\IdeaProjects\\AP_G5\\src\\main\\java\\Assignment\\fitnessGoals.txt"))){
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
        File file = new File("C:\\Users\\User\\IdeaProjects\\AP_G5\\src\\main\\java\\Assignment\\fitnessGoals.txt");

        try (Scanner scanner = new Scanner(file)) {

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



    public void updateFitnessGoal(){
        currentWeightField.setText(Double.toString(currentWeight));
        targetWeightField.setText(Double.toString(targetWeight));
        chestField.setText(Double.toString(chest));
        waistField.setText(Double.toString(waist));
        hipField.setText(Double.toString(hip));
        targetCalorieField.setText(Double.toString(calorieGoal));
    }
}

