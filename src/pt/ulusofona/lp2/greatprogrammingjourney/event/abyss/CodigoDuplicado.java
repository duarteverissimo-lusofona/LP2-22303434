package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class CodigoDuplicado extends Abyss {
    public CodigoDuplicado() {

        super("Código Duplicado", 5, "duplicated-code.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
