package hibiscus3000.cellular_automaton.view.field;

import hibiscus3000.cellular_automaton.model.cell.BinaryCell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class BinaryFieldPaneUpdater extends FieldPaneUpdater<BinaryCell> {

    private final Paint alivePaint = Color.WHITE;
    private final Paint deadPaint = Color.BLACK;

    public BinaryFieldPaneUpdater(FieldPane fieldPane) {
        super(fieldPane);
    }

    @Override
    public void updateCell(BinaryCell cell, Rectangle cellRect) {
        if (cell.isAlive()) {
            cellRect.setFill(alivePaint);
        } else {
            cellRect.setFill(deadPaint);
        }
    }
}
