package seedu.duke.storage;

import seedu.duke.tasklist.Category;
import seedu.duke.tasklist.CategoryList;
// import seedu.duke.task.Todo;
// import seedu.duke.task.Deadline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class Storage {
    private String todoFilePath;
    private String deadlineFilePath;

    public Storage(String todoPath, String deadlinePath) {
        this.todoFilePath = todoPath;
        this.deadlineFilePath = deadlinePath;
    }

    public void save(CategoryList categoryList) throws IOException {
        FileWriter todoWriter = new FileWriter(todoFilePath);
        FileWriter deadlineWriter = new FileWriter(deadlineFilePath);

        for (int i = 0; i < categoryList.getAmount(); i++) {
            Category cat = categoryList.getCategory(i);

            // Save Todos from this category
            for (int j = 0; j < cat.getTodoList().getSize(); j++) {
                todoWriter.write(cat.getName() + " | "
                        + cat.getTodoList().get(j).toFileFormat() + System.lineSeparator());
            }

            // Save Deadlines from this category
            for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                deadlineWriter.write(cat.getName() + " | "
                        + cat.getDeadlineList().get(j).toFileFormat() + System.lineSeparator());
            }
        }
        todoWriter.close();
        deadlineWriter.close();
    }

    public void load(CategoryList categoryList) {
        File todoFile = new File(todoFilePath);
        File deadlineFile = new File(deadlineFilePath);

        if (todoFile.exists()) {
            try (java.util.Scanner s = new java.util.Scanner(todoFile)) {
                while (s.hasNext()) {
                    String[] parts = s.nextLine().split(" \\| ");
                    // Format: Category | T | Status | Description
                    String catName = parts[0];
                    boolean isDone = parts[2].equals("1");
                    String desc = parts[3];

                    // Ensure category exists
                    if (!categoryExists(categoryList, catName)) {
                        categoryList.addCategory(catName);
                    }

                    int catIdx = getCategoryIndex(categoryList, catName);
                    categoryList.addTodo(catIdx, desc);
                    if (isDone) {
                        categoryList.markTodo(catIdx, categoryList.getCategory(catIdx).getTodoList().getSize() - 1);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("No existing Todo file found.");
            }
        }

        if (deadlineFile.exists()) {
            try (java.util.Scanner s = new java.util.Scanner(deadlineFile)) {
                while (s.hasNext()) {
                    String[] parts = s.nextLine().split(" \\| ");
                    // Format: Category | D | Status | Description | ISO-Date
                    String catName = parts[0];
                    boolean isDone = parts[2].equals("1");
                    String desc = parts[3];
                    String dateString = parts[4];

                    DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    java.time.LocalDateTime by = java.time.LocalDateTime.parse(dateString, storageFormatter);

                    if (!categoryExists(categoryList, catName)) {
                        categoryList.addCategory(catName);
                    }

                    int catIdx = getCategoryIndex(categoryList, catName);
                    categoryList.addDeadline(catIdx, desc, by);
                    if (isDone) {
                        categoryList.setDeadlineStatus(catIdx,
                                categoryList.getCategory(catIdx).getDeadlineList().getSize() - 1,
                                true);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("No existing Deadline file found.");
            }
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
