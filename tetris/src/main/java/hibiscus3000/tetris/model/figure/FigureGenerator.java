package hibiscus3000.tetris.model.figure;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FigureGenerator {
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
            values[lastPieceY][lastPieceX] = true;
        }
        return FigureFramer.createFigureFromRawPieces(values, pieces);
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
