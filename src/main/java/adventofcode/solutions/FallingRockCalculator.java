package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FallingRockCalculator {

    private String inputFile;

    private List<Rock> rocks = new ArrayList<>();
    private ArrayList<boolean[]>  chamber = new ArrayList<>();
    private String wind;

    private int matches = 0;

    private Set<IndexPair> indexPairs = new HashSet<>();

    private boolean[] trueRow = new boolean[] {true, true, true, true, true, true, true};
    private int chamberWidth = 7;
    public FallingRockCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public int calculateRocksHeight() throws IOException {
        initialiseRocks();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            while ((line = reader.readLine()) != null && line.length() > 0) {
                wind = line;
            }
        }
        simulateRocks(10000000);
        System.out.println("Found " + matches + " matches");
        return chamber.size();
    }

    private void initialiseRocks() {
        Rock rock1 = new Rock(new Coordinates(0,0), new Coordinates(1, 0), new Coordinates(2,0), new Coordinates(3,0));
        rocks.add(rock1);
        Rock rock2 = new Rock(new Coordinates(1,0), new Coordinates(0, 1), new Coordinates(1,1), new Coordinates(2,1), new Coordinates(1,2));
        rocks.add(rock2);
        Rock rock3 = new Rock(new Coordinates(0,0), new Coordinates(1, 0), new Coordinates(2,0), new Coordinates(2,1), new Coordinates(2,2));
        rocks.add(rock3);
        Rock rock4 = new Rock(new Coordinates(0,0), new Coordinates(0, 1), new Coordinates(0,2), new Coordinates(0,3));
        rocks.add(rock4);
        Rock rock5 = new Rock(new Coordinates(0,0), new Coordinates(1, 0), new Coordinates(0,1), new Coordinates(1,1));
        rocks.add(rock5);
    }

    private void simulateRocks(int rockCount) {
        int windIndex = 0;
        int rockIndex = 0;
        for (int rockNum = 1; rockNum <= rockCount; rockNum++) {
            boolean stopped = false;
            int rockX = 2;
            int rockY = chamber.size() + 3;
            Rock currentRock = rocks.get(rockIndex);
            rockIndex = (rockIndex + 1) % rocks.size();
            while (!stopped) {
                boolean left = wind.substring(windIndex, windIndex + 1).equals("<");
                windIndex = (windIndex + 1) % wind.length();
                if (left) {
                    if (rockX > 0 && !blockedLeft(currentRock, rockX, rockY)) {
                        rockX--;
                    }
                } else {
                    if ((rockX + 1 + currentRock.maxX) < chamberWidth && !blockedRight(currentRock, rockX, rockY)) {
                        rockX++;
                    }
                }
                if (rockY > 0 && !blockedDown(currentRock, rockX, rockY)) {
                    rockY--;
                } else {
                    stopped = true;
                }
            }
            int maxFullRow = addToChamber(currentRock, rockX, rockY);
//            if (maxFullRow >= 0) {
//                if (rockIndex == 1 && windIndex == 2628 && chamber.size() == maxFullRow + 1) {
//                    System.out.println(rockNum + " " + chamber.size());
//                    printChamber();
//                }
////                IndexPair pair = new IndexPair(rockIndex, windIndex, chamber.isEmpty());
////                if (indexPairs.contains(pair) && chamber.isEmpty()) {
////                    System.out.println(rockIndex + " " + windIndex);
////                    matches++;
////                }
////                indexPairs.add(pair);
//            }
            if (rockNum == 1090) {
                System.out.println(chamber.size());
            }
        }
    }

    private int addToChamber(Rock currentRock, int rockX, int rockY) {
        int maxFullRow = -1;
        for (Coordinates coords: currentRock.coords) {
            int pieceX = coords.x + rockX;
            int pieceY = coords.y + rockY;
            while (chamber.size() < pieceY + 1) {
                chamber.add(new boolean[chamberWidth]);
            }
            chamber.get(pieceY)[pieceX] = true;
            if (Arrays.equals(chamber.get(pieceY), trueRow) && pieceY > maxFullRow) {
                maxFullRow = pieceY;
            }
        }
        return maxFullRow;
    }



    private void printChamber() {
        for (int row = chamber.size() - 1; row >= 0; row--) {
            for (boolean piece: chamber.get(row)) {
                if (piece) System.out.print("#");
                else System.out.print(".");
            }
            System.out.println();
        }
    }

    private boolean blockedDown(Rock currentRock, int rockX, int rockY) {
        boolean pieceBlocked = false;
        for (Coordinates coords: currentRock.coords) {
            int pieceX = coords.x + rockX;
            int pieceY = coords.y + rockY;
            if (chamber.size() > pieceY - 1) {
                boolean[] row = chamber.get(pieceY - 1);
                if (row[pieceX]) {
                    pieceBlocked = true;
                    break;
                }
            }
        }
        return pieceBlocked;
    }


    private boolean blockedLeft(Rock currentRock, int rockX, int rockY) {
        boolean pieceBlocked = false;
        for (Coordinates coords: currentRock.coords) {
            int pieceX = coords.x + rockX;
            int pieceY = coords.y + rockY;
            if (chamber.size() > pieceY) {
                boolean[] row = chamber.get(pieceY);
                if (row[pieceX - 1]) {
                    pieceBlocked = true;
                    break;
                }
            }
        }
        return pieceBlocked;
    }

    private boolean blockedRight(Rock currentRock, int rockX, int rockY) {
        boolean pieceBlocked = false;
        for (Coordinates coords: currentRock.coords) {
            int pieceX = coords.x + rockX;
            int pieceY = coords.y + rockY;
            if (chamber.size() > pieceY) {
                boolean[] row = chamber.get(pieceY);
                if (row[pieceX + 1]) {
                    pieceBlocked = true;
                    break;
                }
            }
        }
        return pieceBlocked;
    }

    class Rock {
        private List<Coordinates> coords = new ArrayList<>();
        int maxX;
        int maxY;

        public Rock(Coordinates... coordinates) {
            this.coords = Arrays.asList(coordinates);
            for (Coordinates coords: coordinates) {
                if (coords.x > maxX) maxX = coords.x;
                if (coords.y > maxY) maxY = coords.y;
            }
        }
    }

    class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class IndexPair {
        int rockIndex;
        int windIndex;

        boolean empty;

        public IndexPair(int rockIndex, int windIndex, boolean empty) {
            this.rockIndex = rockIndex;
            this.windIndex = windIndex;
            this.empty = empty;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexPair pair = (IndexPair) o;
            return rockIndex == pair.rockIndex && windIndex == pair.windIndex && empty == pair.empty;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rockIndex, windIndex, empty);
        }
    }
}
