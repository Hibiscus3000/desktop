package hibiscus3000.tetris;

import hibiscus3000.tetris.view.MainPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TetrisApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new MainPane(), 480, 620);
        scene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
        stage.setTitle("Tetris!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}