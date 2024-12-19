package Assignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class ProfileController {

    @FXML
    private TextField signupName;

    @FXML
    private TextField signupEmail ;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private ComboBox<String> nationalityDropdown;

    @FXML
    private Button changePictureButton;

    @FXML
    private Button updateProfileButton;

    @FXML
    private ImageView profileImageView;

    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;

    @FXML
    private DatePicker dobPicker;

    private String username; // Added declaration for username
    private String profilePicturePath;
    private final String BASE_DIRECTORY = "src/main/java/Assignment/File IO";

    @FXML
    public void initialize() {
        // Populate gender options
        genderComboBox.getItems().addAll("Male", "Female");

        // Populate nationality dropdown
        for (String country : Locale.getISOCountries()) {
            Locale locale = new Locale("", country);
            nationalityDropdown.getItems().add(locale.getDisplayCountry());
        }

        // Handle button actions
        changePictureButton.setOnAction(event -> changeProfilePicture());
        updateProfileButton.setOnAction(event -> saveProfileData());
    }

    public void setUsername(String email) {
        System.out.println("setUsername called with: " + email);
        this.username = email;
        loadProfileData(email);
    }

    private void loadProfileData(String email) {
        if (email == null || email.isEmpty()) {
            System.err.println("Email is null or empty!");
            return;
        }

        Path userDataFile = FileManager.getUserFilePath(email, "userdata.txt");
        Path profileDataFile = FileManager.getUserFilePath(email, "profile_data.txt");

        Map<String, String> userData = readFileToMap(userDataFile);
        Map<String, String> profileData = readFileToMap(profileDataFile);

        signupName.setText(userData.getOrDefault("Name", ""));
        signupEmail.setText(userData.getOrDefault("Email", ""));
        passwordField.setText(userData.getOrDefault("Password", ""));

        genderComboBox.setValue(profileData.getOrDefault("Gender", ""));
        nationalityDropdown.setValue(profileData.getOrDefault("Nationality", ""));
        heightField.setText(profileData.getOrDefault("Height", ""));
        weightField.setText(profileData.getOrDefault("Weight", ""));
        String dob = profileData.get("DOB");
        if (dob != null) {
            dobPicker.setValue(LocalDate.parse(dob));
        }
        loadProfilePicture(profileData.get("ProfilePicturePath"));
    }

    private void loadProfilePicture(String picturePath) {
        if (picturePath != null && !picturePath.isEmpty()) {
            File profilePicture = new File(picturePath);
            if (profilePicture.exists()) {
                profileImageView.setImage(new Image(profilePicture.toURI().toString()));
                profilePicturePath = picturePath;
            } else {
                System.err.println("Profile picture file not found at: " + picturePath);
            }
        }
    }

    private void saveProfileData() {
        if (username == null || username.isEmpty()) {
            System.err.println("Username is not set.");
            return;
        }

        Path userDataFile = FileManager.getUserFilePath(username, "userdata.txt");
        Path profileDataFile = FileManager.getUserFilePath(username, "profile_data.txt");

        Map<String, String> userData = new HashMap<>();
        userData.put("Name", signupName.getText());
        userData.put("Email", signupEmail.getText());
        userData.put("Password", passwordField.getText());

        Map<String, String> profileData = new HashMap<>();
        profileData.put("Gender", genderComboBox.getValue());
        profileData.put("Nationality", nationalityDropdown.getValue());
        profileData.put("Height", heightField.getText());
        profileData.put("Weight", weightField.getText());
        LocalDate dob = dobPicker.getValue();
        if (dob != null) {
            profileData.put("DOB", dob.toString());
        }
        profileData.put("ProfilePicturePath", profilePicturePath);

        writeMapToFile(userData, userDataFile);
        writeMapToFile(profileData, profileDataFile);

        System.out.println("Profile data saved successfully.");
    }

    private void changeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            profilePicturePath = selectedFile.getAbsolutePath();
            profileImageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    private Map<String, String> readFileToMap(Path filePath) {
        Map<String, String> dataMap = new HashMap<>();
        if (!Files.exists(filePath)) {
            System.err.println("File not found: " + filePath.toAbsolutePath());
            return dataMap;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    if (parts.length == 2) {
                        dataMap.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    private void writeMapToFile(Map<String, String> dataMap, Path filePath) {
        try {
            Files.createDirectories(filePath.getParent()); // Ensure directory exists
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    writer.write(entry.getKey() + ": " + entry.getValue());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
