package adventofcode;

import adventofcode.solutions.LavaCalculator;

import java.io.IOException;

public class AdventOfCodeRunner {

    public static void main(String[] args) {
        new AdventOfCodeRunner().execute();
    }

    private void execute() {
        try {
            long answer = new LavaCalculator("/home/elfasij/Downloads/adventofcode/day18input.txt").calculateSurfaceArea();

            System.out.println(answer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
