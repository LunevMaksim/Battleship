package org.battleship.player;

import org.battleship.model.Coordinate;

import java.util.Scanner;

public class ConsolePlayer extends Player {
    private static final char ASCII_OFFSET = 'a';
    private final Scanner scanner;

    public ConsolePlayer(int id, int boardSize, Scanner scanner) {
        super(id, boardSize);
        this.scanner = scanner;
    }

    // Запрос координат для обычного выстрела
    public Coordinate inputCoordinateForHit() {
        int size = getCurrBoard().getSize();
        while (true) {
            System.out.println("Игрок " + getId() + ", введите координаты для удара (например: a 1): ");
            String input = scanner.nextLine().trim();
            String[] move = input.split(" ");

            if (move.length < 2) {
                System.out.println("Ошибка! Введите пару координат через пробел (Формат: a 1)");
                continue;
            }

            try {
                int row = move[0].charAt(0) - ASCII_OFFSET;
                int col = Integer.parseInt(move[1]) - 1;

                if (row < 0 || row >= size || col < 0 || col >= size) {
                    System.out.println("Ошибка! Координаты должны быть в интервале [a - j] и [1 - 10]");
                    continue;
                }
                return new Coordinate(row, col);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Вторым параметром должна быть цифра от 1 до 10.");
            }
        }
    }

    // Запрос на использование мины
    public boolean askToUseMine() {
        if (!getHasMine()) return false;

        System.out.println("БОНУС: у вас есть мина для удара радиусом 3х3. Хотите использовать? (да/нет):");
        String answer = scanner.nextLine().trim().toLowerCase();

        return !answer.equals("нет") && !answer.equals("no") && !answer.isEmpty();
    }
}