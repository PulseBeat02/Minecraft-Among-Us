package com.github.ponclure.blockus.config;

import com.github.ponclure.blockus.BlockUs;
import com.github.ponclure.blockus.BlockUsPlugin;
import com.github.ponclure.blockus.arena.ArenaManager;
import com.github.ponclure.blockus.game.GameSettings;
import com.github.ponclure.blockus.utility.Namespace;
import com.github.ponclure.blockus.utility.container.AABB;
import com.github.ponclure.blockus.utility.container.Vec3;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class ConfigManager {

    private static final Pattern PATTERN = Pattern.compile("\\w+\\.yml$", Pattern.CASE_INSENSITIVE);

    private final BlockUs au = BlockUsPlugin.getBlockUs();
    private final File dataFolder = au.dataFolder();
    private final ConfigFactory configFactory = au.configFactory();
    private final Config defaultConfig;

    public ConfigManager() {
        defaultConfig = configFactory.supplyConfig(dataFolder, "config", ConfigType.YML);
    }

    public void load() {
        Logger logger = au.logger();
        defaultConfig.loadAsync(cfg -> {
        }, exc -> logger.info("Couldn't load config: " + exc.toString()));
        loadArenas();
    }

    private void loadArenas() {
        ArenaManager manager = au.arenaManager();
        for (File file : dataFolder.listFiles()) {
            if (file.isDirectory()) {
                continue;
            }
            if (!file.getName().endsWith(ConfigType.YML.getSuffix())) {
                continue;
            }
            Config config;
            String name = PATTERN.matcher(file.getName()).replaceFirst("");
            config = configFactory.supplyConfig(dataFolder + "/arenas/", name, ConfigType.YML);
            config.loadAsync(cfg -> {
                Namespace namespace = Namespace.of(name);
                String displayName = cfg.getString("DisplayName");
                GameSettings defaultSettings = getSettings(cfg);
                UUID worldUuid = Bukkit.getWorld(cfg.getString("World")).getUID();
                Vec3 min = getVector(config, "Arena.Min.");
                Vec3 max = getVector(config, "Arena.Max.");
                Vec3 lobbySpawn = getVector(config, "Spawn.Game.");
                Vec3 gameSpawn = getVector(config, "Spawn.Lobby.");
                manager.loadArena(
                        namespace,
                        displayName,
                        defaultSettings,
                        new AABB(min, max),
                        worldUuid,
                        lobbySpawn,
                        gameSpawn
                );
            });
        }
    }

    private Vec3 getVector(Config config, String basePath) {
        try {
            return Vec3.deserialize((byte[]) config.get(basePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GameSettings getSettings(Config config) {
        String path = "GameSettings.";
        return new GameSettings.Builder()
                .setCommonTaskCount(config.getInt(path + "CommonTaskCount"))
                .setConfirmingEjections(config.getBoolean(path + "ConfirmingEjections"))
                .setCrewmateVision(config.getDouble(path + "CrewmateVision"))
                .build();
    }

    public Config getDefaultConfig() {
        return defaultConfig;
    }
}
