package ru.kochkaev.Seasons4Fabric.Service;

import ru.kochkaev.Seasons4Fabric.EffectsTicker;
import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;

import java.util.ArrayList;
import java.util.List;

public class Effect {

//    FEELS_GOOD(new EffectObject("feelsGood", true, Arrays.asList())), +
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

    private static final List<EffectObject> effects = new ArrayList<>();

    private static final List<EffectObject> effectsInCurrentWeather = new ArrayList<>();

    private static List<EffectObject> onConsumeEffects;

    public static void register(EffectObject effect) {
        effect.register();
        effects.add(effect);
        if (EffectsTicker.isTicking()) EffectsTicker.addEffect(effect);
    }

    public static List<EffectObject> getEffects() { return effects; }

    public static List<EffectObject> getEffectsInCurrentWeather() { return effectsInCurrentWeather; }
    public static void updateEffectsInCurrentWeather() {
        effectsInCurrentWeather.clear();
        for (EffectObject effect : effects) if (effect.isAllowed()) effectsInCurrentWeather.add(effect);
    }


    public static void registerOnConsume(EffectObject effect) {
        onConsumeEffects.add(effect);
    }

    public static List<EffectObject> getOnConsumeEffects() {
        return onConsumeEffects;
    }

}
