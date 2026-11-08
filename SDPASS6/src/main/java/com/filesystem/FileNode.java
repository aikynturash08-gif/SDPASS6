package com.filesystem;

public class FileNode implements FileSystemComponent {
    private final String name;
    private final long size;

    public FileNode(String name, long size) {
        if (name == null || name.isEmpty() || name.contains("/")) {
            throw new IllegalArgumentException("Invalid file name");
        }
        if (size < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}