package org.battleship.player;

import org.battleship.model.Board;
import org.battleship.model.State;

public abstract class Player {
    protected final int id;
    protected final Board currBoard;
    protected final Board enemyBoard;
    protected boolean hasMine;

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

    public boolean hasMine() {
        return hasMine;
    }

    public void useMine() {
        this.hasMine = false;
    }

    public boolean hasAliveShips() {
        int size = currBoard.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currBoard.getGrid()[i][j].getState().equals(State.PART_OF_SHIP)) {
                    return true;
                }
            }
        }
        return false;
    }
}