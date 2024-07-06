package hibiscus3000.cellular_automaton.view.field;

import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.field.Field;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public abstract class FieldPane<C extends Cell> extends Pane {

    private final int rows;
    private final int columns;

    protected final Rectangle[][] cells;

    private final Paint strokePaint = Color.BLACK;
    private final double strokeWidth = 1.5;

    public FieldPane(Field<C> field) {
        this.rows = field.getRowCount();
        this.columns = field.getColumnCount();
        cells = new Rectangle[rows][columns];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                cells[i][j] = createCell(i, j);
                getChildren().add(cells[i][j]);
            }
        }
        updateFieldPane(field);
    }

    private Rectangle createCell(int i, int j) {
        final var cell = new Rectangle();
        final DoubleBinding xSize = widthProperty().subtract(strokeWidth).divide(this.rows);
        final DoubleBinding ySize = heightProperty().subtract(strokeWidth).divide(this.columns);
        cell.setStroke(strokePaint);
        cell.setStrokeWidth(strokeWidth);
        cell.widthProperty().bind(xSize);
        cell.heightProperty().bind(ySize);
        cell.xProperty().bind(xSize.multiply(i).add(strokeWidth / 2));
        cell.yProperty().bind(ySize.multiply(j).add(strokeWidth / 2));
        return cell;
    }

    public void updateFieldPane(Field<C> field) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final Point coordinates = new Point(i, j);
                updateCell(coordinates, field.getCell(coordinates));
            }
        }
    }

    protected abstract void updateCell(Point coordinates, C cell);
}
