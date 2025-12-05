package pt.ulusofona.lp2.greatprogrammingjourney.event.tool;

public class IDE extends Tool {
    public IDE() {
        super("IDE", 4, "IDE.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
