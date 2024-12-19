package Assignment;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class UserSearchService {

    public UserSearchDTO getUserDetails(String email) {
        String sanitizedEmail = sanitizeEmail(email);
        Path userDir = Paths.get("src", "main", "java", "Assignment", "File IO", sanitizedEmail + "_data");

        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            return null;
        }

        double steps = fetchSteps(userDir.resolve("steps.txt"));
        double calories = fetchCalories(userDir.resolve("Nutrition.txt"));
        String fitnessGoal = fetchFitnessGoal(userDir.resolve("fitnessGoals.txt"));
        String exercises = fetchExercises(userDir.resolve("exerciseLog.txt"));

        return new UserSearchDTO(email, steps, calories, fitnessGoal, exercises);
    }

    private String sanitizeEmail(String email) {
        return email.replaceAll("[^a-zA-Z0-9]", "_");
    }

    private double fetchSteps(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                String content = Files.readString(filePath).trim();
                return content.isEmpty() ? 0.0 : Double.parseDouble(content);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private double fetchCalories(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath);
                for (String line : lines) {
                    if (line.startsWith("GoalCalories:")) {
                        return Double.parseDouble(line.split(":")[1].trim());
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private String fetchFitnessGoal(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                return Files.readString(filePath).trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No fitness goal found.";
    }

    private String fetchExercises(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                return Files.readString(filePath).trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No exercises logged.";
    }
}
