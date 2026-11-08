package com.filesystem;

public class TotalSizeVisitor implements FileSystemVisitor {
    private long totalSize = 0;

    @Override
    public void visit(FileNode file) {
        totalSize += file.getSize();
    }

    @Override
    public void visit(DirectoryNode directory) {
        for (FileSystemComponent child : directory.getChildren().values()) {
            child.accept(this);
        }
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void reset() {
        totalSize = 0;
    }
}