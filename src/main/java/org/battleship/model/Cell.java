package org.battleship.model;

public class Cell {
    private State state;

    public Cell(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getEmoji() {
        return state.getEmoji();
    }
}