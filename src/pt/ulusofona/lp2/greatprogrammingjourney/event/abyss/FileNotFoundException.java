package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;


public class FileNotFoundException extends Abyss {
    public FileNotFoundException() {

        super("File Not Found Exeption", 3, "file-not-found-exception.png");
    }

    @Override
    public String toString() {
        return super.toString() + getId();
    }
}
