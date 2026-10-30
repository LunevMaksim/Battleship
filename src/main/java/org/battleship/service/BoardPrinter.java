package org.battleship.service;

import org.battleship.model.Board;
import org.battleship.model.Cell;


public class BoardPrinter {

    public static void print(Board board) {
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
        for (int i = 0; i < board.getSize(); i++) {
            System.out.print(rowLetter + "  ");
            for (int j = 0; j < board.getSize(); j++) {
                System.out.print(board.getGrid()[i][j].getEmoji() + " ");
            }
            System.out.println();
            rowLetter++;
        }
    }
}