package hibiscus3000.cellular_automaton.view;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CellularAutomatonApplication extends Application {

    static final double stageScreenRatio = 0.65;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new CellularAutomatonBox());
        stage.setScene(scene);
        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        stage.setHeight(stageScreenRatio * screenBounds.getHeight());
        stage.setWidth(stageScreenRatio * screenBounds.getWidth());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}