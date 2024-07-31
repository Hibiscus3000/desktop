package hibiscus3000.cellular_automaton.model.cell;

import hibiscus3000.cellular_automaton.model.function.FunctionType;

import java.util.EnumSet;
import java.util.Set;

public enum CellType {
    BINARY {
        @Override
        public Set<FunctionType> getValidFunctionTypes() {
            return EnumSet.of(FunctionType.LIFE);
        }

        @Override
        public String toString() {
            return "Двоичная";
        }
    };

    abstract public Set<FunctionType> getValidFunctionTypes();
}
