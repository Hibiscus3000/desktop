package hibiscus3000.cellular_automaton.view.field;

import hibiscus3000.cellular_automaton.model.CellStateChanger;
import hibiscus3000.cellular_automaton.model.Point;
import hibiscus3000.cellular_automaton.model.field.FieldView;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class FieldPane extends Pane implements FieldView {

    private final Rectangle[][] cells;

    private final DoubleBinding xSize;
    private final DoubleBinding ySize;
    private final Paint strokePaint = Color.BLACK;
    private final double strokeWidth = 1.5;

    private final CellStateChanger cellStateChanger;
    private boolean canAcceptInput = false;

    public FieldPane(int rows, int columns, CellStateChanger cellStateChanger) {
        this.cellStateChanger = cellStateChanger;

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
        cell.setOnMouseClicked(event -> {
            event.consume();
            if (MouseButton.PRIMARY != event.getButton()) {
                return;
            }
            Optional<String> input = Optional.empty();
            if (canAcceptInput) {
                throw new RuntimeException("Not yet implemented"); //TODO
            }
            cellStateChanger.changeState(new Point(i, j), input);
        });
        return cell;
    }

    Rectangle getCellRectangle(Point coordinates) {
        return cells[coordinates.getRow()][coordinates.getColumn()];
    }

    void setCanAcceptInput(boolean canAcceptInput) {
        this.canAcceptInput = canAcceptInput;
    }
}
