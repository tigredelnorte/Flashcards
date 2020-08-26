package flashcards;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Application application = new Application();
        if (args.length == 0) {
            application.start();
        } else {
            application.start(args);
        }
    }
}