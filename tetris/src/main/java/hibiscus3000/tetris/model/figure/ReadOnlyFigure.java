package hibiscus3000.tetris.model.figure;

public interface ReadOnlyFigure {

    int getWidth();

    int getHeight();

    int getNumberOfPieces();

    boolean isOccupied(int x, int y);
}
