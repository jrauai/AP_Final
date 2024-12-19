package Assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private static final String BASE_DIRECTORY = "src/main/java/Assignment/File IO/";

    public static Path getUserFilePath(String email, String filename) {
        String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
        return Paths.get(BASE_DIRECTORY, sanitizedEmail + "_data", filename);
    }
    public static void ensureUserDirectoryExists(String email) throws IOException {
        String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
        Path userDir = Paths.get(BASE_DIRECTORY, sanitizedEmail + "_data");
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }
    }
}
