package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CraneCalculator {

    private String inputFile;
    private int stackCount = 9;
    public CraneCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public String calculateTopRow() throws IOException {
        List<List<String>> stacks = new ArrayList<>();
        for (int i = 0; i < stackCount; i++) {
            stacks.add(new ArrayList<>());
        }
        long total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            lineReader:
            while ((line = reader.readLine()) != null && line.length() > 0) {
                for (int i = 0; i < stackCount; i++) {
                    try {
                        String letter = line.substring(i * 4 + 1, i * 4 + 2);
                        try {
                            Integer.parseInt(letter);
                            break lineReader;
                        } catch (NumberFormatException e) {
                            // Expected
                        }
                        if (!letter.equals(" ")) {
                            stacks.get(i).add(letter);
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        break;
                    }
                }
            }

        }
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            lineReader:
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("move")) {
                    String[] split = line.split(" ");
                    int count = Integer.parseInt(split[1]);
                    int from = Integer.parseInt(split[3]);
                    int to = Integer.parseInt(split[5]);
                    doMove(stacks, count, from, to);
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stackCount; i++) {
            if (stacks.get(i).size() > 0) {
                sb.append(stacks.get(i).get(0));
            }

        }
        return sb.toString();
    }

    private void doMove(List<List<String>> stacks, int count, int from, int to) {
        List<String> letters = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            letters.add(stacks.get(from - 1).remove(0));
        }
        for (int i = count - 1; i >= 0; i--) {
            stacks.get(to - 1).add(0, letters.get(i));
        }
    }

}
