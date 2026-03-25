package seedu.duke.command;

import seedu.duke.appcontainer.AppContainer;
import seedu.duke.ui.CommandHelp;

public class HelpCommand implements Command {
    private final String[] sentence;

    public HelpCommand(String[] sentence) {
        this.sentence = sentence;
    }

    @Override
    public void execute(AppContainer container) {
        if (sentence.length == 1) {
            System.out.println(CommandHelp.getHelp(null));
        } else {
            String topic = sentence[1];
            System.out.println(CommandHelp.getHelp(topic));
        }
    }
}