package hibiscus3000.cellular_automaton.view.field;

import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field.Field;
import javafx.scene.shape.Rectangle;

public abstract class FieldPaneUpdater<C extends Cell> implements FieldViewUpdater<C> {

    private final FieldPane fieldPane;

    public FieldPaneUpdater(FieldPane fieldPane) {
        this.fieldPane = fieldPane;
    }

    abstract void updateCell(C cell, Rectangle cellRect);

    @Override
    public void update(Field<C> field) {
        for (int i = 0; i < field.getRowCount(); ++i) {
            for (int j = 0; j < field.getColumnCount(); ++j) {
                final Point coordinates = new Point(i, j);
                updateCell(field.getCell(coordinates), fieldPane.getCellRectangle(coordinates));
            }
        }
    }
}
