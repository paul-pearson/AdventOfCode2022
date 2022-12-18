package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RucksackCalculator {

    private static final String PRIORITIES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String inputFile;
    public RucksackCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateTotalScore() throws IOException {
        long total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                String part1 = line.substring(0, line.length() / 2);
                String part2 = line.substring(line.length() / 2);
                String character = getCommonItem(part1, part2);
                long priority = PRIORITIES.indexOf(character) + 1;
                total += priority;
            }
        }
        return total;
    }

    public long calculateBadges() throws IOException {
        long total = 0;
        List<String> batch = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                batch.add(line);
                if (batch.size() == 3)  {
                    total += getPriorityOfSharedItem(batch);
                    batch = new ArrayList<>();
                }
            }
        }
        return total;
    }

    private long getPriorityOfSharedItem(List<String> batch) {
        for (int i = 0; i < batch.get(0).length(); i++) {
            String letter = batch.get(0).substring(i, i+1);
            if (batch.get(1).contains(letter) && batch.get(2).contains(letter)) {
                return PRIORITIES.indexOf(letter) + 1;
            }
        }
        throw new RuntimeException("Error");
    }

    private String getCommonItem(String part1, String part2) {
        for (int i = 0; i < part1.length(); i++) {
            String letter = part1.substring(i, i+1);
            if (part2.contains(letter)) {
                return letter;
            }
        }
        return null;
    }


}
