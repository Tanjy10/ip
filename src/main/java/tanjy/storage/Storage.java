package tanjy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import tanjy.task.Task;

/**
 * Handles loading and saving of tasks to a local file.
 */
public class Storage {
    private Path filePath;
    private List<String> savedList = new ArrayList<String>();

    /**
     * Constructs a storage with the specified filePath.
     *
     * @param filePath FilePath containing the list of tasks.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    public List<String> getSavedList() {
        return savedList;
    }

    /**
     * Checks whether the storage file exists.
     *
     * @return True if the file exists, false otherwise.
     */
    public Boolean doesFileExist() {
        return Files.exists(filePath);
    }

    /**
     * Loads saved task description lines from the storage file.
     *
     * @throws IOException if the storage file cannot be read.
     */
    public void loadSavedFile() throws IOException {
        savedList = Files.readAllLines(filePath);
    }


    /**
     * Creates a new storage file at the specified file path.
     *
     * @throws IOException if the storage file cannot be created.
     */
    public void createNewFile() throws IOException {
        Files.createFile(filePath);
    }

    /**
     * Updates the internal savedlist based on the tasklist given.
     *
     * @param taskList List of tasks to update.
     */
    public void updateSavedList(ArrayList<Task> taskList) {
        savedList.clear();
        for (Task t : taskList) {
            String line = t.getTaskType() + "|" + t.getStatus() + "|" + t.getName();
            savedList.add(line);
        }
    }

    /**
     * Saves the updated list onto the "data/Tanjy.txt" file.
     *
     * @throws IOException if Files.write(...) does not work.
     */
    public void saveFile() throws IOException {
        Files.write(filePath, savedList, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
