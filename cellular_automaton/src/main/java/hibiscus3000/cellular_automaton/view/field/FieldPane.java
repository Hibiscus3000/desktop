package hibiscus3000.cellular_automaton.view.field;

import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.field.FieldView;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class FieldPane extends Pane implements FieldView {

    private final Rectangle[][] cells;

    private final DoubleBinding xSize;
    private final DoubleBinding ySize;
    private final Paint strokePaint = Color.BLACK;
    private final double strokeWidth = 1.5;

    public FieldPane(int rows, int columns) {
        xSize = widthProperty().subtract(strokeWidth).divide(rows);
        ySize = heightProperty().subtract(strokeWidth).divide(columns);
        cells = new Rectangle[rows][columns];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                cells[i][j] = createCell(i, j);
                getChildren().add(cells[i][j]);
            }
        }
    }

    private Rectangle createCell(int i, int j) {
        final var cell = new Rectangle();
        cell.setStroke(strokePaint);
        cell.setStrokeWidth(strokeWidth);
        cell.widthProperty().bind(xSize);
        cell.heightProperty().bind(ySize);
        cell.xProperty().bind(xSize.multiply(i).add(strokeWidth / 2));
        cell.yProperty().bind(ySize.multiply(j).add(strokeWidth / 2));
        return cell;
    }

    Rectangle getCellRectangle(Point coordinates) {
        return cells[coordinates.getRow()][coordinates.getColumn()];
    }
}
