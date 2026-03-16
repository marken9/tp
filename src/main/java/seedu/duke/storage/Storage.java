package seedu.duke.storage;


import java.time.LocalDateTime;
import java.util.logging.Level;

import seedu.duke.exception.UniTaskerException;
import seedu.duke.util.DateUtils;
import seedu.duke.exception.IllegalDateException;

import seedu.duke.tasklist.Category;
import seedu.duke.tasklist.CategoryList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Handles the loading and saving of task data to local text files.
 * This class ensures that user data persists across different sessions by
 * serializing {@code CategoryList} objects into a readable pipe-delimited format.
 */
public class Storage {

    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    private String todoFilePath;
    private String deadlineFilePath;
    private String eventFilePath;

    public Storage(String todoPath, String deadlinePath, String eventPath) {
        this.todoFilePath = todoPath;
        this.deadlineFilePath = deadlinePath;
        this.eventFilePath = eventPath;
    }

    /**
     * Serializes and writes the current state of the {@code CategoryList} to the disk.
     * Overwrites existing files with the updated task information.
     *
     * @param categoryList The list of categories and tasks to save.
     * @throws IOException If there is an error writing to any of the files.
     */
    public void save(CategoryList categoryList) throws IOException {
        assert categoryList != null : "CategoryList should not be null when saving";
        logger.info("Starting save process...");
        try (FileWriter todoWriter = new FileWriter(todoFilePath);
             FileWriter deadlineWriter = new FileWriter(deadlineFilePath);
             FileWriter eventWriter = new FileWriter(eventFilePath)) {

            for (int i = 0; i < categoryList.getAmount(); i++) {
                Category cat = categoryList.getCategory(i);

                for (int j = 0; j < cat.getTodoList().getSize(); j++) {
                    todoWriter.write(cat.getName() + " | "
                            + cat.getTodoList().get(j).toFileFormat() + System.lineSeparator());
                }

                for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                    deadlineWriter.write(cat.getName() + " | "
                            + cat.getDeadlineList().get(j).toFileFormat() + System.lineSeparator());
                }

                // Save Events from this category
                for (int j = 0; j < cat.getEventList().getSize(); j++) {
                    eventWriter.write(cat.getName() + " | "
                            + cat.getEventList().get(j).toFileFormat() + System.lineSeparator());
                }
            }
            logger.info("Data successfully saved to files.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write to storage files.", e);
            throw e;
        }
    }

    /**
     * Reads task data from the local storage files and populates the provided {@code CategoryList}.
     * If a file does not exist, it skips that specific loading process.
     *
     * @param categoryList The list to be populated with data from the files.
     */
    public void load(CategoryList categoryList) {
        assert categoryList != null : "CategoryList should not be null when loading";
        logger.info("Loading data from disk...");

        File todoFile = new File(todoFilePath);
        File deadlineFile = new File(deadlineFilePath);
        File eventFile = new File(eventFilePath);

        if (todoFile.exists()) {
            try (java.util.Scanner s = new java.util.Scanner(todoFile)) {
                while (s.hasNextLine()) {
                    String[] parts = s.nextLine().split(" \\| ");

                    if (parts.length < 5) {
                        continue;
                    }

                    String catName = parts[0];
                    boolean isDone = parts[2].equals("1");
                    String priority = parts[3];
                    String desc = parts[4];

                    ensureCategoryExists(categoryList, catName);

                    int catIdx = getCategoryIndex(categoryList, catName);
                    categoryList.addTodo(catIdx, desc);
                    if (isDone) {
                        categoryList.markTodo(catIdx, categoryList.getCategory(catIdx).getTodoList().getSize() - 1);
                    }
                    int priorityInt = Integer.parseInt(priority);
                    categoryList.setTodoPriority(catIdx,
                            categoryList.getCategory(catIdx).getTodoList().getSize() - 1,
                            priorityInt);

                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("No existing Todo file found.");
            } catch (UniTaskerException e) {
                System.out.println("Error during load: " + e.getMessage());
            }
        }

        if (deadlineFile.exists()) {
            logger.info("Attempting to load deadlines from: " + deadlineFilePath);

            try (java.util.Scanner s = new java.util.Scanner(deadlineFile)) {
                int lineCount = 0;
                while (s.hasNextLine()) {
                    lineCount++;
                    String line = s.nextLine();
                    String[] parts = line.split(" \\| ");

                    if (parts.length < 5) {
                        logger.log(Level.WARNING, "Skipping malformed line " + lineCount + " in deadlines.txt");
                        continue;
                    }

                    String catName = parts[0].trim();
                    boolean isDone = parts[2].trim().equals("1");
                    String desc = parts[3].trim();
                    String dateString = parts[4].trim();

                    LocalDateTime by;

                    try {
                        by = DateUtils.parseDateTime(dateString);
                    } catch (IllegalDateException e) {
                        logger.warning( "Skipping line " + lineCount + " - Reason:" + e.getMessage());
                        continue;
                    }

                    // Ensure category exists
                    ensureCategoryExists(categoryList, catName);

                    int catIdx = getCategoryIndex(categoryList, catName);
                    // ASSERTION: The index should be valid if ensureCategoryExists worked correctly
                    assert catIdx >= 0 : "Category index should be valid for " + catName;

                    categoryList.addDeadline(catIdx, desc, by);
                    if (isDone) {
                        categoryList.setDeadlineStatus(catIdx,
                                categoryList.getCategory(catIdx).getDeadlineList().getSize() - 1,
                                true);
                    }
                }
                logger.info("Successfully loaded deadlines from file.");
            } catch (java.io.FileNotFoundException e) {
                logger.log(Level.SEVERE, "Deadline file vanished during read process", e);
            }
        } else {
            logger.info("No deadline file found at " + deadlineFilePath + ". Skipping load.");
        }
        if (eventFile.exists()) {
            try (java.util.Scanner s = new java.util.Scanner(eventFile)) {
                while (s.hasNextLine()) {
                    String[] parts = s.nextLine().split(" \\| ");

                    if (parts.length < 6) {
                        continue;
                    }

                    // Format: Category | E | Status | Description | Date (from) | Date (to)
                    String catName = parts[0];
                    boolean isDone = parts[2].equals("1");
                    String desc = parts[3];
                    String stringFrom = parts[4];
                    String stringTo = parts[5];

                    // Ensure category exists
                    ensureCategoryExists(categoryList, catName);
                    // Convert the string dateTime to dateTime objects
                    DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                    java.time.LocalDateTime from = java.time.LocalDateTime.parse(stringFrom, storageFormatter);
                    java.time.LocalDateTime to = java.time.LocalDateTime.parse(stringTo, storageFormatter);

                    int catIdx = getCategoryIndex(categoryList, catName);
                    categoryList.addEvent(catIdx, desc, from, to);
                    if (isDone) {
                        categoryList.setEventStatus(catIdx,
                                categoryList.getCategory(catIdx).getEventList().getSize() - 1, true);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("No existing Event file found.");
            }
        }
    }

    private void ensureCategoryExists(CategoryList categoryList, String catName) {
        if (!categoryExists(categoryList, catName)) {
            categoryList.addCategory(catName);
        }
    }

    private boolean categoryExists(CategoryList list, String name) {
        for (int i = 0; i < list.getAmount(); i++) {
            if (list.getCategory(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private int getCategoryIndex(CategoryList list, String name) {
        for (int i = 0; i < list.getAmount(); i++) {
            if (list.getCategory(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
