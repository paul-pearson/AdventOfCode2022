package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RockPaperScissorsCalculator {

    private String inputFile;
    public RockPaperScissorsCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateTotalScore() throws IOException {
        long total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                RPS him = getRPSFromLine(line, 0);
                RPS me = RPS.getChoice(him, line.substring(2, 3));
                int score = me.score + me.pointsVs(him);
                total += score;
            }
        }
        return total;
    }




    private RPS getRPSFromLine(String line, int index) {
        return RPS.getValue(line.substring(index, index+1));
    }

    public enum RPS {
        ROCK(1, "A", "X"),
        PAPER(2, "B", "Y"),
        SCISSORS(3, "C", "Z");

        int score;
        List<String> letters;

        RPS(int score, String... letters) {
            this.score = score;
            this.letters = Arrays.asList(letters);
        }

        public static RPS getValue(String letter) {
            for (RPS value: RPS.values()) {
                if (value.letters.contains(letter)) {
                    return value;
                }
            }
            return null;
        }

        public int beats(RPS rps) {
            if (ordinal() == rps.ordinal()) return 0;
            else if (ordinal() == rps.ordinal() - 1 || (ordinal() == RPS.values().length -1 && rps.ordinal() == 0)) return -1;
            else return 1;
        }

        public int pointsVs(RPS rps) {
            if (beats(rps) > 0) return 6;
            else if (beats(rps) == 0) return 3;
            else return 0;
        }

        public static RPS getChoice(RPS him, String strategy) {
            if (strategy.equals("Z")) return RPS.values()[((him.ordinal() + 1) % RPS.values().length)];
            else if (strategy.equals("X")) return RPS.values()[((him.ordinal() - 1 < 0 ? RPS.values().length + him.ordinal() - 1 : him.ordinal() - 1) % RPS.values().length)];
            else return him;
        }
    }

}
