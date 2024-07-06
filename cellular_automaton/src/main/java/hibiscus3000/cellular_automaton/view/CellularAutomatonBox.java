package hibiscus3000.cellular_automaton.view;

import hibiscus3000.cellular_automaton.model.field.BinaryField;
import hibiscus3000.cellular_automaton.view.field.BinaryFieldPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;

public class CellularAutomatonBox extends VBox {

    public CellularAutomatonBox() {
        final BinaryField field = new BinaryField(30, 30);
        field.initialize(Map.of());
        final BinaryFieldPane fieldPane = new BinaryFieldPane(field);
        setVgrow(fieldPane, Priority.ALWAYS);
        getChildren().add(fieldPane);
    }
}
