package Assignment;

public class UserSearchDTO {
    private String username;
    private double steps;
    private double calories;
    private String fitnessGoal;
    private String exercises;

    public UserSearchDTO(String username, double steps, double calories, String fitnessGoal, String exercises) {
        this.username = username;
        this.steps = steps;
        this.calories = calories;
        this.fitnessGoal = fitnessGoal;
        this.exercises = exercises;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(double steps) {
        this.steps = steps;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }
}
