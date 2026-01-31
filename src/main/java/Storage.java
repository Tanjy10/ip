import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Storage {

    public Boolean doesFileExist(Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());

        // If file exists, load it. Else, create a new file.
        if (Files.exists(filePath)) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> loadSavedFile(Path filePath) throws IOException {
        return Files.readAllLines(filePath);
    }

    public void createNewFile(Path filePath) throws IOException {
        Files.createFile(filePath);
    }

    public void saveFile(Path filePath, List<String> list) throws IOException {
        Files.write(filePath, list, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
