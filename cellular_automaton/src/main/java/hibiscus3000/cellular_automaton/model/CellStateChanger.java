package hibiscus3000.cellular_automaton.model;

import java.util.Optional;

public interface CellStateChanger {
    void changeState(Point coordinates, Optional<String> input);
}
