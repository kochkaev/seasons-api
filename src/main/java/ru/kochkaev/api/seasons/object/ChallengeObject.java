package ru.kochkaev.api.seasons.object;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import ru.kochkaev.api.seasons.ChallengesTicker;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.WeatherDamageType;
import ru.kochkaev.api.seasons.provider.Task;
import ru.kochkaev.api.seasons.provider.Weather;
import ru.kochkaev.api.seasons.util.Message;

import java.util.*;
import java.util.function.Function;

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
    protected ArrayList<WeatherObject> weathers = new ArrayList<>();
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
     *     ChallengeObject yourChallenge = new ChallengeObject("Your message", Arrays.asList(Weather.FIRST_WEATHER, Weather.SECOND_WEATHER), false) { public void register() { ... } public void logic(PlayerEntity player, int countOfInARowCalls, int ticksPerAction) { ... } public void challengeEnd(PlayerEntity player) { ... } };<br>
     * </code>
     * @param weathers {@link #weathers}
     * @param allowIfPrevious {@link #allowIfPrevious}
     */
    public ChallengeObject(String id, List<WeatherObject> weathers, boolean allowIfPrevious) {
        this.id = id;
        this.weathers.addAll(weathers);
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
     * {@code public void logic(PlayerEntity player, int countOfInARowCalls) { ... }}
     * @param player player to whom the logic applies.
     * @param countOfInARowCalls count of fulfilled conditions in "logic" in a row.
     * @return new value for countOfInARowCalls
     */
    public abstract int logic(PlayerEntity player, int countOfInARowCalls, int ticksPerAction);

    /**
     * Challenge start event.<br><br>
     * This method will be called on this challenge starts.
     * You must realize this method in your challenge. Use {@code @Override} annotation for this method.<br>
     * @param player player to whom the logic applies.
     */
    public abstract void onChallengeStart(PlayerEntity player);
    /**
     * Challenge end.<br><br>
     * This method will be called on weather change and if {@link #weathers} don't contain this weather (only if {@link #allowIfPrevious} == true), on condition countOfInARowCalls != 0.
     * You must realize this method in your challenge. Use {@code @Override} annotation for this method.<br>
     * @param player player to whom the logic applies.
     */
    public abstract void onChallengeEnd(PlayerEntity player);

    /**
     * You can use this method for damage player.
     * @param player player, who we will damage
     * @param amount (optional) amount of damage (hp) | default = 1.0f
     * @param key (optional) damage type
     */
    protected void damage(PlayerEntity player, float amount, RegistryKey<DamageType> key) {
        DamageSource type = WeatherDamageType.of(player.getWorld(), key);
        player.damage(Objects.requireNonNull(player.getServer()).getWorld(player.getWorld().getRegistryKey()), type, amount);
    }
    /** See {@link  #damage(PlayerEntity, float, RegistryKey)} */
    protected void damage(PlayerEntity player, RegistryKey<DamageType> key) { damage(player, 1.0f, key); }

    /** See {@link  #damage(PlayerEntity, float, RegistryKey)} */
    protected void damageCold(PlayerEntity player) { damage(player, 1.0f, WeatherDamageType.WEATHER_COLDS_DAMAGE_TYPE); }
    /** See {@link  #damage(PlayerEntity, float, RegistryKey)} */
    protected void damageHot(PlayerEntity player) { damage(player, 1.0f, WeatherDamageType.WEATHER_HOTS_DAMAGE_TYPE); }
    /** See {@link  #damage(PlayerEntity, float, RegistryKey)} */
    protected void damageStorm(PlayerEntity player) { damage(player, 1.0f, WeatherDamageType.WEATHER_STORMY_DAMAGE_TYPE); }

    /**
     * You can use this method for give player effect.
     * @param player player, who we will give effect
     * @param effect effect, who we will give (you can use StatusEffects.EFFECT_NAME)
     * @param duration (optional) effect duration (seconds) | default = -1
     * @param amplifier (optional) effect level | default = 0
     * @param hideParticles (optional) hide effect particles | default = false
     */
    protected void giveEffect(PlayerEntity player, RegistryEntry<StatusEffect> effect, int duration, int amplifier, boolean hideParticles) {
        player.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, hideParticles, hideParticles));
    }
    /** See {@link  #giveEffect(PlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void giveEffect(PlayerEntity player, RegistryEntry<StatusEffect> effect, int duration, int amplifier) { giveEffect(player, effect, duration, amplifier, false); }
    /** See {@link  #giveEffect(PlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void giveEffect(PlayerEntity player, RegistryEntry<StatusEffect> effect, int amplifier) { giveEffect(player, effect, -1, amplifier, false); }
    /** See {@link  #giveEffect(PlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void giveEffect(PlayerEntity player, RegistryEntry<StatusEffect> effect) { giveEffect(player, effect, -1, 0, false); }
    /**
     * You can use this method for remove player effect.
     * @param player player, who we will remove effect
     * @param effect effect, who we will remove (you can use StatusEffects.EFFECT_NAME)
     */
    protected void removeEffect(PlayerEntity player, RegistryEntry<StatusEffect> effect)  {
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
     */
    protected void giveFrozen(PlayerEntity player) {
        Function<List<?>, List<?>> task = (args) -> {
            int count = (int) args.getFirst();
            PlayerEntity playr = (PlayerEntity) args.get(1);
            playr.setFrozenTicks(count);
            if (count < 140) return Arrays.asList(count+1, playr);
            return args;
        };
        Task.addTask(getTaskKey(player, "frozen"), task, Arrays.asList(1, player));
    }
    /**
     * You can use this method for remove player freeze effect.<br>
     * @param player player for effect removing.
     */
    protected void removeFrozen(PlayerEntity player) {
        Task.removeTask(getTaskKey(player, "frozen"));
    }
    protected String getTaskKey(PlayerEntity player, String taskName) {
        return player.getName()+"-"+id+"-"+taskName+"-Task";
    }

    /**
     * You can use this method for spawn particles.<br>
     * @param player player for whom will spawn particles.
     * @param particles spawn particle type.
     */
    protected void spawnParticles(PlayerEntity player, ParticleEffect particles, boolean falling, double offsetY, int count) {
        SeasonsAPI.getOverworld().spawnParticles(particles, player.getX(), player.getY()+offsetY, player.getZ(), count, 0, falling ? -1 : 1, 0, 0.1);
    }


    @Deprecated
    protected void sendMessage(PlayerEntity player, String message, Map<String, String> placeholders) {
        Message.sendMessage2Player(message, player, placeholders);
    }
    /**
     * You can use this method for send message to server players.
     * @see #sendMessage(PlayerEntity, Text)
     * @param player player who we will send message.
     * @param message message for send.
     * @param placeholders Map of placeholders.
     */
    protected void sendMessage(PlayerEntity player, Text message, Map<String, Text> placeholders) {
        Message.sendMessage2Player(message, player, placeholders);
    }
    @Deprecated
    protected void sendMessage(PlayerEntity player, String message) {
        Message.sendMessage2PlayerDefaultPlaceholders(message, player);
    }
    /**
     * You can use this method for send a message to player chat.<br><br>
     *
     * You also can use placeholders:<br>
     *     - {@code &} for colours (will replace to paragraph symbol).<br>
     *     - {@code %seasons:season%} for get current season name from lang config.<br>
     *     - {@code %seasons:weather%} for get current weather name from lang config.<br>
     *     - And others, if you have PlaceholderAPI.<br>
     * @param player player, who we will send message
     * @param message message, who we will send
     * @see #sendMessage(PlayerEntity, Text, Map)
     */
    protected void sendMessage(PlayerEntity player, Text message) {
        Message.sendMessage2Player(message, player);
    }

    /** This method check this challenge available in current weather (or if {@link #allowIfPrevious} == true and {@link #weathers} contains previous weather).
     * @return true, if {@link #weathers} contains current weather or {@link #allowIfPrevious} == true and {@link #weathers} contains previous weather | false, if not.
     */
    public boolean isAllowed() {
        return (this.weathers.contains(Weather.getCurrent()) || (this.allowIfPrevious && this.weathers.contains(Weather.getPreviousCurrent()))) && this.enabled;
    }
    public boolean isAllowed(WeatherObject weather) {
        return (this.weathers.contains(weather) || (this.allowIfPrevious && this.weathers.contains(Weather.getPreviousCurrent()))) && this.enabled;
    }

    /** This method check this challenge allowed in ChallengesTicker.
     * @return true, if allowedChallenges list in ChallengesTicker contains current challenge | false, if not.
     */
    public boolean isAllowedInTicker() {
        return ChallengesTicker.isChallengeAllowed(this);
    }
    /** This method returns weathers, that available this challenge.
     * @return {@link #weathers}
     */
    public ArrayList<WeatherObject> getWeathers() { return this.weathers; }
    public void addWeather(WeatherObject weather) {
        this.weathers.add(weather);
    }
    public void removeWeather(WeatherObject weather) {
        this.weathers.remove(weather);
    }

    /** This method returns challenge id.
     * @return {@link #id}
     */
    public String getID() { return this.id; }

    public boolean isEnabled() { return this.enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

}
