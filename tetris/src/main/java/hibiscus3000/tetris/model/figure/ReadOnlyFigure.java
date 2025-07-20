package hibiscus3000.tetris.model.figure;

public interface ReadOnlyFigure {

    int getSize();

    int getNumberOfPieces();

    boolean isOccupied(int x, int y);
}
