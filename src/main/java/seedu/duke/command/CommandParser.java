package seedu.duke.command;

public class CommandParser {

    public static final int INDEX_OF_COMMANDTYPE = 0;

    //@@author marken9
    public Command parse(String line) {
        if (line.contains("|")) {
            return new UnknownCommand("|");
        }
        String[] sentence = line.trim().split("\\s+");
        String commandWord = sentence[INDEX_OF_COMMANDTYPE];

        switch (commandWord) {
        case "add":
            return new AddCommand(sentence);
        case "delete":
            return new DeleteCommand(sentence);
        case "mark":
            return new MarkCommand(sentence, true);
        case "unmark":
            return new MarkCommand(sentence, false);
        case "reorder":
            return new ReorderCommand(sentence);
        case "priority":
            return new PriorityCommand(sentence);
        case "sort":
            return new SortCommand(sentence);
        case "list":
            return new ListCommand(sentence);
        case "find":
            return new FindCommand(sentence);
        case "course":
            return new CourseCommand(line);
        case "limit":
            return new LimitCommand(sentence);
        case "help":
            return new HelpCommand(sentence);
        case "reminder":
            return new ReminderCommand();
        case "exit":
            return new ExitCommand();
        case "undo":
            return new UndoCommand();
        default:
            return new UnknownCommand(sentence[0]);
        }
    }
}
