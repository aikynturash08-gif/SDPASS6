package com.filesystem;

public class StatisticsVisitor implements FileSystemVisitor {
    private int fileCount = 0;
    private int directoryCount = 0;
    private int maxDepth = 0;
    private int currentDepth = 0;

    @Override
    public void visit(FileNode file) {
        fileCount++;
        // Если глубина файла больше максимальной, фиксируем её
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
        currentDepth++;
        for (FileSystemComponent child : directory.getChildren().values()) {
            child.accept(this);
        }
        currentDepth--;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}