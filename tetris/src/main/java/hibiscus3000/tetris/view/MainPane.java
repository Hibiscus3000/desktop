package hibiscus3000.tetris.view;

import hibiscus3000.tetris.model.Field;
import hibiscus3000.tetris.model.manager.GameManager;
import hibiscus3000.tetris.view.control.ControlPanel;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane implements FieldController {

    private ControlPanel controlPanel = new ControlPanel(this);
    private GameManager gameManager;

    public MainPane() {
        setTop(controlPanel);
        setField(controlPanel.getFieldWidth(), controlPanel.getFieldHeight());
        gameManager.start();
    }

    @Override
    public void setField(int width, int height) {
        FieldView fieldView = new FieldView();
        setCenter(fieldView);
        Field field = new Field(controlPanel.getFieldWidth(), controlPanel.getFieldHeight());
        fieldView.setField(field);
        gameManager = new GameManager(field);
        gameManager.addFigureListener(fieldView);
        controlPanel.setMainListener(gameManager);
        controlPanel.setGameProperties(gameManager.getGameInProgressProperty(), gameManager.getGameRunningProperty(), gameManager.getGameLostProperty());
        setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case S -> gameManager.moveDown();
                case A -> gameManager.moveLeft();
                case D -> gameManager.moveRight();
                case Q -> gameManager.rotateLeft();
                case E -> gameManager.rotateRight();
                case W -> gameManager.rotateRight();
                default -> {
                    return;
                }
            }
            event.consume();
        });
    }
}
