package de.pdinklag.mcstats;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Describes a MinecraftStats data source on the local file system.
 */
public class FileSystemDataSource implements DataSource {
    private static final String PLAYER_STORAGE_PATH_NAME = "players";
    private static final String ADVANCEMENTS_PATH_NAME = "advancements";
    private static final String STATS_PATH_NAME = "stats";

    private final Path serverPath;
    private final String worldName;

    /**
     * Constructs a data source.
     * @param serverPath
     * @param worldName
     */
    public FileSystemDataSource(Path serverPath, String worldName) {
        this.serverPath = serverPath;
        this.worldName = worldName;
    }

    @Override
    public Path getServerPath() {
        return serverPath;
    }

    private Path getWorldPath() {
        return serverPath.resolve(worldName);
    }

    private Path getPlayerStoragePath() {
        final Path worldPath = getWorldPath();
        final Path modernPlayerStoragePath = worldPath.resolve(PLAYER_STORAGE_PATH_NAME);

        // Minecraft Java 26.1 moved player storage from world/{stats,advancements,playerdata}
        // to world/players/{stats,advancements,data}. Prefer the new layout when it exists,
        // but keep the legacy layout as a fallback for older worlds and partially migrated copies.
        if (Files.isDirectory(modernPlayerStoragePath)) {
            return modernPlayerStoragePath;
        } else {
            return worldPath;
        }
    }

    @Override
    public Path getPlayerStatsPath() {
        return getPlayerStoragePath().resolve(STATS_PATH_NAME);
    }

    @Override
    public Path getPlayerAdvancementsPath() {
        return getPlayerStoragePath().resolve(ADVANCEMENTS_PATH_NAME);
    }
}
