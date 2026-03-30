package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.CommandHelp;

//@@author benguy6
public class HelpCommand implements Command {
    public static final int HELP_MIN_LENGTH = 1;
    public static final int INDEX_OF_HELPTOPIC = 1;

    private final String[] sentence;

    public HelpCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length == HELP_MIN_LENGTH) {
            System.out.println(CommandHelp.getHelp(null));
        } else {
            String topic = sentence[INDEX_OF_HELPTOPIC];
            System.out.println(CommandHelp.getHelp(topic));
        }
    }
}
