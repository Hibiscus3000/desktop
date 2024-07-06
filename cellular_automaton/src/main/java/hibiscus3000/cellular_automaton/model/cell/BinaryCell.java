package hibiscus3000.cellular_automaton.model.cell;

public class BinaryCell extends Cell {

    public static final BinaryCell ALIVE_CELL = new BinaryCell(true);
    public static final BinaryCell DEAD_CELL = new BinaryCell(false);

    private boolean alive;

    private BinaryCell(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
