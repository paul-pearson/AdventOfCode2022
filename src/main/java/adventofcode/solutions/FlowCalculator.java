package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FlowCalculator {
    private String inputFile;


    private int currentSize = 0;

    private Map<String, Valve> allValvesMap = new HashMap<>();

    private List<String> canOpenValves = new ArrayList<>();

    //private List<List<Action>> allPaths = new ArrayList<>();
    public FlowCalculator(String inputFile) {
        this.inputFile = inputFile;
    }


    private long highestTotalFlow = 0;
    private int canOpenCount = 0;

    public long calculateHighestFlow() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] split = line.split(" ");
                Valve valve = new Valve(split[1], Integer.parseInt(split[4].split("=")[1].substring(0, split[4].split("=")[1].length() - 1)));
                allValvesMap.put(valve.name, valve);
                if (valve.flowRate > 0) {
                    canOpenCount++;
                    canOpenValves.add(valve.name);
                }
                String leadsToStr;
                if (line.contains("valves")) {
                    leadsToStr = line.split("valves ")[1];
                } else {
                    leadsToStr = line.split("valve ")[1];
                }
                split = leadsToStr.split(", ");
                for (String leadsTo: split) {
                    valve.leadsTo.add(leadsTo);
                }
            }
        }
        Valve startingValve = allValvesMap.get("AA");
        Action firstAction = new Action(new Move("AA"));
        //List<Action> firstPath = new ArrayList<>();
        //firstPath.add(firstAction);
        //allPaths.add(firstPath);
        Path firstPath = new Path();
        firstPath.startValve = "AA";
        firstPath.visited.add("AA");
        getPathsFromPath2(firstPath);
        for (String startValve: canOpenValves) {
            Path path = new Path();
            path.currentValve = startValve;
            path.startValve = startValve;
            path.visited.add(startValve);
            getPathsFromPath2(path);
        }
        getPathsFromPath3(new PathPair(new Path(), new Path()));


        return highestTotalFlow;
    }



    private void getPathsFromPath3(PathPair pair) {
        if (highestTotalFlow < pair.path1.currentTotal + pair.path2.currentTotal) {
            highestTotalFlow = pair.path1.currentTotal + pair.path2.currentTotal;
            System.out.println(highestTotalFlow);
        }

        List<PathPair> newPathsPairs = new ArrayList<>();
        if (pair.path1.timeRemaining <= 0 && pair.path2.timeRemaining <= 0) {
            System.out.println("stopping");
        } else {
            for (String valveName: allValvesMap.get(pair.path1.currentValve).shortestPathToValves.keySet()) {
                int timeRemaining = pair.path1.timeRemaining - (allValvesMap.get(pair.path1.currentValve).shortestPathToValves.get(valveName) + 1);
                if (timeRemaining > 0 && !pair.path1.visited.contains(valveName)  && !pair.path2.visited.contains(valveName)) {
                    int currentTotal = pair.path1.currentTotal + (timeRemaining * allValvesMap.get(valveName).flowRate);
                    Path newPath = new Path();
                    newPath.visited = new ArrayList<>(pair.path1.visited);
                    newPath.visited.add(valveName);
                    newPath.currentValve = valveName;
                    newPath.currentTotal = currentTotal;
                    newPath.timeRemaining = timeRemaining;
                    newPathsPairs.add(new PathPair(newPath, pair.path2));
                    for (String valveName2: allValvesMap.get(pair.path2.currentValve).shortestPathToValves.keySet()) {

                        int timeRemaining2 = pair.path2.timeRemaining - (allValvesMap.get(pair.path2.currentValve).shortestPathToValves.get(valveName2) + 1);
                        if (timeRemaining2 > 0 && !newPath.visited.contains(valveName2)  && !pair.path2.visited.contains(valveName2)
                        && !valveName2.equals(valveName)) {
                            int currentTotal2 = pair.path2.currentTotal + (timeRemaining2 * allValvesMap.get(valveName2).flowRate);
                            Path newPath2 = new Path();
                            newPath2.visited = new ArrayList<>(pair.path2.visited);
                            newPath2.visited.add(valveName2);
                            newPath2.currentValve = valveName2;
                            newPath2.currentTotal = currentTotal2;
                            newPath2.timeRemaining = timeRemaining2;
                            newPathsPairs.add(new PathPair(newPath, newPath2));
                            //newPathsPairs.add(new PathPair(pair.path1, newPath2));
                        }
                    }
                }
            }
            for (PathPair newPathPair: newPathsPairs) {
                getPathsFromPath3(newPathPair);
            }

        }
    }


    private List<Path> getPathsFromPath2(Path path) {
        List<Path> paths = new ArrayList<>();
        for (String valveName: allValvesMap.get(path.currentValve).leadsTo) {
            if (!path.visited.contains(valveName)) {
                Path newPath = new Path();
                newPath.currentValve = valveName;
                newPath.startValve = path.startValve;
                newPath.moves = path.moves + 1;
                newPath.visited = new ArrayList<>(path.visited);
                newPath.visited.add(valveName);

                if (canOpenValves.contains(valveName)) {
                    Integer shortestDistanceTo = allValvesMap.get(path.startValve).shortestPathToValves.get(valveName);
                    if (shortestDistanceTo == null || shortestDistanceTo > newPath.moves) {
                        allValvesMap.get(path.startValve).shortestPathToValves.put(valveName, newPath.moves);
                    }
                }
                paths.add(newPath);
            }
        }
        for (Path newPath: paths) {
            getPathsFromPath2(newPath);
        }
        return paths;
    }

    class PathPair {
        Path path1;
        Path path2;

        public PathPair(Path path1, Path path2) {
            this.path1 = path1;
            this.path2 = path2;
        }
    }

    class Valve {
        String name;
        int flowRate;

        Map<String, Integer> shortestPathToValves = new HashMap<>();

        public Valve(String name, int flowRate) {
            this.name = name;
            this.flowRate = flowRate;
        }

        List<String> leadsTo = new ArrayList<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Valve valve = (Valve) o;
            return name.equals(valve.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    class Action {
        Move move;
        Open open;

        public Action(Move move) {
            this.move = move;
        }

        public Action(Open open) {
            this.open = open;
        }

        boolean isMoveTo(String valveName) {
            return move != null && move.valveName.equals(valveName);
        }

        boolean isOpen() {
            return open != null;
        }

        public String getCurrentValveName() {
            if (move != null) return move.valveName;
            else return open.valveName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Action action = (Action) o;
            return Objects.equals(move, action.move) && Objects.equals(open, action.open);
        }

        @Override
        public int hashCode() {
            return Objects.hash(move, open);
        }
    }

    class Move {
        String valveName;
        public Move(String valveName) {
            this.valveName = valveName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Move move = (Move) o;
            return valveName.equals(move.valveName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valveName);
        }
    }

    class Open {

        String valveName;
        public Open(String valveName) {
            this.valveName = valveName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Open open = (Open) o;
            return valveName.equals(open.valveName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valveName);
        }
    }

    class Path {
        List<String> opened = new ArrayList<>();
        List<String> visited = new ArrayList<>();
        String currentValve = "AA";
        int moves = 0;
        int timeRemaining = 26;

        private String startValve;

        int currentTotal = 0;
    }
}
