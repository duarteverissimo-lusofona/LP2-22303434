package pt.ulusofona.lp2.greatprogrammingjourney.event.tool;

public class Heranca extends Tool {
    public Heranca() {

        super("Herança", 0, "inheritance.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
