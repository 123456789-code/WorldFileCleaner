package com.wang125510.cleaner.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wang125510.cleaner.WorldFileCleaner;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final String MOD_ID = WorldFileCleaner.MOD_ID;
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "autoclean.json";

    private final Path configPath;
    private CleanerConfig config;

    public ConfigManager() {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);
        load();
    }

    private void load() {
        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                config = GSON.fromJson(json, CleanerConfig.class);
                if (config == null) {
                    config = new CleanerConfig();
                }
            } catch (IOException e) {
                LOGGER.error("Failed to load config, using defaults", e);
                config = new CleanerConfig();
            }
        } else {
            config = new CleanerConfig();
            save();
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, GSON.toJson(config));
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    public CleanerConfig getConfig() {
        return config;
    }
}