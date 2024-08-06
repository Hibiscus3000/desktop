package hibiscus3000.cellular_automaton.model.field;

import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.cell.BinaryCell;

import java.util.Map;
import java.util.Optional;

public class BinaryField extends Field<BinaryCell> {

    private static final BinaryCell DEFAULT_CELL = BinaryCell.DEAD_CELL;

    public BinaryField(int rows, int columns) {
        super(new BinaryCell[rows][columns], rows, columns);
    }

    @Override
    public void initialize(Map<Point, BinaryCell> config) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final Point coordinates = new Point(i, j);
                setCell(coordinates, config.getOrDefault(coordinates, DEFAULT_CELL));
            }
        }
    }

    @Override
    public void updateExternal(Point coordinates, Optional<String> input) {
        setCell(coordinates,
                new BinaryCell(input.map(Boolean::parseBoolean).orElseGet(() -> !getCell(coordinates).isAlive())));
    }
}
