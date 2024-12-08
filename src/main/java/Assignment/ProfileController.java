package Assignment;

import UserInformation.User;
import UserInformation.UserStorage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class ProfileController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button changePictureButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private ComboBox<String> nationalityDropdown;

    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;

    @FXML
    private Button updateProfileButton;

    private UserStorage userStorage;
    private User currentUser;

    @FXML
    public void initialize() {
        // Initialize user storage
        userStorage = new UserStorage();

        // Load the current user (for demonstration purposes, assume the first user is the current user)
        currentUser = userStorage.getUserByEmail("example@example.com"); // Replace with dynamic email retrieval

        // Populate gender options
        genderComboBox.getItems().addAll("Male", "Female");

        // Populate nationality dropdown
        for (String country : Locale.getISOCountries()) {
            Locale locale = new Locale("", country);
            nationalityDropdown.getItems().add(locale.getDisplayCountry());
        }

        // Load user data into fields
        if (currentUser != null) {
            loadUserData();
        }

        // Handle button actions
        changePictureButton.setOnAction(event -> changeProfilePicture());
        updateProfileButton.setOnAction(event -> updateUserData());
    }

    private void loadUserData() {
        nameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        genderComboBox.setValue(currentUser.getGender());
        dobPicker.setValue(java.time.LocalDate.parse(currentUser.getDateOfBirth()));
        nationalityDropdown.setValue(currentUser.getNationality());
        heightField.setText(currentUser.getHeight());
        weightField.setText(currentUser.getWeight());

        // Load profile picture if available
        File profilePicture = new File("path/to/profile/picture.jpg"); // Replace with actual path logic
        if (profilePicture.exists()) {
            profileImageView.setImage(new Image(profilePicture.toURI().toString()));
        }
    }

    private void changeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            profileImageView.setImage(new Image(selectedFile.toURI().toString()));
            // Save the selected file path (implement saving logic if necessary)
        }
    }

    private void updateUserData() {
        // Update current user fields
        currentUser.setName(nameField.getText());
        currentUser.setGender(genderComboBox.getValue());
        currentUser.setDateOfBirth(dobPicker.getValue().toString());
        currentUser.setNationality(nationalityDropdown.getValue());
        currentUser.setHeight(heightField.getText());
        currentUser.setWeight(weightField.getText());

        // Load users from storage
        UserStorage userStorage = new UserStorage();
        List<User> users = userStorage.loadUsers();

        // Debug print to confirm loaded users
        System.out.println("Loaded users from storage: ");
        for (User user : users) {
            System.out.println(user);
        }

        // Update the specific user in the list
        boolean userFound = false;
        for (int i = 0; i < users.size(); i++) {
            System.out.println("Checking user: " + users.get(i)); // Debug print
            if (users.get(i).getEmail().equals(currentUser.getEmail())) {
                System.out.println("Updating user: " + currentUser); // Debug print
                users.set(i, currentUser);
                userFound = true;
                break;
            }
        }

        if (userFound) {
            // Debug print before saving
            System.out.println("Users before saving: ");
            for (User user : users) {
                System.out.println(user); // Debug print
            }
            userStorage.saveUsers(users); // Save the updated users
            System.out.println("User data updated and saved."); // Debug print
        } else {
            System.err.println("User not found in storage.");
        }

        // Confirmation alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Updated");
        alert.setHeaderText(null);
        alert.setContentText("Your profile has been successfully updated.");
        alert.showAndWait();
    }
}
