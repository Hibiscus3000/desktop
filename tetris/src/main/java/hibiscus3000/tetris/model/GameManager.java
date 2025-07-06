package hibiscus3000.tetris.model;

import hibiscus3000.tetris.model.figure.Figure;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

public class GameManager implements GameListener, AutoCloseable {

    private final Field field;
    private final Random rnd = new Random();

    private ScheduledExecutorService gameRunner = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gameCycle;
    volatile boolean gameRunning = false;
    volatile boolean gameLost = false;
    private ExecutorService userFigureMover = Executors.newSingleThreadExecutor();
    private Figure figure = null;
    private final int figureSize = 3;
    private int figureStartX;
    private int figureStartY;

    private long millisecondsPerMove;

    private static final long START_MILLISECONDS_PER_MOVE = 600;
    private static final long MILLISECONDS_PER_MOVE_STEP = 5;
    private static final long MIN_MILLISECONDS_PER_MOVE = 200;

    public GameManager(Field field) {
        this.field = field;
    }

    @Override
    public void start() {
        runGame();
    }

    @Override
    public void pause() {
        if (gameRunning) {
            stopGame();
        } else {
            runGame();
        }
    }

    @Override
    public void stop() {
        stopGame();
        field.clear();
        figure = null;
        gameLost = false;
    }

    @Override
    public void close() throws Exception {
        gameRunner.shutdown();
        try {
            if (!gameRunner.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                gameRunner.shutdownNow();
            }
        } catch (InterruptedException e) {
            gameRunner.shutdownNow();
        }
        try {
            if (!gameRunner.awaitTermination(50, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Unable to shutdown game runner");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void stopGame() {
        if (!gameRunning) {
            return;
        }
        gameCycle.cancel(false);
        gameRunning = false;
    }

    private void decreaseMillisecondsPerMove() {
        millisecondsPerMove = Math.max(MIN_MILLISECONDS_PER_MOVE, millisecondsPerMove - MILLISECONDS_PER_MOVE_STEP);
    }

    private synchronized void cycleFigure() {
        if (!gameRunning) {
            return;
        }
        try {
            if (null == figure) {
                figure = Figure.generate(figureSize);
                figureStartX = rnd.nextInt(-figure.getLeftmostOccupiedCell(), field.getWidth() - figure.getRightmostOccupiedCell());
                figureStartY = -figure.getHighestOccupiedCell();
                if (isFigureMergedShift(figure, 0, 0)) {
                    removeFigure();
                } else {
                    occupyCells();
                }
            } else {
                moveFigure(0, 1);
            }
            decreaseMillisecondsPerMove();
            gameCycle = gameRunner.schedule(this::cycleFigure, millisecondsPerMove, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void runGame() {
        if (gameRunning || gameLost) {
            return;
        }
        millisecondsPerMove = START_MILLISECONDS_PER_MOVE;
        gameRunner = Executors.newSingleThreadScheduledExecutor();
        gameRunner.submit(this::cycleFigure);
        gameRunning = true;
    }

    @Override
    public void moveDown() {
        submitMove(0, 1);
    }

    @Override
    public void moveRight() {
        submitMove(1, 0);
    }

    @Override
    public void moveLeft() {
        submitMove(-1, 0);
    }

    private void submitMove(int stepX, int stepY) {
        userFigureMover.submit(() -> moveFigure(stepX, stepY));
    }

    @Override
    public void rotateRight() {
        submitRotate(true);
    }

    @Override
    public void rotateLeft() {
        submitRotate(false);
    }

    private void submitRotate(boolean clockwise) {
        userFigureMover.submit(() -> rotateFigure(clockwise));
    }

    private synchronized void rotateFigure(boolean clockwise) {
        if (!gameRunning) {
            return;
        }
        if (null == figure) {
            return;
        }
        releaseCells();
        Figure rotatedFigure = figure.rotate(clockwise);
        if (isFigureMergedShift(rotatedFigure, 0, 0)) {
            removeFigure();
        } else {
            figure = rotatedFigure;
            occupyCells();
        }
    }

    private synchronized void moveFigure(int stepX, int stepY) {
        if (!gameRunning) {
            return;
        }
        if (null == figure) {
            return;
        }
        int newFigureStartX = figureStartX + stepX;
        newFigureStartX = Math.max(newFigureStartX, -figure.getLeftmostOccupiedCell());
        newFigureStartX = Math.min(newFigureStartX, field.getWidth() - figure.getRightmostOccupiedCell() - 1);
        int newFigureStartY = figureStartY + stepY;
        releaseCells();
        if (isFigureMerged(figure, newFigureStartX, newFigureStartY)) {
            removeFigure();
            return;
        }
        figureStartX = newFigureStartX;
        figureStartY = newFigureStartY;
        occupyCells();
    }

    private void releaseCells() {
        applyOnFigureCells((fieldX, fieldY) -> field.setOccupied(fieldX, fieldY, false));
    }

    private void occupyCells() {
        applyOnFigureCells((fieldX, fieldY) -> field.setOccupied(fieldX, fieldY, true));
    }

    private boolean isFigureMergedShift(Figure figure, int figureShiftX, int figureShiftY) {
        int figureStartX = this.figureStartX + figureShiftX;
        int figureStartY = this.figureStartY + figureShiftY;
        return isFigureMerged(figure, figureStartX, figureStartY);
    }

    private boolean isFigureMerged(Figure figure, int figureStartX, int figureStartY) {
        if (figureStartY + figure.getLowestOccupiedCell() >= field.getHeight()) {
            return true;
        }
        for (int x = 0; x < figureSize; ++x) {
            for (int y = 0; y < figureSize; ++y) {
                if (figure.isOccupied(x, y) && field.getOccupied(x + figureStartX, y + figureStartY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeFigure() {
        occupyCells();
        freeLines();
        figure = null;
        if (!field.isLineFree(0)) {
            stopGame();
            gameLost = true;
        }
    }

    private void freeLines() {
        for (int y = figure.getHighestOccupiedCell(); y <= figure.getLowestOccupiedCell(); ++y) {
            int lineI = figureStartY + y;
            if (field.isLineOccupied(lineI)) {
                field.clearLine(lineI);
                field.shitDownUpTo(lineI + 1);
            }
        }
    }

    private void applyOnFigureCells(BiConsumer<Integer, Integer> callback) {
        for (int x = 0; x < figureSize; ++x) {
            for (int y = 0; y < figureSize; ++y) {
                if (figure.isOccupied(x, y)) {
                    callback.accept(x + figureStartX, y + figureStartY);
                }
            }
        }
    }
}
