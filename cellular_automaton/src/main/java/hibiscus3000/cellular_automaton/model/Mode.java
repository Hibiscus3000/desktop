package hibiscus3000.cellular_automaton.model;

import hibiscus3000.cellular_automaton.model.function.FunctionType;

import java.util.EnumSet;
import java.util.Set;

public enum Mode {
    SYNC {
        @Override
        public Set<FunctionType> getValidFunctionTypes() {
            return EnumSet.of(FunctionType.LIFE);
        }

        @Override
        public String toString() {
            return "Синхронный";
        }
    },
    ASYNC {
        @Override
        public Set<FunctionType> getValidFunctionTypes() {
            return EnumSet.noneOf(FunctionType.class);
        }

        @Override
        public String toString() {
            return "Асинхронный";
        }
    };

    abstract public Set<FunctionType> getValidFunctionTypes();
}
