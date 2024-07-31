package hibiscus3000.cellular_automaton.model.field_iterator;

import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field.Field;
import hibiscus3000.cellular_automaton.model.function.Function;

import java.util.List;

public abstract class AutomatonIterator<C extends Cell> {

    protected final Function<C> function;

    public AutomatonIterator(Function<C> function) {
        this.function = function;
    }

    public abstract void apply(IteratorTask<C> task);

    protected void applyFunction(Point cellCoordinates,
                                 IteratorTask<C> task) {
        final Field<C> fieldToGet = task.fieldToGet();
        final C cell = fieldToGet.getCell(cellCoordinates);
        final List<C> neighbours = fieldToGet.getNeighbours(cellCoordinates, task.neighbourPolicy());
        final C processedCell = function.apply(cell, neighbours);
        task.fieldToSet().setCell(cellCoordinates, processedCell);
    }
}
