package seedu.duke.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneralUiTest {

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
    void printDottedLine_outputsLine() {
        GeneralUi.printDottedLine();
        assertTrue(out.toString().contains(GeneralUi.DOTTED_LINE));
    }

    @Test
    void printBordered_wrapsMessageBetweenLines() {
        GeneralUi.printBordered("Hello test");
        String output = out.toString();
        assertTrue(output.contains("Hello test"));
        // Should appear between two dotted lines
        assertTrue(output.indexOf(GeneralUi.DOTTED_LINE) <
                output.indexOf("Hello test"));
    }

    @Test
    void printWithBorder_nullLabel_doesNotPrintLabel() {
        GeneralUi.printWithBorder(null, "Content only");
        String output = out.toString();
        assertTrue(output.contains("Content only"));
        assertFalse(output.contains("null"));
    }

    @Test
    void printWithBorder_withLabel_showsBothLabelAndContent() {
        GeneralUi.printWithBorder("My label", "My content");
        String output = out.toString();
        assertTrue(output.contains("My label"));
        assertTrue(output.contains("My content"));
    }

    @Test
    void printWarning_containsWarningTag() {
        GeneralUi.printWarning("disk full");
        assertTrue(out.toString().contains("[WARNING]"));
        assertTrue(out.toString().contains("disk full"));
    }

    @Test
    void printMessage_outputsExactMessage() {
        GeneralUi.printMessage("Simple message");
        assertTrue(out.toString().contains("Simple message"));
    }

    @Test
    void printWelcome_testMode_doesNotPrintYearRange() {
        GeneralUi.printWelcome(2026, 2030, 8, true);
        String output = out.toString();
        // In test mode, the year-range / limit block should be suppressed
        assertFalse(output.contains("Current Year Range"));
        assertFalse(output.contains("Current Daily Task Limit"));
    }

    @Test
    void printWelcome_normalMode_containsYearRangeAndLimit() {
        GeneralUi.printWelcome(2026, 2030, 5, false);
        String output = out.toString();
        assertTrue(output.contains("2026"));
        assertTrue(output.contains("2030"));
        assertTrue(output.contains("5"));
    }
}
