package hibiscus3000.tetris.model.math;

public class TetrisMath {

    public static int euMod(int a, int b) {
        int ret = a % b;
        if (ret < 0) {
            ret += Math.abs(b);
        }
        return ret;
    }
}
