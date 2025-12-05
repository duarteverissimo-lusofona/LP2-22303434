package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;

import pt.ulusofona.lp2.greatprogrammingjourney.event.Evento;
import pt.ulusofona.lp2.greatprogrammingjourney.event.tool.Tool;

public abstract class Abyss extends Evento {

    Tool anulado;

    public Abyss(String nome, Integer id, String imagem) {
        super(nome, id, imagem);
    }

    @Override
    public void interargirJogador() {

    }
    
    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String toString() {
        return "A: ";
    }
}
