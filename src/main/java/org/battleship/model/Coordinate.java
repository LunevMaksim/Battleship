package org.battleship.model;

public record Coordinate(int x, int y) {

    public Coordinate {
        if (x < 1 || x > 10 || y < 1 || y > 10) {
            throw new IllegalArgumentException();
        }
    }
}
