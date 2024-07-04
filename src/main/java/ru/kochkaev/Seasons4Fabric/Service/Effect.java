package ru.kochkaev.Seasons4Fabric.Service;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import ru.kochkaev.Seasons4Fabric.Config.Config;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;

import java.util.Arrays;
import java.util.List;

public class Effect {

//    FEELS_GOOD(new EffectObject("feelsGood", true, Arrays.asList())),
//    FLUFFY_COAT(new EffectObject("fluffyCoat", true, Arrays.asList())),
//    PRIMITIVE_HEATING(new EffectObject("primitiveHeating", true, Arrays.asList())),
//    REVITALIZED(new EffectObject("revitalized", true, Arrays.asList())),
//    WARMING_STEW(new EffectObject("warmingStew", true, Arrays.asList())),
//    WIND_IN_YOUR_BOOTS(new EffectObject("windInYourBoots", true, Arrays.asList())),
//    DEVASTATION(new EffectObject("devastation", false, Arrays.asList())),
//    FROSTBITE(new EffectObject("frostbite", false, Arrays.asList())),
//    HOLD_ONTO_YOUR_HAT(new EffectObject("holdOntoYourHat", false, Arrays.asList())),
//    HOT_SAND(new EffectObject("hotSand", false, Arrays.asList())),
//    ICY(new EffectObject("icy", false, Arrays.asList())),
//    SOLDERING_IRON(new EffectObject("solderingIron", false, Arrays.asList())),
//    STRONG_CURRENT(new EffectObject("strongCurrent", false, Arrays.asList())),
//    SWEATING(new EffectObject("sweating", false, Arrays.asList())),
//    THE_SHIVERS(new EffectObject("theShivers", false, Arrays.asList())),
//    WET_MUD(new EffectObject("wetMud", false, Arrays.asList()));

    private final EffectObject effect;

    Effect(EffectObject effect) {
        //RegistryKey<DamageType> WEATHER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("seasons4fabric:weather"));
        this.effect = effect;
    }

}
