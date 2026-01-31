import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private Path filePath;
    private List<String> savedList = new ArrayList<String>();

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }

    public List<String> getSavedList() {
        return savedList;
    }

    public Boolean doesFileExist() throws IOException {
        // If file exists, load it. Else, create a new file.
        if (Files.exists(filePath)) {
            return true;
        } else {
            return false;
        }
    }

    public void loadSavedFile() throws IOException {
        savedList = Files.readAllLines(filePath);
    }

    public void createNewFile() throws IOException {
        Files.createFile(filePath);
    }

    public void updateSavedList(ArrayList<Task> taskList) {
        savedList.clear();
        for (Task t : taskList) {
            String line = t.getTaskType() + "|" + t.getStatus() + "|" + t.getName();
            savedList.add(line);
        }
    }

    public void saveFile() throws IOException {
        Files.write(filePath, savedList, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
