package org.battleship;

import org.battleship.model.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[][] playerOneTableCurr = new String[10][10];
        String[][] playerOneTableEnemy = new String[10][10];

        String[][] playerTwoTableCurr = new String[10][10];
        String[][] playerTwoTableEnemy = new String[10][10];


        checkFour(scanner, playerOneTableCurr);
        checkThree(scanner, playerOneTableCurr);
        checkTwo(scanner, playerOneTableCurr);
        checkOne(scanner, playerOneTableCurr);


        printMatrix(playerOneTableCurr);

    }
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

    static void stagingOfFour(String[][] playerOneTableCurr, List<Object> coordinate){
        int len = (int) coordinate.get(0); // Буква
        int height = (int) coordinate.get(1); // Цифра
        char direction = (char) coordinate.get(2);
        int deck = (int) coordinate.get(3);

        switch (direction){
            case 'r':
                for (int i = height; i < height + deck; i++) {
                    playerOneTableCurr[len][i] = State.PART_OF_SHIP.getEmoji();
                }
                break;
            case 'd':
                for (int i = len; i < len + deck; i++) {
                    playerOneTableCurr[i][height] = State.PART_OF_SHIP.getEmoji();
                }
                break;
            case 'l':
                for (int i = height; i > height - deck; i--) {
                    playerOneTableCurr[len][i] = State.PART_OF_SHIP.getEmoji();
                }
                break;
            case 'u':
                for (int i = len; i > len - deck; i--) {
                    playerOneTableCurr[i][height] = State.PART_OF_SHIP.getEmoji();
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
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (arr[i][j] == null) System.out.print(State.EMPTY_SPACE.getEmoji());
                else System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
}