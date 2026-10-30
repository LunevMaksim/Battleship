package org.battleship.model;

public class PlayerKnowledge {
    private final Board knowledgeBoard;


    public PlayerKnowledge(int size) {
        this.knowledgeBoard = new Board(size);
    }


    public Board getKnowledgeBoard() {
        return knowledgeBoard;
    }


    public void recordShot(Coordinate coordinate, State resultState) {
        knowledgeBoard.getGrid()[coordinate.row()][coordinate.col()].setState(resultState);
    }
}