package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CampCleanupCalculator {

    private static final String PRIORITIES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String inputFile;
    public CampCleanupCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateContained() throws IOException {

        long total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] ranges = line.split(",");
                String[] range1 = ranges[0].split("-");
                String[] range2 = ranges[1].split("-");
                if ((Long.parseLong(range1[0]) >= Long.parseLong(range2[0]) && (Long.parseLong(range1[1]) <= Long.parseLong(range2[1])))
                || (Long.parseLong(range2[0]) >= Long.parseLong(range1[0]) && (Long.parseLong(range2[1]) <= Long.parseLong(range1[1])))) {
                    total++;
                }
            }
        }
        return total;
    }

    public long calculateOverlaps() throws IOException {

        long total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] ranges = line.split(",");
                String[] range1 = ranges[0].split("-");
                String[] range2 = ranges[1].split("-");
                if ((Long.parseLong(range1[0]) >= Long.parseLong(range2[0]) && (Long.parseLong(range1[0]) <= Long.parseLong(range2[1])))
                        || (Long.parseLong(range1[1]) >= Long.parseLong(range2[0]) && (Long.parseLong(range1[1]) <= Long.parseLong(range2[1])))
                        || (Long.parseLong(range2[1]) >= Long.parseLong(range1[0]) && (Long.parseLong(range2[1]) <= Long.parseLong(range1[1])))
                        || (Long.parseLong(range2[1]) >= Long.parseLong(range1[0]) && (Long.parseLong(range2[1]) <= Long.parseLong(range1[1])))) {
                    total++;
                }
            }
        }
        return total;
    }
}
