package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class EfeitosSecundarios extends Abyss {
    public EfeitosSecundarios() {
        super("Efeitos Secundários", 6, "secondary-effects.png", "Programação Funcional");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
