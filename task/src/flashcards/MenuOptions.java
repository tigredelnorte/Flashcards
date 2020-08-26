package flashcards;

public enum MenuOptions {
    ADD("add"),
    REMOVE("remove"),
    ASK("ask"),
    IMPORT("import"),
    EXPORT("export"),
    LIST("list"),
    LOG("log"),
    HARDEST_CARD("hardest card"),
    RESET_STATS("reset stats"),
    EXIT("exit");
    private String name;

    MenuOptions(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return name;
    }
}
