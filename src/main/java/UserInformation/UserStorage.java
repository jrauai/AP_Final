package UserInformation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    private static final String FILE_NAME = "src/main/java/Assignment/File IO/users.txt";


    // Load all users from the file into a list
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    User user = new User(
                            parts[0], // email
                            parts[1], // password
                            parts[2], // secretKey
                            parts[3], // gender
                            parts[4], // dateOfBirth
                            parts[5], // nationality
                            parts[6], // height
                            parts[7]  // weight
                    );
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load users: " + e.getMessage());
        }
        return users;
    }

    // Save a single user to the file
    public void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                writer.write(String.join(",",
                        user.getEmail(),
                        user.getPassword(),
                        user.getSecretKey(),
                        user.getGender() != null ? user.getGender() : "",
                        user.getDateOfBirth() != null ? user.getDateOfBirth() : "",
                        user.getNationality() != null ? user.getNationality() : "",
                        user.getHeight() != null ? user.getHeight() : "",
                        user.getWeight() != null ? user.getWeight() : ""
                ));
                writer.newLine();
            }
            System.out.println("Users saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }


    // Add user to storage
    public void addUser(User user) {
        if (getUserByEmail(user.getEmail()) == null) { // Avoid duplicate users
            saveUser(user);
        } else {
            System.err.println("User with email " + user.getEmail() + " already exists.");
        }
    }

    private void saveUser(User user) {
    }

    // Retrieve user by email
    public User getUserByEmail(String email) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public User getUserBySecretKey(String secretKey) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getSecretKey().equals(secretKey)) {
                return user;
            }
        }
        return null; // Return null if no user is found with the given secretKey
    }
}
