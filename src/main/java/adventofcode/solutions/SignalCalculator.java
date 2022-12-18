package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignalCalculator {

    private String inputFile;
    private List<Signal> leftValues = new ArrayList<>();
    private List<Signal> rightValues = new ArrayList<>();

    private List<Signal> allValues = new ArrayList<>();
    public SignalCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long sumCorrectIndices() throws IOException {
        long total = 0;
        boolean isLeft = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    Signal signal = new Signal();
                    if (count < 2) {
                        signal.divider = true;
                    }
                    count++;
                    parseLine(signal, line);
                    if (isLeft) {
                        leftValues.add(signal);
                        isLeft = false;
                    } else {
                        rightValues.add(signal);
                        isLeft = true;
                    }
                }
            }
        }
        allValues.addAll(leftValues);
        allValues.addAll(rightValues);
        Collections.sort(allValues);
        List<Integer> multValues = new ArrayList<>();
        for (int index = 1; index <= allValues.size(); index++) {
            if (allValues.get(index - 1).divider) multValues.add(index);
        }
        return multValues.get(0) * multValues.get(1);
    }

    private void parseLine(Signal signal, String line) {
        if (line.startsWith("[")) {
            List<String> lines = splitList(line);
            Signal newSignal = new Signal();
            signal.signalList.add(newSignal);
            for (String newLine: lines) {
                parseLine(newSignal, newLine);
            }
        } else if (line.length() > 0) {
            signal.signalList.add(new Signal(Integer.parseInt(line)));
        }
    }

    private List<String> splitList(String line) {
        line = line.substring(1, line.length() - 1);
        int level = 0;
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            String character = line.substring(i, i + 1);
            if (character.equals(",")) {
                if (level == 0) {
                    lines.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    sb.append(character);
                }
            } else {
                if (character.equals("[")) {
                    level++;
                } else if (character.equals("]")) {
                    level--;
                }
                sb.append(character);
            }
        }
        if (sb.length() > 0) {
            lines.add(sb.toString());
        }
        return lines;
    }

    class Signal implements Comparable<Signal> {
        List<Signal> signalList = new ArrayList<>();

        Integer value;
        boolean divider = false;

        public Signal() {
        }

        public Signal(int value) {
            this.value = value;
        }

//        public int compareTo(Signal signal) {
//            int i = 0;
//            int comparison = 0;
//            while (comparison == 0) {
//                if (this.signalList.size() < i + 1) {
//                    if (signal.signalList.size() >= i + 1) {
//                        comparison = -1;
//                    } else {
//                        comparison = 0;
//                    }
//                } else if (signal.signalList.size() < i + 1) {
//                    if (this.signalList.size() >= i + 1) {
//                        comparison = 1;
//                    } else {
//                        comparison = 0;
//                    }
//                } else {
//                    Signal thisSignal = this.signalList.get(i);
//                    Signal thatSignal = signal.signalList.get(i);
//                    if (thisSignal.value != null && thatSignal.value != null) {
//                        comparison = thisSignal.value - thatSignal.value;
//                    } else if (thisSignal.value != null) {
//                        Signal newSignal = new Signal();
//                        newSignal.signalList.add(thisSignal);
//                        comparison = newSignal.compareTo(thatSignal);
//                        if (comparison == 0) {
//                            i++;
//                        } else {
//                            return comparison;
//                        }
//                    } else if (thatSignal.value != null) {
//                        Signal newSignal = new Signal();
//                        newSignal.signalList.add(thatSignal);
//                        comparison = thisSignal.compareTo(newSignal);
//                        if (comparison == 0) {
//                            i++;
//                        } else {
//                            return comparison;
//                        }
//                    } else {
//                        comparison = thisSignal.compareTo(thatSignal);
//                    }
//                }
//                i++;
//            }
//            return comparison;
//        }

        public int compareTo(Signal signal) {
            if (this.value != null && signal.value != null) {
                return this.value - signal.value;
            } else if (this.value != null) {
                Signal newSignal = new Signal();
                newSignal.signalList.add(this);
                return newSignal.compareTo(signal);
            } else if (signal.value != null) {
                Signal newSignal = new Signal();
                newSignal.signalList.add(signal);
                return this.compareTo(newSignal);
            } else {
                for (int i = 0; i < Math.min(this.signalList.size(), signal.signalList.size()); i++) {
                    int comparison = this.signalList.get(i).compareTo(signal.signalList.get(i));
                    if (comparison != 0) {
                        return comparison;
                    }
                }
                return this.signalList.size() - signal.signalList.size();
            }
        }
    }

}
