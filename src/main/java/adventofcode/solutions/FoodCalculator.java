package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FoodCalculator {

    private String inputFile;
    public FoodCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateHighestCalories() throws IOException {
        long highest = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            long currentTotal = 0;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    currentTotal += Long.parseLong(line);
                } else {
                    if (currentTotal > highest) {
                        highest = currentTotal;
                    }
                    currentTotal = 0;
                }
            }
            if (currentTotal > highest) {
                highest = currentTotal;
            }
        }
        return highest;
    }

    public long calculateTopNTotal(int n) throws IOException {
        List<Long> allTotals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            long currentTotal = 0;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    currentTotal += Long.parseLong(line);
                } else {
                    allTotals.add(currentTotal);
                    currentTotal = 0;
                }
            }
            allTotals.add(currentTotal);
        }
        allTotals.sort(Long::compare);
        return allTotals.subList(allTotals.size() - n, allTotals.size()).stream().reduce(0L, Long::sum);
    }

}
