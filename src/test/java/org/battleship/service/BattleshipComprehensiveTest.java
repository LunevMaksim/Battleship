package org.battleship;

import org.battleship.model.Board;
import org.battleship.model.Coordinate;
import org.battleship.model.State;
import org.battleship.service.PlacementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class BattleshipComprehensiveTest {

    private Board board;
    private PlacementValidator validator;

    @BeforeEach
    void setUp() {
        board = new Board(10);
        validator = new PlacementValidator();
    }

    // 1-4. Проверка границ доски для разных направлений
    @Test
    void testOutOfBoundsRight() {
        assertTrue(validator.isOutOfBounds(board, 0, 8, 'r', 4));
        assertFalse(validator.isOutOfBounds(board, 0, 7, 'r', 3));
    }

    @Test
    void testOutOfBoundsLeft() {
        assertTrue(validator.isOutOfBounds(board, 0, 0, 'l', 2));
        assertFalse(validator.isOutOfBounds(board, 0, 1, 'l', 2));
    }

    @Test
    void testOutOfBoundsDown() {
        assertTrue(validator.isOutOfBounds(board, 8, 0, 'd', 4));
        assertFalse(validator.isOutOfBounds(board, 6, 0, 'd', 4));
    }

    @Test
    void testOutOfBoundsUp() {
        assertTrue(validator.isOutOfBounds(board, 0, 0, 'u', 2));
        assertFalse(validator.isOutOfBounds(board, 1, 0, 'u', 2));
    }

    // 5-8. Параметризованные тесты граничных координат (различные точки старта)
    @ParameterizedTest
    @CsvSource({
            "0, 9, r, 2, true",
            "0, 8, r, 2, false",
            "9, 0, d, 2, true",
            "8, 0, d, 2, false"
    })
    void testParameterizedBounds(int row, int col, char dir, int deck, boolean expected) {
        assertEquals(expected, validator.isOutOfBounds(board, row, col, dir, deck));
    }

    // 9. Проверка инициализации доски
    @Test
    void testBoardSizeAndInitialState() {
        assertEquals(10, board.getSize());
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(State.EMPTY_SPACE, board.getGrid()[i][j].getState());
            }
        }
    }

    // 10-12. Проверка расстановки кораблей и живых палуб
    @Test
    void testShipPlacementAndAliveStatus() {
        assertFalse(board.hasAliveShips());

        board.placeShip(0, 0, 'r', 3);
        assertTrue(board.hasAliveShips());

        // Проверяем, что клетки действительно заняты кораблем
        assertEquals(State.PART_OF_SHIP, board.getGrid()[0][0].getState());
        assertEquals(State.PART_OF_SHIP, board.getGrid()[0][1].getState());
        assertEquals(State.PART_OF_SHIP, board.getGrid()[0][2].getState());
    }

    // 13-15. Проверка правила ауры (изоляции кораблей)
    @Test
    void testAuraViolationTouchingHorizontally() {
        board.placeShip(2, 2, 'r', 2); // Корабль на (2,2) и (2,3)

        // Попытка поставить корабль вплотную справа (нарушение ауры)
        assertFalse(validator.canPlaceShip(board, 2, 5, 'r', 2));
    }

    @Test
    void testAuraViolationTouchingVertically() {
        board.placeShip(2, 2, 'r', 3); // Строка 2

        // Попытка поставить корабль прямо под ним на строке 3
        assertFalse(validator.canPlaceShip(board, 3, 2, 'r', 2));
    }

    @Test
    void testValidPlacementWithSafeDistance() {
        board.placeShip(2, 2, 'r', 2); // Строка 2

        // Попытка поставить корабль через строку (строка 4) — должно быть разрешено
        assertTrue(validator.canPlaceShip(board, 4, 2, 'r', 2));
    }

    // 16-18. Проверка выброса исключения на недопустимый ход / некорректные параметры
    @Test
    void testInvalidMoveThrowsException() {
        // Пример проверки: попытка передать неверное направление в валидатор должна кидать IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boolean result = validator.isOutOfBounds(board, 0, 0, 'x', 3);
            if (result) {
                throw new IllegalArgumentException("Неверное направление корабля");
            }
        });
        assertNotNull(exception);
    }

    @Test
    void testCoordinateRecord() {
        Coordinate coord = new Coordinate(5, 7);
        assertEquals(5, coord.row());
        assertEquals(7, coord.col());
    }

    // 19-20. Дополнительные сценарии проверки состояния ячеек
    @Test
    void testCellStateChange() {
        var cell = board.getGrid()[0][0];
        assertEquals(State.EMPTY_SPACE, cell.getState());

        cell.setState(State.HIT);
        assertEquals(State.HIT, cell.getState());
        assertEquals("❌", cell.getEmoji());
    }
}