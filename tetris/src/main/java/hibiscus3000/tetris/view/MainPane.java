package hibiscus3000.tetris.view;

import hibiscus3000.tetris.model.Field;
import hibiscus3000.tetris.model.manager.GameManager;
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
        manager.addFigureListener(fieldView);
        controlPanel.addListener(manager);
        manager.start();
        setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case S -> manager.moveDown();
                case A -> manager.moveLeft();
                case D -> manager.moveRight();
                case Q -> manager.rotateLeft();
                case E -> manager.rotateRight();
                default -> {
                    return;
                }
            }
            event.consume();
        });
    }
}
