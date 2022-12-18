package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ElevationCalculator {
    private String inputFile;
    private String ELEVATIONS = "abcdefghijklmnopqrstuvwxyz";
    private Position startPosition;
    private Position endPosition;
    private List<List<Integer>> elevations = new ArrayList<>();

    private Map<Position, Integer> quickestRoutes = new HashMap<>();

    public ElevationCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateBestPath() throws IOException {
        parseInputFile();
        return getAbsoluteShortest();
    }

    private void parseInputFile() throws IOException {
        int row = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Integer> elevationRow = new ArrayList<>();
                elevations.add(elevationRow);
                System.out.println(line);
                for (int column = 0; column < line.length(); column++) {
                    String pos = line.substring(column, column + 1);
                    if (pos.equals("S")) {
                        startPosition = new Position(row, column);
                        elevationRow.add(0);
                    } else if (pos.equals("E")) {
                        endPosition = new Position(row, column);
                        elevationRow.add(25);
                    } else {
                        elevationRow.add(ELEVATIONS.indexOf(pos));
                    }
                }
                row++;
            }
        }
    }

    private int getAbsoluteShortest() {
        int currentShortest = Integer.MAX_VALUE;
        for (int row = 0; row < elevations.size(); row++) {
            for (int column = 0; column < elevations.get(row).size(); column++) {
                if (elevations.get(row).get(column) == 0) {
                    Integer candidate = getShortest(row, column);
                    if (candidate != null && candidate < currentShortest) {
                        currentShortest = candidate;
                    }
                }
            }
        }
        return currentShortest;
    }

    private Integer getShortest(int row, int column) {
        List<Path> startPaths = new ArrayList<>();
        startPaths.add(new Path(new Position(row, column)));
        List<Path> endingPaths = new ArrayList<>();

        buildPaths(startPaths, endingPaths);
        Collections.sort(endingPaths, Comparator.comparingInt(Path::size));
        return endingPaths.size() > 0 ? endingPaths.get(0).size() - 1 : null;
    }

    List<Path> buildPaths(List<Path> partialPaths, List<Path> endingPaths) {
        List<Path> extendedPaths = new ArrayList<>();
        for (Path partialPath: partialPaths) {
            if (partialPath.endSquare.equals(endPosition)) {
                endingPaths.add(partialPath);
            } else {
                extendedPaths = extendPartialPath(partialPath);
                buildPaths(extendedPaths, endingPaths);
            }
        }
        return extendedPaths;
    }

    private List<Path> extendPartialPath(Path path) {
        List<Path> newPaths = new ArrayList<>();
        for (Position position: getPossibleMoves(path.endSquare)) {
            if (!quickestRoutes.containsKey(position) || quickestRoutes.get(position) > path.size) {
                quickestRoutes.put(position, path.size);
                Path newPath = new Path(position, path.size + 1);
                newPaths.add(newPath);
            }
        }
        return newPaths;
    }

    private List<Position> getPossibleMoves(Position position) {
        List<Position> possible = new ArrayList<>();
        int currentElevation = elevations.get(position.row).get(position.column);
        if (position.column + 1 < elevations.get(position.row).size() && elevations.get(position.row).get(position.column + 1) <= currentElevation + 1) {
            possible.add(new Position(position.row, position.column + 1));
        }
        if (position.column - 1 >= 0 && elevations.get(position.row).get(position.column - 1) <= currentElevation + 1) {
            possible.add(new Position(position.row, position.column - 1));
        }
        if (position.row + 1 < elevations.size() && elevations.get(position.row + 1).get(position.column) <= currentElevation + 1) {
            possible.add(new Position(position.row + 1, position.column));
        }
        if (position.row - 1 >= 0 && elevations.get(position.row - 1).get(position.column) <= currentElevation + 1) {
            possible.add(new Position(position.row - 1, position.column));
        }
        return possible;
    }

    class Position {
        int row;
        int column;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && column == position.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    class Path {
        Position endSquare;
        int size;

        public Path(Position endSquare) {
            this.endSquare = endSquare;
            this.size = 1;
        }

        public Path(Position endSquare, int size) {
            this.endSquare = endSquare;
            this.size = size;
        }

        public int size() {
            return size;
        }

    }
}