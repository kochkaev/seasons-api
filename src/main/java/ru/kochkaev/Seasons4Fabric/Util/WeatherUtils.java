package ru.kochkaev.Seasons4Fabric.Util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.SpecialSpawner;
import org.jetbrains.annotations.Nullable;
import ru.kochkaev.Seasons4Fabric.Service.Weather;

import java.util.List;
import java.util.concurrent.Executor;

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
