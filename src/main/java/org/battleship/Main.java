package org.battleship;

import org.battleship.service.GameEngine;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();
    }
}