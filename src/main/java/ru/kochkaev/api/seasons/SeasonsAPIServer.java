package ru.kochkaev.api.seasons;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import ru.kochkaev.api.seasons.integration.Environment;
import ru.kochkaev.api.seasons.object.WeatherObject;

public class SeasonsAPIServer extends Environment implements ModInitializer {
	private static MinecraftServer server;
	private static ServerWorld overworld;

	@Override
	public void onInitialize() {
		SeasonsAPI.start();
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			SeasonsAPI.regEnvironment(this, true);
			SeasonsAPIServer.server = server;
			SeasonsAPIServer.overworld = server.getOverworld();
			SeasonsAPI.onWorldStarted();
		});
		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> SeasonsAPI.onWorldShutdown());
	}

	public static MinecraftServer getServer() {
		return server;
	}

	@Override
	public World getOverworld() {
		return overworld;
	}

	@Override
	public void setWeather(WeatherObject weather) {
		overworld.setWeather(-1, -1, weather.getRaining(), weather.getThundering());
	}

	@Override
	public <T extends ParticleEffect> int spawnParticles(T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
		return overworld.spawnParticles(particle, x, y, z, count, deltaX, deltaY, deltaZ, speed);
	}
}