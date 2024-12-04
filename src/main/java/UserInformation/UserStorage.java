package UserInformation;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private static final String FILE_NAME = "users.ser";
    private Map<String, User> userMap;

    public UserStorage() {
        this.userMap = loadUsers();
    }

    // Load users from file
    private Map<String, User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    // Save users to file
    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(userMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add user to the storage
    public void addUser(User user) {
        userMap.put(user.getEmail(), user);
        saveUsers();
    }

    // Retrieve user by email
    public User getUserByEmail(String email) {
        return userMap.get(email);
    }
}
