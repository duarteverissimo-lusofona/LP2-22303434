package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import pt.ulusofona.lp2.greatprogrammingjourney.event.abyss.*;
import pt.ulusofona.lp2.greatprogrammingjourney.event.tool.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para verificar o comportamento dos Abismos no jogo.
 * Baseado nos testes do professor, adaptados para a estrutura atual do projeto.
 */
public class TestAbyss {

    private GameManager gm;

    // ========================= Helper Methods =========================

    /**
     * Cria um cenário inicial com jogadores e tamanho do tabuleiro especificados.
     */
    private void createScenario(String[][] players, int worldSize) {
        gm = new GameManager();
        assertTrue(gm.createInitialBoard(players, worldSize));
    }

    /**
     * Cria um cenário inicial com jogadores, tamanho do tabuleiro e eventos.
     */
    private void createScenario(String[][] players, int worldSize, String[][] abyssesAndTools) {
        gm = new GameManager();
        assertTrue(gm.createInitialBoard(players, worldSize, abyssesAndTools));
    }

    private String getState(int playerId) {
        String[] info = gm.getProgrammerInfo(playerId);
        return info[6];
    }

    private String getAbyssOnSlot(int nrSlot) {
        String[] info = gm.getSlotInfo(nrSlot);
        return info[2];
    }

    private int getPosition(int playerId) {
        String[] info = gm.getProgrammerInfo(playerId);
        return Integer.parseInt(info[4]);
    }

    private String getTools(int playerId) {
        String[] info = gm.getProgrammerInfo(playerId);
        return info[5];
    }

    // ========================= Test Abysses Properties ====================

    @Test
    public void testAbyss_ErroDeSintaxe_hasCorrectProperties() {
        ErroDeSintaxe abyss = new ErroDeSintaxe();
        assertEquals("Erro de sintaxe", abyss.getNome());
        assertEquals(0, abyss.getId());
        assertEquals(1, abyss.getCasasRecuo());
        assertEquals("IDE", abyss.getFerramentaAnuladora());
    }

    @Test
    public void testAbyss_ErroDeLogica_hasCorrectProperties() {
        ErroDeLogica abyss = new ErroDeLogica();
        assertEquals("Erro de Lógica", abyss.getNome());
        assertEquals(1, abyss.getId());
        assertEquals(-4, abyss.getCasasRecuo()); // -4 = recua metade do dado
        assertEquals("Testes Unitários", abyss.getFerramentaAnuladora());
    }

    @Test
    public void testAbyss_Exception_hasCorrectProperties() {
        pt.ulusofona.lp2.greatprogrammingjourney.event.abyss.Exception abyss = 
            new pt.ulusofona.lp2.greatprogrammingjourney.event.abyss.Exception();
        assertEquals("Exception", abyss.getNome());
        assertEquals(2, abyss.getId());
        assertEquals(2, abyss.getCasasRecuo());
        assertEquals("Tratamento de Excepções", abyss.getFerramentaAnuladora());
    }

    @Test
    public void testAbyss_FileNotFoundException_hasCorrectProperties() {
        FileNotFoundException abyss = new FileNotFoundException();
        assertEquals("File Not Found Exeption", abyss.getNome());
        assertEquals(3, abyss.getId());
        assertEquals(3, abyss.getCasasRecuo());
        assertEquals("Tratamento de Excepções", abyss.getFerramentaAnuladora());
    }

    @Test
    public void testAbyss_Crash_hasCorrectProperties() {
        Crash abyss = new Crash();
        assertEquals("Crash", abyss.getNome());
        assertEquals(4, abyss.getId());
        assertEquals(-3, abyss.getCasasRecuo()); // -3 = volta para casa 1
        assertNull(abyss.getFerramentaAnuladora()); // Crash não tem ferramenta anuladora
    }

    @Test
    public void testAbyss_CodigoDuplicado_hasCorrectProperties() {
        CodigoDuplicado abyss = new CodigoDuplicado();
        assertEquals("Código Duplicado", abyss.getNome());
        assertEquals(5, abyss.getId());
        assertEquals(-5, abyss.getCasasRecuo()); // -5 = volta posição anterior
        assertEquals("Herança", abyss.getFerramentaAnuladora());
    }

    @Test
    public void testAbyss_EfeitosSecundarios_hasCorrectProperties() {
        EfeitosSecundarios abyss = new EfeitosSecundarios();
        assertEquals("Efeitos Secundários", abyss.getNome());
        assertEquals(6, abyss.getId());
        assertEquals(-6, abyss.getCasasRecuo()); // -6 = volta posição 2 turnos atrás
        assertEquals("Programação Funcional", abyss.getFerramentaAnuladora());
    }

    @Test
    public void testAbyss_BlueScreenOfDeath_hasCorrectProperties() {
        BlueScreenOfDeath abyss = new BlueScreenOfDeath();
        assertEquals("Blue Screen of Death", abyss.getNome());
        assertEquals(7, abyss.getId());
        assertEquals(-1, abyss.getCasasRecuo()); // -1 = jogador derrotado
        assertNull(abyss.getFerramentaAnuladora()); // BSOD não tem ferramenta anuladora
    }

    @Test
    public void testAbyss_CicloInfinito_hasCorrectProperties() {
        CicloInfinito abyss = new CicloInfinito();
        assertEquals("Ciclo Infinito", abyss.getNome());
        assertEquals(8, abyss.getId());
        assertEquals(-2, abyss.getCasasRecuo()); // -2 = jogador preso
        assertEquals("Ajuda Do Professor", abyss.getFerramentaAnuladora());
    }

    @Test
    public void testAbyss_SegmentationFault_hasCorrectProperties() {
        SegmentationFault abyss = new SegmentationFault();
        assertEquals("Segmentation Fault", abyss.getNome());
        assertEquals(9, abyss.getId());
        assertEquals(-7, abyss.getCasasRecuo()); // -7 = todos recuam 3 se 2+ jogadores
        assertNull(abyss.getFerramentaAnuladora()); // SegFault não tem ferramenta anuladora
    }

    // ========================= Test Tools Properties ====================

    @Test
    public void testTool_IDE_hasCorrectProperties() {
        IDE tool = new IDE();
        assertEquals("IDE", tool.getNome());
        assertEquals(4, tool.getId());
        assertTrue(tool.isTool());
    }

    @Test
    public void testTool_UnitTests_hasCorrectProperties() {
        UnitTests tool = new UnitTests();
        assertEquals("Testes Unitários", tool.getNome());
        assertEquals(2, tool.getId());
        assertTrue(tool.isTool());
    }

    @Test
    public void testTool_TratamentoDeExcepcoes_hasCorrectProperties() {
        TratamentoDeExcepcoes tool = new TratamentoDeExcepcoes();
        assertEquals("Tratamento de Excepções", tool.getNome());
        assertEquals(3, tool.getId());
        assertTrue(tool.isTool());
    }

    @Test
    public void testTool_Heranca_hasCorrectProperties() {
        Heranca tool = new Heranca();
        assertEquals("Herança", tool.getNome());
        assertEquals(0, tool.getId());
        assertTrue(tool.isTool());
    }

    @Test
    public void testTool_ProgramacaoFuncional_hasCorrectProperties() {
        ProgramacaoFuncional tool = new ProgramacaoFuncional();
        assertEquals("Programação Funcional", tool.getNome());
        assertEquals(1, tool.getId());
        assertTrue(tool.isTool());
    }
    
    @Test
    public void testTool_AjudaDoProfessor_hasCorrectProperties() {
        AjudaDoProfessor tool = new AjudaDoProfessor();
        assertEquals("Ajuda Do Professor", tool.getNome());
        assertEquals(5, tool.getId());
        assertTrue(tool.isTool());
    }

    // ========================= Test Abyss Effects ====================

    @Test
    public void test_syntax_error_sem_tool() {
        // Cenário: jogador 1 e 2, tabuleiro de 10 casas
        // Abismo ErroDeSintaxe (id=0) na casa 3
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "0", "3"} // tipo 0 = Abyss, subtipo 0 = ErroDeSintaxe, posição 3
        };
        createScenario(players, 10, events);
        int player = 1;

        // Começa na posição 1
        assertEquals(1, getPosition(player));

        // Anda 2 -> cai na casa 3 (abismo)
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 1 -> 3
        gm.reactToAbyssOrTool();

        // Abismo ErroDeSintaxe recua 1 casa: 3 -> 2
        assertEquals(2, getPosition(player));
    }

    @Test
    public void test_exception_sem_tool() {
        // Cenário: abismo Exception (id=2) na casa 5
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "2", "5"} // tipo 0 = Abyss, subtipo 2 = Exception, posição 5
        };
        createScenario(players, 10, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // 1 -> 5
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(4));
        gm.reactToAbyssOrTool();

        // Recua 2 casas: 5 -> 3
        assertEquals(3, getPosition(player));
    }

    @Test
    public void test_exception_com_tool() {
        // Cenário: ferramenta TratamentoDeExcepcoes (id=3) na casa 3, abismo Exception (id=2) na casa 5
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"1", "3", "3"}, // tipo 1 = Tool, subtipo 3 = TratamentoDeExcepcoes, posição 3
            {"0", "2", "5"}  // tipo 0 = Abyss, subtipo 2 = Exception, posição 5
        };
        createScenario(players, 10, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // 1ª jogada: apanha Tratamento de Excepções na casa 3
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 1 -> 3
        gm.reactToAbyssOrTool();
        assertEquals(3, getPosition(player));

        // 2ª jogada: cai no abismo na casa 5, mas tem counter
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 3 -> 5
        gm.reactToAbyssOrTool();

        // Fica na casa 5 (ferramenta anulou o abismo)
        assertEquals(5, getPosition(player));
    }

    @Test
    public void test_file_not_found_sem_tool() {
        // Cenário: abismo FileNotFoundException (id=3) na casa 6
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "3", "6"} // tipo 0 = Abyss, subtipo 3 = FileNotFoundException, posição 6
        };
        createScenario(players, 10, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // 1 -> 6
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(5));
        gm.reactToAbyssOrTool();

        // Recua 3 casas: 6 -> 3
        assertEquals(3, getPosition(player));
    }

    @Test
    public void test_crash_envia_para_inicio() {
        // Cenário: jogador começa na posição 5, crash na casa 8
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "4", "8"} // tipo 0 = Abyss, subtipo 4 = Crash, posição 8
        };
        createScenario(players, 15, events);
        int player = 1;

        // Move para posição 5 primeiro
        assertTrue(gm.moveCurrentPlayer(4)); // 1 -> 5
        gm.reactToAbyssOrTool();
        assertEquals(5, getPosition(player));

        // Agora é a vez do jogador 2, passa
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Volta ao jogador 1 -> move para crash na casa 8
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(3)); // 5 -> 8
        gm.reactToAbyssOrTool();

        // Vai para o início (casa 1)
        assertEquals(1, getPosition(player));
    }

    @Test
    public void test_duplicated_code_sem_tool() {
        // Cenário: abismo CodigoDuplicado (id=5) na casa 5
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "5", "5"} // tipo 0 = Abyss, subtipo 5 = CodigoDuplicado, posição 5
        };
        createScenario(players, 10, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // 1 -> 5 (casa com abismo)
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(4));
        gm.reactToAbyssOrTool();

        // Volta para a posição anterior (1)
        assertEquals(1, getPosition(player));
    }

    @Test
    public void test_duplicated_code_com_tool() {
        // Cenário: ferramenta Herança (id=0) na casa 3, abismo CodigoDuplicado (id=5) na casa 5
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"1", "0", "3"}, // tipo 1 = Tool, subtipo 0 = Herança, posição 3
            {"0", "5", "5"}  // tipo 0 = Abyss, subtipo 5 = CodigoDuplicado, posição 5
        };
        createScenario(players, 10, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // 1ª jogada: apanha Herança na casa 3
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 1 -> 3
        gm.reactToAbyssOrTool();
        assertEquals(3, getPosition(player));

        // 2ª jogada: cai no abismo na casa 5, mas tem counter
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 3 -> 5
        gm.reactToAbyssOrTool();

        // Fica na casa 5
        assertEquals(5, getPosition(player));
    }

    @Test
    public void test_secondary_effects_sem_tool() {
        // Cenário: abismo EfeitosSecundarios (id=6) na casa 7
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "6", "7"} // tipo 0 = Abyss, subtipo 6 = EfeitosSecundarios, posição 7
        };
        createScenario(players, 15, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // Jogada 1: 1 -> 3
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();
        assertEquals(3, getPosition(player));

        // Jogador 2 joga
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Jogada 2: 3 -> 5
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();
        assertEquals(5, getPosition(player));

        // Jogador 2 joga
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Jogada 3: 5 -> 7 (abismo SECONDARY_EFFECTS)
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();

        // Volta para posição de duas jogadas atrás: 3
        assertEquals(3, getPosition(player));
    }

    @Test
    public void test_secondary_effects_com_tool() {
        // Cenário: ferramenta ProgramacaoFuncional (id=1) na casa 3, abismo EfeitosSecundarios (id=6) na casa 7
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"1", "1", "3"}, // tipo 1 = Tool, subtipo 1 = ProgramacaoFuncional, posição 3
            {"0", "6", "7"}  // tipo 0 = Abyss, subtipo 6 = EfeitosSecundarios, posição 7
        };
        createScenario(players, 15, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // Jogada 1: apanha ProgramacaoFuncional na casa 3
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 1 -> 3
        gm.reactToAbyssOrTool();
        assertEquals(3, getPosition(player));

        // Jogador 2 joga
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Jogada 2: 3 -> 5
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();
        assertEquals(5, getPosition(player));

        // Jogador 2 joga
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Jogada 3: 5 -> 7 (abismo), mas com counter
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();

        // Mantém-se na casa 7
        assertEquals(7, getPosition(player));
    }

    @Test
    public void test_segmentation_fault() {
        // Cenário: abismo SegmentationFault (id=9) na casa 4
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "9", "4"} // tipo 0 = Abyss, subtipo 9 = SegmentationFault, posição 4
        };
        createScenario(players, 10, events);
        int player1 = 1;
        int player2 = 2;

        // Ambos começam na casa 1
        assertEquals(1, getPosition(player1));
        assertEquals(1, getPosition(player2));

        // Jogador 1 cai sozinho no abismo na casa 4
        assertEquals(player1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(3)); // 1 -> 4
        gm.reactToAbyssOrTool();

        // Só ele está na casa, portanto não recua
        assertEquals(4, getPosition(player1));
        assertEquals(1, getPosition(player2));

        // Jogador 2 também vai para a casa 4
        assertEquals(player2, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(3)); // 1 -> 4
        gm.reactToAbyssOrTool();

        // Agora há 2 jogadores na casa, ambos recuam 3 casas: 4 -> 1
        assertEquals(1, getPosition(player1));
        assertEquals(1, getPosition(player2));
    }

    // ========================= BSOD ====================

    @Test
    public void test_blue_screen_of_death_defeats_player() {
        // Cenário: abismo BlueScreenOfDeath (id=7) na casa 7
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "7", "7"} // tipo 0 = Abyss, subtipo 7 = BlueScreenOfDeath, posição 7
        };
        createScenario(players, 15, events);
        int playerId = 1;

        // Move para posição 5 primeiro
        assertTrue(gm.moveCurrentPlayer(4)); // 1 -> 5
        gm.reactToAbyssOrTool();
        assertEquals(5, getPosition(playerId));
        assertEquals("Em Jogo", getState(playerId));

        // Jogador 2 joga
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Move para posição 7 (BlueScreenOfDeath)
        assertEquals(playerId, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 5 -> 7
        gm.reactToAbyssOrTool();

        assertEquals(7, getPosition(playerId));
        assertEquals("Derrotado", getState(playerId));
    }

    // ========================= INFINITE_LOOP ====================

    @Test
    public void test_infinite_loop_basic() {
        // Cenário: abismo CicloInfinito (id=8) na casa 5
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"},
            {"3", "Player3", "Kotlin", "GREEN"}
        };
        String[][] events = {
            {"0", "8", "5"} // tipo 0 = Abyss, subtipo 8 = CicloInfinito, posição 5
        };
        createScenario(players, 15, events);
        int player1 = 1;
        int player2 = 2;

        assertEquals("Em Jogo", getState(player1));
        assertEquals("Em Jogo", getState(player2));

        // Jogador 1 vai para o abismo CicloInfinito
        assertEquals(player1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(4)); // 1 -> 5
        gm.reactToAbyssOrTool();

        // Jogador 1 fica preso
        assertEquals(5, getPosition(player1));
        assertEquals("Preso", getState(player1));

        // Jogador 2 joga normalmente
        assertEquals(player2, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();
        assertEquals("Em Jogo", getState(player2));
    }

    @Test
    public void test_infinite_loop_libertar_jogadores() {
        // Cenário: quando um novo jogador entra no ciclo infinito, liberta os anteriores
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "8", "5"} // tipo 0 = Abyss, subtipo 8 = CicloInfinito, posição 5
        };
        createScenario(players, 15, events);
        int player1 = 1;
        int player2 = 2;

        // Jogador 1 vai para o abismo CicloInfinito
        assertEquals(player1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(4)); // 1 -> 5
        gm.reactToAbyssOrTool();

        // Jogador 1 fica preso
        assertEquals(5, getPosition(player1));
        assertEquals("Preso", getState(player1));

        // Jogador 2 também vai para o abismo na posição 5
        assertEquals(player2, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(4)); // 1 -> 5
        gm.reactToAbyssOrTool();

        // Jogador 2 fica preso, jogador 1 é libertado
        assertEquals(5, getPosition(player2));
        assertEquals("Preso", getState(player2));
        assertEquals("Em Jogo", getState(player1));
    }

    @Test
    public void test_infinite_loop_jogador_preso_nao_move() {
        // Cenário: jogador preso não pode mover
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "8", "5"} // tipo 0 = Abyss, subtipo 8 = CicloInfinito, posição 5
        };
        createScenario(players, 15, events);
        int player1 = 1;
        int player2 = 2;

        // Jogador 1 vai para o abismo CicloInfinito
        assertTrue(gm.moveCurrentPlayer(4)); // 1 -> 5
        gm.reactToAbyssOrTool();
        assertEquals("Preso", getState(player1));

        // Jogador 2 passa a vez normalmente
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Agora é a vez do jogador 1, que está preso - não deve conseguir mover
        assertEquals(player1, gm.getCurrentPlayerID());
        assertFalse(gm.moveCurrentPlayer(1)); // Não pode mover porque está preso
        assertEquals(5, getPosition(player1)); // Continua na mesma posição
    }

    @Test
    public void test_infinite_loop_tool() {
        // Cenário: ferramenta AjudaDoProfessor (id=5) na casa 3, abismo CicloInfinito (id=8) na casa 5
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"1", "5", "3"}, // tipo 1 = Tool, subtipo 5 = AjudaDoProfessor, posição 3
            {"0", "8", "5"}  // tipo 0 = Abyss, subtipo 8 = CicloInfinito, posição 5
        };
        createScenario(players, 15, events);
        int player1 = 1;

        // Jogador 1 apanha a ferramenta
        assertTrue(gm.moveCurrentPlayer(2)); // 1 -> 3
        gm.reactToAbyssOrTool();
        assertEquals(3, getPosition(player1));
        assertEquals("Em Jogo", getState(player1));

        // Jogador 2 joga
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Jogador 1 vai para o abismo mas tem ferramenta
        assertEquals(player1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 3 -> 5
        gm.reactToAbyssOrTool();

        // Não fica preso porque tem a ferramenta
        assertEquals(5, getPosition(player1));
        assertEquals("Em Jogo", getState(player1));
    }

    // ========================= Logic Error ====================

    @Test
    public void test_logic_error_sem_tool() {
        // Cenário: abismo ErroDeLogica (id=1) na casa 7
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"0", "1", "7"} // tipo 0 = Abyss, subtipo 1 = ErroDeLogica, posição 7
        };
        createScenario(players, 15, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // 1 -> 7, dado = 6
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(6));
        gm.reactToAbyssOrTool();

        // lastRoll = 6, recua 3 casas: 7 -> 4
        assertEquals(4, getPosition(player));
    }

    @Test
    public void test_logic_error_com_tool() {
        // Cenário: ferramenta UnitTests (id=2) na casa 3, abismo ErroDeLogica (id=1) na casa 7
        String[][] players = {
            {"1", "Player1", "Java", "PURPLE"},
            {"2", "Player2", "Python", "BLUE"}
        };
        String[][] events = {
            {"1", "2", "3"}, // tipo 1 = Tool, subtipo 2 = UnitTests, posição 3
            {"0", "1", "7"}  // tipo 0 = Abyss, subtipo 1 = ErroDeLogica, posição 7
        };
        createScenario(players, 15, events);
        int player = 1;

        assertEquals(1, getPosition(player));

        // 1ª jogada: apanha UNIT_TESTS na casa 3
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2)); // 1 -> 3
        gm.reactToAbyssOrTool();
        assertEquals(3, getPosition(player));

        // Jogador 2 joga
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // 2ª jogada: cai no abismo na casa 7, mas tem counter
        assertEquals(player, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(4)); // 3 -> 7
        gm.reactToAbyssOrTool();

        // Não recua, fica na casa 7
        assertEquals(7, getPosition(player));
    }

    // ========================= Test toString methods ====================

    @Test
    public void testAbyss_toString_returnsCorrectFormat() {
        ErroDeSintaxe abyss = new ErroDeSintaxe();
        assertEquals("A:0", abyss.toString());
        
        BlueScreenOfDeath bsod = new BlueScreenOfDeath();
        assertEquals("A:7", bsod.toString());
    }

    @Test
    public void testTool_toString_returnsCorrectFormat() {
        IDE ide = new IDE();
        assertEquals("T:4", ide.toString());
        
        Heranca heranca = new Heranca();
        assertEquals("T:0", heranca.toString());
    }

    // ========================= Test isTool method ====================

    @Test
    public void testAbyss_isTool_returnsFalse() {
        ErroDeSintaxe abyss = new ErroDeSintaxe();
        assertFalse(abyss.isTool());
    }

    @Test
    public void testTool_isTool_returnsTrue() {
        IDE tool = new IDE();
        assertTrue(tool.isTool());
    }
}
