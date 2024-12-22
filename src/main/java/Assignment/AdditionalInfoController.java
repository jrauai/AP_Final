package Assignment;

import UserInformation.User;
import UserInformation.UserStorage;
import Verification.GoogleTotpUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class AdditionalInfoController {

    // QR Code and OTP functionality
    private final GoogleTotpUtil totpUtil = new GoogleTotpUtil();

    @FXML
    private ImageView qrImageView;

    @FXML
    private TextField otpField;

    @FXML
    private Button continueButton;

    @FXML
    private Label messageLabel;

    // Profile Information
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
    private RadioButton feetRadioButton;

    @FXML
    private RadioButton cmRadioButton;

    @FXML
    private RadioButton lbsRadioButton;

    @FXML
    private RadioButton kgRadioButton;

    private ToggleGroup heightUnitGroup;
    private ToggleGroup weightUnitGroup;

    @FXML
    public void initialize() {
        // Initialize ToggleGroups
        heightUnitGroup = new ToggleGroup();
        weightUnitGroup = new ToggleGroup();

        feetRadioButton.setToggleGroup(heightUnitGroup);
        cmRadioButton.setToggleGroup(heightUnitGroup);

        lbsRadioButton.setToggleGroup(weightUnitGroup);
        kgRadioButton.setToggleGroup(weightUnitGroup);

        // Populate nationality dropdown
        for (String country : java.util.Locale.getISOCountries()) {
            java.util.Locale locale = new java.util.Locale("", country);
            nationalityDropdown.getItems().add(locale.getDisplayCountry());
        }

        // Populate gender dropdown
        genderComboBox.getItems().addAll("Male", "Female");

    }

    private String userEmail;
    private String secretKey;

    public void setUserDetails(String email, String secretKey) {
        this.userEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
        this.secretKey = secretKey;
        displayQrCode();
    }



    private void displayQrCode() {
        if (secretKey == null || secretKey.isEmpty()) {
            return;
        }
        // Generate QR code for the TOTP
        String totpUri = String.format("otpauth://totp/FitnessApp?secret=%s&issuer=FitnessApp", secretKey);
        byte[] qrCodeBytes = totpUtil.generateQrCode(totpUri);

        if (qrCodeBytes != null) {
            Image qrImage = new Image(new ByteArrayInputStream(qrCodeBytes));
            qrImageView.setImage(qrImage);
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Failed to generate QR Code.");
        }
    }

    @FXML
    private void handleContinue() {
        String otp = otpField.getText();

        if (otp.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("OTP is required.");
            return;
        }

        try {
            int otpCode = Integer.parseInt(otp);
            if (totpUtil.verifyCode(secretKey, otpCode)) {
                // Collect user details
                String gender = genderComboBox.getValue() != null ? genderComboBox.getValue() : "";
                String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";
                String nationality = nationalityDropdown.getValue() != null ? nationalityDropdown.getValue() : "";
                String height = heightField.getText();
                String weight = weightField.getText();

                if (!height.matches("\\d+(\\.\\d+)?") || !weight.matches("\\d+(\\.\\d+)?")) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Height and weight must be valid numbers.");
                    return;
                }

                // Save profile data
                Path userDir = FileManager.getUserFilePath(userEmail, "");
                Path profileDataFile = userDir.resolve("profile_data.txt");

                String content = String.format(
                        "Gender: %s%nDate of Birth: %s%nNationality: %s%nHeight: %s%nWeight: %s",
                        gender, dob, nationality, height, weight
                );

                Files.createDirectories(userDir);
                Files.writeString(profileDataFile, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

                // Load Home Page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
                Parent homePageRoot = loader.load();

                Stage currentStage = (Stage) continueButton.getScene().getWindow();
                currentStage.close();

                Stage homeStage = new Stage();
                homeStage.setScene(new Scene(homePageRoot, 1024, 700));
                homeStage.setTitle("Home Page");
                homeStage.show();

                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Welcome to the Home Page!");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Invalid OTP. Please try again.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("OTP must be a numeric value.");
        } catch (IOException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Failed to save additional information or load Home Page.");
            e.printStackTrace();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("An unexpected error occurred.");
            e.printStackTrace();
        }
    }

}