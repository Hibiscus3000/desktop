package hibiscus3000.tetris.view;

import hibiscus3000.tetris.model.Field;
import hibiscus3000.tetris.model.GameManager;
import hibiscus3000.tetris.view.control.ControlPanel;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {

    private volatile boolean eventProcessed = false;

    public MainPane() {
        ControlPanel controlPanel = new ControlPanel();
        setTop(controlPanel);
        FieldView fieldView = new FieldView();
        setCenter(fieldView);
        Field field = new Field(controlPanel.getFieldWidth(), controlPanel.getFieldHeight());
        fieldView.setField(field);
        GameManager manager = new GameManager(field);
        controlPanel.addListener(manager);
        manager.start();
        addEventFilter(
                KeyEvent.ANY,
                event -> {
                    if (!eventProcessed) {
                        switch (event.getCode()) {
                            case S, DOWN -> manager.moveDown();
                            case A, LEFT -> manager.moveLeft();
                            case D, RIGHT -> manager.moveRight();
                            case Q, UP -> manager.rotateLeft();
                            case E -> manager.rotateRight();
                            default -> {
                                return;
                            }
                        }
                    }
                    eventProcessed = !eventProcessed;
                    event.consume();
                });
    }
}
