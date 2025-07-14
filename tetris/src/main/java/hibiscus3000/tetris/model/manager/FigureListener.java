package hibiscus3000.tetris.model.manager;

import hibiscus3000.tetris.model.figure.ReadOnlyFigure;
import hibiscus3000.tetris.model.math.Point;

public interface FigureListener {

    void newFigure(Point startPos, ReadOnlyFigure figure);

    void updateFigurePos(Point figureStartPos);

    void removeFigure();
}
