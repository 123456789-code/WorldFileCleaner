package com.wang125510.cleaner;

import com.wang125510.cleaner.config.CleanerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class CleanerLogic {
    private static final String MOD_ID = WorldFileCleaner.MOD_ID;
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void cleanWorld(Path worldDir, CleanerConfig config) {
        if (!Files.isDirectory(worldDir)) {
            LOGGER.warn("World directory does not exist: {}", worldDir);
            return;
        }

        LOGGER.info("Starting cleanup for world: {}", worldDir);

        List<PathMatcher> deleteMatchers = config.getDeletePatterns().stream()
                .map(p -> FileSystems.getDefault().getPathMatcher("glob:" + p))
                .toList();

        List<PathMatcher> whitelistMatchers = config.getWhiteList().stream()
                .map(p -> FileSystems.getDefault().getPathMatcher("glob:" + p))
                .toList();

        int deleted = 0;
        int failed = 0;

        try (Stream<Path> stream = Files.walk(worldDir)) {
            List<Path> targets = stream
                    .filter(Files::isRegularFile)
                    .filter(file -> shouldDelete(worldDir.relativize(file), deleteMatchers, whitelistMatchers))
                    .toList();

            for (Path file : targets) {
                try {
                    Files.deleteIfExists(file);
                    LOGGER.debug("Deleted: {}", worldDir.relativize(file));
                    deleted++;
                } catch (IOException e) {
                    LOGGER.warn("Failed to delete: {}", worldDir.relativize(file), e);
                    failed++;
                }
            }

            removeEmptyDirs(worldDir);

        } catch (IOException e) {
            LOGGER.error("Error during world cleanup", e);
        }

        LOGGER.info("Cleanup done. Deleted: {}, Failed: {}", deleted, failed);
    }

    private static boolean shouldDelete(Path relative, List<PathMatcher> deleteMatchers, List<PathMatcher> whitelistMatchers) {
        if (isSafetyExcluded(relative)) return false;

        for (PathMatcher m : whitelistMatchers) {
            if (m.matches(relative)) return false;
        }

        for (PathMatcher m : deleteMatchers) {
            if (m.matches(relative)) return true;
        }

        return false;
    }

    private static boolean isSafetyExcluded(Path relative) {
        String name = relative.getFileName().toString().toLowerCase();
        // Never delete region/chunk data files
        if (name.endsWith(".mca") || name.endsWith(".mcc")) return true;
        // Never delete anything inside playerdata
        for (int i = 0; i < relative.getNameCount(); i++) {
            if (relative.getName(i).toString().equals("playerdata")) return true;
        }
        return false;
    }

    private static void removeEmptyDirs(Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (dir.equals(root)) return FileVisitResult.CONTINUE;
                try (Stream<Path> entries = Files.list(dir)) {
                    if (entries.findAny().isEmpty()) {
                        Files.delete(dir);
                        LOGGER.debug("Removed empty directory: {}", root.relativize(dir));
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}