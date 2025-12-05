package pt.ulusofona.lp2.greatprogrammingjourney.event.tool;

public class AjudaDoProfessor extends Tool {
    public AjudaDoProfessor() {

        super("Ajuda Do Professor", 5, "ajuda-professor.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
