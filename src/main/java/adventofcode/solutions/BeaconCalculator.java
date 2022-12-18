package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BeaconCalculator {

    private String inputFile;
    private int maxVal = 4000000;
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private List<Sensor> sensors = new ArrayList<>();

    private Map<Integer, List<Range>> ruledOutRanges = new HashMap<>();

    public BeaconCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateNoPossibleBeacon() throws IOException {
        long total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] split = line.split(" ");
                String sensorXWithComma = split[2].split("=")[1];
                int sensorX = Integer.parseInt(sensorXWithComma.substring(0, sensorXWithComma.length() - 1));
                if (sensorX < minX) minX = sensorX;
                if (sensorX > maxX) maxX = sensorX;
                String sensorYWithComma = split[3].split("=")[1];
                int sensorY = Integer.parseInt(sensorYWithComma.substring(0, sensorYWithComma.length() - 1));
                if (sensorY < minY) minY = sensorY;
                if (sensorY > maxY) maxY = sensorY;
                String beaconXWithComma = split[8].split("=")[1];
                int beaconX = Integer.parseInt(beaconXWithComma.substring(0, beaconXWithComma.length() - 1));
                if (beaconX < minX) minX = beaconX;
                if (beaconX > maxX) maxX = beaconX;
                String beaconYWithoutComma = split[9].split("=")[1];
                int beaconY = Integer.parseInt(beaconYWithoutComma);
                if (beaconY < minY) minY = beaconY;
                if (beaconY > maxY) maxY = beaconY;
                Sensor sensor = new Sensor(sensorX, sensorY, beaconX, beaconY);
                sensors.add(sensor);
            }
        }
//        outer:
//        for (int y = 0; y <= 4000000; y++) {
//            total = 0;
//            for (int x = 0; x <= 4000000; x++) {
//                boolean couldBe = true;
//                for (Sensor sensor : sensors) {
//                    if (sensor.getDistanceToSensor(x, y) <= sensor.getSensorDistanceToBeacon() || hasBeaconOrSensor(x, y)) {
//                        couldBe = false;
//                        break;
//                    }
//                }
//                if (couldBe) {
//                    return x * 4000000 + y;
//                }
//            }
//            if (y % 10 == 0) System.out.println(y);
//        }
        for (Sensor sensor: sensors) {
            sensor.getRuledOut();
        }
        int valX = -1;
        int valY = -1;
        outer:
        for (int y = 0; y <= maxVal; y++) {
            List<Range> ranges = ruledOutRanges.get(y);
            Collections.sort(ranges);
            List<Range> rangesToRemove = new ArrayList<>();
            boolean removedNone = false;
            while (!removedNone) {
                for (int i = 0; i < ranges.size() - 1; i++) {
                    if (ranges.get(i + 1).maxX <= ranges.get(i).maxX) {
                        rangesToRemove.add(ranges.get(i + 1));
                    }
                }
                if (rangesToRemove.size() == 0) {
                    removedNone = true;
                } else {
                    ranges.removeAll(rangesToRemove);
                    rangesToRemove.clear();
                }
            }
            for (int i = 0; i < ranges.size() - 1; i++) {
                if (ranges.get(i + 1).minX > ranges.get(i).maxX + 1) {
                    int x = ranges.get(i).maxX + 1;
                    if (x >= 0 && x <=maxVal) {
                        valX = x;
                        valY = y;
                        break outer;
                    }
                }
            }
            if (y % 1000 == 0) {
                System.out.println(y);
            }
        }
        return valX * 4000000L + valY;
    }

    public boolean hasBeaconOrSensor(int x, int y) {
        for (Sensor sensor: sensors) {
            if ((sensor.beaconX == x && sensor.beaconY == y) ||
                    (sensor.sensorX == x && sensor.sensorY == y)) {
                return true;
            }
        }
        return false;
    }

    class Sensor {
        int sensorX;
        int sensorY;
        int beaconX;
        int beaconY;

        public Sensor(int sx, int sy, int bx, int by) {
            this.sensorX = sx;
            this.sensorY = sy;
            this.beaconX = bx;
            this.beaconY = by;
        }

        public int getSensorDistanceToBeacon() {
            return Math.abs(sensorX - beaconX) + Math.abs(sensorY - beaconY);
        }

        public int getDistanceToSensor(int x, int y) {
            return Math.abs(sensorX - x) + Math.abs(sensorY - y);
        }

        public void getRuledOut() {
            int distance = getSensorDistanceToBeacon();
            int width = 0;
            for (int y = sensorY - distance; y <= sensorY; y++) {
                Range range = new Range(sensorX - width, sensorX + width);
                width++;
                if (ruledOutRanges.containsKey(y)) {
                    ruledOutRanges.get(y).add(range);
                } else {
                    List<Range> ranges = new ArrayList<>();
                    ranges.add(range);
                    ruledOutRanges.put(y, ranges);
                }
            }
            width = distance - 1;
            for (int y = sensorY + 1; y <= sensorY + distance; y++) {
                Range range = new Range(sensorX - width, sensorX + width);
                width--;
                if (ruledOutRanges.containsKey(y)) {
                    ruledOutRanges.get(y).add(range);
                } else {
                    List<Range> ranges = new ArrayList<>();
                    ranges.add(range);
                    ruledOutRanges.put(y, ranges);
                }
            }
        }
    }

    class Range implements Comparable<Range> {
        int minX;
        int maxX;

        public Range(int minX, int maxX) {
            this.minX = minX;
            this.maxX = maxX;
        }

        @Override
        public int compareTo(Range range) {
            if (minX != range.minX) {
                return minX - range.minX;
            } else {
                return maxX - range.maxX;
            }
        }
    }
}
