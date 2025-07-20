package hibiscus3000.tetris.model.manager;

import javafx.beans.property.ReadOnlyBooleanProperty;

public interface GameListener {

    void start();

    void pause();

    void stop();

    void moveDown();

    void moveRight();

    void rotateRight();

    void moveLeft();

    void rotateLeft();

    ReadOnlyBooleanProperty getGameInProgressProperty();

    ReadOnlyBooleanProperty getGameLostProperty();
    
    ReadOnlyBooleanProperty getGameRunningProperty();
}
