package hibiscus3000.tetris.view;

import hibiscus3000.tetris.model.Field;
import hibiscus3000.tetris.model.figure.ReadOnlyFigure;
import hibiscus3000.tetris.model.manager.FigureListener;
import hibiscus3000.tetris.model.math.Point;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FieldView extends Pane implements FigureListener {

    private DoubleBinding xSize;
    private DoubleBinding ySize;

    private Rectangle[][] cells;
    private Field field;

    private static final double PREF_STROKE_WIDTH = 2.0;
    private static final Color BORDER_COLOR = Color.BLACK;
    private static final Color OCCUPIED_COLOR = Color.BLUE;
    private static final Color[] FIGURE_COLORS = {Color.RED,
            Color.YELLOW,
            Color.CHARTREUSE,
            Color.BLUEVIOLET,
            Color.DARKTURQUOISE,
            Color.ORANGERED};
    private static final Color FREE_COLOR = Color.DARKGRAY;

    private ReadOnlyFigure figure = null;
    private Point figureStartPos = null;

    public void setField(Field field) {
        this.field = field;
        xSize = widthProperty().subtract(PREF_STROKE_WIDTH).divide(field.getWidth());
        ySize = heightProperty().subtract(PREF_STROKE_WIDTH).divide(field.getHeight());
        createCells();
    }

    private void createCells() {
        cells = new Rectangle[field.getHeight()][field.getWidth()];
        for (int h = 0; h < field.getHeight(); ++h) {
            for (int w = 0; w < field.getWidth(); ++w) {
                final var cell = new Rectangle();
                cell.widthProperty().bind(xSize);
                cell.heightProperty().bind(ySize);
                cell.xProperty().bind(xSize.multiply(w).add(PREF_STROKE_WIDTH / 2));
                cell.yProperty().bind(ySize.multiply(h).add(PREF_STROKE_WIDTH / 2));
                cell.setStroke(BORDER_COLOR);
                cell.setStrokeWidth(PREF_STROKE_WIDTH);
                getChildren().add(cell);
                cells[h][w] = cell;
                field.getOccupiedProperty(w, h).addListener((observable, oldVal, newVal) -> {
                    setCellColor(cell, newVal);
                });
                setCellColor(cell, field.getOccupied(w, h));
            }
        }
    }

    private void setCellColor(Rectangle cell, boolean isOccupied) {
        if (isOccupied) {
            cell.setFill(OCCUPIED_COLOR);
        } else {
            cell.setFill(FREE_COLOR);
        }
    }

    @Override
    public void newFigure(Point startPos, ReadOnlyFigure figure) {
        assert null == this.figure;
        this.figureStartPos = startPos;
        this.figure = figure;
        setFigureColorOccupied();
    }

    @Override
    public void updateFigurePos(Point figureStartPos) {
        setFigureColor(FREE_COLOR);
        this.figureStartPos = figureStartPos;
        setFigureColorOccupied();
    }

    @Override
    public void removeFigure() {
        setFigureColor(FREE_COLOR);
    }

    private void setFigureColorOccupied() {
        setFigureColor(FIGURE_COLORS[figure.getNumberOfPieces() % FIGURE_COLORS.length]);
    }

    private void setFigureColor(Color color) {
        for (int w = 0; w < figure.getWidth(); ++w) {
            for (int h = 0; h < figure.getHeight(); ++h) {
                if (figure.isOccupied(w, h)) {
                    cells[figureStartPos.y + h][figureStartPos.x + w].setFill(color);
                }
            }
        }
    }
}
