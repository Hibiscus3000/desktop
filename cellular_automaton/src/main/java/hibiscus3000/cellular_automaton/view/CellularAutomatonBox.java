package hibiscus3000.cellular_automaton.view;

import hibiscus3000.cellular_automaton.model.CellAutomatonEngine;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.cell.CellType;
import hibiscus3000.cellular_automaton.model.field_processor.FieldProcessor;
import hibiscus3000.cellular_automaton.view.field.BinaryFieldPaneUpdater;
import hibiscus3000.cellular_automaton.view.field.FieldPane;
import hibiscus3000.cellular_automaton.view.field.FieldPaneUpdater;
import hibiscus3000.cellular_automaton.view.field.FieldViewHolder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CellularAutomatonBox extends HBox implements FieldViewHolder, FieldViewUpdaterCreator {

    private final VBox fieldPaneBox = new VBox();
    private FieldPane fieldPane = null;

    public CellularAutomatonBox() {
        getChildren().add(fieldPaneBox);
        setHgrow(fieldPaneBox, Priority.ALWAYS);
        getChildren().add(new SettingsBox(new CellAutomatonEngine(this), this));
    }

    @Override
    public void setNewFieldView(int rows, int columns) {
        fieldPaneBox.getChildren().remove(fieldPane);
        fieldPane = new FieldPane(rows, columns);
        fieldPaneBox.getChildren().add(fieldPane);
        VBox.setVgrow(fieldPane, Priority.ALWAYS);
    }

    @Override
    public boolean hasFieldView() {
        return null != fieldPane;
    }

    @Override
    public <C extends Cell> void createViewUpdaters(FieldProcessor<C> fieldProcessor,
                                                    CellType cellType) {
        final FieldPaneUpdater<C> fieldPaneUpdater = switch (cellType) {
            case BINARY -> (FieldPaneUpdater<C>) new BinaryFieldPaneUpdater(fieldPane);
        }; // TODO think of some better way
        fieldProcessor.addFieldViewUpdater(fieldPaneUpdater);
    }
}
