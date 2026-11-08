package com.filesystem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DirectoryNode implements FileSystemComponent {
    private final String name;
    private final Map<String, FileSystemComponent> children = new LinkedHashMap<>();
    private DirectoryNode parent;

    public DirectoryNode(String name) {
        if (name == null || name.isEmpty() || name.contains("/")) {
            throw new IllegalArgumentException("Invalid directory name");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public DirectoryNode getParent() {
        return parent;
    }

    public Map<String, FileSystemComponent> getChildren() {
        return Collections.unmodifiableMap(children);
    }

    public void addChild(FileSystemComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        String childName = component.getName();
        if (children.containsKey(childName)) {
            throw new IllegalArgumentException("Duplicate sibling name: " + childName);
        }

        if (component instanceof DirectoryNode) {
            DirectoryNode childDir = (DirectoryNode) component;
            if (childDir == this || isAncestorOf(childDir)) {
                throw new IllegalArgumentException("Cycle detected: cannot add parent as child");
            }
            if (childDir.parent != null) {
                throw new IllegalStateException("Directory already has a parent");
            }
            childDir.parent = this;
        } else if (component instanceof FileNode) {
            // File nodes don't have parent tracking in this simple model, but handled safely
        }

        children.put(childName, component);
    }

    private boolean isAncestorOf(DirectoryNode dir) {
        DirectoryNode current = this.parent;
        while (current != null) {
            if (current == dir) {
                return true;
            }
            current = current.parent;
        }
        return false;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}