package seedu.duke.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorUiTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void redirect() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void restore() {
        System.setOut(originalOut);
    }

    @Test
    void printError_singleMessage_containsErrorPrefix() {
        ErrorUi.printError("Something went wrong");
        assertTrue(out.toString().contains("Error: Something went wrong"));
    }

    @Test
    void printError_prefixAndMessage_bothAppear() {
        ErrorUi.printError("Load", "File not found");
        assertTrue(out.toString().contains("Load: File not found"));
    }

    @Test
    void printUnknownCommand_containsCommandAndOptions() {
        ErrorUi.printUnknownCommand("add", "todo/deadline/event");
        String output = out.toString();
        assertTrue(output.contains("add"));
        assertTrue(output.contains("todo/deadline/event"));
    }

    @Test
    void printCommandFailed_withFormat_showsFormatLine() {
        ErrorUi.printCommandFailed("Add", "missing /by", "add deadline <cat> <desc> /by <date>");
        String output = out.toString();
        assertTrue(output.contains("Add failed: missing /by"));
        assertTrue(output.contains("add deadline <cat> <desc> /by <date>"));
    }

    @Test
    void printCommandFailed_nullFormat_doesNotPrintFormatLine() {
        ErrorUi.printCommandFailed("Delete", "index out of bounds", null);
        String output = out.toString();
        assertTrue(output.contains("Delete failed: index out of bounds"));
        assertFalse(output.contains("Correct format:"));
    }

    @Test
    void printMissingArgs_containsHint() {
        ErrorUi.printMissingArgs("Use: add todo <cat> <desc>");
        assertTrue(out.toString().contains("Use: add todo <cat> <desc>"));
    }

    @Test
    void printInvalidNumber_containsValidNumberText() {
        ErrorUi.printInvalidNumber();
        assertTrue(out.toString().contains("valid number"));
    }

    @Test
    void printIndexNotFound_containsIndexText() {
        ErrorUi.printIndexNotFound();
        assertTrue(out.toString().contains("index"));
    }

    @Test
    void printRangeOutOfBounds_containsYears() {
        ErrorUi.printRangeOutOfBounds(2026, 2030);
        String output = out.toString();
        assertTrue(output.contains("2026"));
        assertTrue(output.contains("2030"));
    }

    @Test
    void printUnknownCommandHint_showsAvailableCommands() {
        ErrorUi.printUnknownCommandHint("blah");
        String output = out.toString();
        assertTrue(output.contains("blah"));
        assertTrue(output.contains("add"));
        assertTrue(output.contains("help"));
    }

    @Test
    void printLimitFormatError_containsLimitKeyword() {
        ErrorUi.printLimitFormatError();
        assertTrue(out.toString().contains("limit"));
    }

    @Test
    void printLimitYearBeforeStart_containsStartYear() {
        ErrorUi.printLimitYearBeforeStart(2026);
        assertTrue(out.toString().contains("2026"));
    }
}
