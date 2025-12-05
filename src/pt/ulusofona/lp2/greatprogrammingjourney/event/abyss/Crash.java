package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class Crash extends Abyss {
    public Crash() {
        super("Crash", 4, "crash.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
