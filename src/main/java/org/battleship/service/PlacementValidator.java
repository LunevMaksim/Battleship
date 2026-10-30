package org.battleship.service;

import org.battleship.model.Board;
import org.battleship.model.State;

public class PlacementValidator {


    public boolean isOutOfBounds(Board board, int row, int col, char direction, int deck) {
        int size = board.getSize();
        switch (direction) {
            case 'r': return col + deck > size;
            case 'l': return col - deck + 1 < 0; // Восстановлен минус
            case 'd': return row + deck > size;
            case 'u': return row - deck + 1 < 0; // Восстановлен минус
            default: return true;
        }
    }

    public boolean canPlaceShip(Board board, int row, int col, char direction, int deck) {
        int size = board.getSize();
        int startX = row, endX = row;
        int startY = col, endY = col;

        // Восстановлены знаки минус для границ корабля
        switch (direction) {
            case 'r': endY = col + deck - 1; break;
            case 'l': startY = col - deck + 1; break;
            case 'd': endX = row + deck - 1; break;
            case 'u': startX = row - deck + 1; break;
        }

        var grid = board.getGrid();
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                int rMin = Math.max(0, i - 1);
                int rMax = Math.min(size - 1, i + 1);
                int cMin = Math.max(0, j - 1);
                int cMax = Math.min(size - 1, j + 1);

                for (int r = rMin; r <= rMax; r++) {
                    for (int c = cMin; c <= cMax; c++) {
                        if (grid[r][c].getState().equals(State.PART_OF_SHIP)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}