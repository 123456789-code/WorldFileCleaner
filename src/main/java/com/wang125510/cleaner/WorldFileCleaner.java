package com.wang125510.cleaner;

import com.wang125510.cleaner.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class WorldFileCleaner implements ModInitializer {
	public static final String MOD_ID = "world_file_cleaner";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ConfigManager configManager;

	public static ConfigManager getConfigManager() {
		return configManager;
	}

	@Override
	public void onInitialize() {
		configManager = new ConfigManager();
		LOGGER.info("WorldFileCleaner initialized");

		ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			ServerLifecycleEvents.SERVER_STARTING.register(this::onGameStopping);
		}

	}

	private void onServerStopping(MinecraftServer server) {
		if (configManager.getConfig().isWorldFileEnabled()) {
			Path worldDir = server.getWorldPath(LevelResource.ROOT);
			CleanerLogic.cleanWorld(worldDir, configManager.getConfig());
		}
	}

	private void onGameStopping(MinecraftServer server) {
		if (configManager.getConfig().isServerFileEnabled()) {
			Path serverFileDir = FabricLoader.getInstance().getGameDir();
			CleanerLogic.cleanServer(serverFileDir, configManager.getConfig());
		}
	}
}