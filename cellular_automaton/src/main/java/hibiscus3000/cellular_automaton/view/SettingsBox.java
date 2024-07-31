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

    private final ChoiceBox<FunctionType> functionTypeChoiceBox;

    private static final String neighbourPolicyLabelText = "Соседи";
    private static final String modeLabelText = "Режим";

    public SettingsBox(CellAutomatonEngine engine, FieldViewHolder fieldViewHolder) {
        this.engine = engine;
        this.fieldViewHolder = fieldViewHolder;

        createIterationLabel();
        createCellTypeChoiceBox();
        functionTypeChoiceBox = createFunctionTypeChoiceBox();
        createModeGroup();
        createNeighbourPolicyGroup();
    }

    private Label createIterationLabel() {
        final Label iterationLabel = new Label();
        iterationLabel.textProperty().bind(Bindings.concat("Итерация ").concat(engine.iterationCountProperty()));
        getChildren().add(iterationLabel);
        return iterationLabel;
    }

    private ChoiceBox<CellType> createCellTypeChoiceBox() {
        return createChoiceBox(Arrays.stream(CellType.values()).toList(),
                engine.getCellType(),
                engine.cellTypeProperty());
    }

    private ChoiceBox<FunctionType> createFunctionTypeChoiceBox() {
        return createChoiceBox(engine.getValidFunctionTypes(),
                engine.getFunctionType(),
                engine.functionTypeProperty());
    }

    private <E extends Enum<E>> ChoiceBox<E> createChoiceBox(Collection<E> values,
                                                             E initVal,
                                                             ObjectProperty<E> engineProperty) {
        final ChoiceBox<E> choiceBox = new ChoiceBox<>();
        if (values instanceof ObservableList<E> observableValues) {
            fillChoiceBox(choiceBox, observableValues);
        } else {
            fillChoiceBox(choiceBox, values);
        }

        choiceBox.getSelectionModel().select(initVal);
        engineProperty.bind(choiceBox.getSelectionModel().selectedItemProperty());

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
        button.selectedProperty().addListener((observable, oldMode, newMode) -> {
            if (newMode) {
                engineProperty.set(value);
            }
        });
        return button;
    }
}
