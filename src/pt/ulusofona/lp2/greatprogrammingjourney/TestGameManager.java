package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestGameManager {

    private String[][] validPlayers() {
        return new String[][]{
                {"1", "Dev One", "Java", "PURPLE"},
                {"2", "Dev Two", "Python", "BLUE"},
                {"3", "Dev Three", "C", "GREEN"}
        };


    }

    @Test
    void testGetGameResults_afterWin_returnsFormattedList() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(validPlayers(), 6));

        gm.moveCurrentPlayer(5);

        ArrayList<String> results = gm.getGameResults();
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Após a vitória deve devolver uma lista preenchida");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("THE GREAT PROGRAMMING JOURNEY");
        expected.add("");
        expected.add("NR. DE TURNOS");
        expected.add("2");
        expected.add("");
        expected.add("VENCEDOR");
        expected.add("Dev One");
        expected.add("");
        expected.add("RESTANTES");
        expected.add("Dev Two 1");
        expected.add("Dev Three 1");

        assertEquals(expected, results);
    }

    @Test
    void testMoveCurrentPlayer_boardNotInitialized_returnsFalse() {
        GameManager gm = new GameManager();
        assertFalse(gm.moveCurrentPlayer(2), "Sem tabuleiro inicializado deve retornar false");
    }


    @Test
    void testCreateInitialBoard_validInput_returnsTrue() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(validPlayers(), 6),
                "Deveria retornar true para valor de dados entre 1 - 6 e com worldSize >= (2 * nº jogadores)");
    }

    @Test
    void testCreateInitialBoard_invalidWorldSize_returnsFalse() {
        GameManager gm = new GameManager();
        assertFalse(gm.createInitialBoard(validPlayers(), 5),
                "Deve retornar false se o tamanho do tabuleiro for menor que (2 * nº jogadores)");
    }


    @Test
    void testMoveCurrentPlayer_invalidSpaces_returnsFalse() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(validPlayers(), 6));

        assertFalse(gm.moveCurrentPlayer(0));
        assertFalse(gm.moveCurrentPlayer(7));
    }

    @Test
    void testMoveCurrentPlayer_exceedsBoardSize_bouncesBack() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(validPlayers(), 6));

        // Move 7 casas num tabuleiro de 6 → deve recuar
        assertTrue(gm.moveCurrentPlayer(6)); // Vai para posição 6 (bounce: 6 - (7-6) = 5)

        String[] info = gm.getProgrammerInfo(1);
        assertEquals("5", info[4]); // Verifica posição final
    }

    @Test
    void testGameIsOver_noWinner_returnsFalse() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(validPlayers(), 6));

        assertFalse(gm.gameIsOver());
    }

    @Test
    void testCurrentPlayerRotation() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(validPlayers(), 6));

        assertEquals(1, gm.getCurrentPlayerID());
        gm.moveCurrentPlayer(1);
        assertEquals(2, gm.getCurrentPlayerID());
        gm.moveCurrentPlayer(1);
        assertEquals(3, gm.getCurrentPlayerID());
    }


}