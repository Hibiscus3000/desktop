package hibiscus3000.tetris.model.figure;

public class FigureFramer {

    static Figure createFigureFromRawPieces(boolean[][] pieces, int numberOfPieces) {
        int startX = getLeftmostOccupiedCell(pieces);
        int startY = getHighestOccupiedCell(pieces);
        int endX = getRightmostOccupiedCell(pieces);
        int endY = getLowestOccupiedCell(pieces);
        int width = endX - startX + 1;
        int height = endY - startY + 1;
        int size = Math.max(width, height);
        boolean[][] newPieces = new boolean[size][size];
        int newStartX = 0;
        int newStartY = 0;
        if (size != width) {
            newStartX = Math.max((size - width) / 2, 1);
        }
        if (size != height) {
            newStartY = Math.max((size - height) / 2, 1);
        }
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                newPieces[newStartY + y][newStartX + x] = pieces[startY + y][startX + x];
            }
        }
        return new Figure(newPieces, numberOfPieces);
    }

    public static int getLeftmostOccupiedCell(boolean[][] pieces) {
        for (int x = 0; x < pieces[0].length; ++x) {
            for (int y = 0; y < pieces.length; ++y) {
                if (pieces[y][x]) {
                    return x;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    public static int getRightmostOccupiedCell(boolean[][] pieces) {
        for (int x = pieces[0].length - 1; x >= 0; --x) {
            for (int y = 0; y < pieces.length; ++y) {
                if (pieces[y][x]) {
                    return x;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    public static int getHighestOccupiedCell(boolean[][] pieces) {
        for (int y = 0; y < pieces.length; ++y) {
            for (int x = 0; x < pieces[0].length; ++x) {
                if (pieces[y][x]) {
                    return y;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    public static int getLowestOccupiedCell(boolean[][] pieces) {
        for (int y = pieces[0].length - 1; y >= 0; --y) {
            for (int x = 0; x < pieces.length; ++x) {
                if (pieces[y][x]) {
                    return y;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }
}
