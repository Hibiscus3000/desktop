package hibiscus3000.cellular_automaton.model.function.binary;

import hibiscus3000.cellular_automaton.model.cell.BinaryCell;

import java.util.List;

public class BinaryLife extends BinaryFunction {

    @Override
    public BinaryCell apply(BinaryCell cell, List<BinaryCell> neighbours) {
        int aliveNeighbours = 0;
        for (var neighbour : neighbours) {
            if (neighbour.isAlive()) {
                ++aliveNeighbours;
            }
        }
        if (3 == aliveNeighbours || 2 == aliveNeighbours && cell.isAlive()) {
            return BinaryCell.ALIVE_CELL;
        } else {
            return BinaryCell.DEAD_CELL;
        }
    }
}
