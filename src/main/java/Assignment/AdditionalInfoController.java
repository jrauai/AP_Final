package Assignment;

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

public class AdditionalInfoController {

    // QR Code and OTP functionality
    private String secretKey; // Secret key generated during registration
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

        // Configure QR Code display (if the secret key is set)
        displayQrCode();
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        displayQrCode();
    }

    private void displayQrCode() {
        if (secretKey == null || secretKey.isEmpty()) {
            return; // Do nothing if the secret key isn't set
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
                // Navigate to Home Page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
                Parent homePageRoot = loader.load();

                Stage homeStage = new Stage();
                homeStage.setScene(new Scene(homePageRoot, 1024, 700));
                homeStage.setTitle("Home Page");
                homeStage.show();

                // Close the current stage
                ((Stage) continueButton.getScene().getWindow()).close();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Invalid OTP. Please try again.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("OTP must be a numeric value.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Failed to load Home Page.");
        }
    }
}
