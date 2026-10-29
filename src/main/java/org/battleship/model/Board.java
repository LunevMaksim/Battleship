package org.battleship.model;

import static org.battleship.model.State.*;

public class Board {
    private final String[][] grid;
    private final int size;

    public Board(int size) {
        this.size = size;
        this.grid = new String[size][size];
        initializeBoard();
    }

    public int getSize() {
        return size;
    }

    public String[][] getGrid() {
        return grid;
    }

    // Инициализация пустого поля
    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = EMPTY_SPACE.getEmoji();
            }
        }
    }

    // Печать матрицы
    public void printMatrix() {
        System.out.print("    ");
        for (int j = 1; j <= 3; j++) {
            System.out.print(j + "  ");
        }
        System.out.print(" ");
        for (int j = 4; j <= 7; j++) {
            System.out.print(j + "  ");
        }
        System.out.print(" ");
        for (int j = 8; j <= 10; j++) {
            System.out.print(j + "  ");
        }
        System.out.println();

        char rowLetter = 'a';
        for (int i = 0; i < size; i++) {
            System.out.print(rowLetter + "  ");
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == null) {
                    System.out.print(EMPTY_SPACE.getEmoji() + " ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println();
            rowLetter++;
        }
    }

    // Проверка выхода за границы
    public boolean isOutOfBounds(int len, int height, char direction, int deck) {
        switch (direction) {
            case 'r': return height + deck > size;
            case 'l': return height - deck + 1 < 0;
            case 'd': return len + deck > size;
            case 'u': return len - deck + 1 < 0;
            default: return true;
        }
    }

    // Проверка касаний и пересечений
    public boolean canPlaceShip(int len, int height, char direction, int deck) {
        int startX = len, endX = len;
        int startY = height, endY = height;

        switch (direction) {
            case 'r': endY = height + deck - 1; break;
            case 'l': startY = height - deck + 1; break;
            case 'd': endX = len + deck - 1; break;
            case 'u': startX = len - deck + 1; break;
        }

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                int rMin = Math.max(0, i - 1);
                int rMax = Math.min(size - 1, i + 1);
                int cMin = Math.max(0, j - 1);
                int cMax = Math.min(size - 1, j + 1);

                for (int r = rMin; r <= rMax; r++) {
                    for (int c = cMin; c <= cMax; c++) {
                        if (grid[r][c].equals(PART_OF_SHIP.getEmoji())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Размещение корабля на доске
    public void placeShip(int len, int height, char direction, int deck) {
        switch (direction) {
            case 'r':
                for (int i = height; i < height + deck; i++) grid[len][i] = PART_OF_SHIP.getEmoji();
                break;
            case 'd':
                for (int i = len; i < len + deck; i++) grid[i][height] = PART_OF_SHIP.getEmoji();
                break;
            case 'l':
                for (int i = height; i > height - deck; i--) grid[len][i] = PART_OF_SHIP.getEmoji();
                break;
            case 'u':
                for (int i = len; i > len - deck; i--) grid[i][height] = PART_OF_SHIP.getEmoji();
                break;
        }
    }

    // Проверка, остались ли еще живые корабли на поле
    public boolean hasShipsAlive() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j].equals(PART_OF_SHIP.getEmoji())) {
                    return false;
                }
            }
        }
        return true;
    }
}