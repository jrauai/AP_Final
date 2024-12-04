package Assignment;

import UserInformation.User;
import UserInformation.UserStorage;
import Verification.GoogleTotpUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends Application {
    private final GoogleTotpUtil totpUtil = new GoogleTotpUtil(); // TOTP utility
    private final UserStorage userStorage = new UserStorage(); // User storage

    @Override
    public void start(Stage primaryStage) {
        HBox mainContainer = new HBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #e9ecef;");

        // Sign Up Section
        VBox signUpSection = new VBox(10);
        signUpSection.setMinWidth(300);
        signUpSection.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-background-radius: 8;");

        Label signUpTitle = new Label("Create Account");
        signUpTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        // Error message label with wrapping
        Label qrCodeMessage = new Label();
        qrCodeMessage.setStyle("-fx-text-fill: red;");
        qrCodeMessage.setWrapText(true); // Enable text wrapping

        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

        signUpButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Validation checks
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                qrCodeMessage.setText("All fields are required.");
                qrCodeMessage.setStyle("-fx-text-fill: red;");
            } else if (!password.equals(confirmPassword)) {
                qrCodeMessage.setText("Passwords do not match.");
                qrCodeMessage.setStyle("-fx-text-fill: red;");
            } else if (!isValidEmail(email)) {
                qrCodeMessage.setText("Invalid email format.");
                qrCodeMessage.setStyle("-fx-text-fill: red;");
            } else if (isEmailAlreadyRegistered(email)) {
                qrCodeMessage.setText("Email is already registered.");
                qrCodeMessage.setStyle("-fx-text-fill: red;");
            } else if (!isValidPassword(password)) {
                qrCodeMessage.setText("Password must be at least 8 characters long and contain both letters and numbers.");
                qrCodeMessage.setStyle("-fx-text-fill: red;");
            } else {
                String secretKey = totpUtil.generateSecretKey();
                String totpUri = String.format("otpauth://totp/FitnessApp:%s?secret=%s&issuer=FitnessApp", email, secretKey);

                // Save user to storage
                User newUser = new User(email, password, secretKey);
                userStorage.addUser(newUser);

                // Generate QR Code as a byte array
                byte[] qrCodeBytes = totpUtil.generateQrCode(totpUri);

                if (qrCodeBytes != null) {
                    // Convert the byte array into an Image
                    javafx.scene.image.Image qrImage = new javafx.scene.image.Image(
                            new java.io.ByteArrayInputStream(qrCodeBytes)
                    );

                    // Display the QR Code in a new ImageView
                    ImageView qrCodeView = new ImageView(qrImage);
                    qrCodeView.setFitWidth(200); // Adjust size
                    qrCodeView.setFitHeight(200);

                    // Add the QR Code to the sign-up section
                    signUpSection.getChildren().add(qrCodeView);

                    qrCodeMessage.setStyle("-fx-text-fill: green;");
                    qrCodeMessage.setText("Account created! Scan the QR Code for OTP setup.");
                } else {
                    qrCodeMessage.setStyle("-fx-text-fill: red;");
                    qrCodeMessage.setText("Failed to generate QR Code.");
                }
            }
        });

        signUpSection.getChildren().addAll(signUpTitle, nameField, emailField, passwordField, confirmPasswordField, signUpButton, qrCodeMessage);

        // Separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        // Sign In Section
        VBox signInSection = new VBox(10);
        signInSection.setMinWidth(300);
        signInSection.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-background-radius: 8;");

        Label signInTitle = new Label("Welcome Back");
        signInTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField loginEmailField = new TextField();
        loginEmailField.setPromptText("Email Address");

        PasswordField loginPasswordField = new PasswordField();
        loginPasswordField.setPromptText("Password");

        TextField otpField = new TextField();
        otpField.setPromptText("Enter OTP");

        Label signInMessage = new Label();
        signInMessage.setStyle("-fx-text-fill: red;");

        Button signInButton = new Button("Sign In");
        signInButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        signInButton.setOnAction(event -> {
            String email = loginEmailField.getText();
            String password = loginPasswordField.getText();
            String otp = otpField.getText();

            if (email.isEmpty() || password.isEmpty() || otp.isEmpty()) {
                signInMessage.setText("Email, password, and OTP are required.");
            } else {
                User user = userStorage.getUserByEmail(email);
                if (user != null && user.getPassword().equals(password)) {
                    int otpCode = Integer.parseInt(otp);
                    if (totpUtil.verifyCode(user.getSecretKey(), otpCode)) {
                        signInMessage.setStyle("-fx-text-fill: green;");
                        signInMessage.setText("Sign in successful!");
                    } else {
                        signInMessage.setStyle("-fx-text-fill: red;");
                        signInMessage.setText("Invalid OTP.");
                    }
                } else {
                    signInMessage.setStyle("-fx-text-fill: red;");
                    signInMessage.setText("Invalid credentials.");
                }
            }
        });

        signInSection.getChildren().addAll(signInTitle, loginEmailField, loginPasswordField, otpField, signInButton, signInMessage);

        mainContainer.getChildren().addAll(signUpSection, separator, signInSection);

        Scene scene = new Scene(mainContainer, 700, 400);
        primaryStage.setTitle("Fitness App Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Validate email format using regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Check if the email is already registered
    private boolean isEmailAlreadyRegistered(String email) {
        return userStorage.getUserByEmail(email) != null;
    }

    // Validate password strength
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
