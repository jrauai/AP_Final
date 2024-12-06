package Assignment;

import UserInformation.User;
import UserInformation.UserStorage;
import Verification.GoogleTotpUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        Label signUpMessage = new Label();
        signUpMessage.setStyle("-fx-text-fill: red;");
        signUpMessage.setWrapText(true);

        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

        signUpButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                signUpMessage.setText("All fields are required.");
            } else if (!password.equals(confirmPassword)) {
                signUpMessage.setText("Passwords do not match.");
            } else if (!isValidEmail(email)) {
                signUpMessage.setText("Invalid email format.");
            } else if (isEmailAlreadyRegistered(email)) {
                signUpMessage.setText("Email is already registered.");
            } else if (!isValidPassword(password)) {
                signUpMessage.setText("Password must be at least 8 characters long and contain both letters and numbers.");
            } else {
                String secretKey = totpUtil.generateSecretKey();
                User newUser = new User(email, password, secretKey);
                userStorage.addUser(newUser);

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdditionalInfo.fxml"));
                    Parent additionalInfoRoot = loader.load();

                    // Pass the secretKey to AdditionalInfoController
                    AdditionalInfoController controller = loader.getController();
                    controller.setSecretKey(secretKey);

                    Stage additionalInfoStage = new Stage();
                    additionalInfoStage.setScene(new Scene(additionalInfoRoot));
                    additionalInfoStage.setTitle("Additional Information");
                    additionalInfoStage.show();

                    // Close the current registration window
                    ((Stage) signUpButton.getScene().getWindow()).close();
                } catch (Exception e) {
                    e.printStackTrace();
                    signUpMessage.setText("Failed to load Additional Info page.");
                }
            }
        });

        signUpSection.getChildren().addAll(signUpTitle, nameField, emailField, passwordField, confirmPasswordField, signUpButton, signUpMessage);

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
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
                            Parent homePageRoot = loader.load();

                            Stage homeStage = new Stage();
                            homeStage.setScene(new Scene(homePageRoot, 1024, 700));
                            homeStage.setTitle("Home Page");
                            homeStage.show();

                            ((Stage) signInButton.getScene().getWindow()).close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            signInMessage.setText("Failed to load Home Page.");
                        }
                    } else {
                        signInMessage.setText("Invalid OTP.");
                    }
                } else {
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isEmailAlreadyRegistered(String email) {
        return userStorage.getUserByEmail(email) != null;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
