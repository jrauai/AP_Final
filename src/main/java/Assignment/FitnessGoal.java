package Assignment;

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

}

