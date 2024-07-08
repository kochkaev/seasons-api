package ru.kochkaev.Seasons4Fabric.util;

import net.minecraft.server.world.ServerWorld;
import ru.kochkaev.Seasons4Fabric.service.Weather;

public class WeatherUtils {

    //static WeatherUtils instance;

    //public WeatherUtils(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<SpecialSpawner> spawners, boolean shouldTickTime, @Nullable RandomSequencesState randomSequencesState) {
    //    super(server, workerExecutor, session, properties, worldKey, dimensionOptions, worldGenerationProgressListener, debugWorld, seed, spawners, shouldTickTime, randomSequencesState);
    //}

    public static void setWeather (Weather weather, ServerWorld world){
        int clearDuration = -1;
        int rainDuration = -1;
        boolean raining = weather.getRaining();
        boolean thundering = weather.getThundering();
        world.setWeather(clearDuration, rainDuration, raining, thundering);
    }

    //public static WeatherUtils getInstance() { return instance; }

}
