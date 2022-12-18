package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LavaCalculator {

    private String inputFile;
    public LavaCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    private List<Cube> cubes = new ArrayList<>();

    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private int minZ = Integer.MAX_VALUE;
    private int maxZ = Integer.MIN_VALUE;

    public long calculateSurfaceArea() throws IOException {
        long surfaceArea = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;

            lineReader:
            while ((line = reader.readLine()) != null && line.length() > 0) {
                String[] split = line.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                int z = Integer.parseInt(split[2]);
                Cube cube = new Cube(x, y, z);
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
                if (z < minZ) minZ = z;
                if (z > maxZ) maxZ = z;
                cubes.add(cube);
            }
        }

        return findSurfaceArea();

    }

    private long findSurfaceArea() {
        long count = 0;
        for (Cube cube: cubes) {
            Cube frontFaceCube = new Cube(cube.x, cube.y, cube.z - 1);
            if (!cubes.contains(frontFaceCube) && expandSpace(new HashSet<>(), frontFaceCube)) count++;
            Cube backFaceCube = new Cube(cube.x, cube.y, cube.z + 1);
            if (!cubes.contains(backFaceCube) && expandSpace(new HashSet<>(), backFaceCube)) count++;
            Cube leftFaceCube = new Cube(cube.x - 1, cube.y, cube.z);
            if (!cubes.contains(leftFaceCube) && expandSpace(new HashSet<>(), leftFaceCube)) count++;
            Cube rightFaceCube = new Cube(cube.x + 1, cube.y, cube.z);
            if (!cubes.contains(rightFaceCube) && expandSpace(new HashSet<>(), rightFaceCube)) count++;
            Cube topFaceCube = new Cube(cube.x, cube.y + 1, cube.z);
            if (!cubes.contains(topFaceCube) && expandSpace(new HashSet<>(), topFaceCube)) count++;
            Cube bottomFaceCube = new Cube(cube.x, cube.y - 1, cube.z);
            if (!cubes.contains(bottomFaceCube) && expandSpace(new HashSet<>(), bottomFaceCube)) count++;
        }
        return count;
    }

    // Return true if air escaped
    private boolean expandSpace(Set<Cube> space, Cube cube) {
        if (cube.x > maxX || cube.x < minX || cube.y > maxY || cube.y < minY || cube.z > maxZ || cube.z < minZ) {
            return true;
        } else {
            List<Cube> adjacentSpace = new ArrayList<>();
            Cube frontFaceCube = new Cube(cube.x, cube.y, cube.z - 1);
            if (!cubes.contains(frontFaceCube) && !space.contains(frontFaceCube)) {
                space.add(frontFaceCube);
                adjacentSpace.add(frontFaceCube);
            }
            Cube backFaceCube = new Cube(cube.x, cube.y, cube.z + 1);
            if (!cubes.contains(backFaceCube) && !space.contains(backFaceCube)) {
                space.add(backFaceCube);
                adjacentSpace.add(backFaceCube);
            }
            Cube leftFaceCube = new Cube(cube.x - 1, cube.y, cube.z);
            if (!cubes.contains(leftFaceCube) && !space.contains(leftFaceCube)) {
                space.add(leftFaceCube);
                adjacentSpace.add(leftFaceCube);
            }
            Cube rightFaceCube = new Cube(cube.x + 1, cube.y, cube.z);
            if (!cubes.contains(rightFaceCube) && !space.contains(rightFaceCube)) {
                space.add(rightFaceCube);
                adjacentSpace.add(rightFaceCube);
            }
            Cube topFaceCube = new Cube(cube.x, cube.y + 1, cube.z);
            if (!cubes.contains(topFaceCube) && !space.contains(topFaceCube)) {
                space.add(topFaceCube);
                adjacentSpace.add(topFaceCube);
            }
            Cube bottomFaceCube = new Cube(cube.x, cube.y - 1, cube.z);
            if (!cubes.contains(bottomFaceCube) && !space.contains(bottomFaceCube)) {
                space.add(bottomFaceCube);
                adjacentSpace.add(bottomFaceCube);
            }
            for (Cube newCube: adjacentSpace) {
                if (expandSpace(space, newCube)) return true;
            }
            return false;
        }
    }

    private long findNaiveSurfaceArea() {
        long facesToSubtract = 0;
        for (int i = 0; i < cubes.size() - 1; i++) {
            for (int j = i + 1; j < cubes.size(); j++) {
                if (cubes.get(i).facesTouching(cubes.get(j))) {
                    facesToSubtract++;
                }
            }
        }
        return cubes.size() * 6 - facesToSubtract * 2;
    }

    class Cube {

        int x;
        int y;
        int z;
        public Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean facesTouching(Cube cube) {
            int[] diffs = new int[] {Math.abs(this.x - cube.x), Math.abs(this.y - cube.y), Math.abs(this.z - cube.z)};
            int zeroCount = 0;
            int oneCount = 0;
            for (int val: diffs) {
                if (val == 0) zeroCount++;
                else if (val == 1) oneCount++;
            }
            return zeroCount == 2 && oneCount == 1;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return x == cube.x && y == cube.y && z == cube.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
