package org.battleship;

import org.battleship.service.GameEngine;
import org.battleship.service.GameSettings;

public class Main {
    public static void main(String[] args) {
        GameSettings settings = new GameSettings(10, System.currentTimeMillis());

        GameEngine engine = new GameEngine(settings);
        engine.start();
    }
}