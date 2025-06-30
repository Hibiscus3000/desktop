package hibiscus3000.tetris.model;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public class Field {

    private final int width;
    private final int height;
    private final ReadOnlyBooleanWrapper[][] field;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        field = new ReadOnlyBooleanWrapper[height][width];
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                field[h][w] = new ReadOnlyBooleanWrapper(false);
            }
        }
    }

    public void setOccupied(int w, int h, boolean occupied) {
        if (field[h][w].getValue() == occupied) {
            var err = String.format("Trying to set cell (%d, %d) to %b, but it already is", w, h, occupied);
            throw new RuntimeException(err);
        }
        field[h][w].setValue(occupied);
    }

    public boolean getOccupied(int w, int h) {
        return field[h][w].getValue();
    }

    public ReadOnlyBooleanProperty getOccupiedProperty(int w, int h) {
        return field[h][w].getReadOnlyProperty();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
