package Assignment;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SearchController {
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> resultList;

    private final UserSearchService userSearchService = new UserSearchService();

    @FXML
    public void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            resultList.getItems().clear();
            resultList.getItems().add("Enter a valid email.");
            return;
        }

        UserSearchDTO userDTO = userSearchService.getUserDetails(query);
        if (userDTO == null) {
            resultList.getItems().clear();
            resultList.getItems().add("No user found with the provided email.");
        } else {
            resultList.getItems().clear();
            resultList.getItems().add("Username: " + userDTO.getUsername());
            resultList.getItems().add("Steps: " + userDTO.getSteps());
            resultList.getItems().add("Calories: " + userDTO.getCalories());
            resultList.getItems().add("Fitness Goal: " + userDTO.getFitnessGoal());
            resultList.getItems().add("Exercises: " + userDTO.getExercises());
        }
    }
}
