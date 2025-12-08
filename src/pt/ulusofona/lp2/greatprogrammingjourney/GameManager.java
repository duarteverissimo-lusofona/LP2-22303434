package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.event.Evento;
import pt.ulusofona.lp2.greatprogrammingjourney.event.abyss.*;
import pt.ulusofona.lp2.greatprogrammingjourney.event.abyss.Exception;
import pt.ulusofona.lp2.greatprogrammingjourney.event.tool.*;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Enum que representa as cores disponíveis para os jogadores.
 * Cada jogador deve ter uma cor única no jogo.
 */
enum Cor {
    BLUE,
    BROWN,
    GREEN,
    PURPLE;

    /**
     * Converte uma String para o enum Cor correspondente.
     * Exemplo: "blue" ou "BLUE" -> Cor.BLUE
     * 
     * @param cor Nome da cor em texto
     * @return O enum Cor correspondente, ou null se não existir
     */
    public static Cor fromString(String cor){
        if(cor == null){
            return null;
        }
        for(Cor c : values()){
            if(c.name().equalsIgnoreCase(cor.trim())){
                return c;
            }
        }
        return null;
    }
}

/**
 * Enum que representa os possíveis estados de um jogador durante o jogo.
 * 
 * EM_JOGO    - O jogador está ativo e pode jogar normalmente
 * DERROTADO  - O jogador foi eliminado (ex: caiu num BlueScreenOfDeath)
 * PRESO      - O jogador está preso (ex: caiu num CicloInfinito) e não pode mover
 */
enum Estado {
    EM_JOGO,
    DERROTADO,
    PRESO
}


/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                              GAME MANAGER                                     ║
 * ║                                                                               ║
 * ║  Classe principal que gere toda a lógica do jogo "The Great Programming       ║
 * ║  Journey". Controla o tabuleiro, jogadores, turnos e eventos do jogo.        ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class GameManager {

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 1: CAMPOS DA CLASSE (Variáveis que guardam o estado do jogo)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * O tabuleiro do jogo - contém todas as casas (slots) e os jogadores.
     * É onde toda a ação acontece!
     */
    private Tabuleiro tabuleiro;
    
    /**
     * Índice do jogador que está a jogar neste momento.
     * Usado para saber de quem é a vez na rotação de turnos.
     * Nota: Este valor representa o ID do jogador atual, não a posição na lista.
     */
    private int jogadorAtualIndex;
    
    /**
     * Contador de turnos - começa em 1 e aumenta a cada movimento válido.
     * Usado para estatísticas do jogo.
     */
    private int numTurnos;
    
    /**
     * Guarda o último valor do dado lançado.
     * Necessário para o abismo "ErroDeLogica" que faz recuar metade do dado.
     */
    private int ultimoDado;
    
    /**
     * Mapa que guarda a posição anterior de cada jogador.
     * Chave: ID do jogador | Valor: posição onde estava antes do último movimento
     * Usado pelo abismo "CodigoDuplicado" para anular o último movimento.
     */
    private HashMap<Integer, Integer> posicaoAnterior;
    
    /**
     * Mapa que guarda a posição de cada jogador há 2 turnos atrás.
     * Chave: ID do jogador | Valor: posição onde estava há 2 turnos
     * Usado pelo abismo "EfeitosSecundarios".
     */
    private HashMap<Integer, Integer> posicaoHaDoisTurnos;

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 2: CONSTRUTOR
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Construtor do GameManager.
     * Inicializa os mapas de tracking de posições (vazios).
     * O tabuleiro só é criado quando se chama createInitialBoard().
     */
    public GameManager() {
        this.posicaoAnterior = new HashMap<>();
        this.posicaoHaDoisTurnos = new HashMap<>();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 3: CRIAÇÃO DE JOGADORES
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Cria um novo jogador a partir de um array de strings com as suas informações.
     * 
     * O array deve ter exatamente 4 elementos:
     *   [0] = ID do jogador (número inteiro >= 0)
     *   [1] = Nome do jogador (não pode ser vazio)
     *   [2] = Linguagens de programação (separadas por ";", ex: "Java;Python")
     *   [3] = Cor do jogador (BLUE, BROWN, GREEN ou PURPLE)
     * 
     * @param playerInfo Array com as informações do jogador
     * @return O jogador criado, ou null se alguma validação falhar
     */
    public Jogador createJogador(String[] playerInfo) {

        // Extrair cada campo do array para variáveis mais legíveis
        String idStr = playerInfo[0];           // O ID vem como texto, precisamos converter
        String nome = playerInfo[1];            // Nome do jogador
        String linguagensStr = playerInfo[2];   // Linguagens separadas por ";"
        String corStr = playerInfo[3];          // Cor como texto

        // VALIDAÇÃO 1: O array tem de ter exatamente 4 elementos
        if (playerInfo == null || playerInfo.length != 4) {
            return null;  // Array inválido - não conseguimos criar o jogador
        }

        // VALIDAÇÃO 2: O ID não pode ser vazio
        if (idStr == null || idStr.isEmpty()) {
            return null;
        }

        // VALIDAÇÃO 3: O ID tem de conter apenas dígitos (0-9)
        for (int i = 0; i < idStr.length(); i++) {
            if (!Character.isDigit(idStr.charAt(i))) {
                return null;  // Encontrámos um caractere que não é número
            }
        }

        // Converter o ID de texto para número
        int id = Integer.parseInt(idStr);

        // VALIDAÇÃO 4: O ID não pode ser negativo
        if (id < 0) {
            return null;
        }

        // VALIDAÇÃO 5: O nome não pode ser vazio ou só com espaços
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }

        // VALIDAÇÃO 6: Tem de existir pelo menos uma linguagem
        if (linguagensStr == null) {
            return null;
        }

        // Separar as linguagens pelo caractere ";"
        // Exemplo: "Java;Python;C" -> ["Java", "Python", "C"]
        ArrayList<String> linguagensProgramacao = new ArrayList<>();
        String[] partes = linguagensStr.split(";");
        for(String s : partes){
            linguagensProgramacao.add(s.trim());  // trim() remove espaços extra
        }


        // VALIDAÇÃO 7: A cor tem de existir
        if (corStr == null) {
            return null;
        }

        // Converter a cor de texto para o enum Cor
        Cor cor = Cor.fromString(corStr);

        // VALIDAÇÃO 8: A cor tem de ser válida (existir no enum)
        if (cor == null) {
            return null;  // Cor não reconhecida
        }

        // Tudo validado! Criar o jogador na posição 1 (início do tabuleiro)
        Jogador jogador = new Jogador(Integer.parseInt(idStr), nome, cor, linguagensProgramacao, 1);
        return jogador;
    }


    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 4: INICIALIZAÇÃO DO TABULEIRO
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Cria o tabuleiro inicial do jogo com os jogadores fornecidos.
     * 
     * Esta é a função que "arranca" o jogo! Cria o tabuleiro, valida todos
     * os jogadores e coloca-os na posição inicial (casa 1).
     * 
     * REGRAS DE VALIDAÇÃO:
     * - Deve haver entre 2 e 4 jogadores
     * - O tamanho do tabuleiro deve ser >= (nº jogadores × 2)
     * - Cada jogador deve ter um ID único
     * - Cada jogador deve ter uma cor única
     * 
     * @param playerInfo Array bidimensional com info de cada jogador
     * @param worldSize Tamanho do tabuleiro (número de casas)
     * @return true se o tabuleiro foi criado com sucesso, false se houve erro
     */
    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {

        // Criar o tabuleiro com o tamanho especificado
        this.tabuleiro = new Tabuleiro(worldSize);

        // VALIDAÇÃO 1: Número de jogadores entre 2 e 4
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            System.out.println("player sé nulll ou num players errado");
            return false;
        }

        // VALIDAÇÃO 2: Tabuleiro grande o suficiente para os jogadores
        // Regra: worldSize >= (número de jogadores × 2)
        if (worldSize < (playerInfo.length * 2)) {
            System.out.println("regra de worldsize nao cumprida");
            return false;
        }

        // Listas para verificar unicidade de IDs e cores
        ArrayList<Integer> ids = new ArrayList<>();   // IDs já usados
        ArrayList<String> cores = new ArrayList<>();  // Cores já usadas

        // Criar cada jogador e adicioná-lo ao tabuleiro
        for (String[] player : playerInfo) {
            // Tentar criar o jogador (valida todos os campos)
            Jogador jogador = createJogador(player);

            if (jogador == null) {
                System.out.println("jogador e null");
                return false;  // Falhou a validação do jogador
            }

            // VALIDAÇÃO 3: ID único (não pode repetir)
            int id = Integer.parseInt(player[0]);
            if (ids.contains(id)) {
                return false;  // ID duplicado!
            }
            ids.add(id);

            // VALIDAÇÃO 4: Cor única (não pode repetir)
            String cor = player[3];
            if (cores.contains(cor)) {
                return false;  // Cor duplicada!
            }
            cores.add(cor);

            // Colocar o jogador na casa 1 (posição inicial)
            tabuleiro.botarJogador(jogador, 1);
        }

        // Encontrar o jogador com menor ID para começar o jogo
        int menorId = Integer.MAX_VALUE;
        for(Jogador jogador : tabuleiro.getListaJogadores()){
                if(jogador.getId() < menorId){
                    menorId = jogador.getId();
                }
        }

        // Definir quem começa: o jogador com menor ID
        jogadorAtualIndex = menorId;
        
        // O jogo começa no turno 1
        numTurnos = 1;
        
        // Limpar dados de jogos anteriores (reset completo)
        ultimoDado = 0;
        posicaoAnterior.clear();
        posicaoHaDoisTurnos.clear();
        
        // DEBUG: Mostrar jogadores criados
        System.out.println("[DEBUG INIT] === JOGADORES CRIADOS ===");
        for (Jogador j : tabuleiro.getListaJogadores()) {
            System.out.println("[DEBUG INIT] ID: " + j.getId() + ", Nome: " + j.getNome() + ", Linguagens: " + j.getLinguagens());
        }
        System.out.println("[DEBUG INIT] jogadorAtualIndex = " + jogadorAtualIndex + ", numTurnos = " + numTurnos);
        System.out.println("[DEBUG INIT] ========================");

        
        return true;  // Tabuleiro criado com sucesso!
    }



    public String getImagePng(int nrSquare) {

        // Validar se o tabuleiro existe
        if (tabuleiro == null) {
            return null;
        }

        int boardSize = tabuleiro.getWorldSize();

        // Validação: nrSquare < 1 ou > tamanho do tabuleiro
        if (nrSquare < 1 || nrSquare > boardSize) {
            return null;
        }

        // Última casa (meta) → retorna "glory.png"
        if (nrSquare == boardSize) {
            return "glory.png";
        }

        if(tabuleiro.getSlot(nrSquare).getEvento() == null){
            return null;
        }

        String png = tabuleiro.getSlot(nrSquare).getEvento().getImagem();

        if(png != null){
            return png;
        }

        return null;
    }

    public String[] getProgrammerInfo(int id) {

        List<Jogador> jogadores = tabuleiro.getListaJogadores();
        // Validar se o tabuleiro existe
        if (tabuleiro == null || tabuleiro.slots == null) {
            return null;
        }

        // Validar se a lista de jogadores existe
        if (jogadores == null) {
            return null;
        }

        // Procurar o jogador pelo ID
        for (Jogador jogador : jogadores) {
            if (jogador.id == id) {
                String[] info = new String[7];

                // [0] ID do programador
                info[0] = String.valueOf(jogador.id);

                // [1] Nome
                info[1] = jogador.nome;

                // [2] Linguagens favoritas (separadas por ";")
                StringBuilder linguagens = new StringBuilder();
                if (jogador.linguagensProgramacao != null && !jogador.linguagensProgramacao.isEmpty()) {
                    for (int i = 0; i < jogador.linguagensProgramacao.size(); i++) {
                        linguagens.append(jogador.linguagensProgramacao.get(i));
                        if (i < jogador.linguagensProgramacao.size() - 1) {
                            linguagens.append(";");
                        }
                    }
                }
                info[2] = linguagens.toString();

                // [3] Cor em MAIÚSCULAS
                info[3] = jogador.cor.name().charAt(0) + jogador.cor.name().substring(1).toLowerCase();

                // [4] Posição actual no tabuleiro
                int posicao = 1; // posição padrão

                for (Slot slot : tabuleiro.slots) {
                    if (slot.jogadores != null) {
                        for (Jogador j : slot.jogadores) {
                            if (j.id == id) {
                                posicao = slot.nrSlot;
                                break;
                            }
                        }
                    }
                }
                info[4] = String.valueOf(posicao);

                // [5] Ferramentas (separadas por ";") ou "No tools"
                ArrayList<String> ferramentas = jogador.getFerramentas();
                if (ferramentas == null || ferramentas.isEmpty()) {
                    info[5] = "No tools";
                } else {
                    info[5] = String.join(";", ferramentas);
                }

                // [6] Estado (Em Jogo, Derrotado, Preso)
                if (jogador.estado == Estado.EM_JOGO) {
                    info[6] = "Em Jogo";
                } else if (jogador.estado == Estado.DERROTADO) {
                    info[6] = "Derrotado";
                } else {
                    info[6] = "Preso";
                }

                return info;
            }
        }

        // Se o ID não existir, retorna null
        return null;
    }

    public String getProgrammerInfoAsStr(int id){
        List<Jogador> jogadores = tabuleiro.getListaJogadores();
        // Validar se o tabuleiro existe
        if (tabuleiro == null || tabuleiro.slots == null) {
            return null;
        }

        // Validar se a lista de jogadores existe
        if (jogadores == null) {
            return null;
        }

        // Procurar o jogador pelo ID
        for (Jogador jogador : jogadores) {
            if (jogador.id == id) {

                // Obter a posição do jogador
                int posicao = 1;
                for (Slot slot : tabuleiro.slots) {
                    if (slot.jogadores != null) {
                        for (Jogador j : slot.jogadores) {
                            if (j.id == id) {
                                posicao = slot.nrSlot;
                                break;
                            }
                        }
                    }
                }

                StringBuilder linguagens = new StringBuilder();

                if (jogador.linguagensProgramacao != null && !jogador.linguagensProgramacao.isEmpty()) {
                    // Criar array de strings com os nomes das linguagens
                    ArrayList<String> nomesLinguagens = new ArrayList<>(jogador.linguagensProgramacao.size());
                    for (int i = 0; i < jogador.linguagensProgramacao.size(); i++) {
                        nomesLinguagens.add(jogador.linguagensProgramacao.get(i));
                    }

                    // Ordenar alfabeticamente
                    nomesLinguagens.sort(String.CASE_INSENSITIVE_ORDER);

                    // Construir string com separador "; "
                    for (int i = 0; i < nomesLinguagens.size(); i++) {
                        linguagens.append(nomesLinguagens.get(i));
                        if (i < nomesLinguagens.size() - 1) {
                            linguagens.append("; ");
                        }
                    }
                }

                // Obter as ferramentas
                String ferramentasStr;
                ArrayList<String> ferramentas = jogador.getFerramentas();
                if (ferramentas == null || ferramentas.isEmpty()) {
                    ferramentasStr = "No tools";
                } else {
                    ferramentasStr = String.join(";", ferramentas);
                }

                // Obter o estado
                String estado;
                if (jogador.estado == Estado.EM_JOGO) {
                    estado = "Em Jogo";
                } else if (jogador.estado == Estado.DERROTADO) {
                    estado = "Derrotado";
                } else {
                    estado = "Preso";
                }


                // Formato: <ID> | <Nome> | <Pos> | <Ferramentas> | <Linguagens favoritas> | <Estado>
                return jogador.id + " | " + jogador.nome + " | " + posicao + " | " +
                        ferramentasStr + " | " + linguagens.toString() + " | " + estado;
            }
        }

        // Se o ID não existir, retorna null
        return null;
    }

    public String[] getSlotInfo(int position) {
        // Validar tabuleiro
        if (tabuleiro == null || tabuleiro.slots == null) {
            return null;
        }

        // Validar posição
        int boardSize = tabuleiro.getWorldSize();
        if (position < 1 || position > boardSize) {
            return null;
        }

        // Obter o slot pela posição (1-indexed)
        Slot slot = tabuleiro.getSlot(position);
        if (slot == null) {
            return null;
        }

        // [0] IDs dos jogadores
        String ids = (slot.jogadores == null || slot.jogadores.isEmpty()) ? "" : slot.getIDJogadores();

        // [1] Nome do evento (descrição)
        Evento evento = slot.getEvento();
        String nomeEvento = (evento == null) ? "" : evento.getNome();

        // [2] Tipo do evento (formato "A:X" ou "T:X")
        String tipo = (evento == null) ? "" : evento.toString();

        return new String[]{ids, nomeEvento, tipo};
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 5: ROTAÇÃO DE JOGADORES (Quem joga a seguir?)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Obtém o ID do jogador que está a jogar atualmente.
     * 
     * @return O ID do jogador atual
     */
    public int getCurrentPlayerID(){
        return jogadorAtualIndex;
    }
    
    /**
     * Descobre qual é o próximo jogador a jogar.
     * 
     * A rotação funciona por ordem de ID:
     * - Se temos jogadores com IDs 1, 2, 3 e está a jogar o 1, o próximo é o 2
     * - Se está a jogar o 3 (o maior), volta ao início (jogador 1)
     * 
     * @return O ID do próximo jogador
     */
    public int getNextPlayer(){

        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        // Encontrar o próximo ID maior que o atual
        int proximoID = Integer.MAX_VALUE;
        int menorID = Integer.MAX_VALUE;

        for(Jogador j : jogadores){
            // Ignorar jogadores derrotados (não participam na rotação)
            if (j.getEstado() == Estado.DERROTADO) {
                continue;
            }
            
            int id = j.getId();

            // Procurar o menor ID que seja maior que o atual
            if(id > jogadorAtualIndex && id < proximoID){
                proximoID = id;
            }

            // Guardar o menor ID (para quando precisamos "dar a volta")
            if(id < menorID){
                menorID = id;
            }
        }

        // Se não encontrámos ninguém com ID maior, voltamos ao jogador com menor ID
        return (proximoID == Integer.MAX_VALUE) ? menorID : proximoID;
    }

    /**
     * Descobre qual foi o jogador anterior (o que jogou antes do atual).
     * 
     * Funciona ao contrário do getNextPlayer:
     * - Se está a jogar o 2, o anterior é o 1
     * - Se está a jogar o 1 (o menor), o anterior é o jogador com maior ID
     * 
     * @return O ID do jogador anterior
     */
    public int getPreviousPlayer(){
        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        // Encontrar o maior ID menor que o atual
        int anteriorID = Integer.MIN_VALUE;
        int maiorID = Integer.MIN_VALUE;

        for(Jogador j : jogadores){
            // Ignorar jogadores derrotados (não participam na rotação)
            if (j.getEstado() == Estado.DERROTADO) {
                continue;
            }
            
            int id = j.getId();

            // Procurar o maior ID que seja menor que o atual
            if(id < jogadorAtualIndex && id > anteriorID){
                anteriorID = id;
            }

            // Guardar o maior ID (para quando precisamos "dar a volta")
            if(id > maiorID){
                maiorID = id;
            }
        }

        // Se não encontrámos ninguém com ID menor, voltamos ao jogador com maior ID
        return (anteriorID == Integer.MIN_VALUE) ? maiorID : anteriorID;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 6: MOVIMENTO DE JOGADORES
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Move o jogador atual um determinado número de casas para a frente.
     * Esta é uma das funções mais importantes do jogo!
     * 
     * COMO FUNCIONA:
     * 1. Valida se o movimento é válido (1 a 6 casas)
     * 2. Verifica se o jogador pode mover (não está PRESO ou DERROTADO)
     * 3. Aplica restrições de linguagem (C: max 3, Assembly: max 2)
     * 4. Calcula a nova posição (com "bounce" se ultrapassar o tabuleiro)
     * 5. Move o jogador e passa o turno
     * 
     * @param nrSpaces Número de casas a avançar (1 a 6, como um dado)
     * @return true se o movimento foi realizado, false se não foi possível
     */
    public boolean moveCurrentPlayer(int nrSpaces){
        // Sem tabuleiro, não há jogo!
        if(tabuleiro == null){
            return false;
        }

        // VALIDAÇÃO: O dado só pode ter valores de 1 a 6
        if(nrSpaces < 1 || nrSpaces > 6){
            return false;  // Turno não muda para dado inválido
        }
        
        // Obter o jogador que está a jogar
        Jogador jogadorAtual = tabuleiro.getPlayer(jogadorAtualIndex);
        if (jogadorAtual == null) {
            return false;
        }
        
        // REGRA: Jogadores PRESOS ou DERROTADOS não podem jogar
        // O turno passa e conta como uma jogada
        if (jogadorAtual.getEstado() == Estado.PRESO || jogadorAtual.getEstado() == Estado.DERROTADO) {
            numTurnos++;  // Conta como um turno
            jogadorAtualIndex = getNextPlayer();  // Passa para o próximo
            return false;
        }
        
        // ═════════════════════════════════════════════════════════════════
        // RESTRIÇÕES DE LINGUAGEM
        // Algumas linguagens têm limites de movimento:
        // - C: máximo 3 casas por turno
        // - Assembly: máximo 2 casas por turno
        // A restrição aplica-se se o jogador TEM a linguagem (qualquer posição)
        // ═════════════════════════════════════════════════════════════════
        ArrayList<String> linguagens = jogadorAtual.getLinguagens();
        
        if (linguagens != null) {
            // Verificar se tem C em qualquer posição
            boolean temC = false;
            boolean temAssembly = false;
            
            for (String lang : linguagens) {
                if (lang != null && lang.trim().equalsIgnoreCase("C")) {
                    temC = true;
                }
                if (lang != null && lang.trim().equalsIgnoreCase("Assembly")) {
                    temAssembly = true;
                }
            }
            
            // Programadores C só podem mover até 3 casas
            if (temC && nrSpaces > 3) {
                return false;
            }
            
            // Programadores Assembly só podem mover até 2 casas
            if (temAssembly && nrSpaces > 2) {
                return false;
            }
        }

        
        // Guardar o valor do dado (necessário para o abismo "ErroDeLogica")
        ultimoDado = nrSpaces;
        
        // ═════════════════════════════════════════════════════════════════
        // GUARDAR HISTÓRICO DE POSIÇÕES
        // Necessário para os abismos CodigoDuplicado e EfeitosSecundarios
        // ═════════════════════════════════════════════════════════════════
        int posicaoAtual = tabuleiro.getPosOf(jogadorAtual);
        
        // Guardar posição de 2 turnos atrás (para EfeitosSecundarios)
        Integer posAnterior = posicaoAnterior.get(jogadorAtual.getId());
        if (posAnterior != null) {
            posicaoHaDoisTurnos.put(jogadorAtual.getId(), posAnterior);
        }
        // Guardar posição atual como anterior (para CodigoDuplicado)
        posicaoAnterior.put(jogadorAtual.getId(), posicaoAtual);

        // ═════════════════════════════════════════════════════════════════
        // CÁLCULO DA POSIÇÃO FINAL
        // Se ultrapassar o fim do tabuleiro, o jogador "ressalta" (bounce)
        // Exemplo: tabuleiro de 6 casas, está na 5, avança 3 = vai para 4
        // ═════════════════════════════════════════════════════════════════
        int posicaoDestino = posicaoAtual + nrSpaces;
        int boardSize = tabuleiro.getWorldSize();
        int posicaoFinal = 0;

        if(posicaoDestino >= boardSize){
            // Ultrapassou! Calcular o "bounce"
            int excesso = posicaoDestino - boardSize;
            posicaoFinal = boardSize - excesso;
        } else {
            // Movimento normal
            posicaoFinal = posicaoDestino;
        }

        // ═════════════════════════════════════════════════════════════════
        // EXECUTAR O MOVIMENTO
        // Remove o jogador da casa atual e coloca na nova
        // ═════════════════════════════════════════════════════════════════
        Slot slotAtual = tabuleiro.getSlot(posicaoAtual);
        Slot slotDestino = tabuleiro.getSlot(posicaoFinal);
        if(slotAtual != null && slotDestino != null){
            slotAtual.removePlayer(jogadorAtual);
            slotDestino.addPlayer(jogadorAtual);
        }

        return true;  // Movimento realizado com sucesso!

    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 7: ESTADO DO JOGO (O jogo acabou? Quem ganhou?)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Verifica se o jogo terminou.
     * O jogo termina quando um jogador chega à última casa do tabuleiro.
     * 
     * @return true se o jogo acabou (há um vencedor), false se ainda está a decorrer
     */
    public boolean gameIsOver(){

        // Sem tabuleiro, não há jogo para terminar
        if (tabuleiro == null || tabuleiro.slots == null) {
            return false;
        }

        List<Jogador> jogadores = tabuleiro.getListaJogadores();
        
        // Sem jogadores, não há jogo
        if (jogadores == null) {
            return false;
        }

        // Se existe um vencedor, o jogo terminou!
        if(tabuleiro.getWinner() != null){
            return true;
        }

        return false;  // Jogo ainda a decorrer
    }

    /**
     * Obtém os resultados finais do jogo para mostrar ao utilizador.
     * Só deve ser chamada depois de gameIsOver() retornar true!
     * 
     * FORMATO DO RESULTADO:
     * - Linha 1: "THE GREAT PROGRAMMING JOURNEY"
     * - Linha 2: (vazia)
     * - Linha 3: "NR. DE TURNOS"
     * - Linha 4: número de turnos
     * - Linha 5: (vazia)
     * - Linha 6: "VENCEDOR"
     * - Linha 7: nome do vencedor
     * - Linha 8: (vazia)
     * - Linha 9: "RESTANTES"
     * - Linhas seguintes: jogadores ordenados por proximidade da meta
     * 
     * @return Lista de strings com os resultados, ou null se o jogo não terminou
     */
    public ArrayList<String> getGameResults(){

        if(tabuleiro == null){
            return null;
        }

        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        // Obter o vencedor
        Jogador vencedor = tabuleiro.getWinner();

        // Se não há vencedor, o jogo ainda não acabou
        if(vencedor == null){
            return null;
        }

        String nomeVencedor = tabuleiro.getWinner().getNome();

        // Ordenar os jogadores restantes:
        // 1º: Por posição (mais perto da meta = maior posição = primeiro)
        // 2º: Em caso de empate, alfabeticamente pelo nome
        jogadores.sort((a,b) -> {
            int jogadorA = tabuleiro.getPosOf(a);
            int jogadorB = tabuleiro.getPosOf(b);
            
            // Quem está mais à frente vem primeiro
            int comparacaoPosicao = Integer.compare(jogadorB, jogadorA);
            if (comparacaoPosicao != 0) {
                return comparacaoPosicao;
            }
            // Empate? Ordena por nome (A-Z)
            return a.getNome().compareTo(b.getNome());
        });

        // Construir a lista de resultados
        ArrayList<String> resultado = new ArrayList<>();

        resultado.add("THE GREAT PROGRAMMING JOURNEY");
        resultado.add("");  // Linha vazia
        resultado.add("NR. DE TURNOS");
        resultado.add(String.valueOf(numTurnos));
        resultado.add("");  // Linha vazia
        resultado.add("VENCEDOR");
        resultado.add(nomeVencedor);
        resultado.add("");  // Linha vazia
        resultado.add("RESTANTES");

        // Adicionar os jogadores restantes (o vencedor é o primeiro, por isso começamos em 1)
        for(int i = 1; i < jogadores.size(); i++){
            Jogador jogador = jogadores.get(i);
            int posicao = tabuleiro.getPosOf(jogador);
            resultado.add(jogador.getNome() + " " + posicao);
        }
        
        return resultado;
    }



    public JPanel getAuthorsPanel(){
            return new JPanel();
        //A função getAuthorsPanel() devolve um painel gráfico personalizado para a janela de "Créditos".
        //
        //Retorno:
        //JPanel com conteúdo criado pelos alunos (ex: nomes, números, imagens).
        //null se não implementado (mostra mensagem padrão).
        //Formato:
        //Janela de 300x300 pixels.
        //Pode incluir JLabels, textos formatados, imagens, etc.
        //Exemplo:
        //JPanel panel = new JPanel();
        //panel.add(new JLabel("Nome: João Silva"));
        //return panel;

    }

    public HashMap<String, String> customizeBoard(){

        return new HashMap<>();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 8: CRIAÇÃO DE EVENTOS (Abismos e Ferramentas)
    // ═══════════════════════════════════════════════════════════════════════════

    // 
    // O jogo tem dois tipos de eventos que podem aparecer nas casas:
    // - ABISMOS: Armadilhas que prejudicam o jogador (recuar, prender, derrotar)
    // - FERRAMENTAS: Itens que ajudam o jogador (anulam abismos específicos)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Cria um abismo (Abyss) baseado no seu ID.
     * 
     * LISTA DE ABISMOS:
     * ID 0: ErroDeSintaxe    - Recua 1 casa (anulado por Herança)
     * ID 1: ErroDeLogica     - Recua metade do dado
     * ID 2: Exception        - Recua 2 casas (anulado por TratamentoDeExcepcoes)
     * ID 3: FileNotFound     - Recua 3 casas (anulado por IDE)
     * ID 4: Crash            - Volta para casa 1
     * ID 5: CodigoDuplicado  - Volta para posição anterior
     * ID 6: EfeitosSecundarios - Volta para posição de 2 turnos atrás
     * ID 7: BlueScreenOfDeath - Jogador é DERROTADO! (anulado por AjudaDoProfessor)
     * ID 8: CicloInfinito    - Jogador fica PRESO (anulado por ProgramacaoFuncional)
     * ID 9: SegmentationFault - Derrotado se 2+ jogadores na mesma casa (anulado por UnitTests)
     * 
     * @param id ID do abismo a criar
     * @return O abismo criado, ou null se ID inválido
     */
    private Abyss createAbyss(int id) {
        return switch (id) {
            case 0 -> new ErroDeSintaxe();
            case 1 -> new ErroDeLogica();
            case 2 -> new Exception();
            case 3 -> new FileNotFoundException();
            case 4 -> new Crash();
            case 5 -> new CodigoDuplicado();
            case 6 -> new EfeitosSecundarios();
            case 7 -> new BlueScreenOfDeath();
            case 8 -> new CicloInfinito();
            case 9 -> new SegmentationFault();
            default -> null;
        };
    }

    /**
     * Cria uma ferramenta (Tool) baseada no seu ID.
     * 
     * LISTA DE FERRAMENTAS:
     * ID 0: Herança             - Anula ErroDeSintaxe
     * ID 1: ProgramacaoFuncional - Anula CicloInfinito
     * ID 2: UnitTests           - Anula SegmentationFault
     * ID 3: TratamentoDeExcepcoes - Anula Exception
     * ID 4: IDE                  - Anula FileNotFoundException
     * ID 5: AjudaDoProfessor    - Anula BlueScreenOfDeath
     * 
     * @param id ID da ferramenta a criar
     * @return A ferramenta criada, ou null se ID inválido
     */
    private Tool createTool(int id) {
        return switch (id) {
            case 0 -> new Heranca();
            case 1 -> new ProgramacaoFuncional();
            case 2 -> new UnitTests();
            case 3 -> new TratamentoDeExcepcoes();
            case 4 -> new IDE();
            case 5 -> new AjudaDoProfessor();
            default -> null;
        };
    }

    /**
     * Cria um evento (Abismo ou Ferramenta) baseado no tipo e subtipo.
     * 
     * @param tipo 0 = Abismo, 1 = Ferramenta
     * @param subtipo ID específico do evento (ver createAbyss/createTool)
     * @return O evento criado, ou null se inválido
     */
    private Evento createEvento(int tipo, int subtipo) {
        return switch (tipo) {
            case 0 -> createAbyss(subtipo);   // Tipo 0 = Abismo
            case 1 -> createTool(subtipo);    // Tipo 1 = Ferramenta
            default -> null;
        };
    }


public Jogador getJogador(int id) {
    if (tabuleiro == null) {
       return null;
    }
    return tabuleiro.getPlayer(id);
}

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {



        // Cria o tabueliro normal inicial com a funçao original
        if (!createInitialBoard(playerInfo, worldSize)) {
            return false;
        }

        // Se existeirem eventos na input,são adicionados
        if (abyssesAndTools != null) {

            // abyssesAndTools é Array de Array -> array de eventos (cada linha), cada evento (linha) é um array de 3 informaçoes (cada linha tem 3 partes)
            // Para cada linha/item verifico se tem 3 partes e não é null
            for (String[] item : abyssesAndTools) {
                if (item == null || item.length != 3) {
                    return false;
                }

                // Vou buscar as 3 informaçoes desta linha que rpepresenta um evento
                // posicao 0 -> tipo (abyss ou tool - 0 ou 1)
                // posicao 1 -> ID
                // posicao 2 -> posicao do evento no tabuleiro
                int tipo;
                int subtipo;
                int position;

                // Converter informaços "raw" que vem em String para int
                // posiçao está feito.
                try {
                    tipo = Integer.parseInt(item[0]);
                    subtipo = Integer.parseInt(item[1]);
                    position = Integer.parseInt(item[2]);
                } catch (NumberFormatException e) {
                    return false;
                }

                //Validações do tipo
                if(tipo < 0 || tipo > 1){
                    return false;
                }

                //Validações do subtipo
                if(subtipo < 0 ){
                    return false;
                }
                // Validações da posição
                if (position <= 1 || position >= worldSize) {
                    return false;
                }

                Evento evento = createEvento(tipo, subtipo);
                if (evento == null) {
                    return false;
                }

                Slot slot = tabuleiro.getSlot(position);
                if (slot.getEvento() != null) {
                    return false; // Já existe um evento neste slot
                }
                slot.setEvento(evento);
            }
        }

        return true;
    }

    public String getProgrammersInfo() {
        if (tabuleiro == null || tabuleiro.getListaJogadores() == null) {
            return "";
        }

        List<Jogador> jogadores = tabuleiro.getListaJogadores();
        StringBuilder result = new StringBuilder();
        List<Jogador> jogadoresVivos = new ArrayList<>();

        for (Jogador jogador : jogadores) {
            // Jogadores vivos incluem EM_JOGO e PRESO (só DERROTADOS são excluídos)
            if (jogador.getEstado() != Estado.DERROTADO) {
                jogadoresVivos.add(jogador);
            }
        }
        
        
        for (int i = 0; i < jogadoresVivos.size(); i++) {
            Jogador jogador = jogadoresVivos.get(i);
            result.append(jogador.getNome());
            result.append(" : ");

            ArrayList<String> ferramentas = jogador.getFerramentas();
            if (ferramentas == null || ferramentas.isEmpty()) {
                result.append("No tools");
            } else {
                result.append(String.join(";", ferramentas));
            }

            if (i < jogadoresVivos.size() - 1) {
                result.append(" | ");
            }
        }

        return result.toString();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SEÇÃO 9: REAÇÃO A EVENTOS (O que acontece quando pisas num abismo/ferramenta?)
    // ═══════════════════════════════════════════════════════════════════════════
    
    /**
     * Processa a reação do jogador ao evento na casa onde parou.
     * Deve ser chamada DEPOIS de moveCurrentPlayer()!
     * 
     * Esta função verifica se o jogador parou numa casa com evento e aplica o efeito:
     * - FERRAMENTA: O jogador recolhe (se ainda não tiver)
     * - ABISMO: O efeito negativo é aplicado (recuar, prender, derrotar, etc.)
     *          A menos que o jogador tenha a ferramenta que anula este abismo!
     * 
     * @return Mensagem descrevendo o que aconteceu, ou null se não havia evento
     */
    public String reactToAbyssOrTool() {
        // Validações iniciais
        if (tabuleiro == null) {
            numTurnos++;
            jogadorAtualIndex = getNextPlayer();
            return null;
        }
        
        // Obter jogador atual (o turno ainda não avançou)
        int idJogadorAtual = getCurrentPlayerID();
        Jogador jogador = tabuleiro.getPlayer(idJogadorAtual);

        if (jogador == null) {
            numTurnos++;
            jogadorAtualIndex = getNextPlayer();
            return null;
        }
        
        // Encontrar posição atual do jogador
        int posicaoJogador = tabuleiro.getPosOf(jogador);
        if (posicaoJogador < 1) {
            return null;
        }
        
        Slot slotAtual = tabuleiro.getSlot(posicaoJogador);
        if (slotAtual == null) {
            return null;
        }
        
        // Se não houver evento na casa, avança turno e retorna null
        Evento evento = slotAtual.getEvento();
        if (evento == null) {
            numTurnos++;
            jogadorAtualIndex = getNextPlayer();
            return null;
        }
        
        // POLIMORFISMO: usa isTool() para distinguir sem instanceof
        if (evento.isTool()) {
            // É uma ferramenta - jogador recolhe (se ainda não tiver)
            String nomeFerramenta = evento.getNome();
            if (!jogador.getFerramentas().contains(nomeFerramenta)) {
                // Jogador não tem esta ferramenta - adiciona
                jogador.addFerramenta(nomeFerramenta);
                // A ferramenta permanece no slot para outros jogadores
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Recolheu ferramenta: " + nomeFerramenta;
            } else {
                // Jogador já tem esta ferramenta - retorna mensagem
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Já possui ferramenta: " + nomeFerramenta;
            }
        } else {
            // É um Abyss - fazer cast seguro para Abyss (sabemos que não é Tool)
            Abyss abyss = (Abyss) evento;
            String ferramentaAnuladora = abyss.getFerramentaAnuladora();
            
            // Verificar se existe ferramenta que anula E se o jogador a tem
            if (ferramentaAnuladora != null && jogador.getFerramentas().contains(ferramentaAnuladora)) {
                // Jogador tem a ferramenta - anula o abismo
                jogador.getFerramentas().remove(ferramentaAnuladora);
                
                // CicloInfinito: mesmo anulado, liberta outros jogadores na mesma casa
                if (abyss.getCasasRecuo() == -2) {
                    libertarJogadoresNaCasa(slotAtual, jogador);
                }
                
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return abyss.getNome() + " anulado por " + ferramentaAnuladora;
            }
            
            // Aplicar efeito do abismo
            int casasRecuo = abyss.getCasasRecuo();
            
            if (casasRecuo == -1) {
                // BlueScreenOfDeath: derrota o jogador e move para casa 1
                jogador.setEstado(Estado.DERROTADO);
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Jogador derrotado";
                
            } else if (casasRecuo == -2) {
                // CicloInfinito: prende o jogador e liberta outros na mesma casa
                jogador.setEstado(Estado.PRESO);
                libertarJogadoresNaCasa(slotAtual, jogador);
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Jogador preso";
                
            } else if (casasRecuo == -3) {
                // Crash: volta para casa 1
                moverJogadorParaPosicao(jogador, slotAtual, 1);
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Volta para casa 1";
                
            } else if (casasRecuo == -4) {
                // ErroDeLogica: recua metade do último dado (arredondado para baixo)
                int recuo = ultimoDado / 2;
                int novaPosicao = Math.max(1, posicaoJogador - recuo);
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Recua " + recuo + " casa" + (recuo > 1 ? "s" : "");
                
            } else if (casasRecuo == -5) {
                // CodigoDuplicado: volta para posição anterior (anula o movimento)
                Integer posAnterior = posicaoAnterior.get(jogador.getId());
                int novaPosicao = (posAnterior != null) ? posAnterior : posicaoJogador;
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Volta para posição anterior";
                
            } else if (casasRecuo == -6) {
                // EfeitosSecundarios: volta para posição de 2 turnos atrás
                Integer pos2Turnos = posicaoHaDoisTurnos.get(jogador.getId());
                int novaPosicao = (pos2Turnos != null) ? pos2Turnos : posicaoJogador;
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Volta para posição de 2 turnos atrás";
                
            } else if (casasRecuo == -7) {
                // SegmentationFault: só ativa se 2+ jogadores na mesma casa
                List<Jogador> jogadoresNaCasa = slotAtual.getJogadores();
                if (jogadoresNaCasa.size() >= 2) {
                    // Todos os jogadores na casa recuam 3 casas
                    for (Jogador j : new ArrayList<>(jogadoresNaCasa)) {
                        int novaPosicao = Math.max(1, posicaoJogador - 3);
                        Slot destino = tabuleiro.getSlot(novaPosicao);
                        if (destino != null) {
                            slotAtual.removePlayer(j);
                            destino.addPlayer(j);
                        }
                    }
                    numTurnos++;
                    jogadorAtualIndex = getNextPlayer();
                    return "Caiu num " + abyss.getNome().toLowerCase() + "! Todos recuam 3 casas";
                }
                // Se só há 1 jogador, não acontece nada mas retorna mensagem
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Nada acontece";
                
            } else {
                // Recuar X casas (mínimo posição 1)
                int novaPosicao = Math.max(1, posicaoJogador - casasRecuo);
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                numTurnos++;
                jogadorAtualIndex = getNextPlayer();
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Recua " + casasRecuo + " casa" + (casasRecuo > 1 ? "s" : "");
            }
        }
    }
    
    // Helper: move jogador para uma posição específica
    private void moverJogadorParaPosicao(Jogador jogador, Slot slotAtual, int novaPosicao) {
        Slot slotDestino = tabuleiro.getSlot(novaPosicao);
        if (slotDestino != null && slotAtual != slotDestino) {
            slotAtual.removePlayer(jogador);
            slotDestino.addPlayer(jogador);
        }
    }
    
    // Helper: liberta jogadores presos na mesma casa (exceto o jogador atual)
    private void libertarJogadoresNaCasa(Slot slot, Jogador jogadorAtual) {
        for (Jogador j : slot.getJogadores()) {
            if (j != jogadorAtual && j.getEstado() == Estado.PRESO) {
                j.setEstado(Estado.EM_JOGO);
            }
        }
    }

    public void loadGame(File file) throws InvalidFileException, java.io.FileNotFoundException {


    }

    public boolean saveGame(File file) {

        return true;
    }

}


