package hibiscus3000.cellular_automaton.model.field_processor;

import hibiscus3000.cellular_automaton.model.NeighbourPolicy;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field.Field;
import hibiscus3000.cellular_automaton.model.field_iterator.IteratorTask;
import hibiscus3000.cellular_automaton.model.field_iterator.sync.SyncAutomatonIterator;

import java.util.Map;

public class SyncFieldProcessor<C extends Cell> extends FieldProcessor<C> {

    private final Field<C> field1;
    private final Field<C> field2;

    public SyncFieldProcessor(SyncAutomatonIterator<C> iterator, NeighbourPolicy neighbourPolicy,
                              Field<C> field1,
                              Field<C> field2) {
        super(iterator, neighbourPolicy);
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public void iterate() {
        final Field<C> fieldToGet = (0 == getIterationCount() % 2 ? field1 : field2);
        final Field<C> fieldToSet = (1 == getIterationCount() % 2 ? field1 : field2);
        final IteratorTask<C> task = new IteratorTask<>(fieldToGet, fieldToSet, neighbourPolicy);
        iterate(task);
    }


    @Override
    public Field<C> getCurrentField() {
        return (0 == getIterationCount() % 2 ? field1 : field2);
    }

    @Override
    public void initialize() {
        iterationCount.set(0);
        field1.initialize(Map.of());
        field2.initialize(Map.of());
        updateAllViews();
    }

}
