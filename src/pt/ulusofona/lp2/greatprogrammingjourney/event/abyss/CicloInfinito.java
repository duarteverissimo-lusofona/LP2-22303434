package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class CicloInfinito extends Abyss {
    public CicloInfinito() {

        super("Ciclo Infinito", 8, "infinite-loop.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
