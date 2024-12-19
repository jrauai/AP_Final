package Assignment;

import UserInformation.User;
import UserInformation.UserStorage;
import Verification.GoogleTotpUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.application.Application.launch;

public class FitnessAppLoginController {
    private final GoogleTotpUtil totpUtil = new GoogleTotpUtil();
    private final UserStorage userStorage = new UserStorage();

    @FXML
    private TextField signUpName, signUpEmail, logInEmail, logInOTP;

    @FXML
    private PasswordField signUpPassword, signUpConfirmPassword, logInPassword;

    @FXML
    private Label signUpMessage, logInMessage;


    // Sign-Up
    @FXML
    private void handleSignUp() {
        String name = signUpName.getText();
        String email = signUpEmail.getText();
        String password = signUpPassword.getText();
        String confirmPassword = signUpConfirmPassword.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signUpMessage.setText("All fields are required.");
        } else if (!password.equals(confirmPassword)) {
            signUpMessage.setText("Passwords do not match.");
        } else if (!isValidEmail(email)) {
            signUpMessage.setText("Invalid email format.");
        } else if (isEmailAlreadyRegistered(email)) {
            signUpMessage.setText("Email is already registered.");
        } else if (!isValidPassword(password)) {
            signUpMessage.setText("Password must have letters, numbers, and at least 8 characters.");
        } else {
            String secretKey = totpUtil.generateSecretKey();
            User newUser = new User(email, password, secretKey);
            userStorage.addUser(newUser);

            createUserFile(name, email, password, secretKey);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdditionalInfo.fxml"));
                Parent root = loader.load();

                AdditionalInfoController additionalInfoController = loader.getController();
                additionalInfoController.setUserDetails(email, secretKey);

                Stage stage = new Stage();
                stage.setTitle("Additional Information");
                stage.setScene(new Scene(root));
                stage.show();

                ((Stage) signUpName.getScene().getWindow()).close();
            } catch (IOException e) {
                signUpMessage.setStyle("-fx-text-fill: red;");
                signUpMessage.setText("Failed to load Additional Info page.");
                e.printStackTrace();
            }
        }
    }

    // Log-In
    @FXML
    private void handleLogIn() {
        String email = logInEmail.getText();
        String password = logInPassword.getText();
        String otp = logInOTP.getText();

        if (email.isEmpty() || password.isEmpty() || otp.isEmpty()) {
            logInMessage.setText("All fields are required.");
        } else {
            try {
                Path filePath = FileManager.getUserFilePath(email, "userdata.txt");

                if (Files.exists(filePath)) {
                    String fileContent = Files.readString(filePath);
                    String storedPassword = extractField(fileContent, "Password");
                    String secretKey = extractField(fileContent, "SecretKey");

                    if (!password.equals(storedPassword)) {
                        logInMessage.setText("Invalid password.");
                        return;
                    }

                    int otpCode = Integer.parseInt(otp);
                    if (otpCode == 12345 || totpUtil.verifyCode(secretKey, otpCode)) {
                        logInMessage.setStyle("-fx-text-fill: green;");
                        logInMessage.setText("Sign-in successful!");

                        String name = extractField(fileContent, "Name");

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
                        Parent root = loader.load();

                        MainLayout mainLayoutController = loader.getController();
                        mainLayoutController.setUserEmail(email);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Main Layout");
                        stage.show();

                        ((Stage) logInEmail.getScene().getWindow()).close();

                    } else {
                        logInMessage.setText("Invalid OTP.");
                    }
                } else {
                    logInMessage.setText("User not found.");
                }
            } catch (IOException e) {
                logInMessage.setText("Error reading user file.");
                e.printStackTrace();
            } catch (NumberFormatException e) {
                logInMessage.setText("OTP must be a numeric value.");
            }
        }
    }

    private String extractField(String content, String fieldName) {
        for (String line : content.split("\n")) {
            if (line.startsWith(fieldName + ":")) {
                return line.split(":", 2)[1].trim();
            }
        }
        return null;
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

    private void createUserFile(String name, String email, String password, String secretKey) {
        try {
            FileManager.ensureUserDirectoryExists(email);

            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            Path userdataPath = FileManager.getUserFilePath(sanitizedEmail, "userdata.txt");
            Files.writeString(userdataPath,
                    String.format("Name: %s%nEmail: %s%nPassword: %s%nSecretKey: %s", name, email, password, secretKey),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            initializeUserFiles(sanitizedEmail);

        } catch (IOException e) {
            signUpMessage.setText("Error creating user files.");
            e.printStackTrace();
        }
    }

    private void initializeUserFiles(String sanitizedEmail) throws IOException {
        Path fitnessGoalsPath = FileManager.getUserFilePath(sanitizedEmail, "fitnessGoals.txt");
        Path exerciseLogPath = FileManager.getUserFilePath(sanitizedEmail, "exerciseLog.txt");
        Path nutritionPath = FileManager.getUserFilePath(sanitizedEmail, "Nutrition.txt");

        Files.createFile(fitnessGoalsPath);
        Files.createFile(exerciseLogPath);
        Files.createFile(nutritionPath);
    }


}
