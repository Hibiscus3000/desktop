package hibiscus3000.cellular_automaton.model.field_processor;

import hibiscus3000.cellular_automaton.model.NeighbourPolicy;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field.Field;
import hibiscus3000.cellular_automaton.model.field_iterator.AutomatonIterator;
import hibiscus3000.cellular_automaton.model.field_iterator.IteratorTask;
import hibiscus3000.cellular_automaton.view.field.FieldViewUpdater;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;

import java.util.ArrayList;
import java.util.List;

public abstract class FieldProcessor<C extends Cell> {

    protected final AutomatonIterator<C> iterator;
    protected final NeighbourPolicy neighbourPolicy;

    protected final ReadOnlyLongWrapper iterationCount = new ReadOnlyLongWrapper();

    private final List<FieldViewUpdater<C>> fieldViewUpdaters = new ArrayList<>();

    public FieldProcessor(AutomatonIterator<C> iterator, NeighbourPolicy neighbourPolicy) {
        this.iterator = iterator;
        this.neighbourPolicy = neighbourPolicy;
        iterationCount.set(0);
    }

    public abstract Field<C> getCurrentField();

    public void addFieldViewUpdater(FieldViewUpdater<C> fieldViewUpdater) {
        fieldViewUpdaters.add(fieldViewUpdater);
    }

    private void updateAllViews() {
        for (FieldViewUpdater<C> fieldViewUpdater : fieldViewUpdaters) {
            fieldViewUpdater.update(getCurrentField());
        }
    }

    public abstract void iterate();

    protected void iterate(IteratorTask<C> task) {
        iterator.apply(task);
        updateAllViews();
        iterationCount.add(1);
    }

    public long getIterationCount() {
        return iterationCount.get();
    }

    public ReadOnlyLongProperty iterationCountProperty() {
        return iterationCount.getReadOnlyProperty();
    }

    public abstract void initialize();
}
