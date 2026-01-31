package ru.yandex.practicum.sleeptracker;

public enum Chronotype {

    LARK("Жаворонок"),

    OWL("Сова"),

    PIGEON("Голубь");

    private final String displayName;

    Chronotype(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

