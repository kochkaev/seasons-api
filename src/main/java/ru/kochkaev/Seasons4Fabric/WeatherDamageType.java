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

    public static final RegistryKey<DamageType> WEATHER_COLDS_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("seasons4fabric", "weather_colds"));
    public static final RegistryKey<DamageType> WEATHER_HOTS_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("seasons4fabric", "weather_hots"));
    public static final RegistryKey<DamageType> WEATHER_STORMY_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("seasons4fabric", "weather_stormy"));
//    public static ServerWorld world;
//    public static final DamageSource WEATHER_DAMAGE_SOURCE = new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(WEATHER_DAMAGE_TYPE));


    public static DamageSource of(ServerWorld world, RegistryKey<DamageType> key) {
//        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).createEntry(new DamageType("death.attack.weather", 0.1f, DamageEffects.FREEZING)));
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

}
