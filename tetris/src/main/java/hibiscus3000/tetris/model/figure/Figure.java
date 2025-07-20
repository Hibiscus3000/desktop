package hibiscus3000.tetris.model.figure;

import java.util.OptionalInt;

public class Figure implements ReadOnlyFigure {

    private final int size;
    private final boolean[][] pieces;
    private final int numberOfPieces;

    Figure(boolean[][] pieces, int numberOfPieces) {
        this.pieces = pieces;
        size = pieces[0].length;
        this.numberOfPieces = numberOfPieces;
    }

    public boolean isOccupied(int x, int y) {
        return pieces[y][x];
    }

    public Figure rotate(boolean clockwise) {
        final boolean[][] newPieces = new boolean[size][size];
        if (clockwise) {
            for (int x = 0; x < size; ++x) {
                for (int y = 0; y < size; ++y) {
                    newPieces[y][x] = pieces[size - x - 1][y];
                }
            }
        } else {
            for (int x = 0; x < size; ++x) {
                for (int y = 0; y < size; ++y) {
                    newPieces[y][x] = pieces[x][size - y - 1];
                }
            }
        }
        return new Figure(newPieces, numberOfPieces);
    }

    public static Figure generate(int size) {
        return new FigureGenerator(size).generate();
    }

    @Override
    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public int getSize() {
        return size;
    }
    
    private OptionalInt leftmostOccupiedCell = OptionalInt.empty();
    private OptionalInt rightmostOccupiedCell = OptionalInt.empty();
    private OptionalInt lowestOccupiedCell = OptionalInt.empty();
    private OptionalInt highestOccupiedCell = OptionalInt.empty();
    
    public int getLeftmostOccupiedCell() {
        if (leftmostOccupiedCell.isEmpty()) {
            leftmostOccupiedCell = OptionalInt.of(FigureFramer.getLeftmostOccupiedCell(pieces));
        }
        return leftmostOccupiedCell.getAsInt();
    }

    public int getRightmostOccupiedCell() {
        if (rightmostOccupiedCell.isEmpty()) {
            rightmostOccupiedCell = OptionalInt.of(FigureFramer.getRightmostOccupiedCell(pieces));
        }
        return rightmostOccupiedCell.getAsInt();
    }

    public int getLowestOccupiedCell() {
        if (lowestOccupiedCell.isEmpty()) {
            lowestOccupiedCell = OptionalInt.of(FigureFramer.getLowestOccupiedCell(pieces));
        }
        return lowestOccupiedCell.getAsInt();
    }

    public int getHighestOccupiedCell() {
        if (highestOccupiedCell.isEmpty()) {
            highestOccupiedCell = OptionalInt.of(FigureFramer.getHighestOccupiedCell(pieces));
        }
        return highestOccupiedCell.getAsInt();
    }
}
