package tanjy.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import tanjy.task.Task;
import tanjy.task.Todo;
import tanjy.task.Deadline;
import tanjy.task.Event;
import tanjy.exception.TanjyException;

public class Parser {
    private static final DateTimeFormatter IN_DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter IN_DATETIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses the given string into LocalDateTime.
     *
     * @param s Date/time string to parse.
     * @return Parsed LocalDateTime.
     * @throws TanjyException if the input does not match supported format.
     */

    public LocalDateTime parseDateTime(String s) throws TanjyException {
        String input = s.trim();
        try {
            return LocalDateTime.parse(input, IN_DATETIME);
        } catch (DateTimeParseException ignored) {
        }

        try {
            return LocalDate.parse(input, IN_DATE).atStartOfDay();
        } catch (DateTimeParseException ignored) {
        }

        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException ignored) {
        }

        throw new TanjyException("Invalid date format. Use yyyy-MM-dd or yyyy-MM-dd HHmm");
    }

    /**
     * Parses a single saved task line into a Task object.
     *
     * @param s A line from the saved task file.
     * @return The parsed Task object, or null if the task type is unknown.
     * @throws TanjyException if the line has missing fields or invalid status.
     */
    public Task lineToTaskParser(String s) throws RuntimeException{
        String[] lineParts = s.split("\\|", 2);
        String typeOfTask = lineParts[0].trim();
        String taskContents = lineParts.length > 1 ? lineParts[1].trim() : "";

        try {
            switch (typeOfTask) {
                case "T":
                    String[] todoParts = taskContents.split("\\|", 2);
                    int taskStatus = Integer.parseInt(todoParts[0].trim());
                    return new Todo(todoParts[1].trim(), taskStatus);
                case "D":
                    String[] deadlineParts = taskContents.split("\\|", 3);
                    taskStatus = Integer.parseInt(deadlineParts[0].trim());
                    LocalDateTime by = parseDateTime(deadlineParts[2].trim());
                    return new Deadline(deadlineParts[1].trim(), taskStatus, by);
                case "E":
                    String[] eventParts = taskContents.split("\\|", 4);
                    taskStatus = Integer.parseInt(eventParts[0].trim());
                    LocalDateTime from = parseDateTime(eventParts[2].trim());
                    LocalDateTime to = parseDateTime(eventParts[3].trim());
                    return new Event(eventParts[1].trim(), taskStatus, from, to);
                default:
                    return null;
            }
        } catch (TanjyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses a list of saved task descriptions and appends the parsed tasks to the given task list.
     *
     * @param taskList The list to add parsed tasks.
     * @param savedList The list of saved task description strings.
     * @return The same taskList instance after tasks have been added.
     */

    public ArrayList<Task> stringListToTaskList(ArrayList<Task> taskList, List<String> savedList) {
        for (String line : savedList) {
            Task t = lineToTaskParser(line);
            taskList.add(t);
        }
        return taskList;
    }

}
