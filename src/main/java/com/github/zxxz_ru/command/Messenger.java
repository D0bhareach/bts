package com.github.zxxz_ru.command;

import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.List;

@Component
public class Messenger {
    private final PrintStream out = System.out;
    private final PrintStream err = System.err;
    private final String help;

    public Messenger() {
        HelpCommandBuilder hcb = new HelpCommandBuilder();
        this.help = hcb.composeHelpMessage();
    }

    public <T> void print(List<T> list) {
        if (list.size() == 0) {
            out.println("No Result.");
            return;
        }
        for (T ent : list) {
            out.print(ent.toString());
        }
    }

    public void print(int i, String... messages) {
        switch (i) {
            case 1:
                out.println(help);
                break;
            case 2:
                out.println("Use -h or help for advice. To exit type: quit or Ctrl-C.");
                break;
            case 3:
                out.println("Wrong usage! See help. [-h; help]");
                break;
            case 4:
                // Java really lacks dynamic varargs!
                // But this is the only crappy way to make optional arguments.
                if (messages != null && messages.length > 0) {
                    for (String s : messages) {
                        out.println(s);
                    }
                }
                break;
        }
    }

    public void printMessage(String s) {
        out.println(s);
    }

    public void printAdvice() {
        String advice = "Use -h or help for advice. To exit type: quit or Ctrl-C.";
        out.println(advice);
    }

    public void printHelp() {
        out.println(help);
    }

    public void printError(String s) {
        err.println(s);
    }
}
