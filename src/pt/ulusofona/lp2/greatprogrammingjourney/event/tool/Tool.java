package pt.ulusofona.lp2.greatprogrammingjourney.event.tool;

import pt.ulusofona.lp2.greatprogrammingjourney.event.Evento;

public abstract class Tool extends Evento {


    public Tool(String nome, Integer id, String imagem) {
        super(nome, id, imagem);
    }

    @Override
    public void interargirJogador() {

    }
    
    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public String toString() {
        return "T:" + getId();
    }
}
