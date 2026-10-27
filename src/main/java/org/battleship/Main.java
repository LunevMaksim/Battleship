package org.battleship;

import org.battleship.model.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.battleship.model.State.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        String[][] playerOneTableCurr = arrangementTable(scanner);
        String[][] playerTwoTableCurr = arrangementTable(scanner);

        String[][] playerOneTableEnemy = new String[10][10];
        String[][] playerTwoTableEnemy = new String[10][10];

        int move = random.nextInt(1, 3);

        while (endGame(playerOneTableCurr, playerTwoTableCurr) == -1) {

            System.out.println("=== ХОД ИГРОКА " + move + " ===");

            if (move == 1) {
                System.out.println("Ваши корабли:");
                printMatrix(playerOneTableCurr);
                System.out.println("\nПоле противника:");
                printMatrix(playerOneTableEnemy);
            } else {
                System.out.println("Ваши корабли:");
                printMatrix(playerTwoTableCurr);
                System.out.println("\nПоле противника:");
                printMatrix(playerTwoTableEnemy);
            }
            System.out.println();

            int[] hit = inputCoordinateForHit(scanner);

            if (move == 1){
                if (playerTwoTableCurr[hit[0]][hit[1]].equals(PART_OF_SHIP.getEmoji())){
                    System.out.println("Вы попали!");
                    playerOneTableEnemy[hit[0]][hit[1]] = HIT.getEmoji();
                    playerTwoTableCurr[hit[0]][hit[1]] = HIT.getEmoji();
                } else {
                    System.out.println("Вы промахнулись!");
                    playerOneTableEnemy[hit[0]][hit[1]] = MISS.getEmoji();
                    playerTwoTableCurr[hit[0]][hit[1]] = MISS.getEmoji();
                    move = 2;
                }
            } else {
                if (playerOneTableCurr[hit[0]][hit[1]].equals(PART_OF_SHIP.getEmoji())){
                    System.out.println("Вы попали!");
                    playerTwoTableEnemy[hit[0]][hit[1]] = HIT.getEmoji();
                    playerOneTableCurr[hit[0]][hit[1]] = HIT.getEmoji();
                } else {
                    System.out.println("Вы промахнулись!");
                    playerTwoTableEnemy[hit[0]][hit[1]] = MISS.getEmoji();
                    playerOneTableCurr[hit[0]][hit[1]] = MISS.getEmoji();
                    move = 1;
                }
            }
        }

        printWinner(playerOneTableCurr, playerTwoTableCurr);
    }

    static void printWinner(String[][] playerOneTableCurr, String[][] playerTwoTableCurr){
        int winner = endGame(playerOneTableCurr, playerTwoTableCurr);

        System.out.println("=== ИГРА ЗАКОНЧЕНА!!! ===");
        System.out.println("=== ПОБЕДИТЕЛЬ - ИГРОК " + winner + " ===");

        System.out.println("Поле игрока 1:");
        printMatrix(playerOneTableCurr);
        System.out.println("Поле игрока 2:");
        printMatrix(playerTwoTableCurr);
    }

    static int[] inputCoordinateForHit(Scanner scanner){
        System.out.println("Введите координаты для удара: ");
        String[] move = scanner.nextLine().split(" ");

        int len = (int)move[0].charAt(0) - 97; // Слева буквы
        int height = Integer.parseInt(move[1]) - 1; // Сверху цифры

        return new int[]{len, height};
    }

    static int endGame(String[][] arrOne, String[][] arrTwo){
        boolean noShipsOne = true;
        boolean noShipsTwo = true;

        for (int i = 0; i < arrOne.length; i++) {
            for (int j = 0; j < arrOne.length; j++) {
                if (arrOne[i][j].equals(PART_OF_SHIP.getEmoji())){
                    noShipsOne = false;
                    break;
                }
            }
        }
        for (int i = 0; i < arrTwo.length; i++) {
            for (int j = 0; j < arrTwo.length; j++) {
                if (arrTwo[i][j].equals(PART_OF_SHIP.getEmoji())){
                    noShipsTwo = false;
                    break;
                }
            }
        }

        if (noShipsOne) return 1;
        if (noShipsTwo) return 2;
        return -1;
    }

    // Запись для своего поля
    static String[][] arrangementTable(Scanner scanner) {
        String[][] table = new String[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                table[i][j] = State.EMPTY_SPACE.getEmoji();
            }
        }

        checkFour(scanner, table);
        checkThree(scanner, table);
        checkTwo(scanner, table);
        checkOne(scanner, table);

        cleaning(scanner);

        return table;
    }

    // Перевод строчки в координаты и направление
    static List<Object> toCoordinates(String[] fourDeck, int deck){
        int len = (int)fourDeck[0].charAt(0) - 97; // Слева буквы
        int height = Integer.parseInt(fourDeck[1]) - 1; // Сверху цифры
        char direction = fourDeck[2].charAt(0); // Направление корабля

        List<Object> objectList = new ArrayList<>();

        objectList.add(len);
        objectList.add(height);
        objectList.add(direction);
        objectList.add(deck);

        return objectList;
    }

    // Расстановка кораблей на поле по направлению
    static void stagingOfFour(String[][] playerOneTableCurr, List<Object> coordinate){
        int len = (int) coordinate.get(0); // Буква
        int height = (int) coordinate.get(1); // Цифра
        char direction = (char) coordinate.get(2);
        int deck = (int) coordinate.get(3);

        switch (direction){
            case 'r':
                for (int i = height; i < height + deck; i++) {
                    playerOneTableCurr[len][i] = PART_OF_SHIP.getEmoji();
                }
                break;
            case 'd':
                for (int i = len; i < len + deck; i++) {
                    playerOneTableCurr[i][height] = PART_OF_SHIP.getEmoji();
                }
                break;
            case 'l':
                for (int i = height; i > height - deck; i--) {
                    playerOneTableCurr[len][i] = PART_OF_SHIP.getEmoji();
                }
                break;
            case 'u':
                for (int i = len; i > len - deck; i--) {
                    playerOneTableCurr[i][height] = PART_OF_SHIP.getEmoji();
                }
                break;
        }
    }

    static void checkFour(Scanner scanner, String[][] arr){
        System.out.println("Расставьте корабли:");
        System.out.println("Первый: 4-х палубный");
        String[] fourDeck = scanner.nextLine().split(" ");
        List<Object> coordinateFour = toCoordinates(fourDeck, 4);
        stagingOfFour(arr, coordinateFour);
    }

    static void checkThree(Scanner scanner, String[][] arr){
        for (int i = 0; i < 2; i++) {
            System.out.println("Расставьте корабли:");
            System.out.println("Первый: 3-х палубный");
            String[] threeDeck = scanner.nextLine().split(" ");
            List<Object> coordinateThree = toCoordinates(threeDeck, 3);
            stagingOfFour(arr, coordinateThree);
        }
    }

    static void checkTwo(Scanner scanner, String[][] arr){
        for (int i = 0; i < 3; i++) {
            System.out.println("Расставьте корабли:");
            System.out.println("Первый: 2-х палубный");
            String[] twoDeck = scanner.nextLine().split(" ");
            List<Object> coordinateTwo = toCoordinates(twoDeck, 2);
            stagingOfFour(arr, coordinateTwo);
        }
    }

    static void checkOne(Scanner scanner, String[][] arr){
        for (int i = 0; i < 4; i++) {
            System.out.println("Расставьте корабли:");
            System.out.println("Первый: 1-о палубный");
            String[] oneDeck = scanner.nextLine().split(" ");
            List<Object> coordinateOne = toCoordinates(oneDeck, 1);
            stagingOfFour(arr, coordinateOne);
        }
    }

    static void printMatrix(String[][] arr){
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

        for (int i = 0; i < 10; i++) {
            System.out.print(rowLetter + "  ");

            for (int j = 0; j < 10; j++) {
                if (arr[i][j] == null) {
                    System.out.print(State.EMPTY_SPACE.getEmoji() + " ");
                } else {
                    System.out.print(arr[i][j] + " ");
                }
            }
            System.out.println();
            rowLetter++;
        }
    }

    static void cleaning(Scanner scanner) {
        System.out.println("Готово! Нажмите Enter, чтобы передать ход другому игроку...");
        scanner.nextLine();
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}