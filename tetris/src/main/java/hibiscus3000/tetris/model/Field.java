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

    public void clear() {
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                field[h][w].setValue(false);
            }
        }
    }

    public boolean isLineOccupied(int lineI) {
        for (int x = 0; x < width; ++x) {
            if (!field[lineI][x].get()) {
                return false;
            }
        }
        return true;
    }

    public boolean isLineFree(int lineI) {
        for (int x = 0; x < width; ++x) {
            if (field[lineI][x].get()) {
                return false;
            }
        }
        return true;
    }

    public void clearLine(int lineI) {
        for (int x = 0; x < width; ++x) {
            field[lineI][x].setValue(false);
        }
    }

    public void shitDownUpTo(int lineI) {
        for (int x = 0; x < width; ++x) {
            for (int y = lineI - 1; y > 0; --y) {
                field[y][x].setValue(field[y - 1][x].getValue());
            }
        }
        for (int x = 0; x < width; ++x) {
            field[0][x].setValue(false);
        }
    }
}
