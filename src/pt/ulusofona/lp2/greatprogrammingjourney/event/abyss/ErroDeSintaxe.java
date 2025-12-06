package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class ErroDeSintaxe extends Abyss {
    public ErroDeSintaxe() {
        super("Erro de sintaxe", 0, "syntax.png", "IDE");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
