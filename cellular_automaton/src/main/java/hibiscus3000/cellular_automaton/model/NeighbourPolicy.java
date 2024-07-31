package hibiscus3000.cellular_automaton.model;

import java.util.List;

public enum NeighbourPolicy {
    NEIGHBOURS_4 {
        @Override
        public List<Point> getPos() {
            return List.of(
                    new Point(1, 0),
                    new Point(0, 1),
                    new Point(-1, 0),
                    new Point(0, -1)
            );
        }

        @Override
        public String toString() {
            return "4";
        }
    },
    NEIGHBOURS_8 {
        @Override
        public List<Point> getPos() {
            return List.of(new Point(1, 0),
                    new Point(0, 1),
                    new Point(-1, 0),
                    new Point(0, -1),
                    new Point(1, 1),
                    new Point(1, -1),
                    new Point(-1, 1),
                    new Point(-1, -1));
        }

        @Override
        public String toString() {
            return "8";
        }
    };

    public abstract List<Point> getPos();
}
