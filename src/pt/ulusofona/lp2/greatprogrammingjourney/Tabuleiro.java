package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    List<Slot> slots ;
    int worldSize;
    // Lista para manter ordem de criação dos jogadores
    private List<Jogador> listaJogadoresOrdemCriacao;

    public Tabuleiro(int worldSize) {
        this.worldSize = worldSize;
        this.slots = new ArrayList<>();
        this.listaJogadoresOrdemCriacao = new ArrayList<>();

        for(int i = 0; i < worldSize; i++){
            slots.add(new Slot(i + 1));
        }
    }

    /**
     * Retorna o Slot na posição especificada (1-indexed).
     * Internamente converte para 0-indexed.
     * @param position Posição no tabuleiro (1 a worldSize)
     * @return O Slot nessa posição, ou null se fora dos limites
     */
    Slot getSlot(int position) {
        if (position < 1 || position > slots.size()) {
            return null;
        }
        return slots.get(position - 1);
    }

    /**
     * Define o Slot na posição especificada (1-indexed).
     * Internamente converte para 0-indexed.
     * @param position Posição no tabuleiro (1 a worldSize)
     * @param slot O Slot a colocar nessa posição
     */
    void setSlot(int position, Slot slot) {
        if (position >= 1 && position <= slots.size()) {
            slots.set(position - 1, slot);
        }
    }

    int getWorldSize(){
        return worldSize;
    }

    public List<Jogador> getListaJogadores(){
        // Retorna jogadores na ordem de criação
        return listaJogadoresOrdemCriacao;
    }

    public Jogador getPlayer(int id){

        for(Jogador jogador : getListaJogadores()){
            if(jogador.getId() == id){
                return jogador;
            }
        }
        return null;
    }

    public int getPosOf(Jogador jogador){
        if (jogador == null) {
            return -1;
        }
        
        for(Slot slot : slots){
            for(Jogador j : slot.getJogadores()) {
                if (j.getId() == jogador.getId()) {
                    return slot.getNrSlot();
                }
            }
        }
        return -1;
    }

    public Jogador getWinner() {
        for (Jogador jogador : getListaJogadores()) {
            if (getPosOf(jogador) == worldSize) {
                return jogador;
            }
        }
        return null;
    }

    public boolean botarJogador(Jogador jogador, int posicao) {
        Slot slot = getSlot(posicao);
        if (slot != null) {
            slot.addPlayer(jogador);
            listaJogadoresOrdemCriacao.add(jogador);
            return true;
        }
        return false;
    }

}
