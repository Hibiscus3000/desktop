package hibiscus3000.cellular_automaton.view;

import hibiscus3000.cellular_automaton.model.CellAutomatonEngine;
import hibiscus3000.cellular_automaton.model.Mode;
import hibiscus3000.cellular_automaton.model.NeighbourPolicy;
import hibiscus3000.cellular_automaton.model.cell.CellType;
import hibiscus3000.cellular_automaton.model.function.FunctionType;
import hibiscus3000.cellular_automaton.view.field.FieldViewHolder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collection;

public class SettingsBox extends VBox {

    private final CellAutomatonEngine engine;
    private final FieldViewHolder fieldViewHolder;

    private static final String neighbourPolicyLabelText = "Соседи";
    private static final String modeLabelText = "Режим";
    private static String rowsLabelText = "Строк:";
    private static String columnsLabelText = "Столбцов:";
    private static String updateButtonText = "Обновить";
    private static String iterateButtonText = "Выполнить итерацию";

    private final Spinner<Integer> rowsSpinner;
    private final Spinner<Integer> columnsSpinner;

    private static int MIN_ROWS_COLUMNS = 3;
    private static int MAX_ROWS_COLUMNS = 50;

    public SettingsBox(CellAutomatonEngine engine,
                       FieldViewHolder fieldViewHolder) {
        this.engine = engine;
        this.fieldViewHolder = fieldViewHolder;

        createIterationLabel();
        createCellTypeChoiceBox();
        createFunctionTypeChoiceBox();
        createModeGroup();
        createNeighbourPolicyGroup();

        rowsSpinner = createSpinner(rowsLabelText, engine.getRowCount());
        columnsSpinner = createSpinner(columnsLabelText, engine.getColumnCount());
        createUpdateButton();
        createIterateButton();
        update();
    }


    private Label createIterationLabel() {
        final Label iterationLabel = new Label();
        iterationLabel.textProperty().bind(Bindings.concat("Итерация ").concat(engine.iterationCountProperty()));
        getChildren().add(iterationLabel);
        return iterationLabel;
    }

    private ChoiceBox<CellType> createCellTypeChoiceBox() {
        return createChoiceBox(Arrays.stream(CellType.values()).toList(),
                engine.cellTypeProperty());
    }

    private ChoiceBox<FunctionType> createFunctionTypeChoiceBox() {
        return createChoiceBox(engine.getValidFunctionTypes(),
                engine.functionTypeProperty());
    }

    private <E extends Enum<E>> ChoiceBox<E> createChoiceBox(Collection<E> values,
                                                             ObjectProperty<E> engineProperty) {
        final ChoiceBox<E> choiceBox = new ChoiceBox<>();
        if (values instanceof ObservableList<E> observableValues) {
            fillChoiceBox(choiceBox, observableValues);
        } else {
            fillChoiceBox(choiceBox, values);
        }

        engineProperty.addListener((observable, oldVal, newVal) -> choiceBox.getSelectionModel().select(newVal));
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> engineProperty.set(newVal));
        choiceBox.getSelectionModel().select(engineProperty.get());

        getChildren().add(choiceBox);

        return choiceBox;
    }

    private <E extends Enum<E>> void fillChoiceBox(ChoiceBox<E> choiceBox,
                                                   ObservableList<E> values) {
        choiceBox.itemsProperty().bind(new SimpleObjectProperty<>(values));
    }

    private <E extends Enum<E>> void fillChoiceBox(ChoiceBox<E> choiceBox,
                                                   Collection<E> values) {
        choiceBox.getItems().addAll(values);
    }

    private void createModeGroup() {
        createButtonGroup(modeLabelText, Arrays.stream(Mode.values()).toList(), engine.modeProperty());
    }

    private void createNeighbourPolicyGroup() {
        createButtonGroup(neighbourPolicyLabelText, Arrays.stream(NeighbourPolicy.values()).toList(), engine.neighbourPolicyProperty());
    }

    private <E extends Enum<E>> void createButtonGroup(String valueName,
                                                       Collection<E> values,
                                                       ObjectProperty<E> engineProperty) {
        final ToggleGroup group = new ToggleGroup();
        final VBox box = new VBox();
        box.getChildren().add(new Label(valueName));
        for (E value : values) {
            box.getChildren().add(createButton(group,
                    value,
                    engineProperty));
        }
        getChildren().add(box);
    }

    private <E extends Enum<E>> ToggleButton createButton(ToggleGroup group,
                                                          E value,
                                                          ObjectProperty<E> engineProperty) {
        final RadioButton button = new RadioButton();
        button.setText(value.toString());
        button.setToggleGroup(group);
        button.setSelected(engineProperty.get() == value);
        button.selectedProperty().addListener((observable, oldMode, newMode) -> {
            if (newMode) {
                engineProperty.set(value);
            }
        });
        return button;
    }

    private Spinner<Integer> createSpinner(String labelText, int initValue) {
        final Label spinnerLabel = new Label(labelText);
        final Spinner<Integer> spinner = new Spinner<>(MIN_ROWS_COLUMNS, MAX_ROWS_COLUMNS, initValue);
        final VBox spinnerBox = new VBox();
        spinnerBox.getChildren().addAll(spinnerLabel, spinner);
        getChildren().add(spinnerBox);
        return spinner;
    }

    private Button createUpdateButton() {
        final Button updateButton = new Button(updateButtonText);
        getChildren().add(updateButton);
        updateButton.setOnAction((event) -> {
            event.consume();
            update();
        });
        return updateButton;
    }

    private void update() {
        if (engine.updateSize(rowsSpinner.getValue(), columnsSpinner.getValue()) || !fieldViewHolder.hasFieldView()) {
            fieldViewHolder.setNewFieldView(rowsSpinner.getValue(), columnsSpinner.getValue());
            engine.update();
        } else {
            engine.initialize();
        }
    }

    private Button createIterateButton() {
        final Button iterateButton = new Button(iterateButtonText);
        iterateButton.setOnAction(event -> {
            event.consume();
            engine.iterate();
        });
        getChildren().add(iterateButton);
        return iterateButton;
    }
}
