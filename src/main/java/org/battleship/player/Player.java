package org.battleship.player;

import org.battleship.model.Board;
import org.battleship.model.Cell;

public class Player {
    private final int id;
    private final Board currBoard;
    private final Board enemyBoard;
    private boolean hasMine;

    public Player(int id, int boardSize) {
        this.id = id;
        this.currBoard = new Board(boardSize);
        this.enemyBoard = new Board(boardSize);
        this.hasMine = true;
    }

    public int getId() {
        return id;
    }

    public Board getCurrBoard() {
        return currBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public boolean getHasMine() {
        return hasMine;
    }

    // Проверка, остались ли у игрока живые корабли
    public boolean hasAliveShips() {
        Cell[][] grid = currBoard.getGrid();
        int size = currBoard.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j].equals(org.battleship.model.State.PART_OF_SHIP.getEmoji())) {
                    return true;
                }
            }
        }
        return false;
    }
}