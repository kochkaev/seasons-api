package ru.kochkaev.api.seasons.integration;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import ru.kochkaev.api.seasons.object.WeatherObject;

import java.nio.file.Path;

public abstract class Environment {

//    public abstract ServerWorld getOverworld();
//    public abstract MinecraftServer getServer();
    public abstract void registerCommands();
    public abstract Path getConfigPath();

}
