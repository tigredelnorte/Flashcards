package flashcards;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import flashcards.Main;

public class Application {
    private final Repository flashcardsRepo = new Repository();
    private final Log logFile = new Log();

    public void start() {
        boolean run = true;
        while (run) {
            outputMsg("Input the action (add, remove, import, export, ask, list, log, resets stats, hardest card, exit):");
            try {
                run = userMenu();
            } catch (FlashcardException | IOException e) {
                outputMsg(e.getMessage());
            }
            outputMsg();
        }
        outputMsg("Bye bye!");
    }

    public void start(String[] args) {
        String importOnStartFile = null;
        String exportOnExitFile = null;
        boolean importFromDB = false;
        boolean exportToDB = false;
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].contains("import")) {
                importOnStartFile = args[i + 1];
                importFromDB = true;
            } else if (args[i].contains("export")) {
                exportOnExitFile = args[i + 1];
                exportToDB = true;
            }
        }
        if (importFromDB) {
            importFrom(importOnStartFile);
        }
        boolean run = true;
        while (run) {
            if (args.length > 0)
                outputMsg("Input the action (add, remove, import, export, ask, list, log, resets stats, hardest card, exit):");
            try {
                run = userMenu();
            } catch (FlashcardException | IOException e) {
                outputMsg(e.getMessage());
            }
            outputMsg();
        }
        outputMsg("Bye bye!");
        if(exportToDB) {
            export(exportOnExitFile);
        }
    }

    private String getInput() {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        logFile.getLog().add("> " + text);
        return text;
    }

    private boolean userMenu() throws IOException {
        String input = getInput();
            String menuOption = input.toLowerCase();
            switch (menuOption) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    importFrom();
                    break;
                case "export":
                    export();
                    break;
                case "ask":
                    ask();
                    break;
                case "list":
                    list();
                    break;
                case "log":
                    log();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                case "exit":
                    return false;
                default:
                    outputMsg("Command not known. Please try once again");
            }
        return true;
    }

        private void add () throws FlashcardException {
            outputMsg("The card:");
            String cardName = getInput();
            if (flashcardsRepo.containsByCardName(cardName)) {
                throw new FlashcardException("The card \"" + cardName + "\" already exists.");
            }
            outputMsg("The definition of the card:");
            String definition = getInput();
            if (flashcardsRepo.containsByCardDefinition(definition)) {
                throw new FlashcardException("The definition \"" + definition + "\" already exists.");
            }
            flashcardsRepo.add(new Flashcard(cardName, definition));
            outputMsg("The pair (\"" + cardName + ":" + definition + "\") has been added.");
        }

        private void remove () {
            outputMsg("The card:");
            String cardName = getInput();
            if (flashcardsRepo.containsByCardName(cardName)) {
                flashcardsRepo.remove(cardName);
                outputMsg("The card has been removed.");
            } else outputMsg("Can't remove \"" + cardName + "\": there is no such card.");
        }

        private void ask () {
            outputMsg("How many times to ask?");
            int askTimes = Integer.parseInt(getInput());
            for (int i = 0; i < askTimes; i++) {
                Flashcard cardAsked = flashcardsRepo.getRandomCard();
                outputMsg("Print the definition of \"" + cardAsked.getCardName() + "\":");
                String answer = getInput();
                if (answer.equalsIgnoreCase(cardAsked.getDefinition())) {
                    outputMsg("Correct!");
                } else if (flashcardsRepo.containsByCardDefinition(answer)) {
                    outputMsg("Wrong. The right answer is \"" + cardAsked.getDefinition() + "\", " +
                            "but your definition is correct for \"" + flashcardsRepo.getByCardDefinition(answer).getCardName() + "\".");
                    addMistake(cardAsked);
                } else {
                    outputMsg("Wrong. The right answer is \"" + cardAsked.getDefinition() + "\"");
                    addMistake(cardAsked);
                }
            }
        }

        private int addMistake (Flashcard cardAsked){
            return flashcardsRepo.getByCardName(cardAsked.getCardName()).addMistake();
        }

        private void export () {
            outputMsg("File name:");
            String fileWriteName = getInput();
            File fileToWrite = new File(fileWriteName);
            int counterW = 0;
            try (FileWriter writer = new FileWriter(fileToWrite)) {
                for (Flashcard entry : flashcardsRepo.getAllCards()) {
                    String cardToWrite = entry.getCardName();
                    String definitionToWrite = entry.getDefinition();
                    int mistakesToWrite = entry.getMistake();
                    writer.write(cardToWrite + "=" + definitionToWrite + "=" + mistakesToWrite + "\n");
                    counterW++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputMsg(counterW + " cards have been saved.");
            outputMsg();
        }

    private void export (String file) {
        File fileToWrite = new File(file);
        int counterW = 0;
        try (FileWriter writer = new FileWriter(fileToWrite)) {
            for (Flashcard entry : flashcardsRepo.getAllCards()) {
                String cardToWrite = entry.getCardName();
                String definitionToWrite = entry.getDefinition();
                int mistakesToWrite = entry.getMistake();
                writer.write(cardToWrite + "=" + definitionToWrite + "=" + mistakesToWrite + "\n");
                counterW++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputMsg(counterW + " cards have been saved.");
        outputMsg();
    }

        private void importFrom () {
            outputMsg("File name:");
            String fileName = getInput();
            File file = new File(fileName);
            try (Scanner scanner = new Scanner(file)) {
                int counter = 0;
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split("=");
                    flashcardsRepo.addOrUpdate(new Flashcard(line[0], line[1], Integer.parseInt(line[2])));
                    counter++;
                }
                outputMsg(counter + " cards have been loaded");
            } catch (FileNotFoundException e) {
                outputMsg("File not found.");
            }
            outputMsg();
        }

    private void importFrom (String fileName) {
        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            int counter = 0;
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("=");
                flashcardsRepo.addOrUpdate(new Flashcard(line[0], line[1], Integer.parseInt(line[2])));
                counter++;
            }
            outputMsg(counter + " cards have been loaded");
        } catch (FileNotFoundException e) {
            outputMsg("File not found.");
        }
        outputMsg();
    }

        private void list () {
            outputMsg(flashcardsRepo.getAllCards().toString());
        }

        private void log () throws IOException {
            outputMsg("File name:");
            String file = getInput();
            FileWriter logWritter = new FileWriter(new File(file));
            for (String text : logFile.getLog()) {
                logWritter.write(text + "\n");
            }
            outputMsg("The log has been saved");
        }

        private void outputMsg (String text){
            System.out.println(text);
            logFile.getLog().add((text));
        }
        private void outputMsg () {
            System.out.println();
            logFile.getLog().add((""));
        }

        private void resetStats () {
            flashcardsRepo.resetStats();
            outputMsg("Card statistics has been reset.");
        }

        private void hardestCard () {
            outputMsg(flashcardsRepo.maxMistakesCards());
        }
    }

