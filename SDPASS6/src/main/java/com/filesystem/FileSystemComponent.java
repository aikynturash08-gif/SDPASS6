package com.filesystem;

public interface FileSystemComponent {
    void accept(FileSystemVisitor visitor);
    String getName();
}