package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RopeCalculator {

    private String inputFile;
    public RopeCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    private int messageLength = 14;

    public int calculateVisited() throws IOException {
        List<Position> ropePositions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ropePositions.add(new Position());
        }

        Set<Position> positions = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] split = line.split(" ");
                int moves = Integer.parseInt(split[1]);
                    for (int i = 0; i < moves; i++) {
                        if (split[0].equals("R")) {
                            ropePositions.get(0).x++;
                            for (int j = 0; j < 9; j++) {
                                updateTail(ropePositions.get(j), ropePositions.get(j+1));
                            }
                        } else if (split[0].equals("L")) {
                            ropePositions.get(0).x--;
                            for (int j = 0; j < 9; j++) {
                                updateTail(ropePositions.get(j), ropePositions.get(j+1));
                            }
                        } else if (split[0].equals("U")) {
                            ropePositions.get(0).y++;
                            for (int j = 0; j < 9; j++) {
                                updateTail(ropePositions.get(j), ropePositions.get(j+1));
                            }
                        } else if (split[0].equals("D")) {
                            ropePositions.get(0).y--;
                            for (int j = 0; j < 9; j++) {
                                updateTail(ropePositions.get(j), ropePositions.get(j+1));
                            }
                        }
                        updateAll(ropePositions);
                        Position newT = new Position();
                        newT.x = ropePositions.get(9).x;
                        newT.y = ropePositions.get(9).y;
                        positions.add(newT);
                    }
            }

        }

        return positions.size();
    }

    private void updateAll(List<Position> ropePositions) {
    }

    private void updateTail(Position hPos, Position tPos) {
        if (hPos.x - tPos.x <= 1 && hPos.x - tPos.x >= -1 && hPos.y - tPos.y <= 1 && hPos.y - tPos.y >= -1) {
            return;
        }
        if (hPos.x == tPos.x) {
            if (hPos.y > tPos.y) {
                tPos.y++;
            } else {
                tPos.y--;
            }
        } else if (hPos.y == tPos.y) {
            if (hPos.x > tPos.x) {
                tPos.x++;
            } else {
                tPos.x--;
            }
        } else {
            if (hPos.y > tPos.y) {
                tPos.y++;
            } else {
                tPos.y--;
            }
            if (hPos.x > tPos.x) {
                tPos.x++;
            } else {
                tPos.x--;
            }

        }
    }

    class Position {
        int x = 0;
        int y = 0;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
