package Assignment;

import java.util.Date;

public class ExerciseLog {
    private String logID;
    private String exerciseType;
    private double duration; // in minutes
    private double caloriesBurned; // estimated
    private Date date;

    public ExerciseLog(String logID, String exerciseType, double duration, double caloriesBurned, Date date) {
        this.logID = logID;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }

    public String getLogID() {
        return logID;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public double getDuration() {
        return duration;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public Date getDate() {
        return date;
    }

    public void logExercise(String exerciseType, double duration, double caloriesBurned) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.date = new Date(); // current date
    }

    public void updateLog(String exerciseType, double duration, double caloriesBurned) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    public void displayLogDetails() {
        System.out.println("Log ID: " + logID);
        System.out.println("Exercise Type: " + exerciseType);
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Calories Burned: " + caloriesBurned);
        System.out.println("Date: " + date);
    }
}