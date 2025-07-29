package hibiscus3000.tetris.model.manager;

import hibiscus3000.tetris.model.Field;
import hibiscus3000.tetris.model.figure.Figure;
import hibiscus3000.tetris.model.math.Point;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class GameManager implements GameListener, AutoCloseable {

    private final Field field;
    private final Random rnd = new Random();

    private final ScheduledExecutorService gameRunner = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gameCycle;
    private ReadOnlyBooleanWrapper gameInProgress = new ReadOnlyBooleanWrapper(false);
    private ReadOnlyBooleanWrapper gameLost = new ReadOnlyBooleanWrapper(false);
    private ReadOnlyBooleanWrapper gameRunning = new ReadOnlyBooleanWrapper(false);
    private final ExecutorService userActionController = Executors.newSingleThreadExecutor();

    private Figure figure = null;
    private final int figureSize = 3;
    private final Point figureStartPos = new Point();
    private final List<FigureListener> figureListeners = new ArrayList<>();

    private static final long MILLISECONDS_PER_MOVE = 350;

    public GameManager(Field field) {
        this.field = field;
    }

    @Override
    public void start() {
        runGame();
    }

    @Override
    public void pause() {
        if (gameRunning.get()) {
            pauseGame();
        } else {
            runGame();
        }
    }

    @Override
    public void stop() {
        stopGame();
    }

    private synchronized void runGame() {
        if (gameRunning.get()) {
            return;
        }
        if (gameLost.get()) {
            stopGame();
        }
        gameInProgress.set(true);
        gameRunning.set(true);
        gameRunner.submit(this::runGameCycle);
    }

    private synchronized void pauseGame() {
        if (!gameRunning.get()) {
            return;
        }
        gameRunning.set(false);
        gameCycle.cancel(false);
    }
    
    private synchronized void stopGame() {
        pauseGame();
        field.clear();
        figure = null;
        notifyRemoval();
        gameLost.set(false);
        gameInProgress.set(false);
    }

    @Override
    public void close() {
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

    private synchronized void runGameCycle() {
        if (!gameRunning.get()) {
            return;
        }
        try {
            if (null == figure) {
                figure = Figure.generate(figureSize);
                figureStartPos.x = rnd.nextInt(-figure.getLeftmostOccupiedCell(),
                        field.getWidth() - figure.getRightmostOccupiedCell());
                figureStartPos.y = -figure.getHighestOccupiedCell();
                if (isFigureMerged(figure, figureStartPos.x, figureStartPos.y)) {
                    mergeFigure();
                } else {
                    notifyNewFigure();
                }
            } else {
                moveFigure(0, 1);
            }
            gameCycle = gameRunner.schedule(this::runGameCycle, MILLISECONDS_PER_MOVE, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        userActionController.submit(() -> moveFigure(stepX, stepY));
    }

    @Override
    public void rotateRight() {
        submitRotate(true);
    }

    @Override
    public void rotateLeft() {
        submitRotate(false);
    }

    @Override
    public ReadOnlyBooleanProperty getGameInProgressProperty() {
        return gameInProgress.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyBooleanProperty getGameLostProperty() {
        return gameLost.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyBooleanProperty getGameRunningProperty() {
        return gameRunning.getReadOnlyProperty();
    }

    private void submitRotate(boolean clockwise) {
        userActionController.submit(() -> rotateFigure(clockwise));
    }

    private void rotateFigure(boolean clockwise) {
        updateFigure(() -> {
            Figure rotatedFigure = figure.rotate(clockwise);
            if (isFigureMergedShift(rotatedFigure, 0, 0)) {
                return;
            }
            figure = rotatedFigure;
            notifyRemoval();
            notifyNewFigure();
        });
    }

    private void moveFigure(int stepX, int stepY) {
        updateFigure(() -> {
            int newStartX = figureStartPos.x + stepX;
            newStartX = Math.max(newStartX, -figure.getLeftmostOccupiedCell());
            newStartX = Math.min(newStartX, field.getWidth() - figure.getRightmostOccupiedCell() - 1);
            int newStartY = figureStartPos.y + stepY;
            if (isFigureMerged(figure, newStartX, newStartY)) {
                mergeFigure();
                return;
            }
            figureStartPos.x = newStartX;
            figureStartPos.y = newStartY;
            notifyFigurePos();
        });
    }

    private synchronized void updateFigure(Runnable updater) {
        if (!gameRunning.get()) {
            return;
        }
        if (null == figure) {
            return;
        }
        updater.run();
    }

    private boolean isFigureMergedShift(Figure figure, int shiftX, int shiftY) {
        int startX = this.figureStartPos.x + shiftX;
        int startY = this.figureStartPos.y + shiftY;
        return isFigureMerged(figure, startX, startY);
    }

    private boolean isFigureMerged(Figure figure, int startX, int startY) {
        if (startY + figure.getLowestOccupiedCell() >= field.getHeight()) {
            return true;
        }
        if (startX + figure.getLeftmostOccupiedCell() < 0) {
            return true;
        }
        if (startX + figure.getRightmostOccupiedCell() >= field.getWidth()) {
            return true;
        }
        for (int x = 0; x <= figure.getRightmostOccupiedCell(); ++x) {
            for (int y = 0; y <= figure.getLowestOccupiedCell(); ++y) {
                if (figure.isOccupied(x, y) && field.getOccupied(x + startX, y + startY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void mergeFigure() {
        notifyRemoval();
        occupyFigureCells();
        freeLines();
        figure = null;
        if (!field.isLineFree(0)) {
            pauseGame();
            gameLost.set(true);
        }
    }

    private void occupyFigureCells() {
        for (int x = 0; x < figure.getSize(); ++x) {
            for (int y = 0; y < figure.getSize(); ++y) {
                if (figure.isOccupied(x, y)) {
                    field.setOccupied(figureStartPos.x + x, figureStartPos.y + y, true);
                }
            }
        }
    }

    private void freeLines() {
        for (int y = figure.getHighestOccupiedCell(); y <= figure.getLowestOccupiedCell(); ++y) {
            int lineI = figureStartPos.y + y;
            if (field.isLineOccupied(lineI)) {
                field.clearLine(lineI);
                field.shitDownUpTo(lineI + 1);
            }
        }
    }

    public void addFigureListener(FigureListener listener) {
        figureListeners.add(listener);
    }

    private void notifyRemoval() {
        notifyFigureListeners(listener -> listener.removeFigure());
    }

    private void notifyNewFigure() {
        notifyFigureListeners(listener -> listener.newFigure(new Point(figureStartPos), figure));
    }

    private void notifyFigurePos() {
        notifyFigureListeners(listener -> listener.updateFigurePos(new Point(figureStartPos)));
    }
    
    private void notifyFigureListeners(Consumer<FigureListener> notifier) {
        for (FigureListener listener : figureListeners) {
            notifier.accept(listener);
        }
    }
}
