package ru.kochkaev.api.seasons.object;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.api.seasons.service.Season;
import ru.kochkaev.api.seasons.util.functional.IFunc;
import ru.kochkaev.api.seasons.util.functional.IFuncRet;
import ru.kochkaev.api.seasons.service.Task;
import ru.kochkaev.api.seasons.service.Weather;
import ru.kochkaev.api.seasons.util.Message;
import ru.kochkaev.api.seasons.WeatherDamageType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It's ChallengeObject, object for create your own challenges.<br><br>
 * For create challenge you must extend ChallengeObject.<br>
 * {@code public class YourClass extends ChallengeObject { ... }}
 *
 * @version 1.0
 * @author Dmitrii Kochkaev
 * <p>
 *     <a href="https://t.me/kleverdi">Telegram channel</a>
 *     <a href="https://youtube.com/@kochkaev">YouTube channel</a>
 *     <a href="https://vk.com/kleverdi">VK</a>
 *     <a href="https://github.com/kochkaev">GitHub</a>
 *     <a href="https://gitverse.ru/kochkaev">GitVerse</a>
 * </p>
 */
public abstract class ChallengeObject {

    /** weathers contains weathers, that available this challenge. */
    protected List<WeatherObject> weathers;
    /** Set true if this challenge is available if {@link #weathers} contains previous weather (night is also the weather). */
    protected boolean allowIfPrevious;

    /** It's challenge enabled. */
    protected boolean enabled = true;

    /** ID of this challenge. */
    protected String id;


    /**
     * That's constructor.<br><br>
     * Use this for set challenge data (call from your class constructor).<br>
     * <code>
     *     public YourClass() { super("Your message", Arrays.asList(Weather.FIRST_WEATHER, Weather.SECOND_WEATHER), false); }<br>
     * </code>
     * You can also create anonymous class for create challenge.<br>
     * <code>
     *     ChallengeObject yourChallenge = new ChallengeObject("Your message", Arrays.asList(Weather.FIRST_WEATHER, Weather.SECOND_WEATHER), false) { public void register() { ... } public void logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction) { ... } public void challengeEnd(ServerPlayerEntity player) { ... } };<br>
     * </code>
     * @param weathers {@link #weathers}
     * @param allowIfPrevious {@link #allowIfPrevious}
     */
    public ChallengeObject(String id, List<WeatherObject> weathers, boolean allowIfPrevious) {
        this.id = id;
        this.weathers = weathers;
        this.allowIfPrevious = allowIfPrevious;
    }

    /**
     * Challenge registration.<br><br>
     * This method will be called during registration.
     * You must realize this method in your challenge. Use {@code @Override} annotation for this method.<br>
     * {@code public void register() { ... }}
     */
    public abstract void register();

    /**
     * Challenge logic. <br><br>
     * This method will be called every {@code conf.tick.secondsPerTick} (from config.txt) seconds.
     * Method contains challenge logic for one thing player.<br><br>
     * This method must be realized in your challenge. Use {@code @Override} annotation for this method.<br>
     * {@code public void logic(ServerPlayerEntity player, int countOfInARowCalls) { ... }}
     * @param player player to whom the logic applies.
     * @param countOfInARowCalls count of fulfilled conditions in "logic" in a row.
     * @return new value for countOfInARowCalls
     */
    public abstract int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction);

    /**
     * Challenge start event.<br><br>
     * This method will be called on this challenge starts.
     * You must realize this method in your challenge. Use {@code @Override} annotation for this method.<br>
     * @param player player to whom the logic applies.
     */
    public abstract void onChallengeStart(ServerPlayerEntity player);
    /**
     * Challenge end.<br><br>
     * This method will be called on weather change and if {@link #weathers} don't contain this weather (only if {@link #allowIfPrevious} == true), on condition countOfInARowCalls != 0.
     * You must realize this method in your challenge. Use {@code @Override} annotation for this method.<br>
     * @param player player to whom the logic applies.
     */
    public abstract void onChallengeEnd(ServerPlayerEntity player);

    /**
     * You can use this method for damage player.
     * @param player player, who we will damage
     * @param amount (optional) amount of damage (hp) | default = 1.0f
     * @param key (optional) damage type
     */
    protected void damage(ServerPlayerEntity player, float amount, RegistryKey<DamageType> key) {
        DamageSource type = WeatherDamageType.of(player.getServerWorld(), key);
        player.damage(type, amount);
    }
    /** See {@link  #damage(ServerPlayerEntity, float, RegistryKey)} */
    protected void damage(ServerPlayerEntity player, RegistryKey<DamageType> key) { damage(player, 1.0f, key); }

    /** See {@link  #damage(ServerPlayerEntity, float, RegistryKey)} */
    protected void damageCold(ServerPlayerEntity player) { damage(player, 1.0f, WeatherDamageType.WEATHER_COLDS_DAMAGE_TYPE); }
    /** See {@link  #damage(ServerPlayerEntity, float, RegistryKey)} */
    protected void damageHot(ServerPlayerEntity player) { damage(player, 1.0f, WeatherDamageType.WEATHER_HOTS_DAMAGE_TYPE); }
    /** See {@link  #damage(ServerPlayerEntity, float, RegistryKey)} */
    protected void damageStorm(ServerPlayerEntity player) { damage(player, 1.0f, WeatherDamageType.WEATHER_STORMY_DAMAGE_TYPE); }

    /**
     * You can use this method for give player effect.
     * @param player player, who we will give effect
     * @param effect effect, who we will give (you can use StatusEffects.EFFECT_NAME)
     * @param duration (optional) effect duration (seconds) | default = -1
     * @param amplifier (optional) effect level | default = 0
     * @param hideParticles (optional) hide effect particles | default = false
     */
    protected void giveEffect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect, int duration, int amplifier, boolean hideParticles) {
        player.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, hideParticles, hideParticles));
    }
    /** See {@link  #giveEffect(ServerPlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void giveEffect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect, int duration, int amplifier) { giveEffect(player, effect, duration, amplifier, false); }
    /** See {@link  #giveEffect(ServerPlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void giveEffect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect, int amplifier) { giveEffect(player, effect, -1, amplifier, false); }
    /** See {@link  #giveEffect(ServerPlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void giveEffect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect) { giveEffect(player, effect, -1, 0, false); }
    /**
     * You can use this method for remove player effect.
     * @param player player, who we will remove effect
     * @param effect effect, who we will remove (you can use StatusEffects.EFFECT_NAME)
     */
    protected void removeEffect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect)  {
        player.removeStatusEffect(effect);
    }

//    /**
//     * You can use this for call your method on event.<br>
//     * {@code registerOnEventMethod("TARGET_EVENT_ID", this::yourMethod)  { ... }} <br><br>
//     * You can create onEffect method for event in this way:<br>
//     * {@code public void yourMethod(Object... args)  { ... }}
//     * <p>For get arguments of event, you can use <br>{@code Object arg = (Object) args[0];} <br>Object may be int, String, etc.</p>
//     * @param eventID id of target event.
//     * @param method method, who we will call.
//     * @return EventObject of target event.
//     */
//    protected EventObject registerOnEventMethod(String eventID, IFunc method) {
//        EventObject event = Event.getEventByID(eventID);
//        event.addMethod(method);
//        return event;
//    }

    /**
     * You can use this method for give player freezing effect (blue hurts and snowflakes on the screen).<br>
     * @param player player, who we will give effect.
     * @return task method.
     */
    protected IFuncRet giveFrozen(ServerPlayerEntity player) {
        IFuncRet task = (args) -> {
            int count = (int) args.getFirst();
            ServerPlayerEntity playr = (ServerPlayerEntity) args.get(1);
            playr.setFrozenTicks(count);
            if (count < 140) return Arrays.asList(count+1, playr);
            return args;
        };
        Task.addTask(task, Arrays.asList(1, player));
        return task;
    }
    /**
     * You can use this method for remove player freeze effect.<br>
     * @param task task, who we will remove.
     */
    protected void removeFrozen(IFuncRet task) {
        Task.removeTask(task);
    }

    /**
     * You can use this method for spawn particles.<br>
     * @param player player for whom will spawn particles.
     * @param particles spawn particle type.
     */
    protected void spawnParticles(ServerPlayerEntity player, ParticleEffect particles, boolean falling, double offsetY, int count) {
        player.getServerWorld().spawnParticles(particles, player.getX(), player.getY()+offsetY, player.getZ(), count, 0, falling ? -1 : 1, 0, 0.1);
    }


    /**
     * You can use this method for send message to server players.
     * @param player player who we will send message.
     * @param message message for send.
     * @param placeholders Map of placeholders.
     */
    protected void sendMessage(ServerPlayerEntity player, String message, Map<String, String> placeholders) {
        Message.sendMessage2Player(message, player, placeholders);
    }
    /**
     * You can use this method for send a message to player chat.<br><br>
     *
     * You also can use placeholders:<br>
     *     - {@code &} for colours (will replace to paragraph symbol).<br>
     *     - {@code %season%} for get current season name from lang config.<br>
     *     - {@code %weather%} for get current weather name from lang config.<br>
     * @param player player, who we will send message
     * @param message message, who we will send
     * @see #sendMessage(ServerPlayerEntity, String, Map)
     */
    protected void sendMessage(ServerPlayerEntity player, String message) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%season%", Season.getCurrent().getName());
        placeholders.put("%weather%", Weather.getCurrent().getName());
        sendMessage(player, message, placeholders);
    }

    /** This method check this challenge available in current weather (or if {@link #allowIfPrevious} == true and {@link #weathers} contains previous weather).
     * @return true, if {@link #weathers} contains current weather or {@link #allowIfPrevious} == true and {@link #weathers} contains previous weather | false, if not.
     */
    public boolean isAllowed() {
        return (this.weathers.contains(Weather.getCurrent()) || (this.allowIfPrevious && this.weathers.contains(Weather.getPreviousCurrent()))) && this.enabled;
    }
    /** This method returns weathers, that available this challenge.
     * @return {@link #weathers}
     */
    public List<WeatherObject> getWeathers() { return this.weathers; }

    /** This method returns challenge id.
     * @return {@link #id}
     */
    public String getID() { return this.id; }

    public boolean isEnabled() { return this.enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

}
