package org.battleship.model;

import static org.battleship.model.State.*;

public class Board {
    private final int size;
    private final Cell[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell(EMPTY_SPACE);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Cell[][] getGrid() {
        return grid;
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
                System.out.print(grid[i][j].getEmoji() + " ");
            }
            System.out.println();
            rowLetter++;
        }
    }

    // Проверка выхода за границы
    public boolean isOutOfBounds(int row, int col, char direction, int deck) {
        switch (direction) {
            case 'r': return col + deck > size;
            case 'l': return col - deck + 1 < 0;
            case 'd': return row + deck > size;
            case 'u': return row - deck + 1 < 0;
            default: return true;
        }
    }

    // Проверка ауры (касаний и пересечений)
    public boolean canPlaceShip(int row, int col, char direction, int deck) {
        int startX = row, endX = row;
        int startY = col, endY = col;

        switch (direction) {
            case 'r': endY = col + deck - 1; break;
            case 'l': startY = col - deck + 1; break;
            case 'd': endX = row + deck - 1; break;
            case 'u': startX = row - deck + 1; break;
        }

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                int rMin = Math.max(0, i - 1);
                int rMax = Math.min(size - 1, i + 1);
                int cMin = Math.max(0, j - 1);
                int cMax = Math.min(size - 1, j + 1);

                for (int r = rMin; r <= rMax; r++) {
                    for (int c = cMin; c <= cMax; c++) {
                        if (grid[r][c].getState().equals(PART_OF_SHIP)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Размещение корабля
    public void placeShip(int row, int col, char direction, int deck) {
        switch (direction) {
            case 'r':
                for (int j = col; j < col + deck; j++) grid[row][j].setState(PART_OF_SHIP);
                break;
            case 'd':
                for (int i = row; i < row + deck; i++) grid[i][col].setState(PART_OF_SHIP);
                break;
            case 'l':
                for (int j = col; j > col - deck; j--) grid[row][j].setState(PART_OF_SHIP);
                break;
            case 'u':
                for (int i = row; i > row - deck; i--) grid[i][col].setState(PART_OF_SHIP);
                break;
        }
    }

    // Проверка, остались ли живые корабли
    public boolean hasAliveShips() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j].getState().equals(PART_OF_SHIP)) {
                    return true;
                }
            }
        }
        return false;
    }
}