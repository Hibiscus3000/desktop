package hibiscus3000.tetris.view.control;

import hibiscus3000.tetris.model.GameListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ControlPanel extends HBox {

    private final List<GameListener> listeners = new ArrayList<>();

    private final VBox buttonBox = new VBox();

    private Button startButton;
    private Button pauseButton;
    private Button stopButton;

    private static final String START_BUTTON_LABEL = "Начать";
    private static final String PAUSE_BUTTON_LABEL = "Пауза";
    private static final String STOP_BUTTON_LABEL = "Сброс";

    public ControlPanel() {
        setAlignment(Pos.CENTER);
        getChildren().add(createButtonBox());
    }

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    private void applyToAllListeners(Consumer<GameListener> callback) {
        for (var listener : listeners) {
            callback.accept(listener);
        }
    }

    private void applyToAllListeners(BiConsumer<GameListener, Integer> callback, int arg) {
        for (var listener : listeners) {
            callback.accept(listener, arg);
        }
    }

    private VBox createButtonBox() {
        assert null == startButton && null == pauseButton && null == stopButton : "Can only call it once";
        startButton = createButton(START_BUTTON_LABEL, GameListener::start);
        pauseButton = createButton(PAUSE_BUTTON_LABEL, GameListener::pause);
        stopButton = createButton(STOP_BUTTON_LABEL, GameListener::stop);
        buttonBox.getChildren().addAll(startButton, pauseButton, stopButton);
        buttonBox.getStyleClass().add("control-box");
        return buttonBox;
    }

    private Button createButton(String label, Consumer<GameListener> callback) {
        Button button = new Button(label);
        button.setOnAction(e -> {
            applyToAllListeners(callback);
            e.consume();
        });
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }
}
