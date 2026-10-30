package org.battleship.player;

import org.battleship.model.Coordinate;

import java.util.Scanner;

public class ConsolePlayer extends Player {
    private static final char ASCII_OFFSET = 'а'; // Или 'a' в зависимости от раскладки (лучше английская 'a')
    private final Scanner scanner;

    public ConsolePlayer(int id, int boardSize, Scanner scanner) {
        super(id, boardSize);
        this.scanner = scanner;
    }

    public Coordinate inputCoordinateForHit() {
        int size = currBoard.getSize();
        while (true) {
            System.out.println("Игрок " + id + ", введите координаты для удара (например: a 1): ");
            String input = scanner.nextLine().trim();
            String[] move = input.split(" ");

            if (move.length < 2) {
                System.out.println("Ошибка! Введите пару координат через пробел (Формат: a 1)");
                continue;
            }

            try {
                // Исправлено: возвращены знаки минус
                int row = move[0].charAt(0) - 'a'; // или ASCII_OFFSET
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

    public boolean askToUseMine() {
        if (!hasMine) return false;

        System.out.println("БОНУС: у вас есть мина для удара радиусом 3х3. Хотите использовать? (да/нет):");
        String answer = scanner.nextLine().trim().toLowerCase();

        return !answer.equals("нет") && !answer.equals("no") && !answer.isEmpty();
    }
}