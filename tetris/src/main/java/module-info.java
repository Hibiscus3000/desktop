module hibiscus3000.tetris {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;

    exports hibiscus3000.tetris to javafx.graphics;
    exports hibiscus3000.tetris.view to javafx.graphics;
    exports hibiscus3000.tetris.view.control to javafx.graphics;
}