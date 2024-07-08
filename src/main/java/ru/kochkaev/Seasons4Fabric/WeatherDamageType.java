package ru.kochkaev.Seasons4Fabric;

import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class WeatherDamageType {

    public static final RegistryKey<DamageType> WEATHER_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("seasons4fabric", "weather"));


    public static DamageSource of(ServerWorld world, RegistryKey<DamageType> key) {
        DamageSource source = new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).createEntry(new DamageType("seasons4fabric:deathMessage.weather", 0.1f, DamageEffects.FREEZING)));
        return source;
    }

}
