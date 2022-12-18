package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcCalculator {

    private String inputFile;
    private Integer[] valuesOfInterest = new Integer[] {20, 60, 100, 140, 180, 220};
    private List<Integer> valuesOfInterestList = Arrays.asList(valuesOfInterest);

    public ProcCalculator(String inputFile) {
        this.inputFile= inputFile;
    }

    public long calculateTotalScore() throws IOException {
        long total = 0;
         int cycle = 1;
        long xVal = 1;
        List<List<Boolean>> rows = new ArrayList<>();
        List<Boolean> currentRow = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {

                if (line.equals("noop")) {
                    if (xVal >= cycle % 40 - 2 && xVal <= cycle% 40) {
                        currentRow.add(true);
                    } else {
                        currentRow.add(false);
                    }
                    if (currentRow.size() == 40) {
                        rows.add(currentRow);
                        currentRow = new ArrayList<>();
                    }
                    cycle++;
                } else {
                    String[] split = line.split(" ");
                    int diff = Integer.parseInt(split[1]);
                    if (xVal >= cycle % 40 - 2 && xVal <= cycle% 40) {
                        currentRow.add(true);
                    } else {
                        currentRow.add(false);
                    }
                    if (currentRow.size() == 40) {
                        rows.add(currentRow);
                        currentRow = new ArrayList<>();
                    }
                    cycle++;
                    if (xVal >= cycle % 40 - 2 && xVal <= cycle% 40) {
                        currentRow.add(true);
                    } else {
                        currentRow.add(false);
                    }
                    if (currentRow.size() == 40) {
                        rows.add(currentRow);
                        currentRow = new ArrayList<>();
                    }
                    xVal += diff;
                    cycle++;
                }
            }
        }
        for (List<Boolean> row: rows) {
            StringBuilder sb = new StringBuilder();
            for (boolean b : row) {
                if (b) sb.append("#");
                else sb.append(".");
            }
            System.out.println(sb);
        }
        return total;
    }

}