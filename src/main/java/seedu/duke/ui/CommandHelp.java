package seedu.duke.ui;

/**
 * Centralized help system for UniTasker.
 * Provides contextual help for each mode and command.
 */
//@@author benguy6
public class CommandHelp {
    private static final String DOTTED_LINE = "______________________________________________________________________";

    /**
     * Get general help showing all 4 modes and basic commands.
     */
    public static String getGeneralHelp() {
        return DOTTED_LINE + "\n" +
                "UniTasker - Complete Task & Course Management System\n" +
                "\n" +
                "4 FEATURE MODES:\n" +
                "  1. Todo Management    - Manage todos with priority & organization\n" +
                "  2. Deadline Manager   - Track assignments with auto-sorted dates\n" +
                "  3. Event Manager      - Schedule events and recurring meetings\n" +
                "  4. Course Tracker     - Manage courses and assessment grades\n" +
                "\n" +
                "SYSTEM COMMANDS:\n" +
                "  find <keyword>        - Search for tasks by keyword\n" +
                "  limit task <num>      - Set daily task limit (default: 8)\n" +
                "  limit year <num>      - Set end year for dates (default: 2030)\n" +
                "  list limit            - Show current limits\n" +
                "  reminder              - Show all pending tasks for the day\n" +
                "  exit                  - Quit UniTasker\n" +
                "\n" +
                "GETTING HELP:\n" +
                "  help                  - Show this message\n" +
                "  help task             - Show task management commands\n" +
                "  help deadline         - Show deadline management commands\n" +
                "  help event            - Show event management commands\n" +
                "  help course           - Show course tracker commands\n" +
                "  help add              - Show all 'add' command variants\n" +
                "  help delete           - Show all 'delete' command variants\n" +
                "  help list             - Show all 'list' command variants\n" +
                "\n" +
                "COMMAND ALIASES: a=add, d=delete, l=list, m=mark, u=unmark, p=priority, s=sort, f=find\n" +
                DOTTED_LINE;
    }

    /**
     * Get help for Task Management mode.
     */
    public static String getTaskHelp() {
        return DOTTED_LINE + "\n" +
                "TASK MANAGEMENT COMMANDS\n" +
                "Manage todos with priority levels (0-5) within categories.\n" +
                "\n" +
                "ADD TODOS:\n" +
                "  add todo <cat_idx> <description>\n" +
                "  Example: add todo 1 Review CS2113 notes\n" +
                "\n" +
                "MARK/UNMARK:\n" +
                "  mark todo <cat_idx> <idx>\n" +
                "  unmark todo <cat_idx> <idx>\n" +
                "  Example: mark todo 1 1\n" +
                "\n" +
                "SET PRIORITY (0=low, 5=high):\n" +
                "  priority todo <cat_idx> <idx> <priority>\n" +
                "  Example: priority todo 1 2 4\n" +
                "\n" +
                "REORDER/SORT:\n" +
                "  reorder todo <cat_idx> <idx1> <idx2>\n" +
                "  sort todo <cat_idx>          - Sort by priority\n" +
                "\n" +
                "DELETE:\n" +
                "  delete todo <cat_idx> <idx>\n" +
                "\n" +
                "LIST:\n" +
                "  list todo                    - List all todos\n" +
                "  list category                - List all categories\n" +
                "  list category <idx>          - List specific category\n" +
                DOTTED_LINE;
    }

    /**
     * Get help for Deadline Management mode.
     */
    public static String getDeadlineHelp() {
        return DOTTED_LINE + "\n" +
                "DEADLINE MANAGEMENT COMMANDS\n" +
                "Track assignments with auto-sorted dates and workload validation.\n" +
                "Date format: dd-MM-yyyy | Time format: HHmm (24-hour)\n" +
                "\n" +
                "ADD DEADLINE:\n" +
                "  add deadline <cat_idx> <description> /by <date> <time>\n" +
                "  Example: add deadline 1 Submit essay /by 25-03-2026 2359\n" +
                "\n" +
                "MARK/UNMARK:\n" +
                "  mark deadline <cat_idx> <idx>\n" +
                "  unmark deadline <cat_idx> <idx>\n" +
                "\n" +
                "DELETE:\n" +
                "  delete deadline <cat_idx> <idx>\n" +
                "  delete deadline <cat_idx> all    - Delete all deadlines in category\n" +
                "\n" +
                "LIST:\n" +
                "  list deadline                    - List all deadlines\n" +
                "  list range <start> <end> [/deadline]\n" +
                "  Example: list range 25-03-2026 31-03-2026 /deadline\n" +
                DOTTED_LINE;
    }

    /**
     * Get help for Event Management mode.
     */
    public static String getEventHelp() {
        return DOTTED_LINE + "\n" +
                "EVENT MANAGEMENT COMMANDS\n" +
                "Schedule events, meetings, and recurring activities.\n" +
                "Date format: dd-MM-yyyy | Time format: HHmm (24-hour) | Days: Monday-Sunday\n" +
                "\n" +
                "ADD EVENT (One-time):\n" +
                "  add event <cat_idx> <desc> /from <date> <time> /to <date> <time>\n" +
                "  Example: add event 1 Lecture /from 25-03-2026 1400 /to 25-03-2026 1530\n" +
                "\n" +
                "ADD RECURRING EVENT (Weekly):\n" +
                "  add recurring <cat_idx> <desc>\n" +
                "  /from <day> <time> /to <day> <time> [/month <num> | /date <date>]\n" +
                "  Example: add recurring 1 Lecture\n" +
                "           /from Monday 1400 /to Monday 1530 /month 3\n" +
                "  - /month N  : Repeat for N months\n" +
                "  - /date END : Repeat until END date (format: dd-MM-yyyy)\n" +
                "\n" +
                "MARK/UNMARK:\n" +
                "  mark event <cat_idx> <idx>\n" +
                "  unmark event <cat_idx> <idx>\n" +
                "  mark occurrence <cat_idx> <idx>\n" +
                "  unmark occurrence <cat_idx> <idx>\n" +
                "\n" +
                "DELETE:\n" +
                "  delete event <cat_idx> <idx>\n" +
                "  delete event <cat_idx> all\n" +
                "  delete recurring <cat_idx> <id>\n" +
                "  delete occurrence <cat_idx> <id>\n" +
                "\n" +
                "LIST:\n" +
                "  list event                     - List events with recurring events collapsed to one group\n" +
                "  list event /all                - List all events\n" +
                "  list event /normal             - List only non-recurring events\n" +
                "  list recurring                 - List recurring events\n" +
                "  list occurrence <cat_idx> <id> - List occurrence of recurring event group\n" +
                "  list range <start> <end> [/event]\n" +
                DOTTED_LINE;
    }

    /**
     * Get help for Course Tracker mode.
     */
    public static String getCourseHelp() {
        return DOTTED_LINE + "\n" +
                "COURSE TRACKER COMMANDS\n" +
                "Manage courses and track assessment grades.\n" +
                "\n" +
                "ADD/LIST COURSES:\n" +
                "  course add <code>                - Add a new course\n" +
                "  course list                      - List all courses\n" +
                "  course view <code>               - View course assessments\n" +
                "  Example: course add CS2113\n" +
                "\n" +
                "ADD ASSESSMENT:\n" +
                "  course add-assessment <code> /n <name> /w <weightage> /ms <max_score>\n" +
                "  Example: course add-assessment CS2113 /n Finals /w 40 /ms 100\n" +
                "  - /n  : Assessment name\n" +
                "  - /w  : Weightage percentage\n" +
                "  - /ms : Maximum score\n" +
                "\n" +
                "RECORD SCORE:\n" +
                "  course score <code> /n <assessment_name> /s <score_obtained>\n" +
                "  Example: course score CS2113 /n Finals /s 85\n" +
                "\n" +
                "DELETE:\n" +
                "  course delete <code>\n" +
                "  course delete-assessment <code> <assessment_name>\n" +
                DOTTED_LINE;
    }

    /**
     * Get help for 'add' command across all modes.
     */
    public static String getAddCommandHelp() {
        return DOTTED_LINE + "\n" +
                "ADD COMMAND VARIANTS\n" +
                "\n" +
                "CATEGORIES:\n" +
                "  add category <name>\n" +
                "  Example: add category CS2113\n" +
                "\n" +
                "TODOS:\n" +
                "  add todo <cat_idx> <description>\n" +
                "  Example: add todo 1 Review notes\n" +
                "\n" +
                "DEADLINES:\n" +
                "  add deadline <cat_idx> <desc> /by <date> <time>\n" +
                "  Example: add deadline 1 Submit /by 25-03-2026 2359\n" +
                "\n" +
                "EVENTS:\n" +
                "  add event <cat_idx> <desc> /from <date> <time> /to <date> <time>\n" +
                "  Example: add event 1 Meeting /from 25-03-2026 1400 /to 25-03-2026 1500\n" +
                "\n" +
                "RECURRING EVENTS:\n" +
                "  add recurring <cat_idx> <desc> /from <day> <time> /to <day> <time>\n" +
                "  Example: add recurring 1 Lecture /from Monday 1400 /to Monday 1530\n" +
                "\n" +
                "COURSES:\n" +
                "  course add <code>\n" +
                "  Example: course add CS2113\n" +
                "\n" +
                "ASSESSMENTS:\n" +
                "  course add-assessment <code> /n <name> /w <weightage> /ms <max_score>\n" +
                "  Example: course add-assessment CS2113 /n Finals /w 40 /ms 100\n" +
                DOTTED_LINE;
    }

    /**
     * Get help for 'delete' command across all modes.
     */
    public static String getDeleteCommandHelp() {
        return DOTTED_LINE + "\n" +
                "DELETE COMMAND VARIANTS\n" +
                "\n" +
                "CATEGORIES:\n" +
                "  delete category <idx>\n" +
                "\n" +
                "TODOS:\n" +
                "  delete todo <cat_idx> <idx>\n" +
                "\n" +
                "DEADLINES:\n" +
                "  delete deadline <cat_idx> <idx>\n" +
                "  delete deadline <cat_idx> all\n" +
                "\n" +
                "EVENTS:\n" +
                "  delete event <cat_idx> <idx>\n" +
                "  delete event <cat_idx> all\n" +
                "\n" +
                "RECURRING:\n" +
                "  delete recurring <cat_idx> <idx>\n" +
                "\n" +
                "OCCURRENCE:\n" +
                "  delete occurrence <cat_idx> <idx>\n" +
                "\n" +
                "COURSES:\n" +
                "  course delete <code>\n" +
                "\n" +
                "ASSESSMENTS:\n" +
                "  course delete-assessment <code> <assessment_name>\n" +
                DOTTED_LINE;
    }

    /**
     * Get help for 'list' command across all modes.
     */
    public static String getListCommandHelp() {
        return DOTTED_LINE + "\n" +
                "LIST COMMAND VARIANTS\n" +
                "\n" +
                "CATEGORIES:\n" +
                "  list category                - List all categories\n" +
                "  list category <idx>          - List specific category\n" +
                "\n" +
                "TODOS:\n" +
                "  list todo                    - List all todos\n" +
                "\n" +
                "DEADLINES:\n" +
                "  list deadline                - List all deadlines\n" +
                "\n" +
                "EVENTS:\n" +
                "  list event                     - List events with recurring events collapsed to one group\n" +
                "  list event /all                - List all events\n" +
                "  list event /normal             - List only non-recurring events\n" +
                "  list recurring                 - List recurring events\n" +
                "  list occurrence <cat_idx> <id> - List occurrence of recurring event group\n" +
                "\n" +
                "DATE RANGES (all types):\n" +
                "  list range <start> <end>           - All tasks in range\n" +
                "  list range <start> <end> /deadline - Only deadlines\n" +
                "  list range <start> <end> /event    - Only events\n" +
                "  Example: list range 25-03-2026 31-03-2026 /deadline\n" +
                "\n" +
                "COURSES:\n" +
                "  course list                  - List all courses\n" +
                "  course view <code>           - View assessments\n" +
                "\n" +
                "SYSTEM:\n" +
                "  list limit                   - Show current limits\n" +
                DOTTED_LINE;
    }

    /**
     * Route help requests to appropriate method.
     */
    public static String getHelp(String topic) {
        if (topic == null || topic.isEmpty() || topic.equalsIgnoreCase("general")) {
            return getGeneralHelp();
        }

        return switch (topic.toLowerCase()) {
        case "task", "todo" -> getTaskHelp();
        case "deadline" -> getDeadlineHelp();
        case "event" -> getEventHelp();
        case "course" -> getCourseHelp();
        case "add" -> getAddCommandHelp();
        case "delete" -> getDeleteCommandHelp();
        case "list" -> getListCommandHelp();
        default -> "Unknown help topic: '" + topic + "'\n" +
                "Try: help, help task, help deadline, help event," +
                " help course, help add, help delete, help list\n" +
                DOTTED_LINE;
        };
    }
}
