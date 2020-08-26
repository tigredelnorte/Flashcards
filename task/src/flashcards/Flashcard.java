package flashcards;

public class Flashcard {
    private String cardName;
    private String definition;
    private int mistakes;

    public Flashcard(String cardName, String definition) {
        this.cardName = cardName;
        this.definition = definition;
        this.mistakes = 0;
    }

    public Flashcard(String cardName, String definition, int mistakes) {
        this.cardName = cardName;
        this.definition = definition;
        this.mistakes = mistakes;
    }

    public Flashcard() {
        this.cardName = null;
        this.definition = null;
        this.mistakes = 0;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "Flashcard: " +
                cardName +
                " = " + definition +
                ", mistakes = " + mistakes;
    }

    public int getMistake() {
        return mistakes;
    }

    public int addMistake() {
        return this.mistakes += 1;
    }
    public void setMistakeToZero() {
        this.mistakes = 0;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }
}

