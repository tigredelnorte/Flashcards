package flashcards;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Log {
    private final List<String> logFile = new ArrayList<>();

    public List<String> getLog(){
        return this.logFile;
    }
}
