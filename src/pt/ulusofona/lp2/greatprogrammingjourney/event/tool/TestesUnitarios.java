package pt.ulusofona.lp2.greatprogrammingjourney.event.tool;

public class TestesUnitarios extends Tool {
    public TestesUnitarios() {

        super("Testes Unitários", 2, "unit-tests.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
