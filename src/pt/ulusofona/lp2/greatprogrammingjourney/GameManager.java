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

enum Cor {
    BLUE,
    BROWN,
    GREEN,
    PURPLE;

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

enum Estado {
    EM_JOGO,
    DERROTADO,
    PRESO
}


public class GameManager {

    private Tabuleiro tabuleiro;
    //private ArrayList<Jogador> jogadores;
    private int jogadorAtualIndex;
    private int numTurnos;
    
    // Tracking para efeitos de abismos
    private int ultimoDado; // Para ErroDeLogica (recua metade do dado)
    private HashMap<Integer, Integer> posicaoAnterior; // Para CodigoDuplicado (ID jogador -> posição anterior)
    private HashMap<Integer, Integer> posicaoHaDoisTurnos; // Para EfeitosSecundarios (ID jogador -> posição de 2 turnos atrás)

    public GameManager() {
        this.posicaoAnterior = new HashMap<>();
        this.posicaoHaDoisTurnos = new HashMap<>();
    }


    //A funçao jogadorValido vai ver se o jogador é valido
    public Jogador createJogador(String[] playerInfo) {

        String idStr = playerInfo[0];
        String nome = playerInfo[1];
        String linguagensStr = playerInfo[2];
        String corStr = playerInfo[3];

        // Verificar se o array tem o tamanho correto
        if (playerInfo == null || playerInfo.length != 4) {
            return null;
        }

        //Validar ID (deve ser numérico e >= 1)
        if (idStr == null || idStr.isEmpty()) {
            return null;
        }

        for (int i = 0; i < idStr.length(); i++) {
            if (!Character.isDigit(idStr.charAt(i))) {
                return null;
            }
        }

        int id = Integer.parseInt(idStr);

        if (id < 0) {
            return null;
        }

        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }

        if (linguagensStr == null) {
            return null;
        }

        ArrayList<String> linguagensProgramacao = new ArrayList<>();

        String[] partes = linguagensStr.split(";");

        for(String s : partes){
            linguagensProgramacao.add(s.trim());
        }


        if (corStr == null) {
            return null;
        }

        Cor cor = Cor.fromString(corStr);

        if (cor == null) {
            return null;
        }

        Jogador jogador = new Jogador(Integer.parseInt(idStr),nome, cor, linguagensProgramacao, 1);
        System.out.println(jogador);
        return jogador;
    }

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {

        this.tabuleiro = new Tabuleiro(worldSize);


        //Número de jogadores entre 2 e 4.
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            System.out.println("player sé nulll ou num players errado");
            return false;
        }

        //worldSize >= (número de jogadores × 2)
        if (worldSize < (playerInfo.length * 2)) {
            System.out.println("regra de worldsize nao cumprida");
            return false;
        }

        // IDs únicos e >=1, nomes não vazios, cores válidas e únicas
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> cores = new ArrayList<>();

        for (String[] player : playerInfo) {
            Jogador jogador = createJogador(player);

            if (jogador == null) {
                System.out.println("jogador e null");
                return false;
            }

            int id = Integer.parseInt(player[0]);
            if (ids.contains(id)) {
                return false;
            }
            ids.add(id);

            String cor = player[3];
            if (cores.contains(cor)) {
                return false;
            }
            cores.add(cor);

            // Adicionar o jogador à lista
            tabuleiro.botarJogador(jogador, 1);
        }

        int menorId = Integer.MAX_VALUE;
        for(Jogador jogador : tabuleiro.getListaJogadores()){
                if(jogador.getId() < menorId){
                    menorId = jogador.getId();
                }
        }

        jogadorAtualIndex = menorId;
        numTurnos = 1;
        System.out.println(worldSize);

        for(Jogador jogador : tabuleiro.getListaJogadores()){
            System.out.println(jogador);
        }
        return true;
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

        if(tabuleiro.getSlot(nrSquare - 1).getEvento() == null){
            return null;
        }

        String png = tabuleiro.getSlot(nrSquare - 1).getEvento().getImagem();

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

        // Obter o slot (position - 1 porque os slots começam em index 0)
        Slot slot = tabuleiro.getSlot(position - 1);
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

    public int getCurrentPlayerID(){

        return jogadorAtualIndex;
    }

    public int getNextPlayer(){
        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        // Encontrar o próximo ID maior que o atual
        int proximoID = Integer.MAX_VALUE;
        int menorID = Integer.MAX_VALUE;

        for(Jogador j : jogadores){
            int id = j.getId();

            // Procurar o menor ID maior que o atual
            if(id > jogadorAtualIndex && id < proximoID){
                proximoID = id;
            }

            // Guardar o menor ID geral (para wrap-around)
            if(id < menorID){
                menorID = id;
            }
        }

        // Se não há próximo maior, volta ao início
        return (proximoID == Integer.MAX_VALUE) ? menorID : proximoID;
    }

    // Obtém o jogador anterior (o que acabou de jogar antes do turno atual)
    public int getPreviousPlayer(){
        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        // Encontrar o maior ID menor que o atual
        int anteriorID = Integer.MIN_VALUE;
        int maiorID = Integer.MIN_VALUE;

        for(Jogador j : jogadores){
            int id = j.getId();

            // Procurar o maior ID menor que o atual
            if(id < jogadorAtualIndex && id > anteriorID){
                anteriorID = id;
            }

            // Guardar o maior ID geral (para wrap-around)
            if(id > maiorID){
                maiorID = id;
            }
        }

        // Se não há anterior menor, volta ao maior (wrap-around)
        return (anteriorID == Integer.MIN_VALUE) ? maiorID : anteriorID;
    }

    public boolean moveCurrentPlayer(int nrSpaces){
        if(tabuleiro == null){
            return false;
        }

        //Parâmetros: nrSpaces (número de casas a avançar, 1 a 6).
        //Validações:
        //Se nrSpaces < 1 ou > 6 → retorna false (turno não muda).
        if(nrSpaces < 1 || nrSpaces > 6){
            return false;
        }
        
        Jogador jogadorAtual = tabuleiro.getPlayer(jogadorAtualIndex);
        if (jogadorAtual == null) {
            return false;
        }
        
        // Se o jogador está PRESO (CicloInfinito) ou DERROTADO, não pode mover mas turno avança
        if (jogadorAtual.getEstado() == Estado.PRESO || jogadorAtual.getEstado() == Estado.DERROTADO) {
            numTurnos++;
            jogadorAtualIndex = getNextPlayer();
            return false; // Movimento não aconteceu, mas turno passou
        }
        
        // Restrições de movimento baseadas na primeira linguagem
        ArrayList<String> linguagens = jogadorAtual.getLinguagens();
        boolean movimentoRestrigido = false;
        if (linguagens != null && !linguagens.isEmpty()) {
            String primeiraLinguagem = linguagens.get(0);
            if (primeiraLinguagem.equalsIgnoreCase("C") && nrSpaces > 3) {
                // C: máximo 3 casas - jogador fica na mesma posição mas turno avança
                movimentoRestrigido = true;
            } else if (primeiraLinguagem.equalsIgnoreCase("Assembly") && nrSpaces > 2) {
                // Assembly: máximo 2 casas - jogador fica na mesma posição mas turno avança
                movimentoRestrigido = true;
            }
        }
        
        if (movimentoRestrigido) {
            // Jogador fica na mesma posição, mas o turno passa para o próximo
            numTurnos++;
            jogadorAtualIndex = getNextPlayer();
            return true; // Retorna true porque o turno avançou
        }
        
        // Guardar último dado para ErroDeLogica
        ultimoDado = nrSpaces;
        
        // Obter posição atual
        int posicaoAtual = tabuleiro.getPosOf(jogadorAtual);

        // Guardar histórico de posições ANTES de mover
        // Atualizar posição de 2 turnos atrás com a posição anterior guardada
        Integer posAnterior = posicaoAnterior.get(jogadorAtual.getId());
        if (posAnterior != null) {
            posicaoHaDoisTurnos.put(jogadorAtual.getId(), posAnterior);
        }
        // Guardar posição atual como anterior
        posicaoAnterior.put(jogadorAtual.getId(), posicaoAtual);

        //Se ultrapassar o tabuleiro, recua para a posição válida.
        int posicaoDestino = posicaoAtual + nrSpaces;
        int boardSize = tabuleiro.getWorldSize();
        int posicaoFinal = 0;

        if(posicaoDestino >= boardSize){
            int excesso = posicaoDestino - boardSize;
            posicaoFinal = boardSize - excesso;
            System.out.println();
            System.out.println("Posicao de destino: " + (posicaoDestino - 1) + " Excesso: " + excesso + " Foi para: " + posicaoFinal);
        } else {
            posicaoFinal = posicaoDestino;
        }

        //Move o jogador e incrementa o contador de turnos.
        Slot slotAtual = tabuleiro.getSlot(posicaoAtual - 1);
        Slot slotDestino = tabuleiro.getSlot(posicaoFinal - 1);
        if(slotAtual != null && slotDestino != null){
            slotAtual.removePlayer(jogadorAtual);
            slotDestino.addPlayer(jogadorAtual);
        }
        numTurnos++;

        //Passa o turno para o próximo jogador (ordem circular).
        jogadorAtualIndex = getNextPlayer();
        return true;

    }

    public boolean gameIsOver(){

        // Validar se o tabuleiro existe
        if (tabuleiro == null || tabuleiro.slots == null) {

        return false;

        }

        List<Jogador> jogadores = tabuleiro.getListaJogadores();
        
        // Validar se a lista de jogadores existe
        if (jogadores == null) {

        return false;

        }

        if(tabuleiro.getWinner() != null){
            return true;
        }

        return false;
    }

    public ArrayList<String> getGameResults(){

        if(tabuleiro == null){
            return null;
        }

        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        Jogador vencedor = tabuleiro.getWinner();

        if(vencedor == null){
            return null;
        }

        String nomeVencedor = tabuleiro.getWinner().getNome();

        jogadores.sort((a,b) -> {

            int jogadorA = tabuleiro.getPosOf(a);
            int jogadorB = tabuleiro.getPosOf(b);
            return Integer.compare(jogadorB,jogadorA);
        });

        ArrayList<String> resultado = new ArrayList<>();

        resultado.add("THE GREAT PROGRAMMING JOURNEY");
        resultado.add("");
        resultado.add("NR. DE TURNOS");
        resultado.add(String.valueOf(numTurnos));
        resultado.add("");
        resultado.add("VENCEDOR");
        resultado.add(nomeVencedor);
        resultado.add("");
        resultado.add("RESTANTES");

        for(int i = 1; i < jogadores.size(); i++){
            resultado.add(jogadores.get(i).getNome() + " " + String.valueOf(tabuleiro.getPosOf(jogadores.get(i))));
        }

        System.out.println(resultado);

        return resultado;
        //A função getGameResults() devolve um ArrayList<String> com os resultados finais do jogo no formato exato.
        //
        //Conteúdo:
        //Linha 1: "THE GREAT PROGRAMMING JOURNEY"
        //Linha vazia ("")
        //Linha 3: "NR. DE TURNOS"
        //Linha 4: número de turnos (como string)
        //Linha vazia
        //Linha 6: "VENCEDOR"
        //Linha 7: nome do vencedor
        //Linha vazia
        //Linha 9: "RESTANTES"
        //Linhas subsequentes: nomes dos jogadores restantes, ordenados por proximidade da meta (mais perto primeiro) e, em empate, alfabeticamente.
        //Formato:
        //Linhas vazias são strings vazias ("").
        //O vencedor não aparece na lista de restantes.
        //A função não calcula resultados, apenas retorna o estado atual.

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

    // ======================================================= Parte 2 =================================================

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

    private Evento createEvento(int tipo, int subtipo) {

        return switch (tipo) {
            case 0 -> createAbyss(subtipo);
            case 1 -> createTool(subtipo);
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

        // Debug input info
        {
            System.out.println("\nPlayer input:");
            for (String[] s : playerInfo) {
                for (String ss : s) {
                    System.out.printf(ss + " ");
                }
                System.out.println("");
            }

            System.out.println("\nEvent input (tipo, subtipo, posicao):");
            if (abyssesAndTools != null) {
                for (String[] s : abyssesAndTools) {
                    for (String ss : s) {
                        System.out.printf(ss + " ");
                    }
                    System.out.println("");
                }
            }
        }


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

                Slot slot = tabuleiro.getSlot(position - 1);
                if (slot.getEvento() != null) {
                    return false; // Já existe um evento neste slot
                }
                slot.setEvento(evento);
            }
        }
        for(int i = 0; i < worldSize;i++){
            if(tabuleiro.getSlot(i).getEvento() != null) {
                System.out.println(tabuleiro.getSlot(i).getNrSlot());
                System.out.println(tabuleiro.getSlot(i).getEvento());
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
            if (jogador.getEstado() == Estado.EM_JOGO) {
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

    public String reactToAbyssOrTool() {
        // Validações iniciais
        if (tabuleiro == null) {
            return null;
        }
        
        // Obter jogador que acabou de jogar (moveCurrentPlayer já avançou o turno)
        int idJogadorAnterior = getPreviousPlayer();
        Jogador jogador = tabuleiro.getPlayer(idJogadorAnterior);
        if (jogador == null) {
            return null;
        }
        
        // Encontrar posição atual do jogador
        int posicaoJogador = tabuleiro.getPosOf(jogador);
        if (posicaoJogador < 1) {
            return null;
        }
        
        Slot slotAtual = tabuleiro.getSlot(posicaoJogador - 1);
        if (slotAtual == null) {
            return null;
        }
        
        // Se não houver evento na casa, retorna null
        Evento evento = slotAtual.getEvento();
        if (evento == null) {
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
                return "Recolheu ferramenta: " + nomeFerramenta;
            } else {
                // Jogador já tem esta ferramenta - não acontece nada
                return null;
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
                
                return abyss.getNome() + " anulado por " + ferramentaAnuladora;
            }
            
            // Aplicar efeito do abismo
            int casasRecuo = abyss.getCasasRecuo();
            
            if (casasRecuo == -1) {
                // BlueScreenOfDeath: derrota o jogador e move para casa 1
                //moverJogadorParaPosicao(jogador, slotAtual, 1);
                jogador.setEstado(Estado.DERROTADO);
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Jogador derrotado";
                
            } else if (casasRecuo == -2) {
                // CicloInfinito: prende o jogador e liberta outros na mesma casa
                jogador.setEstado(Estado.PRESO);
                libertarJogadoresNaCasa(slotAtual, jogador);
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Jogador preso";
                
            } else if (casasRecuo == -3) {
                // Crash: volta para casa 1
                moverJogadorParaPosicao(jogador, slotAtual, 1);
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Volta para casa 1";
                
            } else if (casasRecuo == -4) {
                // ErroDeLogica: recua metade do último dado (arredondado para baixo)
                int recuo = ultimoDado / 2;
                int novaPosicao = Math.max(1, posicaoJogador - recuo);
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Recua " + recuo + " casa" + (recuo > 1 ? "s" : "");
                
            } else if (casasRecuo == -5) {
                // CodigoDuplicado: volta para posição anterior (anula o movimento)
                Integer posAnterior = posicaoAnterior.get(jogador.getId());
                int novaPosicao = (posAnterior != null) ? posAnterior : posicaoJogador;
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Volta para posição anterior";
                
            } else if (casasRecuo == -6) {
                // EfeitosSecundarios: volta para posição de 2 turnos atrás
                Integer pos2Turnos = posicaoHaDoisTurnos.get(jogador.getId());
                int novaPosicao = (pos2Turnos != null) ? pos2Turnos : posicaoJogador;
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Volta para posição de 2 turnos atrás";
                
            } else if (casasRecuo == -7) {
                // SegmentationFault: só ativa se 2+ jogadores na mesma casa
                List<Jogador> jogadoresNaCasa = slotAtual.getJogadores();
                if (jogadoresNaCasa.size() >= 2) {
                    // Todos os jogadores na casa recuam 3 casas
                    for (Jogador j : new ArrayList<>(jogadoresNaCasa)) {
                        int novaPosicao = Math.max(1, posicaoJogador - 3);
                        Slot destino = tabuleiro.getSlot(novaPosicao - 1);
                        if (destino != null) {
                            slotAtual.removePlayer(j);
                            destino.addPlayer(j);
                        }
                    }
                    return "Caiu num " + abyss.getNome().toLowerCase() + "! Todos recuam 3 casas";
                }
                // Se só há 1 jogador, não acontece nada mas retorna mensagem
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Nada acontece";
                
            } else {
                // Recuar X casas (mínimo posição 1)
                int novaPosicao = Math.max(1, posicaoJogador - casasRecuo);
                moverJogadorParaPosicao(jogador, slotAtual, novaPosicao);
                return "Caiu num " + abyss.getNome().toLowerCase() + "! Recua " + casasRecuo + " casa" + (casasRecuo > 1 ? "s" : "");
            }
        }
    }
    
    // Helper: move jogador para uma posição específica
    private void moverJogadorParaPosicao(Jogador jogador, Slot slotAtual, int novaPosicao) {
        Slot slotDestino = tabuleiro.getSlot(novaPosicao - 1);
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


