# Advanced File System - Visitor Pattern

Java 17 Maven project implementing an in-memory hierarchical file system using the **Visitor behavioral pattern**[cite: 1, 2]. Developed as Assignment 06 for **ShP-2216 Software Design Patterns** at Astana IT University.

---

## 🚀 Project Overview

The project demonstrates clean separation between object structures and operations by implementing three independent visitor algorithms over a composite file system tree:
1. **`TotalSizeVisitor`** — Computes the total byte size of files in a subtree.
2. **`SearchVisitor`** — Filters files by name criterion and minimum size threshold, outputting deterministic root-relative paths[cite: 1, 2, 4].
3. **`StatisticsVisitor`** — Collects file counts, directory counts, and maximum tree depth.

---

## 🛠️ Requirements & Tech Stack

* **Language:** Java 17[cite: 1]
* **Build Tool:** Apache Maven[cite: 2, 3]
* **Testing Framework:** JUnit 5[cite: 2, 3]
* **Design Pattern:** Visitor Pattern with Double Dispatch[cite: 1, 2]

---

## 📁 Project Structure

```text
advanced-file-system/
├── src
│   ├── main/java/com/filesystem/
│   │   ├── FileSystemComponent.java    # Common element interface
│   │   ├── FileNode.java               # Concrete element (File)
│   │   ├── DirectoryNode.java          # Concrete element (Directory & Container)
│   │   ├── FileSystemVisitor.java      # Visitor interface (Double Dispatch)
│   │   ├── TotalSizeVisitor.java       # Concrete visitor 1 (Size)
│   │   ├── SearchVisitor.java          # Concrete visitor 2 (Search filter)
│   │   └── StatisticsVisitor.java      # Concrete visitor 3 (Stats & Depth)
│   └── test/java/com/filesystem/
│       └── FileSystemTest.java         # JUnit 5 test suite (T1 - T8)
└── pom.xml                             # Maven configuration
