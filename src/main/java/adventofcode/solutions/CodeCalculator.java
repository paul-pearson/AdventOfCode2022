package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeCalculator {

    private String inputFile;
    public CodeCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    private int messageLength = 14;

    public int calculateTopRow() throws IOException {
        long position = 0;
        List<String> letters = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            lineReader:
            while ((line = reader.readLine()) != null && line.length() > 0) {
                for (int i = 0; i < line.length(); i++) {
                    String letter = line.substring(i, i+1);
                    letters.add(letter);
                    if (letters.size() > messageLength) {
                        letters.remove(0);
                    }
                    if (letters.size() == messageLength) {
                        if (allDistinct(letters)) {
                            return i + 1;
                        }
                    }
                }
            }

        }

        return -1;
    }

    private boolean allDistinct(List<String> letters) {
        for (int i = 0; i < letters.size(); i++) {
            List<String> letter = letters.subList(i, i+1);
            if (letters.subList(i+1, letters.size()).contains(letter.get(0))) {
                return false;
            }
        }
        return true;
    }
}
