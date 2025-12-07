package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class CodigoDuplicado extends Abyss {
    public CodigoDuplicado() {
        super("Código Duplicado", 5, "duplicated-code.png", "Herança", -5); // -5 = volta posição anterior
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
