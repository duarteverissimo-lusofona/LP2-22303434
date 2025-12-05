package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class ErroDeLogica extends Abyss {
    public ErroDeLogica() {

        super("Erro de Lógica", 1, "logic.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
