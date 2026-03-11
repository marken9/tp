package seedu.duke;

import java.util.Arrays;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

import seedu.duke.storage.Storage;
import seedu.duke.tasklist.CategoryList;

public class UniTasker {
    private static CategoryList categories;
    private static Storage storage = new Storage("todos.txt", "deadlines.txt");

    UniTasker() {
        categories = new CategoryList();
        storage.load(categories);
    }

    public static void handleMark(String[] sentence, boolean isMark) {
        String secondCommand = sentence[1];
        int categoryIndex = Integer.parseInt(sentence[2]) - 1;
        int taskIndex = Integer.parseInt(sentence[3]) - 1;
        switch (secondCommand) {
        case "todo":
            if (isMark) {
                categories.markTodo(categoryIndex, taskIndex);
            } else {
                categories.unmarkTodo(categoryIndex, taskIndex);
            }
            break;
        case "deadline":
            categories.setDeadlineStatus(categoryIndex, taskIndex, isMark);
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handleDelete(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            int deleteIndex = Integer.parseInt(sentence[2]);
            categories.deleteCategory(deleteIndex - 1);
            break;
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            int todoIndex = Integer.parseInt(sentence[3]) - 1;
            categories.deleteTodo(categoryIndex, todoIndex);
            break;
        case "deadline":
            int deadlineCatIndex = Integer.parseInt(sentence[2]) - 1;
            int deadlineIndex = Integer.parseInt(sentence[3]) - 1;
            categories.deleteDeadline(deadlineCatIndex, deadlineIndex);
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handleAdd(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            String[] nameArr = Arrays.copyOfRange(sentence, 2, sentence.length);
            String name = String.join(" ", nameArr);
            categories.addCategory(name);
            break;
        case "todo":
            int todoIndex = Integer.parseInt(sentence[2]) - 1;
            String[] descriptionArr = Arrays.copyOfRange(sentence, 3, sentence.length);
            String description = String.join(" ", descriptionArr);
            categories.addTodo(todoIndex, description);
            break;
        case "deadline":
            int deadlineIndex = Integer.parseInt(sentence[2]) - 1;
            String raw = String.join(" ", Arrays.copyOfRange(sentence, 3, sentence.length));
            String[] parts = raw.split(" /by ");
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            java.time.LocalDateTime by = java.time.LocalDateTime.parse(parts[1], inputFormatter);
            categories.addDeadline(deadlineIndex, parts[0], by);
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handleReorder(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            int categoryIndex1 = Integer.parseInt(sentence[2]) - 1;
            int categoryIndex2 = Integer.parseInt(sentence[3]) - 1;
            categories.reorderCategory(categoryIndex1, categoryIndex2);
            break;
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            int todoIndex1 = Integer.parseInt(sentence[3]) - 1;
            int todoIndex2 = Integer.parseInt(sentence[4]) - 1;
            categories.reorderTodo(categoryIndex, todoIndex1, todoIndex2);
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handlePriority(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "todo":
            int categoryIndex = Integer.parseInt(sentence[2]) - 1;
            int todoIndex = Integer.parseInt(sentence[3]) - 1;
            int priority = Integer.parseInt(sentence[4]);
            categories.setTodoPriority(categoryIndex, todoIndex, priority);
            break;
        default:
            break;
        }
        saveData();
    }

    public static void handleList(String[] sentence) {
        String secondCommand = sentence[1];
        switch (secondCommand) {
        case "category":
            System.out.println(categories);
            break;
        case "todo":
            System.out.println(categories.getAllTodos());
            break;
        case "deadline":
            System.out.println(categories.getAllDeadlines());
            break;
        default:
            System.out.println("Unknown list command. Use: list category, list todo, or list deadline.");
            break;
        }
        saveData();
    }


    public void run() {
        System.out.println("Welcome to UniTasker");
        Scanner in = new Scanner(System.in);
        while (true) {
            if (!in.hasNextLine()) {  // Check if input is available
                break;
            }
            String line;
            line = in.nextLine();
            String[] sentence = line.split(" ");
            String commandWord = sentence[0];
            switch (commandWord) {
            case "exit":
                System.out.println("Exiting UniTasker.");
                return;
            case "add":
                handleAdd(sentence);
                break;
            case "delete":
                handleDelete(sentence);
                break;
            case "list":
                handleList(sentence);
                break;
            case "mark":
                handleMark(sentence, true);
                break;
            case "unmark":
                handleMark(sentence, false);
                break;
            case "reorder":
                handleReorder(sentence);
                break;
            case "priority":
                handlePriority(sentence);
                break;
            default:
                System.out.println("default echo: " + line);
                break;
            }
        }
        in.close();
    }

    private static void saveData() {
        try {
            storage.save(categories);
        } catch (java.io.IOException e) {
            System.out.println("Error: Could not save data to files.");
        }
    }

    public static void main(String[] args) {
        new UniTasker().run();
    }
}
