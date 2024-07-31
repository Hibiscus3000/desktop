package hibiscus3000.cellular_automaton.view;

import hibiscus3000.cellular_automaton.model.CellAutomatonEngine;
import hibiscus3000.cellular_automaton.model.field.BinaryField;
import hibiscus3000.cellular_automaton.model.field.FieldView;
import hibiscus3000.cellular_automaton.view.field.BinaryFieldPane;
import hibiscus3000.cellular_automaton.view.field.FieldPane;
import hibiscus3000.cellular_automaton.view.field.FieldViewHolder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Map;

public class CellularAutomatonBox extends HBox implements FieldViewHolder {

    private FieldPane<?> fieldPane;

    public CellularAutomatonBox() {
        final BinaryField field = new BinaryField(30, 30);
        field.initialize(Map.of());
        fieldPane = new BinaryFieldPane(field);
        setHgrow(fieldPane, Priority.ALWAYS);
        getChildren().add(fieldPane);
        getChildren().add(new SettingsBox(new CellAutomatonEngine(), this));
    }

    @Override
    public void setNewFieldView(FieldView<?> fieldView) {
        if (fieldView instanceof FieldPane<?> fieldPane) {
            getChildren().remove(fieldPane);
            this.fieldPane = fieldPane;
            getChildren().add(fieldPane);
        } else {
            throw new RuntimeException("Unknown field view type");
        }
    }
}
