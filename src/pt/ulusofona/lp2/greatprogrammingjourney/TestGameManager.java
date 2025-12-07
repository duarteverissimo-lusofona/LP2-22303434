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

    @Test
    void testGetProgrammersInfo() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Player One", "Java", "PURPLE"},
                {"2", "Player Two", "Python", "BLUE"},
                {"3", "Player Three", "C", "GREEN"}
        };
        gm.createInitialBoard(players, 10);

        // Player 1 has one tool
        gm.getJogador(1).addFerramenta("IDE");

        // Player 3 is defeated
        gm.getJogador(3).setEstado(Estado.DERROTADO);

        String expected = "Player One : IDE | Player Two : No tools";
        assertEquals(expected, gm.getProgrammersInfo());
    }



    // ========== TESTES DE RESTRIÇÕES DE MOVIMENTO POR LINGUAGEM ==========

    @Test
    void testAssemblyPlayer_cannotMove3Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        // Assembly só pode mover até 2 casas, 3 deve retornar false
        assertFalse(gm.moveCurrentPlayer(3), 
                "Programador Assembly NÃO deveria conseguir mover 3 casas");
    }

    @Test
    void testAssemblyPlayer_canMove2Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        // Assembly pode mover até 2 casas
        assertTrue(gm.moveCurrentPlayer(2), 
                "Programador Assembly DEVERIA conseguir mover 2 casas");
    }

    @Test
    void testAssemblyPlayer_canMove1Space() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        // Assembly pode mover 1 casa
        assertTrue(gm.moveCurrentPlayer(1), 
                "Programador Assembly DEVERIA conseguir mover 1 casa");
    }

    @Test
    void testCPlayer_cannotMove4Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        // C só pode mover até 3 casas, 4 deve retornar false
        assertFalse(gm.moveCurrentPlayer(4), 
                "Programador C NÃO deveria conseguir mover 4 casas");
    }

    @Test
    void testCPlayer_canMove3Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        // C pode mover até 3 casas
        assertTrue(gm.moveCurrentPlayer(3), 
                "Programador C DEVERIA conseguir mover 3 casas");
    }

    @Test
    void testJavaPlayer_canMoveAnyValidSpaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Java Dev", "Java", "PURPLE"},
                {"2", "Other Dev", "Python", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        // Java não tem restrições (pode mover 1-6)
        assertTrue(gm.moveCurrentPlayer(6), 
                "Programador Java DEVERIA conseguir mover 6 casas");
    }

    @Test
    void testAssemblyPlayer_turnAdvancesOnInvalidMove() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Java Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        assertEquals(1, gm.getCurrentPlayerID());
        
        // Assembly tenta mover 3 (inválido) - deve retornar false mas turno avança
        boolean result = gm.moveCurrentPlayer(3);
        assertFalse(result, "Movimento Assembly 3 casas deve retornar false");
        assertEquals(2, gm.getCurrentPlayerID(), "Turno deveria ter passado para jogador 2");
    }

    // ========== TESTES ASSEMBLY - TODAS AS VARIAÇÕES (1-6 casas) ==========

    @Test
    void testAssemblyPlayer_cannotMove4Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertFalse(gm.moveCurrentPlayer(4), 
                "Programador Assembly NÃO deveria conseguir mover 4 casas");
    }

    @Test
    void testAssemblyPlayer_cannotMove5Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertFalse(gm.moveCurrentPlayer(5), 
                "Programador Assembly NÃO deveria conseguir mover 5 casas");
    }

    @Test
    void testAssemblyPlayer_cannotMove6Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertFalse(gm.moveCurrentPlayer(6), 
                "Programador Assembly NÃO deveria conseguir mover 6 casas");
    }

    // ========== TESTES C - TODAS AS VARIAÇÕES (1-6 casas) ==========

    @Test
    void testCPlayer_canMove1Space() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertTrue(gm.moveCurrentPlayer(1), 
                "Programador C DEVERIA conseguir mover 1 casa");
    }

    @Test
    void testCPlayer_canMove2Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertTrue(gm.moveCurrentPlayer(2), 
                "Programador C DEVERIA conseguir mover 2 casas");
    }

    @Test
    void testCPlayer_cannotMove5Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertFalse(gm.moveCurrentPlayer(5), 
                "Programador C NÃO deveria conseguir mover 5 casas");
    }

    @Test
    void testCPlayer_cannotMove6Spaces() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertFalse(gm.moveCurrentPlayer(6), 
                "Programador C NÃO deveria conseguir mover 6 casas");
    }

    @Test
    void testCPlayer_turnAdvancesOnInvalidMove() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Java Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        assertEquals(1, gm.getCurrentPlayerID());
        
        // C tenta mover 4 (inválido) - deve retornar false mas turno avança
        boolean result = gm.moveCurrentPlayer(4);
        assertFalse(result, "Movimento C 4 casas deve retornar false");
        assertEquals(2, gm.getCurrentPlayerID(), "Turno deveria ter passado para jogador 2");
    }

    // ========== TESTE JOGADOR ASSEMBLY ESPECIFICAMENTE COM 5 CASAS ==========
    
    @Test
    void testAssemblyPlayer_move5Spaces_playerStaysInSamePosition() {
        GameManager gm = new GameManager();
        String[][] players = new String[][]{
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Other Dev", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 20);

        // Verifica posição inicial
        String[] infoBefore = gm.getProgrammerInfo(1);
        assertEquals("1", infoBefore[4], "Jogador deve começar na posição 1");
        
        // Assembly tenta mover 5 casas (inválido)
        boolean result = gm.moveCurrentPlayer(5);
        
        // Verifica que retorna false
        assertFalse(result, "Programador Assembly NÃO deveria conseguir mover 5 casas");
        
        // Verifica que jogador continua na mesma posição
        String[] infoAfter = gm.getProgrammerInfo(1);
        assertEquals("1", infoAfter[4], "Jogador Assembly deve continuar na posição 1 após movimento inválido");
    }


}