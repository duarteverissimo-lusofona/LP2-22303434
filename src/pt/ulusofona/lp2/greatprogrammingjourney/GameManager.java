package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    DERROTADO
}

/* enum LinguagensProgramacao{
    JAVA,
    PYTHON,
    ADA,
    C,
    LISP;

    public static LinguagensProgramacao[] fromString(String lp){
        if(lp == null || lp.isEmpty()){
            return null;
        }

        String[] partes = lp.split(";");

        LinguagensProgramacao[] linguagensP = new LinguagensProgramacao[partes.length];

        int i = 0;

        for(String parte : partes) {
            for (LinguagensProgramacao lps : values()) {
                if (lps.name().equalsIgnoreCase(parte.trim())) {
                    linguagensP[i] = lps;
                    i++;
                    break;
                }
            }
        }
        return linguagensP;
    }

}*/

public class GameManager {

    private Tabuleiro tabuleiro;
    //private ArrayList<Jogador> jogadores;
    private int jogadorAtualIndex;
    private int numTurnos;

    public GameManager() {
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

/*
        // Adicionar todos os jogadores ao primeiro slot (posição inicial)
        Slot primeiroSlot = tabuleiro.getSlot(0);
        if (primeiroSlot != null) {
            for (Jogador jogador : this.jogadores) {
                primeiroSlot.addPlayer(jogador);
            }
        }
*/
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
                String[] info = new String[5];

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


                // Obter o estado
                String estado = jogador.estado == Estado.EM_JOGO ? "Em Jogo" : "Derrotado";


                // Formato: <ID> | <Nome> | <Pos> | <Linguagens favoritas> | <Estado>
                return jogador.id + " | " + jogador.nome + " | " + posicao + " | " +
                        linguagens.toString() + " | " + estado;
            }
        }

        // Se o ID não existir, retorna null
        return null;
    }

    public String[] getSlotInfo(int position){


        // Validar se o tabuleiro existe
        if (tabuleiro == null || tabuleiro.slots == null) {
            return null;
        }
        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        // Validar se a lista de jogadores existe
        if (jogadores == null) {
            return null;
        }

        int boardSize = tabuleiro.getWorldSize();

        if(position < 1 || position > boardSize){
            return null;
        }

        // Obter o slot na posição (position - 1 porque os slots começam em index 0)
        Slot slot = tabuleiro.getSlot(position - 1);

        // Se o slot não existir
        if (slot == null) {
            return null;
        }

        // Se a casa estiver vazia → retorna [""]
        if (slot.jogadores == null || slot.jogadores.isEmpty()) {
            return new String[]{""};
        }

        // Construir string com IDs separados por vírgula
        String ids = slot.getIDJogadores();

        return new String[]{ids};
    }

    public int getCurrentPlayerID(){
        /*
        List<Jogador> jogadores = tabuleiro.getListaJogadores();
        if(jogadores == null || jogadores.isEmpty()){
            return -1; // ou outro valor de erro apropriado
        }

        return jogadores.get(jogadorAtualIndex).getId();
*/
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

    public boolean moveCurrentPlayer(int nrSpaces){
        if(tabuleiro == null){
            return false;
        }
        List<Jogador> jogadores = tabuleiro.getListaJogadores();

        //A função moveCurrentPlayer(int nrSpaces) move o jogador actual e avança o turno.
        //
        //Parâmetros: nrSpaces (número de casas a avançar, 1 a 6).
        //Validações:
        //Se nrSpaces < 1 ou > 6 → retorna false (turno não muda).
        if(nrSpaces < 1 || nrSpaces > 6){
            return false;
        }
        //Processo:
        //Calcula posição destino (posição_actual + nrSpaces).
        Jogador jogadorAtual = tabuleiro.getPlayer(jogadorAtualIndex);
        int posicaoAtual = 0;
        for(Slot slot : tabuleiro.slots){
            if(slot.jogadores != null && slot.jogadores.contains(jogadorAtual)){
                posicaoAtual = slot.nrSlot;
                break;
            }
        }

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
        //Retorno: true se a movimentação for válida.
        //A função não modifica dados externos, apenas atualiza o estado interno.
        //System.out.println(tabuleiro.getListaJogadores().toString());
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

        String nomeVencedor = tabuleiro.getWinner().getNome();

        jogadores.sort((a,b) -> {

            int jogadorA = tabuleiro.getPosOf(a);
            int jogadorB = tabuleiro.getPosOf(b);
            return Integer.compare(jogadorB,jogadorA);
        });

        System.out.println("Isto é a ordenação");
        System.out.println(jogadores.toString());

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
            resultado.add(jogadores.get(i).getNome());
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

}


