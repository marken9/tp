package seedu.duke.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeadlineUiTest {

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

    // --- printItemDeleted: null index branch (delete all) ---

    @Test
    void printItemDeleted_nullIndex_printsDeleteAllMessage() {
        DeadlineUi.printItemDeleted("deadline", null, 0);
        assertTrue(out.toString().contains("All deadlines in this category have been deleted."));
    }

    @Test
    void printItemDeleted_nullIndex_doesNotPrintSingleDeleteMessage() {
        DeadlineUi.printItemDeleted("deadline", null, 0);
        assertFalse(out.toString().contains("Deleted deadline"));
    }

    // --- printItemDeleted: non-null index branch (delete single) ---

    @Test
    void printItemDeleted_nonNullIndex_printsSingleDeleteMessage() {
        // index=0, categoryIndex=0 → displays as "Deleted deadline 1 from category 1"
        DeadlineUi.printItemDeleted("deadline", 0, 0);
        assertTrue(out.toString().contains("Deleted deadline 1 from category 1"));
    }

    @Test
    void printItemDeleted_nonNullIndex_doesNotPrintDeleteAllMessage() {
        DeadlineUi.printItemDeleted("deadline", 0, 0);
        assertFalse(out.toString().contains("All deadlines"));
    }

    @Test
    void printItemDeleted_nonNullIndex_displaysOneBasedIndex() {
        // index=2 should display as 3, categoryIndex=1 should display as 2
        DeadlineUi.printItemDeleted("deadline", 2, 1);
        assertTrue(out.toString().contains("Deleted deadline 3 from category 2"));
    }

    @Test
    void printItemDeleted_nonNullIndex_typeAppearsInOutput() {
        DeadlineUi.printItemDeleted("event", 0, 0);
        assertTrue(out.toString().contains("event"));
    }
}
