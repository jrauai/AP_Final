package UserInformation;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String secretKey;

    public User(String email, String password, String secretKey) {
        this.email = email;
        this.password = password;
        this.secretKey = secretKey;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
