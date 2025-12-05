package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class Exception extends Abyss {
    public Exception() {

        super("Exception", 2, "exception.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
