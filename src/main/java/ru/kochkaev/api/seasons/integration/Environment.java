package ru.kochkaev.api.seasons.integration;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import ru.kochkaev.api.seasons.object.WeatherObject;

public abstract class Environment {

    public abstract World getOverworld();
    public abstract void setWeather(WeatherObject weather);
    public abstract <T extends ParticleEffect> void spawnParticles(T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed);

}
