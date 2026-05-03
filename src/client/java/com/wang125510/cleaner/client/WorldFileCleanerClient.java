package com.wang125510.cleaner.client;

import com.wang125510.cleaner.CleanerLogic;
import com.wang125510.cleaner.WorldFileCleaner;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class WorldFileCleanerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientLifecycleEvents.CLIENT_STOPPING.register(this::onGameStopping);
	}

	private void onGameStopping(Minecraft minecraft) {
		if (WorldFileCleaner.getConfigManager().getConfig().isServerFileEnabled()) {
			Path serverFileDir = FabricLoader.getInstance().getGameDir();
			CleanerLogic.cleanServer(serverFileDir, WorldFileCleaner.getConfigManager().getConfig());
		}
	}
}