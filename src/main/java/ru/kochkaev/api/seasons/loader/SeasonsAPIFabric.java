package ru.kochkaev.api.seasons.loader;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.WeatherCommand;
import ru.kochkaev.api.seasons.API.SeasonsAPIFabricEvents;
import ru.kochkaev.api.seasons.command.SeasonsCommand;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.mixin.WeatherCommandMixin;
import ru.kochkaev.api.seasons.provider.Config;

import java.nio.file.Path;

public class SeasonsAPIFabric extends Loader implements ModInitializer {

	@Override
	public void onInitialize() {
		SeasonsAPI.regLoader(this);
		SeasonsAPI.regEvents(new SeasonsAPIFabricEvents());
		ServerLifecycleEvents.SERVER_STARTED.register(SeasonsAPI::onWorldStarted);
		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> SeasonsAPI.onWorldShutdown());
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((a, b) -> Config.reloadAll());
	}

	@Override
	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> SeasonsCommand.register(dispatcher)));
//		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> WeatherCommandMixin.registerSeasons(dispatcher)));
	}

	@Override
	public Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir().toAbsolutePath();
	}
}