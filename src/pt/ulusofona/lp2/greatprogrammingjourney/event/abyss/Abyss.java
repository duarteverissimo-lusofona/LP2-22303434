package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;

import pt.ulusofona.lp2.greatprogrammingjourney.event.Evento;

public abstract class Abyss extends Evento {

    // Nome da ferramenta que anula este abismo (null se nenhuma anula)
    private String ferramentaAnuladora;

    public Abyss(String nome, Integer id, String imagem, String ferramentaAnuladora) {
        super(nome, id, imagem);
        this.ferramentaAnuladora = ferramentaAnuladora;
    }

    // Getter para obter o nome da ferramenta que anula este abismo
    public String getFerramentaAnuladora() {
        return ferramentaAnuladora;
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

