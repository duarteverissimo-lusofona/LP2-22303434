package pt.ulusofona.lp2.greatprogrammingjourney.event.abyss;

public class SegmentationFault extends Abyss {
    public SegmentationFault() {
        super("Segmentation Fault", 9, "core-dumped.png", null, -7); // -7 = só ativa se 2+ jogadores, todos recuam 3
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
