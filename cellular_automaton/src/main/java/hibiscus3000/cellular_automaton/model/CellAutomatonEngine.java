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
import hibiscus3000.cellular_automaton.view.FieldViewUpdaterCreator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

import static hibiscus3000.cellular_automaton.model.cell.CellType.BINARY;

public class CellAutomatonEngine implements CellStateChanger {

    private FieldProcessor<?> fieldProcessor;

    private final FieldViewUpdaterCreator fieldViewUpdaterCreator;

    private final Mode defaultMode = Mode.SYNC;
    private final CellType defaultCellType = BINARY;
    private final NeighbourPolicy defaultNeighbourPolicy = NeighbourPolicy.NEIGHBOURS_8;

    private final ObjectProperty<FunctionType> functionType = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<NeighbourPolicy> neighbourPolicy = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<CellType> cellType = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<Mode> mode = new ReadOnlyObjectWrapper<>();

    private final ReadOnlyLongWrapper iteration = new ReadOnlyLongWrapper();

    private static final int INIT_ROWS_COLUMNS = 10;

    private final IntegerProperty rowCount = new SimpleIntegerProperty(INIT_ROWS_COLUMNS);
    private final IntegerProperty columnCount = new SimpleIntegerProperty(INIT_ROWS_COLUMNS);

    private final ReadOnlyBooleanWrapper valid = new ReadOnlyBooleanWrapper();

    private final ObservableList<FunctionType> validFunctionTypes = FXCollections.observableArrayList();

    public CellAutomatonEngine(FieldViewUpdaterCreator fieldViewUpdaterCreator) {
        this.fieldViewUpdaterCreator = fieldViewUpdaterCreator;

        mode.set(defaultMode);
        cellType.set(defaultCellType);
        neighbourPolicy.set(defaultNeighbourPolicy);

        functionType.addListener((observable, oldVal, newVal) -> update(newVal));
        neighbourPolicy.addListener((observable, oldVal, newVal) -> update(newVal));
        cellType.addListener((observable, oldVal, newVal) -> update(newVal));
        mode.addListener((observable, oldVal, newVal) -> update(newVal));

        valid.bind(Bindings.size(validFunctionTypes).isNotEqualTo(0));

        update();
    }

    private void update(FunctionType functionType) {
        this.functionType.set(functionType);
        update();
    }

    private void update(NeighbourPolicy neighbourPolicy) {
        this.neighbourPolicy.set(neighbourPolicy);
        update();
    }

    private void update(CellType cellType) {
        this.cellType.set(cellType);
        update();
    }

    private void update(Mode mode) {
        this.mode.set(mode);
        update();
    }

    public void update() {
        checkAndSetFunctionType();
        if (!valid.get()) {
            return;
        }
        final Function<?> function = functionType.get().createFunction();
        switch (cellType.get()) {
            case BINARY -> createFieldProcessor(BINARY,
                    (Function<BinaryCell>) function,
                    new BinaryField(rowCount.get(), columnCount.get()),
                    new BinaryField(rowCount.get(), columnCount.get()));
        }

        initialize();
    }

    private void checkAndSetFunctionType() {
        validFunctionTypes.clear();
        validFunctionTypes.addAll(mode.get().getValidFunctionTypes());
        validFunctionTypes.retainAll(cellType.get().getValidFunctionTypes());

        if (!validFunctionTypes.contains(functionType.get())) {
            final Optional<FunctionType> fTypeOpt = validFunctionTypes.stream().findAny();
            fTypeOpt.ifPresent(functionType::set);
        }
    }

    public boolean updateSize(int rows, int columns) {
        if (changed(columnCount.get(), columns) || changed(rowCount.get(), rows)) {
            columnCount.set(columns);
            rowCount.set(rows);
            return true;
        }
        return false;
    }

    private boolean changed(Integer oldVal, int newVal) {
        return null == oldVal || oldVal != newVal;
    }

    private <C extends Cell> void createFieldProcessor(CellType cellType, Function<C> function,
                                                       Field<C> field1, Field<C> field2) {
        FieldProcessor<C> fieldProcessor = switch (mode.get()) {
            case SYNC -> new SyncFieldProcessor<>(
                    new SerialSyncAutomatonIterator<>(function), neighbourPolicy.get(), field1, field2);
            default -> throw new UnsupportedOperationException("Unsupported mode"); // TODO
        };
        fieldViewUpdaterCreator.createViewUpdaters(fieldProcessor, cellType);
        iteration.unbind();
        iteration.bind(fieldProcessor.iterationCountProperty());
        this.fieldProcessor = fieldProcessor;
    }

    public void initialize() {
        fieldProcessor.initialize();
    }

    public void iterate() {
        fieldProcessor.iterate();
    }

    @Override
    public void changeState(Point coordinates, Optional<String> input) {
        fieldProcessor.changeState(coordinates, input);
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

    public int getRowCount() {
        return rowCount.get();
    }

    public int getColumnCount() {
        return columnCount.get();
    }

    public ReadOnlyBooleanProperty validProperty() {
        return valid.getReadOnlyProperty();
    }

    public ObservableList<FunctionType> getValidFunctionTypes() {
        return validFunctionTypes;
    }

    public ReadOnlyLongProperty iterationCountProperty() {
        return iteration.getReadOnlyProperty();
    }

}
