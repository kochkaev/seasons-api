package ru.kochkaev.api.seasons;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.api.seasons.command.SeasonsCommand;
import ru.kochkaev.api.seasons.integration.Environment;

import java.nio.file.Path;

public class SeasonsAPIFabric extends Environment implements ModInitializer {

	@Override
	public void onInitialize() {
		SeasonsAPI.regEnvironment(this);
		ServerLifecycleEvents.SERVER_STARTED.register(SeasonsAPI::onWorldStarted);
		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> SeasonsAPI.onWorldShutdown());
	}

	@Override
	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> SeasonsCommand.register(dispatcher)));
	}

	@Override
	public Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir().toAbsolutePath();
	}
}