package hibiscus3000.tetris.model;

public interface GameListener {

    default void start() {
    }

    default void pause() {
    }

    default void stop() {
    }
    
    default void changeWidth(int width) {
    }

    default void changeHeight(int height) {
    }

    default void moveDown() {
    }

    default void moveRight() {
    }

    default void rotateRight() {
    }

    default void moveLeft() {
    }

    default void rotateLeft() {
    }
}
