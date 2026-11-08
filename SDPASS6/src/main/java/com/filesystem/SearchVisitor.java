package com.filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchVisitor implements FileSystemVisitor {
    private final String nameCriterion;
    private final long minSizeThreshold;
    private final List<String> matchingPaths = new ArrayList<>();
    private final List<String> currentPathSegments = new ArrayList<>();

    public SearchVisitor(String nameCriterion, long minSizeThreshold) {
        if (nameCriterion == null) {
            throw new IllegalArgumentException("Name criterion cannot be null");
        }
        if (minSizeThreshold < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }
        this.nameCriterion = nameCriterion;
        this.minSizeThreshold = minSizeThreshold;
    }

    @Override
    public void visit(FileNode file) {
        if (file.getName().contains(nameCriterion) && file.getSize() >= minSizeThreshold) {
            List<String> fullPath = new ArrayList<>(currentPathSegments);
            fullPath.add(file.getName());
            matchingPaths.add(String.join("/", fullPath));
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        currentPathSegments.add(directory.getName());
        for (FileSystemComponent child : directory.getChildren().values()) {
            child.accept(this);
        }
        currentPathSegments.remove(currentPathSegments.size() - 1);
    }

    public List<String> getMatchingPaths() {
        List<String> sorted = new ArrayList<>(matchingPaths);
        Collections.sort(sorted);
        return sorted;
    }
}