package hibiscus3000.tetris.view;

import hibiscus3000.tetris.view.control.ControlPanel;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {

    public MainPane() {
        setTop(new ControlPanel());
    }
}
