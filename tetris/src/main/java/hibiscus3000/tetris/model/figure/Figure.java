package hibiscus3000.tetris.model.figure;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Figure {

    private final int size;
    private final boolean[][] pieces;

    public Figure(int size, boolean[][] pieces) {
        this.size = size;
        this.pieces = pieces;
    }

    public boolean isOccupied(int x, int y) {
        return pieces[x][y];
    }

    public Figure rotate(boolean clockwise) {
        boolean[][] newPieces = new boolean[size][size];
        Function<Integer, Integer> getX;
        Function<Integer, Integer> getY;
        if (clockwise) {
            getX = (y) -> size - y - 1;
            getY = (x) -> x;
        } else {
            getX = (y) -> y;
            getY = (x) -> size - x - 1;
        }
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                newPieces[x][y] = pieces[getY.apply(y)][getX.apply(x)];
            }
        }
        return new Figure(size, newPieces);
    }

    public int getLeftmostOccupiedCell() {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                if (pieces[x][y]) {
                    return x;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    public int getRightmostOccupiedCell() {
        for (int x = size - 1; x >= 0; --x) {
            for (int y = 0; y < size; ++y) {
                if (pieces[x][y]) {
                    return x;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    public int getHighestOccupiedCell() {
        for (int y = 0; y < size; ++y) {
            for (int x = 0; x < size; ++x) {
                if (pieces[x][y]) {
                    return y;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    public int getLowestOccupiedCell() {
        for (int y = size - 1; y >= 0; --y) {
            for (int x = 0; x < size; ++x) {
                if (pieces[x][y]) {
                    return y;
                }
            }
        }
        throw new RuntimeException("Figure cannot be empty");
    }

    public static Figure generate(int size) {
        return new FigureGenerator(size).generate();
    }

    private static class FigureGenerator {

        private static final int[] CHANCE_TO_ADD_NEW_PIECE = {100, 90, 80, 70, 50, 50, 40, 40, 20};

        private final int size;
        private final boolean[][] values;
        private int pieces = 0;
        private int lastPieceX;
        private int lastPieceY;

        private static final Random rnd = new Random();

        public FigureGenerator(int size) {
            this.size = size;
            values = new boolean[size][size];
        }

        public Figure generate() {
            lastPieceX = getInitCoord();
            lastPieceY = getInitCoord();
            while (true) {
                var availablePoses = getAvailablePositions();
                if (availablePoses.isEmpty() || !shouldAddNextPiece()) {
                    break;
                }
                var nextPieceId = availablePoses.get(rnd.nextInt(0, availablePoses.size()));
                lastPieceX = nextPieceId.getKey();
                lastPieceY = nextPieceId.getValue();
                ++pieces;
                values[lastPieceX][lastPieceY] = true;
            }
            return new Figure(size, values);
        }

        private int getInitCoord() {
            return rnd.nextInt(0, size);
        }

        private List<Pair<Integer, Integer>> getAvailablePositions() {
            var availablePoses = new ArrayList<Pair<Integer, Integer>>();
            tryAddPos(availablePoses, lastPieceX - 1, lastPieceY);
            tryAddPos(availablePoses, lastPieceX + 1, lastPieceY);
            tryAddPos(availablePoses, lastPieceX, lastPieceY - 1);
            tryAddPos(availablePoses, lastPieceX, lastPieceY + 1);
            return availablePoses;
        }

        private void tryAddPos(List<Pair<Integer, Integer>> poses, int x, int y) {
            if (posFree(x, y)) {
                poses.add(new Pair<>(x, y));
            }
        }

        private boolean posFree(int x, int y) {
            if (x < 0 || x >= size || y < 0 || y >= size) {
                return false;
            }
            return !values[y][x];
        }

        private boolean shouldAddNextPiece() {
            if (pieces >= CHANCE_TO_ADD_NEW_PIECE.length) {
                return false;
            }
            int chance = rnd.nextInt(0, 101);
            if (chance >= 100 - CHANCE_TO_ADD_NEW_PIECE[pieces]) {
                return true;
            }
            return false;
        }
    }
}
