package ru.kochkaev.api.seasons;

import net.fabricmc.api.ClientModInitializer;
import ru.kochkaev.api.seasons.integration.mod.ClothConfig;
import ru.kochkaev.api.seasons.integration.mod.ClothConfigClient;

public class SeasonsAPIFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SeasonsAPI.checkIntegrations();
		if (SeasonsAPI.getClothConfig()!=null) ClothConfig.setClient(new ClothConfigClient());
		SeasonsAPI.start();
//		ClientTickEvents.START_WORLD_TICK.register((ClientWorld world) -> {
//			if (Objects.equals(world.getDimensionEntry(), DimensionTypes.OVERWORLD)) {
//				SeasonsAPI.regEnvironment(this, false);
//				overworld = world;
//				SeasonsAPI.onWorldStarted();
//			}
//		});
//		ClientTickEvents.END_WORLD_TICK.register((world) -> {
//			if (Objects.equals(world.getDimensionEntry(), overworld)) {
//				overworld = null;
//				SeasonsAPI.onWorldShutdown();
//			}
//		});
	}
}