package hibiscus3000.tetris.model;

public interface GameListener {

    void start();

    void pause();

    void stop();

    void moveDown();

    void moveRight();

    void rotateRight();

    void moveLeft();

    void rotateLeft();
}
