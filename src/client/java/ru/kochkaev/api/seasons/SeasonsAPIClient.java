package ru.kochkaev.api.seasons;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.dimension.DimensionTypes;
import ru.kochkaev.api.seasons.integration.Environment;
import ru.kochkaev.api.seasons.object.WeatherObject;

import java.util.Objects;

public class SeasonsAPIClient extends Environment implements ClientModInitializer {

	private static ClientWorld overworld = null;

	@Override
	public void onInitializeClient() {
		SeasonsAPI.start();
		ClientTickEvents.START_WORLD_TICK.register((ClientWorld world) -> {
//			if (Objects.equals(world.getDimensionEntry(), DimensionTypes.OVERWORLD)) {
//				SeasonsAPI.regEnvironment(this, false);
//				overworld = world;
//				SeasonsAPI.onWorldStarted();
//			}
		});
//		ClientTickEvents.END_WORLD_TICK.register((world) -> {
//			if (Objects.equals(world.getDimensionEntry(), overworld)) {
//				overworld = null;
//				SeasonsAPI.onWorldShutdown();
//			}
//		});
	}

	public World getOverworld() {
		return overworld;
	}

	public void setWeather(WeatherObject weather){
		ClientWorld.Properties prop = overworld.getLevelProperties();
		prop.setRaining(weather.getRaining());
	}

	public <T extends ParticleEffect> void spawnParticles(T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
		for (int i = 0; i < count; i++) overworld.addParticle(particle, x, y, z, speed, speed, speed);
	}
}