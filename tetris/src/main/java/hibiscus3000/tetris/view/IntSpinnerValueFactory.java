package hibiscus3000.tetris.view;

import javafx.scene.control.SpinnerValueFactory;

import static hibiscus3000.tetris.model.math.TetrisMath.euMod;

public class IntSpinnerValueFactory extends SpinnerValueFactory.IntegerSpinnerValueFactory {
    private final int length;

    public IntSpinnerValueFactory(int min, int max, int initialValue, int step) {
        super(min, max, initialValue, step);
        length = max - min + 1;
    }

    @Override
    public void increment(final int steps) {
        int newValue = euMod((getValue() + steps * getAmountToStepBy() - getMin()), length);
        newValue += getMin();
        setValue(newValue);
    }

    @Override
    public void decrement(int steps) {
        int newValue = euMod((getValue() - steps * getAmountToStepBy() - getMin()), length);
        newValue += getMin();
        setValue(newValue);
    }
}
