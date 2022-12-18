package adventofcode.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirSizeCalculator {

    private String inputFile;
    private int stackCount = 9;
    public DirSizeCalculator(String inputFile) {
        this.inputFile = inputFile;
    }

    public long calculateTotalDirSizes() throws IOException {
        long total = 0;
        Directory currentDirectory = null;
        Directory rootDirectory = new Directory("/", null);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line = reader.readLine();
            while (line != null && line.length() > 0) {

                if (line.equals("$ cd /")) {
                    currentDirectory = rootDirectory;
                    line = reader.readLine();
                } else if (line.equals("$ ls")) {
                    while ((line = reader.readLine()) != null && line.length() > 0 && !line.startsWith("$")) {
                        if (line.startsWith("dir ")) {
                            String dirName = line.split(" ")[1];
                            currentDirectory.dirs.add(new Directory(dirName, currentDirectory));
                        } else {
                            String[] split = line.split(" ");
                            String filename = split[1];
                            long fileSize = Long.parseLong(split[0]);
                            currentDirectory.files.add(new File(filename, fileSize));
                        }
                    }
                } else if (line.startsWith("$ cd ")) {
                    String dirToChangeTo = line.split(" ")[2];
                    if (dirToChangeTo.equals("..")) {
                        currentDirectory = currentDirectory.parent;
                    } else {
                        for (Directory dir: currentDirectory.dirs) {
                            if (dir.name.equals(dirToChangeTo)) {
                                currentDirectory = dir;
                                break;
                            }
                        }
                    }
                    line = reader.readLine();
                }
            }

        }
        //return rootDirectory.getDirSize();
//        List<Directory> contributingDirs = new ArrayList<>();
//        rootDirectory.addContributingDirs(contributingDirs, 100000L);
//        long totalSize = 0;
//        for (Directory contributing: contributingDirs) {
//            totalSize += contributing.getDirSize();
//        }
//        return totalSize;
        long spaceRemaining = 70000000 - rootDirectory.getDirSize();
        long spaceRequired = 30000000;
        long needed = spaceRequired - spaceRemaining;
        List<Directory> candidates = new ArrayList<>();
        rootDirectory.addToCandidates(candidates, needed);
        long smallest  = Long.MAX_VALUE;
        for (Directory dir: candidates) {
            if (dir.getDirSize() < smallest) {
                smallest = dir.getDirSize();
            }
        }
        return smallest;
    }



    class Directory {

        String name;
        Directory parent;
        public Directory(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
        }

        List<Directory> dirs = new ArrayList<>();
        List<File> files = new ArrayList<>();

        long getDirSize() {
            long total = 0;
            for (File file: files) {
                total += file.size;
            }
            for (Directory dir: dirs) {
                total += dir.getDirSize();
            }
            return total;
        }

        public long getContribution(long currentTotal, long maxSize) {
            long thisSize = getDirSize();
            if (thisSize <= maxSize) {
                currentTotal += thisSize;
            }
            for (Directory dir: dirs) {
                currentTotal += dir.getContribution(currentTotal, maxSize);
            }
            return currentTotal;
        }

        public void addContributingDirs(List<Directory> contributing, long maxSize) {
            if (getDirSize() <= maxSize) {
                contributing.add(this);
            }
            for (Directory dir: dirs) {
                dir.addContributingDirs(contributing, maxSize);
            }
        }

        public void addToCandidates(List<Directory> candidates, long spaceRequired) {
            long dirSize = getDirSize();
            if (dirSize >= spaceRequired) {
                candidates.add(this);
            }
            for (Directory dir: dirs) {
                dir.addToCandidates(candidates, spaceRequired);
            }
        }
    }

    class File {

        String name;
        long size;
        public File(String name, long size) {
            this.name = name;
            this.size = size;
        }
    }
}
