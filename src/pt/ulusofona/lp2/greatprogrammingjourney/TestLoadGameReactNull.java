package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para identificar quando reactToAbyssOrTool() retorna null inesperadamente.
 * Cada teste carrega um cenário diferente e verifica o comportamento.
 */
public class TestLoadGameReactNull {

    private GameManager gm;
    private static final String BASE_PATH = "c:/Users/Kalanga/IdeaProjects/LP2-22303434/";

    @BeforeEach
    void setUp() {
        gm = new GameManager();
    }

    /**
     * Cenário 1: Jogador já numa casa com evento (posição inicial)
     * Chama reactToAbyssOrTool() SEM mover primeiro.
     */
    @Test
    void test_cenario1_evento_posicao_inicial() throws Exception {
        File f = new File(BASE_PATH + "test_cenario1_evento_posicao_inicial");
        gm.loadGame(f);
        
        System.out.println("=== CENÁRIO 1: Jogador na casa com evento ===");
        System.out.println("Jogador atual: " + gm.getCurrentPlayerID());
        
        // NÃO move - apenas reage ao evento na posição atual
        String resultado = gm.reactToAbyssOrTool();
        
        System.out.println("Resultado reactToAbyssOrTool: " + resultado);
        System.out.println("É null? " + (resultado == null));
        
        // Se o jogador está numa casa COM evento, esperamos retorno não-nulo
        // Descomenta se quiseres forçar o teste a falhar quando for null:
        // assertNotNull(resultado, "reactToAbyssOrTool retornou null quando jogador está em casa com evento");
    }

    /**
     * Cenário 2: Jogador PRESO numa casa com CicloInfinito
     */
    @Test
    void test_cenario2_jogador_preso() throws Exception {
        File f = new File(BASE_PATH + "test_cenario2_jogador_preso");
        gm.loadGame(f);
        
        System.out.println("=== CENÁRIO 2: Jogador PRESO ===");
        System.out.println("Jogador atual: " + gm.getCurrentPlayerID());
        
        // Tenta mover (deve falhar porque está preso)
        boolean moveu = gm.moveCurrentPlayer(3);
        System.out.println("moveCurrentPlayer retornou: " + moveu);
        System.out.println("Jogador atual APÓS move: " + gm.getCurrentPlayerID());
        
        String resultado = gm.reactToAbyssOrTool();
        System.out.println("Resultado reactToAbyssOrTool: " + resultado);
        System.out.println("É null? " + (resultado == null));
    }

    /**
     * Cenário 3: Jogador na posição 1, sem histórico de posição anterior
     * Move para CodigoDuplicado
     */
    @Test
    void test_cenario3_sem_historico_posicao() throws Exception {
        File f = new File(BASE_PATH + "test_cenario3_sem_historico");
        gm.loadGame(f);
        
        System.out.println("=== CENÁRIO 3: Sem histórico de posição ===");
        System.out.println("Jogador atual: " + gm.getCurrentPlayerID());
        
        boolean moveu = gm.moveCurrentPlayer(2);
        System.out.println("moveCurrentPlayer retornou: " + moveu);
        
        String resultado = gm.reactToAbyssOrTool();
        System.out.println("Resultado reactToAbyssOrTool: " + resultado);
        System.out.println("É null? " + (resultado == null));
    }

    /**
     * Cenário 4: Jogador com ID não-convencional (99)
     */
    @Test
    void test_cenario4_id_diferente() throws Exception {
        File f = new File(BASE_PATH + "test_cenario4_id_diferente");
        gm.loadGame(f);
        
        System.out.println("=== CENÁRIO 4: ID diferente (99) ===");
        System.out.println("Jogador atual: " + gm.getCurrentPlayerID());
        
        boolean moveu = gm.moveCurrentPlayer(2);
        System.out.println("moveCurrentPlayer retornou: " + moveu);
        System.out.println("Jogador atual APÓS move: " + gm.getCurrentPlayerID());
        
        String resultado = gm.reactToAbyssOrTool();
        System.out.println("Resultado reactToAbyssOrTool: " + resultado);
        System.out.println("É null? " + (resultado == null));
    }

    /**
     * Cenário 5: Dois jogadores na mesma casa, move para SegmentationFault
     */
    @Test
    void test_cenario5_segmentation_fault() throws Exception {
        File f = new File(BASE_PATH + "test_cenario5_dois_jogadores_mesma_casa");
        gm.loadGame(f);
        
        System.out.println("=== CENÁRIO 5: SegmentationFault com 2 jogadores ===");
        System.out.println("Jogador atual: " + gm.getCurrentPlayerID());
        
        boolean moveu = gm.moveCurrentPlayer(2);
        System.out.println("moveCurrentPlayer retornou: " + moveu);
        
        String resultado = gm.reactToAbyssOrTool();
        System.out.println("Resultado reactToAbyssOrTool: " + resultado);
        System.out.println("É null? " + (resultado == null));
    }

    /**
     * Cenário 6: Jogador move para última casa (vitória) com evento
     */
    @Test
    void test_cenario6_vitoria() throws Exception {
        File f = new File(BASE_PATH + "test_cenario6_vitoria");
        gm.loadGame(f);
        
        System.out.println("=== CENÁRIO 6: Vitória (última casa) ===");
        System.out.println("Jogador atual: " + gm.getCurrentPlayerID());
        System.out.println("gameIsOver antes: " + gm.gameIsOver());
        
        boolean moveu = gm.moveCurrentPlayer(2);
        System.out.println("moveCurrentPlayer retornou: " + moveu);
        System.out.println("gameIsOver depois: " + gm.gameIsOver());
        
        String resultado = gm.reactToAbyssOrTool();
        System.out.println("Resultado reactToAbyssOrTool: " + resultado);
        System.out.println("É null? " + (resultado == null));
    }

    /**
     * Cenário extra: Movimento com restrição de linguagem C (max 3 casas)
     */
    @Test
    void test_cenario_extra_restricao_linguagem() throws Exception {
        File f = new File(BASE_PATH + "test_cenario1_evento_posicao_inicial");
        gm.loadGame(f);
        
        System.out.println("=== CENÁRIO EXTRA: Restrição de linguagem ===");
        System.out.println("Jogador atual: " + gm.getCurrentPlayerID());
        
        // O jogador tem Java, não tem restrição
        // Mas vamos testar movimentos inválidos
        boolean moveu = gm.moveCurrentPlayer(7); // inválido (>6)
        System.out.println("moveCurrentPlayer(7) retornou: " + moveu);
        
        String resultado = gm.reactToAbyssOrTool();
        System.out.println("Resultado reactToAbyssOrTool: " + resultado);
        System.out.println("É null? " + (resultado == null));
    }
}
