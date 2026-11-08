package com.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    private DirectoryNode root;

    @BeforeEach
    void setUp() {
        root = new DirectoryNode("root");
        DirectoryNode docs = new DirectoryNode("docs");
        docs.addChild(new FileNode("resume.pdf", 150));
        docs.addChild(new FileNode("notes.txt", 50));

        DirectoryNode media = new DirectoryNode("media");
        media.addChild(new FileNode("photo.png", 2000));

        root.addChild(docs);
        root.addChild(media);
        root.addChild(new FileNode("config.json", 20));
    }

    @Test
    void T1_doubleDispatchVerification() {
        FileNode file = new FileNode("test.txt", 10);
        TestVisitor visitor = new TestVisitor();
        file.accept(visitor);
        assertTrue(visitor.fileVisited);

        root.accept(visitor);
        assertTrue(visitor.dirVisited);
    }

    @Test
    void T2_sizeAccumulation() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        root.accept(visitor);
        assertEquals(2220, visitor.getTotalSize());
    }

    @Test
    void T3_searchFiltering() {
        SearchVisitor visitor = new SearchVisitor(".", 100);
        root.accept(visitor);
        List<String> paths = visitor.getMatchingPaths();
        assertTrue(paths.contains("root/docs/resume.pdf"));
        assertTrue(paths.contains("root/media/photo.png"));
        assertFalse(paths.contains("root/docs/notes.txt"));
    }

    @Test
    void T4_deterministicRootRelativePaths() {
        SearchVisitor visitor = new SearchVisitor("file", 0);
        DirectoryNode customRoot = new DirectoryNode("myfolder");
        customRoot.addChild(new FileNode("file1.txt", 10));
        customRoot.addChild(new FileNode("file2.txt", 20));
        customRoot.accept(visitor);

        List<String> paths = visitor.getMatchingPaths();
        assertEquals(2, paths.size());
        assertEquals("myfolder/file1.txt", paths.get(0));
        assertEquals("myfolder/file2.txt", paths.get(1));
    }

    @Test
    void T5_statisticsCorrectness() {
        StatisticsVisitor stats = new StatisticsVisitor();
        root.accept(stats);
        assertEquals(4, stats.getFileCount());
        assertEquals(3, stats.getDirectoryCount()); // root, docs, media
        assertEquals(2, stats.getMaxDepth());
    }

    @Test
    void T6_emptyStructuresAndNoMatch() {
        DirectoryNode emptyRoot = new DirectoryNode("empty");
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        emptyRoot.accept(sizeVisitor);
        assertEquals(0, sizeVisitor.getTotalSize());

        SearchVisitor searchVisitor = new SearchVisitor("nonexistent", 0);
        emptyRoot.accept(searchVisitor);
        assertTrue(searchVisitor.getMatchingPaths().isEmpty());
    }

    @Test
    void T7_repeatExecutionsStateLeak() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        root.accept(visitor);
        assertEquals(2220, visitor.getTotalSize());

        // Re-running without reset might leak state if not handled,
        // let's verify re-running with a fresh or reset instance works properly.
        visitor.reset();
        root.accept(visitor);
        assertEquals(2220, visitor.getTotalSize());
    }

    @Test
    void T8_invalidMutationsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad/name", 10));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("file", -5));

        DirectoryNode dir = new DirectoryNode("dir");
        dir.addChild(new FileNode("a.txt", 10));
        assertThrows(IllegalArgumentException.class, () -> dir.addChild(new FileNode("a.txt", 20)));

        // Cycle test
        DirectoryNode parent = new DirectoryNode("parent");
        DirectoryNode child = new DirectoryNode("child");
        parent.addChild(child);
        assertThrows(IllegalArgumentException.class, () -> child.addChild(parent));
    }

    private static class TestVisitor implements FileSystemVisitor {
        boolean fileVisited = false;
        boolean dirVisited = false;

        @Override
        public void visit(FileNode file) {
            fileVisited = true;
        }

        @Override
        public void visit(DirectoryNode directory) {
            dirVisited = true;
        }
    }
}