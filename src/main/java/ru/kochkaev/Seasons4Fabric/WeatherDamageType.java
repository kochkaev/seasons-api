package ru.kochkaev.Seasons4Fabric;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class WeatherDamageType {

    public static final RegistryKey<DamageType> CUSTOM_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("seasons4fabric", "weather"));


    public static DamageSource of(ServerWorld world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

}
