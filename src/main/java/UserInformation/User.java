package UserInformation;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;  // Added serial version UID for better compatibility

    private String email;
    private String password;
    private String secretKey;
    private String gender;
    private String dateOfBirth;
    private String nationality;
    private String height;
    private String weight;

    // Constructor to initialize the user object with essential details
    public User(String email, String password, String secretKey) {
        this.email = email;
        this.password = password;
        this.secretKey = secretKey;
    }

    // Constructor to initialize the user with additional details like gender, DOB, etc.
    public User(String email, String password, String secretKey, String gender, String dateOfBirth, String nationality, String height, String weight) {
        this.email = email;
        this.password = password;
        this.secretKey = secretKey;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.height = height;
        this.weight = weight;
    }

    // Getters and Setters for all fields
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    // Method to display user info as a string (for debugging or logging purposes)
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", nationality='" + nationality + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }

    public String getName() {
        return "";
    }

    public void setName(String text) {
    }
}
