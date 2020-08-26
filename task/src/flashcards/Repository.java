package flashcards;

import java.util.*;
import java.util.stream.Collectors;

public class Repository {
    private final List<Flashcard> list = new ArrayList<>();

    public void add(Flashcard card) {
        if (card == null){
            return;
        }
        list.add(card);
    }

    public void addOrUpdate(Flashcard card) {
        try {
            Flashcard card1 = this.getByCardName(card.getCardName());
            card1.setDefinition(card.getDefinition());
            card1.setMistakes(card.getMistake());
        } catch (FlashcardException e) {
            list.add(card);
        }
    }

    public void remove(String cardName) {
        Flashcard card;
        try {
                card = this.getByCardName(cardName);
            } catch (FlashcardException e) {
            throw new FlashcardException("Can't remove \"" + cardName + "\": there is no such card.");
        }
        list.remove(card);
    }

    public Flashcard getCard(int index) {
        return list.get(index);
    }

    public List<Flashcard> getAllCards() {
        return list;
    }

    public int size() {
        return list.size();
    }

    public Flashcard getRandomCard() {
        Random random = new Random();
        int i = random.nextInt(list.size());
        return list.get(i);
    }

    public Flashcard getByCardName(String cardName) throws FlashcardException {
        return list.stream()
                .filter(x -> x.getCardName()
                .equals(cardName)).findAny()
                .orElseThrow(() -> new FlashcardException("No Card with name " + cardName));
    }

    public Flashcard getByCardDefinition(String cardDefinition) throws FlashcardException {
        return list.stream()
                .filter(x -> x.getDefinition()
                        .equals(cardDefinition)).findAny()
                .orElseThrow(() -> new FlashcardException("No Card with definition " + cardDefinition));
    }
    public boolean containsByCardName(String cardName) {
        return list.stream()
                .anyMatch(x -> x.getCardName().equals(cardName));
    }
    public boolean containsByCardDefinition(String cardDefinition) {
        return list.stream()
                .anyMatch(x -> x.getDefinition().equals(cardDefinition));
    }

    public void resetStats() {
        list.stream().forEach(x -> x.setMistakeToZero());
    }

    public String maxMistakesCards() {

        int mistakes = list.size() ==0 ? 0 : list.stream().mapToInt(x -> x.getMistake()).max().getAsInt();
        if (mistakes > 0){
            List<String> cities = list.stream().filter(x -> x.getMistake() == mistakes)
                    .map(x -> x.getCardName()).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            String isAre = cities.size() == 1 ? "The hardest card is": "The hardest cards are";
            String itThem = cities.size() == 1 ? "it.": "them.";
            sb.append(isAre);
            for (String city : cities) {
                sb.append(" \"" + city + "\",");
            }
            sb.deleteCharAt(sb.lastIndexOf(",")).toString();
            sb.append(". You have " + mistakes + " errors answering " + itThem);
            return sb.toString();
        } else return "There are no cards with errors.";
    }
}
