package hibiscus3000.cellular_automaton.view.field;

import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field.Field;

public interface FieldViewUpdater<C extends Cell> {

    void update(Field<C> field);
}
