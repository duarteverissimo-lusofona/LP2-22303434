package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;

public class BlueScreenOfDeath extends Abyss {

    public BlueScreenOfDeath() {
        super("Blue Screen of Death", 7, "bsod.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
