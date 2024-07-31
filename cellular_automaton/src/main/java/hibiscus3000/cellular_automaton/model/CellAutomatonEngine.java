package hibiscus3000.cellular_automaton.model;

import hibiscus3000.cellular_automaton.model.cell.BinaryCell;
import hibiscus3000.cellular_automaton.model.cell.Cell;
import hibiscus3000.cellular_automaton.model.cell.CellType;
import hibiscus3000.cellular_automaton.model.field.BinaryField;
import hibiscus3000.cellular_automaton.model.field.Field;
import hibiscus3000.cellular_automaton.model.field_iterator.sync.SerialSyncAutomatonIterator;
import hibiscus3000.cellular_automaton.model.field_processor.FieldProcessor;
import hibiscus3000.cellular_automaton.model.field_processor.SyncFieldProcessor;
import hibiscus3000.cellular_automaton.model.function.Function;
import hibiscus3000.cellular_automaton.model.function.FunctionType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

import static hibiscus3000.cellular_automaton.model.cell.CellType.BINARY;

public class CellAutomatonEngine {

    private FieldProcessor<?> fieldProcessor;

    private final Mode defaultMode = Mode.SYNC;
    private final CellType defaultCellType = BINARY;
    private final NeighbourPolicy defaultNeighbourPolicy = NeighbourPolicy.NEIGHBOURS_8;

    private final ObjectProperty<FunctionType> functionType = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<NeighbourPolicy> neighbourPolicy = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<CellType> cellType = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<Mode> mode = new ReadOnlyObjectWrapper<>();

    private final IntegerProperty rowCount = new ReadOnlyIntegerWrapper();
    private final IntegerProperty columnCount = new ReadOnlyIntegerWrapper();

    private final ReadOnlyBooleanWrapper valid = new ReadOnlyBooleanWrapper();

    private final ObservableList<FunctionType> validFunctionTypes = FXCollections.observableArrayList();

    public CellAutomatonEngine() {
        mode.set(defaultMode);
        cellType.set(defaultCellType);
        neighbourPolicy.set(defaultNeighbourPolicy);

        functionType.addListener((observable, oldVal, newVal) -> update(newVal));
        neighbourPolicy.addListener((observable, oldVal, newVal) -> update(newVal));
        cellType.addListener((observable, oldVal, newVal) -> update(newVal));
        mode.addListener((observable, oldVal, newVal) -> update(newVal));

        update();
    }

    private void update(FunctionType functionType) {
        if (functionType != this.functionType.get()) {
            this.functionType.set(functionType);
            update();
        }
    }

    private void update(NeighbourPolicy neighbourPolicy) {
        if (neighbourPolicy != this.neighbourPolicy.get()) {
            this.neighbourPolicy.set(neighbourPolicy);
            update();
        }
    }

    private void update(CellType cellType) {
        if (cellType != this.cellType.get()) {
            this.cellType.set(cellType);
            update();
        }
    }

    private void update(Mode mode) {
        if (mode != this.mode.get()) {
            this.mode.set(mode);
            update();
        }
    }

    private void update() {
        checkAndSetFunctionType();
        final Function<?> function = functionType.get().createFunction();
        switch (cellType.get()) {
            case BINARY -> createFieldProcessor((Function<BinaryCell>) function,
                    new BinaryField(rowCount.get(), columnCount.get()),
                    new BinaryField(rowCount.get(), columnCount.get()));
        }
    }

    private void checkAndSetFunctionType() {
        validFunctionTypes.clear();
        validFunctionTypes.addAll(mode.get().getValidFunctionTypes());
        validFunctionTypes.retainAll(cellType.get().getValidFunctionTypes());

        if (!validFunctionTypes.contains(functionType.get())) {
            final Optional<FunctionType> fTypeOpt = validFunctionTypes.stream().findAny();
            fTypeOpt.ifPresent(functionType::set);
            valid.set(fTypeOpt.isPresent());
        }
    }

    private <C extends Cell> void createFieldProcessor(Function<C> function,
                                                       Field<C> field1, Field<C> field2) {
        fieldProcessor = switch (mode.get()) {
            case SYNC -> new SyncFieldProcessor<>(
                    new SerialSyncAutomatonIterator<>(function), neighbourPolicy.get(), field1, field2);
            default -> throw new UnsupportedOperationException("Unsupported mode");
        };
    }

    public ObjectProperty<FunctionType> functionTypeProperty() {
        return functionType;
    }

    public ObjectProperty<NeighbourPolicy> neighbourPolicyProperty() {
        return neighbourPolicy;
    }

    public ObjectProperty<CellType> cellTypeProperty() {
        return cellType;
    }

    public ObjectProperty<Mode> modeProperty() {
        return mode;
    }

    public FunctionType getFunctionType() {
        return functionType.get();
    }

    public NeighbourPolicy getNeighbourPolicy() {
        return neighbourPolicy.get();
    }

    public CellType getCellType() {
        return cellType.get();
    }

    public Mode getMode() {
        return mode.get();
    }

    public IntegerProperty rowCountProperty() {
        return rowCount;
    }

    public IntegerProperty columnCountProperty() {
        return columnCount;
    }

    public ReadOnlyBooleanProperty validProperty() {
        return valid.getReadOnlyProperty();
    }

    public Field<?> getCurrentField() {
        return fieldProcessor.getCurrentField();
    }

    public ObservableList<FunctionType> getValidFunctionTypes() {
        return validFunctionTypes;
    }

    public ReadOnlyLongProperty iterationCountProperty() {
        return fieldProcessor.iterationCountProperty();
    }
}
