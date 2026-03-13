package seedu.duke.calender;

import seedu.duke.task.Task;
import seedu.duke.task.Timed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Calendar {
    private TreeMap<LocalDate, List<Task>> schedule;

    public Calendar() {
        this.schedule = new TreeMap<>();
    }

    public void registerTask(Task task) {
        if (task instanceof Timed) {
            LocalDate date = ((Timed) task).getDate().toLocalDate();
            schedule.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
        }
    }

    public void clear() {
        schedule.clear();
    }

    public void displayRange(LocalDate start, LocalDate end) {
        var view = schedule.subMap(start, true, end, true);
        if (view.isEmpty()) {
            System.out.println("No tasks found in this range.");
            return;
        }

        for (var entry : view.entrySet()) {
            System.out.println("--- " + entry.getKey() + " ---");
            for (Task t : entry.getValue()) {
                System.out.println(t);
            }
        }
    }

    public <T extends Task> void displaySpecificTypeInRange(LocalDate start, LocalDate end, Class<T> taskType) {
        var view = schedule.subMap(start, true, end, true);
        boolean foundAny = false;

        for (var entry : view.entrySet()) {
            StringBuilder dayOutput = new StringBuilder();
            boolean foundInDay = false;

            for (Task t : entry.getValue()) {
                if (taskType.isInstance(t)) {
                    if (!foundInDay) {
                        dayOutput.append("--- ").append(entry.getKey()).append(" ---\n");
                        foundInDay = true;
                    }
                    dayOutput.append(t).append("\n");
                    foundAny = true;
                }
            }
            System.out.print(dayOutput);
        }

        if (!foundAny) {
            System.out.println("No " + taskType.getSimpleName() + "s found in this range.");
        }
    }

    public int getTaskCountOnDate(LocalDate date) {
        if (!schedule.containsKey(date)) {
            return 0;
        }
        return schedule.get(date).size();
    }
}
