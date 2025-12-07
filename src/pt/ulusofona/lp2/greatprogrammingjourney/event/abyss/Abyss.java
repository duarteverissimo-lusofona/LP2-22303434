package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;

import pt.ulusofona.lp2.greatprogrammingjourney.event.Evento;

public abstract class Abyss extends Evento {

    // Nome da ferramenta que anula este abismo (null se nenhuma anula)
    private String ferramentaAnuladora;
    // Número de casas que o jogador recua (0 = efeito especial como derrotar ou prender)
    private int casasRecuo;

    public Abyss(String nome, Integer id, String imagem, String ferramentaAnuladora, int casasRecuo) {
        super(nome, id, imagem);
        this.ferramentaAnuladora = ferramentaAnuladora;
        this.casasRecuo = casasRecuo;
    }

    // Getter para obter o nome da ferramenta que anula este abismo
    public String getFerramentaAnuladora() {
        return ferramentaAnuladora;
    }
    
    // Getter para obter o número de casas a recuar
    public int getCasasRecuo() {
        return casasRecuo;
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
        return "A:" + getId();
    }
}

