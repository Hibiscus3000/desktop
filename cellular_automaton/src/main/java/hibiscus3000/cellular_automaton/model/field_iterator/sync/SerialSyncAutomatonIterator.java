package hibiscus3000.cellular_automaton.model.field_iterator.sync;

import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field_iterator.IteratorTask;
import hibiscus3000.cellular_automaton.model.function.Function;

public class SerialSyncAutomatonIterator<C extends Cell> extends SyncAutomatonIterator<C> {

    public SerialSyncAutomatonIterator(Function<C> function) {
        super(function);
    }

    @Override
    public void apply(IteratorTask<C> task) {
        for (int i = 0; i < task.getRowsCount(); ++i) {
            for (int j = 0; j < task.getColumnsCount(); ++j) {
                applyFunction(new Point(i, j), task);
            }
        }
    }
}
