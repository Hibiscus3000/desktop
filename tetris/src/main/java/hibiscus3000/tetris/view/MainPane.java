package hibiscus3000.tetris.view;

import hibiscus3000.tetris.model.Field;
import hibiscus3000.tetris.view.control.ControlPanel;
import javafx.scene.layout.BorderPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainPane extends BorderPane {

    public MainPane() {
        ControlPanel controlPanel = new ControlPanel();
        setTop(controlPanel);
        FieldView fieldView = new FieldView();
        setCenter(fieldView);
        Field field = new Field(controlPanel.getFieldWidth(), controlPanel.getFieldHeight());
        fieldView.setField(field);

        final ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> field.setOccupied(1, 1, true), 2, TimeUnit.SECONDS);
        scheduler.schedule(() -> field.setOccupied(3, 3, true), 4, TimeUnit.SECONDS);
        scheduler.schedule(() -> field.setOccupied(6, 6, true), 6, TimeUnit.SECONDS);
    }
}
