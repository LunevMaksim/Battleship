package org.battleship.service;

import org.battleship.model.Coordinate;
import org.battleship.model.State;
import org.battleship.player.ConsolePlayer;
import org.battleship.player.Player;

import java.util.Random;
import java.util.Scanner;

public class GameEngine {
    private static final int BOARD_SIZE = 10;
    private static final char ASCII_OFFSET = 'a';

    private final Scanner scanner;
    private final Random random;
    private final PlacementValidator validator;
    private ConsolePlayer player1;
    private ConsolePlayer player2;

    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.validator = new PlacementValidator();
    }

    public void start() {
        System.out.println("=== ИГРА МОРСКОЙ БОЙ (OOP ARCHITECTURE) ===");

        player1 = new ConsolePlayer(1, BOARD_SIZE, scanner);
        player2 = new ConsolePlayer(2, BOARD_SIZE, scanner);

        System.out.println("Игрок 1 расставляет корабли:");
        arrangementTable(player1);

        System.out.println("Игрок 2 расставляет корабли:");
        arrangementTable(player2);

        int currentMove = random.nextInt(1, 3);

        while (true) {
            ConsolePlayer activePlayer = (currentMove == 1) ? player1 : player2;
            ConsolePlayer opponent = (currentMove == 1) ? player2 : player1;

            System.out.println("\n=== ХОД ИГРОКА " + activePlayer.getId() + " ===");
            System.out.println("Ваши корабли:");
            activePlayer.getCurrBoard().printMatrix();
            System.out.println("\nПоле противника:");
            activePlayer.getEnemyBoard().printMatrix();
            System.out.println();

            boolean wantToUseMine = activePlayer.askToUseMine();

            if (wantToUseMine) {
                activePlayer.useMine();
                boolean hitInShip = hitMine(activePlayer, opponent);
                if (!hitInShip) {
                    currentMove = (currentMove == 1) ? 2 : 1;
                }
            } else {
                Coordinate hit = activePlayer.inputCoordinateForHit();
                var enemyCurrGrid = opponent.getCurrBoard().getGrid();
                var activeEnemyGrid = activePlayer.getEnemyBoard().getGrid();

                int r = hit.row();
                int c = hit.col();

                if (enemyCurrGrid[r][c].getState().equals(State.PART_OF_SHIP)) {
                    System.out.println("Вы попали!");
                    activeEnemyGrid[r][c].setState(State.HIT);
                    enemyCurrGrid[r][c].setState(State.HIT);

                    if (!opponent.hasAliveShips()) {
                        printWinner(activePlayer);
                        break;
                    }
                } else {
                    System.out.println("Вы промахнулись!");
                    activeEnemyGrid[r][c].setState(State.MISS);
                    enemyCurrGrid[r][c].setState(State.MISS);
                    currentMove = (currentMove == 1) ? 2 : 1;
                }
            }
        }
    }

    private void arrangementTable(ConsolePlayer player) {
        checkShips(player, 4, 1);
        checkShips(player, 3, 2);
        checkShips(player, 2, 3);
        checkShips(player, 1, 4);

        cleaning();
    }

    private void checkShips(ConsolePlayer player, int deck, int count) {
        var board = player.getCurrBoard();
        for (int i = 0; i < count; i++) {
            System.out.println("Игрок " + player.getId() + ", расставьте корабли:");
            System.out.println((i + 1) + "-й: " + deck + "-палубный корабль");

            Coordinate coord;
            char direction;

            while (true) {
                System.out.println("Введите координаты и направление (например: a 1 r) [r-вправо, l-влево, d-вниз, u-вверх]: ");
                String input = scanner.nextLine().toLowerCase().trim();
                String[] parts = input.split(" ");

                if (parts.length < 3) {
                    System.out.println("Ошибка! Введите координаты и направление через пробел (например: a 1 r)");
                    continue;
                }

                try {
                    int row = parts[0].charAt(0) - ASCII_OFFSET;
                    int col = Integer.parseInt(parts[1]) - 1;
                    direction = parts[2].charAt(0);

                    if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
                        System.out.println("Ошибка! Начальные координаты вне пределов доски.");
                        continue;
                    }

                    if (validator.isOutOfBounds(board, row, col, direction, deck)) {
                        System.out.println("Ошибка! Корабль такой длины не поместится в этом направлении.");
                        continue;
                    }

                    if (!validator.canPlaceShip(board, row, col, direction, deck)) {
                        System.out.println("Ошибка! Корабль пересекается с другим или касается его.");
                        continue;
                    }

                    coord = new Coordinate(row, col);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка! Вторым параметром должна быть цифра.");
                }
            }

            board.placeShip(coord.row(), coord.col(), direction, deck);
        }
    }

    private boolean hitMine(ConsolePlayer activePlayer, ConsolePlayer opponent) {
        Coordinate hit = activePlayer.inputCoordinateForHit();
        boolean hitInShip = false;

        var enemyGrid = opponent.getCurrBoard().getGrid();
        var activeEnemyGrid = activePlayer.getEnemyBoard().getGrid();

        int startX = Math.max(0, hit.row() - 1);
        int endX = Math.min(BOARD_SIZE - 1, hit.row() + 1);
        int startY = Math.max(0, hit.col() - 1);
        int endY = Math.min(BOARD_SIZE - 1, hit.col() + 1);

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (enemyGrid[i][j].getState().equals(State.PART_OF_SHIP)) {
                    hitInShip = true;
                    enemyGrid[i][j].setState(State.HIT);
                    activeEnemyGrid[i][j].setState(State.HIT);
                } else if (enemyGrid[i][j].getState().equals(State.EMPTY_SPACE)) {
                    enemyGrid[i][j].setState(State.MISS);
                    activeEnemyGrid[i][j].setState(State.MISS);
                }
            }
        }
        return hitInShip;
    }

    private void printWinner(Player winner) {
        System.out.println("=== ИГРА ЗАКОНЧЕНА!!! ===");
        System.out.println("=== ПОБЕДИТЕЛЬ - ИГРОК " + winner.getId() + " ===");

        System.out.println("Поле игрока 1:");
        player1.getCurrBoard().printMatrix();
        System.out.println("Поле игрока 2:");
        player2.getCurrBoard().printMatrix();
    }

    private void cleaning() {
        System.out.println("Готово! Нажмите Enter, чтобы передать ход другому игроку...");
        scanner.nextLine();
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}