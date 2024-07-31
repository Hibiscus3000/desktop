package hibiscus3000.cellular_automaton.model.function;

import hibiscus3000.cellular_automaton.model.cell.Cell;

import java.util.List;


public abstract class Function<C extends Cell> {

    public abstract C apply(C cell, List<C> neighbours);
}
