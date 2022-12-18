package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MonkeyCalculator {
    private String inputFile;
    private List<Monkey> monkeys= new ArrayList<>();

    private List<Item> allItems = new ArrayList<>();
    public MonkeyCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateMonkeyBusiness() throws IOException {
        long highest = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            Monkey currentMonkey = null;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    if (line.startsWith("Monkey")) {
                        currentMonkey = new Monkey();
                        monkeys.add(currentMonkey);
                    } else if (line.trim().startsWith("Starting items: ")) {
                        String[] itemStrings = line.trim().split(", ");
                        for (String itemString: itemStrings) {
                             int worry;
                            if (itemString.startsWith("Starting")) {
                                worry = Integer.parseInt(itemString.split(" ")[2]);
                            } else {
                                worry = Integer.parseInt(itemString.trim());
                            }
                            Item item = new Item(worry);

                            currentMonkey.items.add(item);
                            allItems.add(item);
                        }
                    } else if (line.trim().startsWith("Operation")) {
                        String[] split = line.trim().split(" ");
                        String op = split[split.length - 2];
                        Integer value = null;
                        if (!split[split.length - 1].equals("old")) {
                            value = Integer.parseInt(split[split.length-1]);
                        }
                        Operation operation = new Operation();
                        operation.op = op;
                        operation.value = value;
                        currentMonkey.operation = operation;
                    } else if (line.trim().startsWith("Test")) {
                        int testValue = Integer.parseInt(line.split(" ")[line.split(" ").length - 1]);
                        Test test = new Test();
                        test.value = testValue;
                        currentMonkey.test = test;
                    } else if (line.trim().startsWith("If true:")) {
                        String[] split = line.split(" ");
                        int monk = Integer.parseInt(split[split.length - 1]);
                        currentMonkey.test.trueTest = monk;
                    } else if (line.trim().startsWith("If false:")) {
                        String[] split = line.split(" ");
                        int monk = Integer.parseInt(split[split.length - 1]);
                        currentMonkey.test.falseTest = monk;
                    }
                }
            }
        }
        for (Monkey monkey: monkeys) {
            int value = monkey.test.value;
            for (Item item: allItems) {
                item.worryLevel.modulos.put(value, item.startValue % value);
            }
        }
        doRounds(10000);
        List<Long> thrown = new ArrayList<>();
        for (Monkey monkey: monkeys) {
            thrown.add(monkey.thrownItems);
        }
        Collections.sort(thrown);
        return thrown.get(thrown.size() - 1) * thrown.get(thrown.size() -2);
    }

    private void doRounds(int rounds) {
        for (int round = 0; round < rounds; round++) {
            for (Monkey monkey: monkeys) {
                for (Item item: monkey.items) {
                    Integer opValue = monkey.operation.value;
                    for (Map.Entry<Integer, Integer> entry: item.worryLevel.modulos.entrySet()) {
                        if (monkey.operation.op.equals(("*"))) {
                            if (opValue == null) {
                                item.worryLevel.modulos.put(entry.getKey(), (entry.getValue()* entry.getValue()) % entry.getKey());
                            } else {
                                item.worryLevel.modulos.put(entry.getKey(), (entry.getValue()* opValue) % entry.getKey());
                            }
                        } else {
                            if (opValue == null) {
                                item.worryLevel.modulos.put(entry.getKey(), (entry.getValue()+ entry.getValue()) % entry.getKey());
                            } else {
                                item.worryLevel.modulos.put(entry.getKey(), (entry.getValue()+ opValue) % entry.getKey());
                            }
                        }
                    }
                    if (item.worryLevel.modulos.get(monkey.test.value) == 0) {
                        monkeys.get(monkey.test.trueTest).items.add(item);
                    } else {
                        monkeys.get(monkey.test.falseTest).items.add(item);
                    }
                    monkey.thrownItems++;
                }
                monkey.items.clear();
            }
        }
    }

    class Monkey {
        List<Item> items = new ArrayList<>();

        Operation operation;
        Test test;

        long thrownItems = 0;

    }

    class Item {
        private int startValue;
        public Item(int startValue) {
            this.startValue = startValue;
        }

        WorryLevel worryLevel = new WorryLevel();
    }

    class Operation {
        String op;
        Integer value;
    }

    class Test {
        int value;


        int trueTest;
        int falseTest;
    }

    class WorryLevel {
       private Map<Integer, Integer> modulos = new HashMap<>();
    }
}
