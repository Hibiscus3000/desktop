package hibiscus3000.cellular_automaton.model.field;

import hibiscus3000.cellular_automaton.model.NeighbourPolicy;
import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.cell.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Field<C extends Cell> {

    protected final C[][] cells;
    protected final int rows;
    protected final int columns;

    public Field(C[][] cells, int rows, int columns) {
        this.cells = cells;
        this.rows = rows;
        this.columns = columns;
    }

    public C getCell(Point coordinates) {
        return cells[coordinates.getRow()][coordinates.getColumn()];
    }

    public void setCell(Point coordinates, C cell) {
        cells[coordinates.getRow()][coordinates.getColumn()] = cell;
    }

    public List<C> getNeighbours(Point coordinates, NeighbourPolicy neighbourPolicy) {
        final List<C> neighbours = new ArrayList<>();
        for (Point relativeNeighbourCoordinates : neighbourPolicy.getPos()) {
            final Point neighbourCoordinates = relativeNeighbourCoordinates.add(coordinates);
            neighbours.add(getCell(neighbourCoordinates));
        }
        return neighbours;
    }

    public int getRowCount() {
        return rows;
    }

    public int getColumnCount() {
        return columns;
    }

    public abstract void initialize(Map<Point, C> config);

}
