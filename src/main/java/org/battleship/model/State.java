package org.battleship.model;

public enum State {
    PART_OF_SHIP("\uD83D\uDFE9"),
    EMPTY_SPACE("⬜"),
    HIT("❌"),
    MISS("⭕");

    private final String emoji;

    State(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }
}
