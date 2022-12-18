package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SandCalculator {

    private String inputFile;
    String[][] cave;
    private int maxX = 0;
    private int maxY = 0;
    public SandCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long countSand() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                String[] split = line.split(" -> ");
                for (String coordStr: split) {
                    String[] xy = coordStr.split(",");
                    int x = Integer.parseInt(xy[0]);
                    int y = Integer.parseInt(xy[1]);
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }

        }
        System.out.println(maxX + " " + maxY);
        cave = new String[maxY + 3][maxX + 1000];
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                String[] split = line.split(" -> ");
                int currentX = Integer.parseInt(split[0].split(",")[0]);
                int currentY = Integer.parseInt(split[0].split(",")[1]);
                cave[currentY][currentX] = "R";
                for (int index = 1; index < split.length; index++) {
                    int newX = Integer.parseInt(split[index].split(",")[0]);
                    int newY = Integer.parseInt(split[index].split(",")[1]);
                    if (currentX == newX) {
                        if (currentY < newY) {
                            for (int y = currentY; y <= newY; y++) {
                                cave[y][currentX] = "#";
                            }
                        } else {
                            for (int y = newY; y <= currentY; y++) {
                                cave[y][currentX] = "#";
                            }
                        }
                    } else {
                        if (currentX < newX) {
                            for (int x = currentX; x <= newX; x++) {
                                cave[currentY][x] = "#";
                            }
                        } else {
                            for (int x = newX; x <= currentX; x++) {
                                cave[currentY][x] = "#";
                            }
                        }
                    }
                    currentX = newX;
                    currentY = newY;
                }

            }
            for (int x = 0; x <= maxX + 999; x++) {
                cave[maxY + 2][x] = "F";
            }
        }
        //printCave();
        long total = doCountSand();
        return total;
    }

    private void printCave() {
        for (String[] row: cave) {
            for (String col: row) {
                if (col == null) System.out.print(".");
                else System.out.print(col);
            }
            System.out.println();
        }
    }

    private long doCountSand() {
        int currentX = 0;
        int currentY = 0;
        boolean stuck = false;
        long total = 0;

        while (!stuck) {
            boolean stopped = false;
            currentX = 500;
            currentY = 0;
            while (!stopped && !stuck) {
                if (cave[currentY + 1][currentX] == null) {
                    currentY = currentY + 1;
                } else if (cave[currentY + 1][currentX - 1] == null) {
                    currentX = currentX - 1;
                    currentY = currentY + 1;
                } else if (cave[currentY + 1][currentX + 1] == null) {
                    currentX = currentX + 1;
                    currentY = currentY + 1;
                } else {
                    cave[currentY][currentX] = "o";
                    stopped = true;
                    total++;
                    if (currentY == 0) {
                        stuck = true;
                    }
                }
            }
        }
        return total;
    }
}
