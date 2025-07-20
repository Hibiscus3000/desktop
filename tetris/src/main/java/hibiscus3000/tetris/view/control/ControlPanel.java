package hibiscus3000.tetris.view.control;

import hibiscus3000.tetris.model.manager.GameListener;
import hibiscus3000.tetris.view.FieldController;
import hibiscus3000.tetris.view.IntSpinnerValueFactory;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ControlPanel extends HBox {

    private GameListener mainListener;
    private final FieldController fieldController;

    private final VBox buttonBox = new VBox();
    private final VBox sizeSettingBox = new VBox();

    private Button startButton;
    private Button pauseButton;
    private Button stopButton;

    private static final String CONTROL_BOX_STYLE = "control-box";

    private static final String START_BUTTON_LABEL = "Начать";
    private static final String PAUSE_BUTTON_LABEL = "Пауза";
    private static final String STOP_BUTTON_LABEL = "Сброс";

    private Spinner<Integer> widthSpinner;
    private Spinner<Integer> heightSpinner;

    private static final int DEFAULT_WIDTH = 10;
    private static final int MIN_WIDTH = 5;
    private static final int MAX_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 15;
    private static final int MIN_HEIGHT = 5;
    private static final int MAX_HEIGHT = 100;
    private static final int SPINNER_STEP = 1;

    public ControlPanel(FieldController fieldController) {
        this.fieldController = fieldController;
        setAlignment(Pos.CENTER);
        createButtonBox();
        createSizeSettingBox();
        getChildren().addAll(buttonBox, sizeSettingBox);
    }
    
    public synchronized void setMainListener(GameListener mainListener) {
        this.mainListener = mainListener;
    }

    private void createButtonBox() {
        assert null == startButton && null == pauseButton && null == stopButton : "Can only call it once";
        startButton = createButton(START_BUTTON_LABEL, GameListener::start);
        pauseButton = createButton(PAUSE_BUTTON_LABEL, GameListener::pause);
        stopButton = createButton(STOP_BUTTON_LABEL, GameListener::stop);
        buttonBox.getChildren().addAll(startButton, pauseButton, stopButton);
        buttonBox.getStyleClass().add(CONTROL_BOX_STYLE);
    }

    private Button createButton(String label, Consumer<GameListener> callback) {
        Button button = new Button(label);
        button.setOnAction(e -> {
            callback.accept(mainListener);
            e.consume();
        });
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private void createSizeSettingBox() {
        assert null == widthSpinner && null == heightSpinner : "Can only call it once";
        sizeSettingBox.getStyleClass().add(CONTROL_BOX_STYLE);
        widthSpinner = createSpinner(DEFAULT_WIDTH, MIN_WIDTH, MAX_WIDTH);
        widthSpinner.valueProperty().addListener(((observable, oldWidth, newWidth) -> fieldController.setField(newWidth, heightSpinner.getValue())));
        heightSpinner = createSpinner(DEFAULT_HEIGHT, MIN_HEIGHT, MAX_HEIGHT);
        heightSpinner.valueProperty().addListener(((observable, oldHeight, newHeight) -> fieldController.setField(widthSpinner.getValue(), newHeight)));
        sizeSettingBox.getChildren().addAll(widthSpinner, heightSpinner);
    }

    private Spinner<Integer> createSpinner(int def, int min, int max) {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new IntSpinnerValueFactory(min, max, def, SPINNER_STEP));
        spinner.setEditable(true);
        return spinner;
    }

    public int getFieldWidth() {
        return widthSpinner.getValue();
    }

    public int getFieldHeight() {
        return heightSpinner.getValue();
    }

    public void setGameProperties(ReadOnlyBooleanProperty gameInProgressProperty,
                                  ReadOnlyBooleanProperty gameRunningProperty,
                                  ReadOnlyBooleanProperty gameLostProperty) {
        var disableProperty = gameInProgressProperty.and(gameLostProperty.not());
        widthSpinner.disableProperty().unbind();
        widthSpinner.disableProperty().bind(disableProperty);
        heightSpinner.disableProperty().unbind();
        heightSpinner.disableProperty().bind(disableProperty);
    }
}
