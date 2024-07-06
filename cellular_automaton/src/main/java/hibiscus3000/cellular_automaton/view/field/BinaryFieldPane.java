package hibiscus3000.cellular_automaton.view.field;

import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.cell.BinaryCell;
import hibiscus3000.cellular_automaton.model.field.Field;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BinaryFieldPane extends FieldPane<BinaryCell> {

    private final Paint alivePaint = Color.WHITE;
    private final Paint deadPaint = Color.BLACK;

    public BinaryFieldPane(Field<BinaryCell> field) {
        super(field);
    }

    @Override
    protected void updateCell(Point coordinates, BinaryCell cell) {
        if (cell.isAlive()) {
            cells[coordinates.getRow()][coordinates.getColumn()].setFill(alivePaint);
        } else {
            cells[coordinates.getRow()][coordinates.getColumn()].setFill(deadPaint);
        }
    }
}
