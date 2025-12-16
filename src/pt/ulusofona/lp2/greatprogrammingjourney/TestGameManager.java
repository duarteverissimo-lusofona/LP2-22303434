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

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA ADICIONAL - CreateJogador Validações
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testCreateInitialBoard_singlePlayer_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "Solo", "Java", "PURPLE"}};
        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    void testCreateInitialBoard_fivePlayers_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"},
                {"3", "Player3", "C", "GREEN"},
                {"4", "Player4", "Kotlin", "BROWN"},
                {"5", "Player5", "Ruby"}  // Sem cor - vai usar coresDisponiveis (mas só temos 4 cores)
        };
        assertFalse(gm.createInitialBoard(players, 20));
    }

    @Test
    void testCreateInitialBoard_duplicateID_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"1", "Player2", "Python", "BLUE"}  // ID duplicado
        };
        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    void testCreateInitialBoard_duplicateColor_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "PURPLE"}  // Cor duplicada
        };
        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    void testCreateInitialBoard_emptyName_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "", "Java", "PURPLE"},  // Nome vazio
                {"2", "Player2", "Python", "BLUE"}
        };
        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    void testCreateInitialBoard_invalidColor_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "VERMELHO"},  // Cor inválida
                {"2", "Player2", "Python", "BLUE"}
        };
        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    void testCreateInitialBoard_nonNumericID_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"abc", "Player1", "Java", "PURPLE"},  // ID não numérico
                {"2", "Player2", "Python", "BLUE"}
        };
        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    void testCreateInitialBoard_emptyID_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"", "Player1", "Java", "PURPLE"},  // ID vazio
                {"2", "Player2", "Python", "BLUE"}
        };
        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    void testCreateInitialBoard_withThreeElementArray_usesDefaultColor() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java"},  // Sem cor - usa padrão
                {"2", "Player2", "Python"}  // Sem cor - usa padrão
        };
        assertTrue(gm.createInitialBoard(players, 10));
        assertNotNull(gm.getProgrammerInfo(1));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - CreateInitialBoard com Eventos
    // ═══════════════════════════════════════════════════════════════════════════



    @Test
    void testCreateInitialBoard_withNullEvent_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {null};
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withInvalidEventType_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"5", "0", "3"}  // Tipo inválido (só 0 ou 1)
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withNegativeSubtype_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"0", "-1", "3"}  // Subtipo negativo
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withInvalidPosition_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"0", "0", "1"}  // Posição 1 (inválida - é a partida)
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withPositionAtEnd_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"0", "0", "10"}  // Posição 10 = worldSize (inválida - é a meta)
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withNonNumericEvent_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"abc", "0", "3"}  // Tipo não numérico
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withDuplicateEventPosition_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"0", "0", "3"},  // Evento na posição 3
                {"1", "0", "3"}   // Outro evento na posição 3 - duplicado
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withInvalidAbyssId_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"0", "99", "3"}  // ID de abyss inválido
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    @Test
    void testCreateInitialBoard_withInvalidToolId_returnsFalse() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
                {"1", "99", "3"}  // ID de tool inválido
        };
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - getImagePng
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testGetImagePng_noBoard_returnsNull() {
        GameManager gm = new GameManager();
        assertNull(gm.getImagePng(1));
    }

    @Test
    void testGetImagePng_invalidPosition_returnsNull() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertNull(gm.getImagePng(0));
        assertNull(gm.getImagePng(11));
    }

    @Test
    void testGetImagePng_lastPosition_returnsGlory() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertEquals("glory.png", gm.getImagePng(10));
    }

    @Test
    void testGetImagePng_emptySlot_returnsNull() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertNull(gm.getImagePng(5));
    }

    @Test
    void testGetImagePng_withEvent_returnsImage() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "0", "3"}};  // ErroDeSintaxe tem imagem
        gm.createInitialBoard(players, 10, events);
        assertNotNull(gm.getImagePng(3));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - getProgrammerInfo
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testGetProgrammerInfo_invalidId_returnsNull() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertNull(gm.getProgrammerInfo(999));
    }

    @Test
    void testGetProgrammerInfoAsStr_invalidId_returnsNull() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertNull(gm.getProgrammerInfoAsStr(999));
    }

    @Test
    void testGetProgrammerInfoAsStr_validPlayer_returnsCorrectFormat() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        String info = gm.getProgrammerInfoAsStr(1);
        assertNotNull(info);
        assertTrue(info.contains("Dev One"));
        assertTrue(info.contains("No tools") || info.contains("|"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - getSlotInfo
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testGetSlotInfo_noBoard_returnsNull() {
        GameManager gm = new GameManager();
        assertNull(gm.getSlotInfo(1));
    }

    @Test
    void testGetSlotInfo_invalidPosition_returnsNull() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertNull(gm.getSlotInfo(0));
        assertNull(gm.getSlotInfo(11));
    }

    @Test
    void testGetSlotInfo_emptySlot_returnsEmptyStrings() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        String[] info = gm.getSlotInfo(5);
        assertNotNull(info);
        assertEquals("", info[1]);  // No event name
    }

    @Test
    void testGetSlotInfo_withPlayers_returnsPlayerIds() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        String[] info = gm.getSlotInfo(1);  // Posição inicial
        assertNotNull(info);
        assertFalse(info[0].isEmpty());  // Deve ter IDs dos jogadores
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - Movement e Player States
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testMoveCurrentPlayer_defeatedPlayer_returnsFalse() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).setEstado(Estado.DERROTADO);
        assertFalse(gm.moveCurrentPlayer(2));
    }

    @Test
    void testMoveCurrentPlayer_trappedPlayer_returnsFalse() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).setEstado(Estado.PRESO);
        assertFalse(gm.moveCurrentPlayer(2));
    }

    @Test
    void testMoveCurrentPlayer_bounceBack_worksCorrectly() {
        GameManager gm = new GameManager();
        String[][] players = {
                {"1", "Player1", "Java", "PURPLE"},
                {"2", "Player2", "Python", "BLUE"}
        };
        gm.createInitialBoard(players, 6);
        
        // Move player para posição 5 (1+4)
        gm.moveCurrentPlayer(4);
        gm.reactToAbyssOrTool();
        assertEquals("5", gm.getProgrammerInfo(1)[4]);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - reactToAbyssOrTool
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testReactToAbyssOrTool_noBoard_returnsNull() {
        GameManager gm = new GameManager();
        assertNull(gm.reactToAbyssOrTool());
    }

    @Test
    void testReactToAbyssOrTool_noEvent_advancesTurn() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.moveCurrentPlayer(2);
        String result = gm.reactToAbyssOrTool();
        assertNull(result);  // Sem evento
    }

    @Test
    void testReactToAbyssOrTool_pickupTool() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"1", "0", "2"}};  // Tool Herança na posição 2
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(1);
        String result = gm.reactToAbyssOrTool();
        
        assertNotNull(result);
        assertTrue(result.contains("ferramenta"));
    }

    @Test
    void testReactToAbyssOrTool_alreadyHasTool() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"1", "0", "2"}};  // Tool Herança
        gm.createInitialBoard(players, 10, events);
        
        gm.getJogador(1).addFerramenta("Herança");
        gm.moveCurrentPlayer(1);
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("Já possui"));
    }

    @Test
    void testReactToAbyssOrTool_abyssNullified() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "0", "2"}};  // ErroDeSintaxe (anulado por Herança)
        gm.createInitialBoard(players, 10, events);
        
        gm.getJogador(1).addFerramenta("IDE");
        gm.moveCurrentPlayer(1);
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("anulado"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - Abyss Effects
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testAbyss_ErroDeSintaxe_recua1() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "0", "3"}};  // ErroDeSintaxe
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(2);  // Vai para pos 3
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("Recua 1 casa"));
        assertEquals("2", gm.getProgrammerInfo(1)[4]);
    }

    @Test
    void testAbyss_Exception_recua2() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "2", "4"}};  // Exception
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(3);  // Vai para pos 4
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("Recua 2 casas"));
    }

    @Test
    void testAbyss_FileNotFoundException_recua3() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "3", "5"}};  // FileNotFoundException
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(4);  // Vai para pos 5
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("Recua 3 casas"));
    }

    @Test
    void testAbyss_Crash_voltaCasa1() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "4", "4"}};  // Crash
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(3);  // Vai para pos 4
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("Volta para casa 1"));
        assertEquals("1", gm.getProgrammerInfo(1)[4]);
    }

    @Test
    void testAbyss_ErroDeLogica_recuaMetadeDado() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "1", "5"}};  // ErroDeLogica
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(4);  // Dado=4, vai para pos 5, recua 4/2=2
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("Recua"));
    }

    @Test
    void testAbyss_BlueScreenOfDeath_derrota() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "7", "3"}};  // BlueScreenOfDeath
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(2);
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("derrotado"));
        assertEquals(Estado.DERROTADO, gm.getJogador(1).getEstado());
    }

    @Test
    void testAbyss_CicloInfinito_preso() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "8", "3"}};  // CicloInfinito
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(2);
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("preso"));
        assertEquals(Estado.PRESO, gm.getJogador(1).getEstado());
    }

    @Test
    void testAbyss_CodigoDuplicado_voltaPosAnterior() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "5", "4"}};  // CodigoDuplicado
        gm.createInitialBoard(players, 10, events);
        
        // Primeiro movimento
        gm.moveCurrentPlayer(2);  // Pos 1->3
        gm.reactToAbyssOrTool();
        gm.moveCurrentPlayer(1);  // P2
        gm.reactToAbyssOrTool();
        
        // Segundo movimento leva ao CodigoDuplicado
        gm.moveCurrentPlayer(1);  // P1: Pos 3->4
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("anterior") || result.contains("Volta"));
    }

    @Test
    void testAbyss_SegmentationFault_multiplePlayersRecuam() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "9", "2"}};  // SegmentationFault
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(1);  // P1 para pos 2
        gm.reactToAbyssOrTool();  // SegFault mas só 1 jogador
        
        gm.moveCurrentPlayer(1);  // P2 para pos 2
        String result = gm.reactToAbyssOrTool();
        
        // Com 2 jogadores, deve recuar
        assertTrue(result.contains("recuam") || result.contains("Nada"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - Bebedeira Abyss
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testAbyss_Bebedeira_comCerveja() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "10", "3"}};  // Bebedeira
        gm.createInitialBoard(players, 10, events);
        
        gm.getJogador(1).addFerramenta("Cerveja");
        gm.moveCurrentPlayer(2);
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("teleportado") || result.contains("Bebedeira"));
        assertFalse(gm.getJogador(1).getFerramentas().contains("Cerveja"));
    }

    @Test
    void testAbyss_Bebedeira_semCerveja() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "10", "3"}};  // Bebedeira
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(2);
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("não ativada") || result.contains("não tem"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - gameIsOver e getGameResults
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testGameIsOver_noBoard_returnsFalse() {
        GameManager gm = new GameManager();
        assertFalse(gm.gameIsOver());
    }

    @Test
    void testGetGameResults_noWinner_returnsNull() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertNull(gm.getGameResults());
    }

    @Test
    void testGetGameResults_noBoard_returnsNull() {
        GameManager gm = new GameManager();
        assertNull(gm.getGameResults());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - customizeBoard e getAuthorsPanel
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testCustomizeBoard_returnsHashMap() {
        GameManager gm = new GameManager();
        var result = gm.customizeBoard();
        assertNotNull(result);
        assertEquals("true", result.get("hasNewAbyss"));
        assertEquals("true", result.get("hasNewTool"));
    }

    @Test
    void testGetAuthorsPanel_returnsPanel() {
        GameManager gm = new GameManager();
        assertNotNull(gm.getAuthorsPanel());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - getJogador
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testGetJogador_noBoard_returnsNull() {
        GameManager gm = new GameManager();
        assertNull(gm.getJogador(1));
    }

    @Test
    void testGetJogador_invalidId_returnsNull() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertNull(gm.getJogador(999));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - getProgrammersInfo
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testGetProgrammersInfo_noBoard_returnsEmpty() {
        GameManager gm = new GameManager();
        assertEquals("", gm.getProgrammersInfo());
    }

    @Test
    void testGetProgrammersInfo_allDefeated_returnsEmpty() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).setEstado(Estado.DERROTADO);
        gm.getJogador(2).setEstado(Estado.DERROTADO);
        gm.getJogador(3).setEstado(Estado.DERROTADO);
        assertEquals("", gm.getProgrammersInfo());
    }

    @Test
    void testGetProgrammersInfo_withTrappedPlayer_includes() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).setEstado(Estado.PRESO);
        String result = gm.getProgrammersInfo();
        assertTrue(result.contains("Dev One"));  // PRESO aparece
    }

    @Test
    void testGetProgrammersInfo_withTools() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).addFerramenta("IDE");
        gm.getJogador(1).addFerramenta("UnitTests");
        String result = gm.getProgrammersInfo();
        assertTrue(result.contains("IDE"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - saveGame e loadGame
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testSaveGame_noBoard_returnsFalse() {
        GameManager gm = new GameManager();
        assertFalse(gm.saveGame(new java.io.File("test.txt")));
    }

    @Test
    void testSaveGame_nullFile_returnsFalse() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        assertFalse(gm.saveGame(null));
    }

    @Test
    void testSaveGame_validGame_savesAndLoads() throws Exception {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).addFerramenta("IDE");
        gm.moveCurrentPlayer(2);
        gm.reactToAbyssOrTool();
        
        java.io.File tempFile = java.io.File.createTempFile("test_save", ".game");
        tempFile.deleteOnExit();
        
        assertTrue(gm.saveGame(tempFile));
        
        // Carregar
        GameManager gm2 = new GameManager();
        gm2.loadGame(tempFile);
        
        assertNotNull(gm2.getJogador(1));
        assertEquals("Dev One", gm2.getProgrammerInfo(1)[1]);
    }

    @Test
    void testLoadGame_nullFile_throwsException() {
        GameManager gm = new GameManager();
        assertThrows(java.io.FileNotFoundException.class, () -> gm.loadGame(null));
    }

    @Test
    void testLoadGame_nonExistentFile_throwsException() {
        GameManager gm = new GameManager();
        assertThrows(java.io.FileNotFoundException.class, 
            () -> gm.loadGame(new java.io.File("non_existent_file.game")));
    }

    @Test
    void testLoadGame_invalidFile_throwsInvalidFileException() throws Exception {
        GameManager gm = new GameManager();
        
        java.io.File tempFile = java.io.File.createTempFile("test_invalid", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("invalid content without sections");
        }
        
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_invalidBoardSize_throwsInvalidFileException() throws Exception {
        GameManager gm = new GameManager();
        
        java.io.File tempFile = java.io.File.createTempFile("test_bad_board", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=-1\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
        }
        
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_invalidPlayerFormat_throwsInvalidFileException() throws Exception {
        GameManager gm = new GameManager();
        
        java.io.File tempFile = java.io.File.createTempFile("test_bad_player", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=invalid format\n");  // Faltam campos
        }
        
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_invalidEstado_throwsInvalidFileException() throws Exception {
        GameManager gm = new GameManager();
        
        java.io.File tempFile = java.io.File.createTempFile("test_bad_estado", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Nome|PURPLE|INVALIDO|Java\n");
        }
        
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_invalidEventFormat_throwsInvalidFileException() throws Exception {
        GameManager gm = new GameManager();
        
        java.io.File tempFile = java.io.File.createTempFile("test_bad_event", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Nome|PURPLE|EM_JOGO|Java\n\n");
            fw.write("[EVENTS]\n");
            fw.write("3=X:Invalid\n");  // Tipo inválido
        }
        
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_validWithPositionHistory() throws Exception {
        GameManager gm1 = new GameManager();
        gm1.createInitialBoard(validPlayers(), 10);
        
        // Fazer movimentos para criar histórico
        gm1.moveCurrentPlayer(2);
        gm1.reactToAbyssOrTool();
        gm1.moveCurrentPlayer(1);
        gm1.reactToAbyssOrTool();
        gm1.moveCurrentPlayer(2);
        gm1.reactToAbyssOrTool();
        
        java.io.File tempFile = java.io.File.createTempFile("test_history", ".game");
        tempFile.deleteOnExit();
        
        assertTrue(gm1.saveGame(tempFile));
        
        GameManager gm2 = new GameManager();
        gm2.loadGame(tempFile);
        
        assertNotNull(gm2.getCurrentPlayerID());
    }

    @Test
    void testLoadGame_withEvents() throws Exception {
        GameManager gm1 = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "0", "3"}, {"1", "0", "5"}};
        gm1.createInitialBoard(players, 10, events);
        
        java.io.File tempFile = java.io.File.createTempFile("test_events", ".game");
        tempFile.deleteOnExit();
        
        assertTrue(gm1.saveGame(tempFile));
        
        GameManager gm2 = new GameManager();
        gm2.loadGame(tempFile);
        
        String[] slotInfo = gm2.getSlotInfo(3);
        assertNotNull(slotInfo);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES DE COBERTURA - Edge Cases
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testMoveCurrentPlayer_posicaoFinalMinimum() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "4", "2"}};  // Crash na pos 2 (volta para 1)
        gm.createInitialBoard(players, 6, events);
        
        gm.moveCurrentPlayer(1);  // Vai para pos 2
        gm.reactToAbyssOrTool();  // Crash -> volta para 1
        
        assertEquals("1", gm.getProgrammerInfo(1)[4]);
    }

    @Test
    void testEfeitosSecundarios_semHistorico() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "6", "2"}};  // EfeitosSecundarios
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(1);  // Vai direto para pos 2
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("2 turnos"));
    }

    @Test
    void testLibertarJogadoresNaCasa() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "8", "2"}};  // CicloInfinito
        gm.createInitialBoard(players, 10, events);
        
        // P1 cai no ciclo e fica preso
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        assertEquals(Estado.PRESO, gm.getJogador(1).getEstado());
        
        // P2 cai no mesmo ciclo -> P1 deve ser libertado
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        assertEquals(Estado.EM_JOGO, gm.getJogador(1).getEstado());
        assertEquals(Estado.PRESO, gm.getJogador(2).getEstado());
    }

    @Test
    void testAllTools_creation() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        
        // Testar todas as ferramentas (0-6)
        for (int i = 0; i <= 6; i++) {
            String[][] events = {{"1", String.valueOf(i), "3"}};
            assertTrue(gm.createInitialBoard(players, 10, events));
            gm = new GameManager();  // Reset
        }
    }

    @Test
    void testAllAbysses_creation() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        
        // Testar todos os abismos (0-10)
        for (int i = 0; i <= 10; i++) {
            gm = new GameManager();
            String[][] events = {{"0", String.valueOf(i), "3"}};
            assertTrue(gm.createInitialBoard(players, 10, events), "Abyss ID " + i + " should work");
        }
    }

    @Test
    void testGetProgrammerInfo_prisoExpectedOutput() {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).setEstado(Estado.PRESO);
        
        String[] info = gm.getProgrammerInfo(1);
        assertEquals("Preso", info[6]);
    }

    @Test
    void testSegmentationFault_singlePlayer_nothingHappens() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "9", "2"}};  // SegmentationFault
        gm.createInitialBoard(players, 10, events);
        
        gm.moveCurrentPlayer(1);  // P1 sozinho na pos 2
        String result = gm.reactToAbyssOrTool();
        
        assertTrue(result.contains("Nada acontece"));
    }

    @Test 
    void testVerificarEventosIniciais_withCicloInifintoAtStart() {
        GameManager gm = new GameManager();
        String[][] players = {{"1", "P1", "Java", "PURPLE"}, {"2", "P2", "Python", "BLUE"}};
        String[][] events = {{"0", "8", "1"}};  // CicloInfinito na posição inicial (inválido)
        
        // A posição 1 é inválida para eventos
        assertFalse(gm.createInitialBoard(players, 10, events));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTES ADICIONAIS PARA verificarEventosIniciais
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void testLoadGame_withPlayerOnToolPosition_apanhaFerramenta() throws Exception {
        // Criar ficheiro de save com jogador na posição de uma ferramenta
        java.io.File tempFile = java.io.File.createTempFile("test_tool_pickup", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("3=1|Player1|PURPLE|EM_JOGO|Java\n");  // Jogador na posição 3
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
            fw.write("3=T:0\n");  // Ferramenta Herança na posição 3
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // O jogador deve ter apanhado a ferramenta automaticamente
        assertTrue(gm.getJogador(1).getFerramentas().contains("Herança"));
    }

    @Test
    void testLoadGame_withPlayerOnBlueScreen_ficaDerrotado() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_bluescreen", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("3=1|Player1|PURPLE|EM_JOGO|Java\n");  // Jogador na posição 3
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
            fw.write("3=A:7\n");  // BlueScreenOfDeath na posição 3
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // O jogador deve ficar derrotado
        assertEquals(Estado.DERROTADO, gm.getJogador(1).getEstado());
    }

    @Test
    void testLoadGame_withPlayerOnCicloInfinito_ficaPreso() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_ciclo", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("3=1|Player1|PURPLE|EM_JOGO|Java\n");  // Jogador na posição 3
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
            fw.write("3=A:8\n");  // CicloInfinito na posição 3
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // O jogador deve ficar preso
        assertEquals(Estado.PRESO, gm.getJogador(1).getEstado());
    }



    @Test
    void testLoadGame_playerAlreadyHasTool_doesNotDuplicate() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_dup_tool", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            // Jogador já tem Herança e está na posição com Herança
            fw.write("3=1|Player1|PURPLE|EM_JOGO|Java|Herança\n");
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
            fw.write("3=T:0\n");  // Herança na posição 3
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // Deve ter apenas UMA Herança, não duas
        long count = gm.getJogador(1).getFerramentas().stream()
                .filter(f -> f.equals("Herança")).count();
        assertEquals(1, count);
    }

    @Test
    void testLoadGame_twoPlayersOnCicloInfinito_firstFreedSecondTrapped() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_two_ciclo", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            // Ambos jogadores na mesma posição com CicloInfinito
            fw.write("3=1|Player1|PURPLE|EM_JOGO|Java\n");
            fw.write("3=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
            fw.write("3=A:8\n");  // CicloInfinito na posição 3
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // Pelo menos um dos jogadores deve ficar preso
        boolean player1Preso = gm.getJogador(1).getEstado() == Estado.PRESO;
        boolean player2Preso = gm.getJogador(2).getEstado() == Estado.PRESO;
        assertTrue(player1Preso || player2Preso, "Pelo menos um jogador deve ficar preso");
    }

    @Test
    void testLoadGame_withPositionHistoryAnterior() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_history_anterior", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=3\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n");
            fw.write("anterior:1=2\n");  // Jogador 1 estava na posição 2
            fw.write("doisTurnos:1=1\n\n");  // Há 2 turnos estava na posição 1
            fw.write("[PLAYERS]\n");
            fw.write("3=1|Player1|PURPLE|EM_JOGO|Java\n");
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        assertNotNull(gm.getJogador(1));
        assertEquals("3", gm.getProgrammerInfo(1)[4]);
    }

    @Test
    void testLoadGame_withPlayerInPosition1_notRemovedFromStart() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_pos1", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java\n");  // Na posição 1
            fw.write("2=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // Jogador 1 está na posição 1
        assertEquals("1", gm.getProgrammerInfo(1)[4]);
        // Jogador 2 está na posição 2
        assertEquals("2", gm.getProgrammerInfo(2)[4]);
    }

    @Test
    void testLoadGame_emptyPlayersList() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_no_players", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // Jogo deve carregar mas sem jogadores
        assertNull(gm.getJogador(1));
    }


    @Test
    void testLoadGame_withToolEvent() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_tool_event", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java\n");
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
            fw.write("5=T:4\n");  // IDE na posição 5
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        String[] slotInfo = gm.getSlotInfo(5);
        assertNotNull(slotInfo);
        assertEquals("IDE", slotInfo[1]);
    }

    @Test
    void testLoadGame_invalidEventId_nullEvent() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_null_event", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java\n");
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
            fw.write("5=A:99\n");  // ID inválido -> createAbyss retorna null
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        // O slot não deve ter evento
        String[] slotInfo = gm.getSlotInfo(5);
        assertEquals("", slotInfo[1]);
    }

    @Test
    void testLoadGame_withCommentLines_ignored() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_comments", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("# Este é um comentário\n");
            fw.write("[GAME]\n");
            fw.write("# Outro comentário\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java\n");
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        assertNotNull(gm.getJogador(1));
    }

    @Test
    void testLoadGame_withPlayerPreso_statePreserved() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_preso_state", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=2\n");
            fw.write("turnCount=5\n");
            fw.write("lastDice=3\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("3=1|Player1|PURPLE|PRESO|Java\n");  // Estado PRESO
            fw.write("5=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        assertEquals(Estado.PRESO, gm.getJogador(1).getEstado());
        assertEquals(Estado.EM_JOGO, gm.getJogador(2).getEstado());
    }

    @Test
    void testLoadGame_withPlayerDerrotado_statePreserved() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_derrotado_state", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=2\n");
            fw.write("turnCount=5\n");
            fw.write("lastDice=3\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("3=1|Player1|PURPLE|DERROTADO|Java\n");  // Estado DERROTADO
            fw.write("5=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        assertEquals(Estado.DERROTADO, gm.getJogador(1).getEstado());
    }

    @Test
    void testLoadGame_lineWithNoEquals_throwsInvalidFileException() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_no_equals", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize10\n");  // Falta o =
        }
        
        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_eventWithBadFormat_throwsInvalidFileException() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_bad_event_format", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java\n\n");
            fw.write("[EVENTS]\n");
            fw.write("5=A0\n");  // Falta o : separador
        }
        
        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_playerWithMultipleLanguages() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_multi_lang", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java;Python;C\n");
            fw.write("1=2|Player2|BLUE|EM_JOGO|Kotlin\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        String[] info = gm.getProgrammerInfo(1);
        assertTrue(info[2].contains("Java"));
    }

    @Test
    void testLoadGame_playerWithMultipleTools() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_multi_tools", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java|IDE;UnitTests;Herança\n");
            fw.write("1=2|Player2|BLUE|EM_JOGO|Python\n\n");
            fw.write("[EVENTS]\n");
        }
        
        GameManager gm = new GameManager();
        gm.loadGame(tempFile);
        
        assertTrue(gm.getJogador(1).getFerramentas().contains("IDE"));
        assertTrue(gm.getJogador(1).getFerramentas().contains("UnitTests"));
        assertTrue(gm.getJogador(1).getFerramentas().contains("Herança"));
    }

    @Test
    void testSaveGame_preservesPositionHistory() throws Exception {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        
        // Fazer movimentos para criar histórico
        gm.moveCurrentPlayer(2);
        gm.reactToAbyssOrTool();
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        gm.moveCurrentPlayer(2);
        gm.reactToAbyssOrTool();
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        gm.moveCurrentPlayer(2);
        gm.reactToAbyssOrTool();
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();
        
        java.io.File tempFile = java.io.File.createTempFile("test_save_history", ".game");
        tempFile.deleteOnExit();
        
        assertTrue(gm.saveGame(tempFile));
        
        // Verificar que o ficheiro contém histórico
        String content = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
        assertTrue(content.contains("[POSITION_HISTORY]"));
    }

    @Test
    void testSaveGame_withTools_preservesTools() throws Exception {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).addFerramenta("IDE");
        gm.getJogador(1).addFerramenta("UnitTests");
        
        java.io.File tempFile = java.io.File.createTempFile("test_save_tools", ".game");
        tempFile.deleteOnExit();
        
        assertTrue(gm.saveGame(tempFile));
        
        GameManager gm2 = new GameManager();
        gm2.loadGame(tempFile);
        
        assertTrue(gm2.getJogador(1).getFerramentas().contains("IDE"));
        assertTrue(gm2.getJogador(1).getFerramentas().contains("UnitTests"));
    }

    @Test
    void testSaveGame_withPlayerStates_preservesStates() throws Exception {
        GameManager gm = new GameManager();
        gm.createInitialBoard(validPlayers(), 10);
        gm.getJogador(1).setEstado(Estado.PRESO);
        gm.getJogador(2).setEstado(Estado.DERROTADO);
        
        java.io.File tempFile = java.io.File.createTempFile("test_save_states", ".game");
        tempFile.deleteOnExit();
        
        assertTrue(gm.saveGame(tempFile));
        
        GameManager gm2 = new GameManager();
        gm2.loadGame(tempFile);
        
        assertEquals(Estado.PRESO, gm2.getJogador(1).getEstado());
        assertEquals(Estado.DERROTADO, gm2.getJogador(2).getEstado());
    }

    @Test
    void testLoadGame_playerPositionNonZero_throwsNumberFormatExceptionCaught() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_bad_pos", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("abc=1|Player1|PURPLE|EM_JOGO|Java\n");  // Posição inválida
        }
        
        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_eventPositionNonNumeric_throwsInvalidFileException() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_bad_event_pos", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java\n\n");
            fw.write("[EVENTS]\n");
            fw.write("xyz=A:0\n");  // Posição não numérica
        }
        
        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }

    @Test
    void testLoadGame_eventIdNonNumeric_throwsInvalidFileException() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test_bad_event_id", ".game");
        tempFile.deleteOnExit();
        
        try (java.io.FileWriter fw = new java.io.FileWriter(tempFile)) {
            fw.write("[GAME]\n");
            fw.write("boardSize=10\n");
            fw.write("currentPlayer=1\n");
            fw.write("turnCount=1\n");
            fw.write("lastDice=2\n\n");
            fw.write("[POSITION_HISTORY]\n\n");
            fw.write("[PLAYERS]\n");
            fw.write("1=1|Player1|PURPLE|EM_JOGO|Java\n\n");
            fw.write("[EVENTS]\n");
            fw.write("3=A:xyz\n");  // ID não numérico
        }
        
        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(tempFile));
    }
}