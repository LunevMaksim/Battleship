package org.battleship;

import org.battleship.model.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.battleship.model.State.*;

public class Main {

    static final int BOARD_SIZE = 10;
    static final char ASCII_OFFSET = 'a';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        String[][] playerOneTableCurr = arrangementTable(scanner);
        String[][] playerTwoTableCurr = arrangementTable(scanner);

        String[][] playerOneTableEnemy = new String[BOARD_SIZE][BOARD_SIZE];
        String[][] playerTwoTableEnemy = new String[BOARD_SIZE][BOARD_SIZE];

        boolean playerOneHasMine = true;
        boolean playerTwoHasMine = true;

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

            if (move == 1) {
                boolean wantToUseMine = availabilityMine(scanner, playerOneHasMine);

                if (wantToUseMine) {
                    playerOneHasMine = false;
                    boolean nextStep = hitMine(scanner, playerTwoTableCurr, playerOneTableEnemy);
                    move = nextStep ? 1 : 2;
                } else {
                    int[] hit = inputCoordinateForHit(scanner);
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
                }
            } else {
                boolean wantToUseMine = availabilityMine(scanner, playerTwoHasMine);

                if (wantToUseMine) {
                    playerTwoHasMine = false; // Сжигаем мину навсегда
                    boolean nextStep = hitMine(scanner, playerOneTableCurr, playerTwoTableEnemy);
                    move = nextStep ? 2 : 1;
                } else {
                    int[] hit = inputCoordinateForHit(scanner);
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

        }

        printWinner(playerOneTableCurr, playerTwoTableCurr);
    }

    static boolean hitMine(Scanner scanner, String[][] playerTableEnemy, String[][] playerTableCurr){
        int[] hit = inputCoordinateForHit(scanner);

        boolean hitInShip = false;

        int startX = Math.max(0, hit[0] - 1);
        int endX = Math.min(9, hit[0] + 1);
        int startY = Math.max(0, hit[1] - 1);
        int endY = Math.min(9, hit[1] + 1);

        for (int i = startX; i <= endX ; i++) {
            for (int j = startY; j <= endY; j++) {
                if (playerTableEnemy[i][j].equals(PART_OF_SHIP.getEmoji())){
                    hitInShip = true;
                    playerTableEnemy[i][j] = HIT.getEmoji();
                    playerTableCurr[i][j] = HIT.getEmoji();
                }
                else if (!playerTableEnemy[i][j].equals(PART_OF_SHIP.getEmoji()) && playerTableEnemy[i][j].equals(EMPTY_SPACE.getEmoji())) {
                    playerTableEnemy[i][j] = MISS.getEmoji();
                    playerTableCurr[i][j] = MISS.getEmoji();
                }
            }
        }
        return hitInShip;
    }

    static boolean availabilityMine(Scanner scanner, boolean hasMine){
        if (!hasMine) return false;

        System.out.println("БОНУС: у вас есть мина для удара радиусом 3х3. Хотите использовать?");
        String string = scanner.nextLine().toLowerCase();

        if (string.equals("нет") || string.equals("no")) return false;

        return true;
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
        int len;
        int height;

        while (true){
            System.out.println("Введите координаты для удара (например: a 1): ");
            String input = scanner.nextLine();
            String[] move = input.split(" ");

            if (move.length < 2){
                System.out.println("Ошибка! Введите пару координат через пробел (Формат: a 1)");
                continue;
            }

            try {
                len = move[0].charAt(0) - ASCII_OFFSET;
                height = Integer.parseInt(move[1]) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Вторым параметром должна быть цифра от 1 до 10.");
                continue;
            }

            if (len < 0 || len >= BOARD_SIZE || height < 0 || height >= BOARD_SIZE){
                System.out.println("Ошибка! Координаты должны быть в интервале [a - j] и [1 - 10]");
                continue;
            }
            break;
        }

        return new int[]{len, height};
    }

    static int endGame(String[][] arrOne, String[][] arrTwo){
        boolean noShipsOne = true;
        boolean noShipsTwo = true;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (arrOne[i][j].equals(PART_OF_SHIP.getEmoji())){
                    noShipsOne = false;
                    break;
                }
            }
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
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
        String[][] table = new String[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
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
        int len = (int)fourDeck[0].charAt(0) - ASCII_OFFSET; // Слева буквы
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

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(rowLetter + "  ");

            for (int j = 0; j < BOARD_SIZE; j++) {
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