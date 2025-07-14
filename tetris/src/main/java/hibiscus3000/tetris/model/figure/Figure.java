package hibiscus3000.tetris.model.figure;

public class Figure implements ReadOnlyFigure {

    private final int width;
    private final int height;
    private final boolean[][] pieces;
    private final int numberOfPieces;

    Figure(boolean[][] pieces, int numberOfPieces) {
        this.pieces = pieces;
        width = pieces[0].length;
        height = pieces.length;
        this.numberOfPieces = numberOfPieces;
    }

    public boolean isOccupied(int x, int y) {
        return pieces[y][x];
    }

    public Figure rotate(boolean clockwise) {
        final int newWidth = height;
        final int newHeight = width;
        final boolean[][] newPieces = new boolean[newHeight][newWidth];
        if (clockwise) {
            for (int x = 0; x < newWidth; ++x) {
                for (int y = 0; y < newHeight; ++y) {
                    newPieces[y][x] = pieces[height - x - 1][y];
                }
            }
        } else {
            for (int x = 0; x < newWidth; ++x) {
                for (int y = 0; y < newHeight; ++y) {
                    newPieces[y][x] = pieces[x][width - y - 1];
                }
            }
        }
        return new Figure(newPieces, numberOfPieces);
    }

    public static Figure generate(int size) {
        return new FigureGenerator(size).generate();
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public int getWidth() {
        return width;
    }
}
