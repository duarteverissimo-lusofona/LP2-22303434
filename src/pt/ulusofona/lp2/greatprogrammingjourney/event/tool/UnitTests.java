package pt.ulusofona.lp2.greatprogrammingjourney.event.tool;

public class UnitTests extends Tool {
    public UnitTests() {

        super("Testes Unitários", 2, "unit-tests.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
