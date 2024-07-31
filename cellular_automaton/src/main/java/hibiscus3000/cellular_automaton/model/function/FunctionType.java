package hibiscus3000.cellular_automaton.model.function;

import hibiscus3000.cellular_automaton.model.function.binary.BinaryLife;

public enum FunctionType {
    LIFE {
        @Override
        public String toString() {
            return "Жизнь";
        }

        @Override
        public Function<?> createFunction() {
            return new BinaryLife();
        }
    };

    public abstract Function<?> createFunction();
}
