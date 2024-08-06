package hibiscus3000.cellular_automaton.model.field;

public class InvalidInput extends Exception {

    public InvalidInput(String input) {
        super("Invalid input for external update: " + input);
    }
}
