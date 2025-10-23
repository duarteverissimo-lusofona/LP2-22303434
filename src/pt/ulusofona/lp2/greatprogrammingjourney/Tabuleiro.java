package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    List<Slot> slots ;
    int worldSize;

    public Tabuleiro(int worldSize) {
        this.worldSize = worldSize;
        this.slots = new ArrayList<>();

        for(int i = 0; i < worldSize; i++){
            slots.add(new Slot(i + 1));
        }
    }

    Slot getSlot(int index){
        if(index < 0 || index > slots.size()) {
            return null;
        }
        return slots.get(index);
    }

    int getWorldSize(){
        return worldSize;
    }

    public List<Jogador> getListaJogadores(){

        List<Jogador> listJogadores = new ArrayList<>();

        if(slots.isEmpty()){
            return null;
        }

        for(Slot slot : slots){
            listJogadores.addAll(slot.getJogadores());
        }

        return  listJogadores;
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

        for(Slot slot : slots){
            if(slot.getJogadores().contains(jogador)){
                return slot.getNrSlot();
            }
        }
        return -1;
    }

    public Jogador getWinner(){

        for(Jogador jogador : getListaJogadores()){
            System.out.println(getPosOf(jogador));
            if(getPosOf(jogador) == worldSize){
                System.out.println("Vencedor: " + jogador);
                return jogador;
            }
        }

        return null;
    }

    public boolean botarJogador(Jogador jogador, int posicao){
        slots.get(posicao - 1).addPlayer(jogador);
        return true;
    }

}
