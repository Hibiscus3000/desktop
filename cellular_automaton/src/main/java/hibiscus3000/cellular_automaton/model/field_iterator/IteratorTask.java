package hibiscus3000.cellular_automaton.model.field_iterator;

import hibiscus3000.cellular_automaton.model.NeighbourPolicy;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field.Field;

public record IteratorTask<C extends Cell>(Field<C> fieldToGet,
                                           Field<C> fieldToSet,
                                           NeighbourPolicy neighbourPolicy) {

    public int getRowsCount() {
        return fieldToGet().getRowCount();
    }

    public int getColumnsCount() {
        return fieldToGet().getColumnCount();
    }
}
