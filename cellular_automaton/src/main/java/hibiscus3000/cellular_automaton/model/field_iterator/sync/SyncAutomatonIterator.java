package hibiscus3000.cellular_automaton.model.field_iterator.sync;

import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field_iterator.AutomatonIterator;
import hibiscus3000.cellular_automaton.model.function.Function;

public abstract class SyncAutomatonIterator<C extends Cell> extends AutomatonIterator<C> {

    public SyncAutomatonIterator(Function<C> function) {
        super(function);
    }
}
