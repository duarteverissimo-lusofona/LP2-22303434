package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;


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


    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE DIAGNÓSTICO - ROTAÇÃO DE JOGADORES
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testGetCurrentPlayerID_afterInit_returnsSmallestID() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"5", "Player5", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"},
                {"8", "Player8", "Kotlin", "GREEN"}
        };
        gm.createInitialBoard(players, 10);
        
        // O jogador com menor ID (2) deve começar
        assertEquals(2, gm.getCurrentPlayerID(), "Jogador com menor ID deve começar");
    }

    @Test
    void testGetNextPlayer_fromSmallestID_returnsNextID() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"},
                {"3", "Player3", "Kotlin", "GREEN"}
        };
        gm.createInitialBoard(players, 10);
        
        // Jogador atual é 1, próximo deve ser 2
        assertEquals(1, gm.getCurrentPlayerID(), "Jogador 1 deve começar");
        assertEquals(2, gm.getNextPlayer(), "Próximo jogador deve ser 2");
    }

    @Test
    void testGetNextPlayer_fromLargestID_wrapsToSmallest() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"},
                {"3", "Player3", "Kotlin", "GREEN"}
        };
        gm.createInitialBoard(players, 10);
        
        // Mover duas vezes para chegar ao jogador 3
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool(); // 1 -> 2
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool(); // 2 -> 3
        
        assertEquals(3, gm.getCurrentPlayerID(), "Jogador 3 deve ser o atual");
        assertEquals(1, gm.getNextPlayer(), "Próximo deve voltar ao jogador 1");
    }

    @Test
    void testPlayerRotation_afterMove_advancesToNextPlayer() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        gm.createInitialBoard(players, 10);
        
        System.out.println("=== TESTE ROTAÇÃO ===");
        System.out.println("Antes do movimento:");
        System.out.println("  getCurrentPlayerID() = " + gm.getCurrentPlayerID());
        System.out.println("  getNextPlayer() = " + gm.getNextPlayer());
        
        // Jogador 1 move
        boolean moved = gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        System.out.println("moveCurrentPlayer(1) retornou: " + moved);
        
        System.out.println("Depois do movimento:");
        System.out.println("  getCurrentPlayerID() = " + gm.getCurrentPlayerID());
        
        // Após movimento, deve ser jogador 2
        assertEquals(2, gm.getCurrentPlayerID(), "Após jogador 1 mover, jogador 2 deve jogar");
    }

    @Test
    void testPlayerRotation_fullCycle_returnsToFirstPlayer() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"},
                {"3", "Player3", "Kotlin", "GREEN"}
        };
        gm.createInitialBoard(players, 20);
        
        System.out.println("=== TESTE CICLO COMPLETO ===");
        
        // Jogador 1
        assertEquals(1, gm.getCurrentPlayerID(), "Início: Jogador 1");
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        // Jogador 2
        assertEquals(2, gm.getCurrentPlayerID(), "Após 1º move: Jogador 2");
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        // Jogador 3
        assertEquals(3, gm.getCurrentPlayerID(), "Após 2º move: Jogador 3");
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        // Volta ao Jogador 1
        assertEquals(1, gm.getCurrentPlayerID(), "Após 3º move: Volta ao Jogador 1");
    }

    @Test
    void testPlayerRotation_withNonSequentialIDs() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"10", "Player10", "Java", "PURPLE"},
                {"5", "Player5", "Python", "BLUE"},
                {"20", "Player20", "Kotlin", "GREEN"}
        };
        gm.createInitialBoard(players, 20);
        
        System.out.println("=== TESTE IDS NÃO SEQUENCIAIS ===");
        
        // Menor ID é 5
        assertEquals(5, gm.getCurrentPlayerID(), "Início: Jogador 5 (menor ID)");
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        // Próximo é 10
        assertEquals(10, gm.getCurrentPlayerID(), "Após 1º move: Jogador 10");
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        // Próximo é 20
        assertEquals(20, gm.getCurrentPlayerID(), "Após 2º move: Jogador 20");
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        // Volta ao 5
        assertEquals(5, gm.getCurrentPlayerID(), "Após 3º move: Volta ao Jogador 5");
    }

    @Test
    void testGetPreviousPlayer_basic() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"},
                {"3", "Player3", "Kotlin", "GREEN"}
        };
        gm.createInitialBoard(players, 10);
        
        // Jogador 1 joga
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        // Agora é jogador 2, anterior deve ser 1
        assertEquals(2, gm.getCurrentPlayerID(), "Atual deve ser 2");
        assertEquals(1, gm.getPreviousPlayer(), "Anterior deve ser 1");
    }


    // TESTE TEORIA A (Erro 1): Verificar se turno avança uma ou duas vezes
    @Test
    void testLanguageRestriction_turnAdvancesOnlyOnce() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "C Dev", "C", "PURPLE"},      // C: max 3 casas
                {"2", "Player2", "Java", "BLUE"},
                {"3", "Player3", "Python", "GREEN"}
        };
        gm.createInitialBoard(players, 20);

        // Jogador 1 (C) tenta mover 4 casas (inválido)
        assertEquals(1, gm.getCurrentPlayerID(), "Início: jogador 1");
        gm.moveCurrentPlayer(4); // Deveria falhar e passar para 2
        gm.reactToAbyssOrTool();

        assertEquals(2, gm.getCurrentPlayerID(),
                "Após movimento inválido de C, deveria ser jogador 2, não 3");
    }

    // TESTE TEORIA A (Erro 2): numTurnos para jogadores presos
    @Test
    void testTurnCount_incrementsForTrappedPlayers() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        // Simular jogador 1 preso
        gm.getJogador(1).setEstado(Estado.PRESO);

        // Tentar mover jogador preso
        gm.moveCurrentPlayer(1); // Deve passar turno mas contar?
        gm.reactToAbyssOrTool();

        // Verificar quantos turnos contou
        // (precisa de um getter para numTurnos ou verificar via getGameResults)
    }

    // TESTE TEORIA B (Erro 2): numTurnos para restrições de linguagem
    @Test
    void testTurnCount_incrementsForLanguageRestriction() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Other", "Java", "BLUE"}
        };
        gm.createInitialBoard(players, 10);

        // C tenta mover 5 (inválido)
        gm.moveCurrentPlayer(5);
        gm.reactToAbyssOrTool();

        // O turno deveria ter contado?
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTE DIAGNÓSTICO - Reproduzir erro LanguageBasedMovementRestrictions
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testLanguageRestriction_turnAdvancesToCorrectPlayer() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Java Dev", "Java", "BLUE"},
                {"3", "Python Dev", "Python", "GREEN"}
        };
        gm.createInitialBoard(players, 20);

        System.out.println("=== TESTE Language Restriction ===");
        System.out.println("Jogador inicial: " + gm.getCurrentPlayerID());

        // Jogador 1 (C) tenta mover 4 casas - INVÁLIDO (C max 3)
        boolean result = gm.moveCurrentPlayer(4);
        gm.reactToAbyssOrTool();
        System.out.println("moveCurrentPlayer(4) retornou: " + result);
        System.out.println("Jogador atual após movimento inválido: " + gm.getCurrentPlayerID());

        assertEquals(2, gm.getCurrentPlayerID(),
                "Após jogador C falhar movimento (max 3 casas), deveria passar para jogador 2, não 3");
    }

    @Test
    void testLanguageRestriction_Assembly_turnAdvancesToCorrectPlayer() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Assembly Dev", "Assembly", "PURPLE"},
                {"2", "Java Dev", "Java", "BLUE"},
                {"3", "Python Dev", "Python", "GREEN"}
        };
        gm.createInitialBoard(players, 20);

        System.out.println("=== TESTE Assembly Restriction ===");
        System.out.println("Jogador inicial: " + gm.getCurrentPlayerID());

        // Jogador 1 (Assembly) tenta mover 3 casas - INVÁLIDO (Assembly max 2)
        boolean result = gm.moveCurrentPlayer(3);
        gm.reactToAbyssOrTool();
        System.out.println("moveCurrentPlayer(3) retornou: " + result);
        System.out.println("Jogador atual após movimento inválido: " + gm.getCurrentPlayerID());

        assertEquals(2, gm.getCurrentPlayerID(),
                "Após jogador Assembly falhar movimento (max 2 casas), deveria passar para jogador 2, não 3");
    }

    @Test
    void testLanguageRestriction_multipleFailures_correctRotation() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "C Dev", "C", "PURPLE"},
                {"2", "Assembly Dev", "Assembly", "BLUE"},
                {"3", "Java Dev", "Java", "GREEN"}
        };
        gm.createInitialBoard(players, 20);

        System.out.println("=== TESTE Multiple Language Restrictions ===");

        // Jogador 1 (C) tenta mover 5 - INVÁLIDO
        gm.moveCurrentPlayer(5);
        gm.reactToAbyssOrTool();
        System.out.println("Após C falhar: jogador atual = " + gm.getCurrentPlayerID());
        assertEquals(2, gm.getCurrentPlayerID(), "Deveria ser jogador 2");

        // Jogador 2 (Assembly) tenta mover 4 - INVÁLIDO
        gm.moveCurrentPlayer(4);
        gm.reactToAbyssOrTool();
        System.out.println("Após Assembly falhar: jogador atual = " + gm.getCurrentPlayerID());
        assertEquals(3, gm.getCurrentPlayerID(), "Deveria ser jogador 3");

        // Jogador 3 (Java) move normalmente
        gm.moveCurrentPlayer(4);
        gm.reactToAbyssOrTool();
        System.out.println("Após Java mover: jogador atual = " + gm.getCurrentPlayerID());
        assertEquals(1, gm.getCurrentPlayerID(), "Deveria voltar ao jogador 1");
    }
}