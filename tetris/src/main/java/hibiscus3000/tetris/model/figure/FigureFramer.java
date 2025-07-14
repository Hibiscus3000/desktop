package hibiscus3000.tetris.model.figure;

public class FigureFramer {

    static Figure createFigureFromRawPieces(boolean[][] pieces, int numberOfPieces) {
        int startX = getLeftmostOccupiedCell(pieces);
        int startY = getHighestOccupiedCell(pieces);
        int endX = getRightmostOccupiedCell(pieces);
        int endY = getLowestOccupiedCell(pieces);
        int width = endX - startX + 1;
        int height = endY - startY + 1;
        boolean[][] newPieces = new boolean[height][width];
        for (int x = startX; x <= endX; ++x) {
            for (int y = startY; y <= endY; ++y) {
                newPieces[y - startY][x - startX] = pieces[y][x];
            }
        }
        return new Figure(newPieces, numberOfPieces);
    }

    private static int getLeftmostOccupiedCell(boolean[][] pieces) {
        for (int x = 0; x < pieces[0].length; ++x) {
            for (int y = 0; y < pieces.length; ++y) {
                if (pieces[y][x]) {
                    return x;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    private static int getRightmostOccupiedCell(boolean[][] pieces) {
        for (int x = pieces[0].length - 1; x >= 0; --x) {
            for (int y = 0; y < pieces.length; ++y) {
                if (pieces[y][x]) {
                    return x;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    private static int getHighestOccupiedCell(boolean[][] pieces) {
        for (int y = 0; y < pieces.length; ++y) {
            for (int x = 0; x < pieces[0].length; ++x) {
                if (pieces[y][x]) {
                    return y;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    private static int getLowestOccupiedCell(boolean[][] pieces) {
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
