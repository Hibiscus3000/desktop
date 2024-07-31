package hibiscus3000.cellular_automaton.view;

import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.cell.CellType;
import hibiscus3000.cellular_automaton.model.field_processor.FieldProcessor;

public interface FieldViewUpdaterCreator {

    <C extends Cell> void createViewUpdaters(FieldProcessor<C> fieldProcessor, CellType cellType);
}
