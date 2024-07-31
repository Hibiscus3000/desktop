package hibiscus3000.cellular_automaton.model.field;

import hibiscus3000.cellular_automaton.model.cell.Cell;

public interface FieldView<C extends Cell> {

    void update(Field<C> field);
}
