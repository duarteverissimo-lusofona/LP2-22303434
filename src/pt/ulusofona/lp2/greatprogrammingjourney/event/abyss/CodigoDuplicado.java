package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class CodigoDuplicado extends Abyss {
    public CodigoDuplicado() {
        super("Código Duplicado", 5, "duplicated-code.png", "Herança");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
