package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeCalculator {

    private String inputFile;
    private int stackCount = 9;
    public TreeCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long countVisibileTrees() throws IOException {
        long total = 0;
        List<List<Integer>> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                List<Integer> row = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    int height = Integer.parseInt(line.substring(i, i+1));
                    row.add(height);
                }
                rows.add(row);
            }

        }
        long maxScore = 0;
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                long score = getScenicScore(rows, i, j);
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }
        return maxScore;
    }

    private boolean isVisible(List<List<Integer>> rows, int row, int column) {
        boolean visibleLeft = true;
        for (int i = 0; i < column; i++) {
            if (rows.get(row).get(i) >= rows.get(row).get(column)) {
                visibleLeft = false;
                break;
            }
        }
        boolean visibleRight = true;
        for (int i = column + 1; i < rows.get(row).size(); i++) {
            if (rows.get(row).get(i) >= rows.get(row).get(column)) {
                visibleRight = false;
                break;
            }
        }
        boolean visibleTop = true;
        for (int i = 0; i < row; i++) {
            if (rows.get(i).get(column) >= rows.get(row).get(column)) {
                visibleTop = false;
                break;
            }
        }
        boolean visibleBottom = true;
        for (int i = row + 1; i < rows.size(); i++) {
            if (rows.get(i).get(column) >= rows.get(row).get(column)) {
                visibleBottom = false;
                break;
            }
        }
        return visibleLeft || visibleRight || visibleTop || visibleBottom;
    }

    private long getScenicScore(List<List<Integer>> rows, int row, int column) {
        long visibleLeft = 0;
        for (int i = column - 1; i >=0; i--) {
            visibleLeft++;
            if (rows.get(row).get(i) >= rows.get(row).get(column)) {
                break;
            }
        }
        long visibleRight = 0;
        for (int i = column + 1; i < rows.get(row).size(); i++) {
            visibleRight++;
            if (rows.get(row).get(i) >= rows.get(row).get(column)) {
                break;
            }
        }
        long visibleTop = 0;
        for (int i = row - 1; i >= 0; i--) {
            visibleTop++;
            if (rows.get(i).get(column) >= rows.get(row).get(column)) {
                break;
            }
        }
        long visibleBottom = 0;
        for (int i = row + 1; i < rows.size(); i++) {
            visibleBottom++;
            if (rows.get(i).get(column) >= rows.get(row).get(column)) {
                break;
            }
        }
        return visibleLeft * visibleRight * visibleTop * visibleBottom;
    }
}
