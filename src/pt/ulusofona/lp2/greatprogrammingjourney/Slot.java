package pt.ulusofona.lp2.greatprogrammingjourney;

import pt.ulusofona.lp2.greatprogrammingjourney.event.Evento;

import java.util.ArrayList;
import java.util.List;

public class Slot {

        int nrSlot;
        List<Jogador> jogadores = new ArrayList<>();
        private Evento evento;

        public Slot(int nrSlot) {
            this.nrSlot = nrSlot;
        }

        public void setEvento(Evento evento) {
            this.evento = evento;
        }

        public Evento getEvento() {

            return evento;
        }

        void addPlayer(Jogador jogador){
            jogadores.add(jogador);
        }

        void removePlayer(Jogador jogador){
            jogadores.remove(jogador);
        }

        public String getIDJogadores(){
            if(jogadores.isEmpty()){
                return "";
            }

            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < jogadores.size(); i++){
                if(i>0){
                    sb.append(",");
                }
                sb.append(jogadores.get(i).getId());
            }
            return sb.toString();
        }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public int getNrSlot() {
        return nrSlot;
    }
}

