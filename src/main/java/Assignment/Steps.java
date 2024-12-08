package Assignment;

public class Steps {

    private String week;
    private int stepCount;
    private double distance;
    private double calories;
    private int floors;

    public Steps(String week, int stepCount, double distance, double calories, int floors) {
        this.week = week;
        this.stepCount = stepCount;
        this.distance = distance;
        this.calories = calories;
        this.floors = floors;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }
}
