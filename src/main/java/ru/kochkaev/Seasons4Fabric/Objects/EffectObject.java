package ru.kochkaev.Seasons4Fabric.Objects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Service.Effect;
import ru.kochkaev.Seasons4Fabric.Service.Event;
import ru.kochkaev.Seasons4Fabric.Service.Weather;
import ru.kochkaev.Seasons4Fabric.Util.Message;
import ru.kochkaev.Seasons4Fabric.WeatherDamageType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * It's EffectObject, object for create your own effects.<br><br>
 * For create effect you must extend EffectObject.<br>
 * {@code public class YourClass extends EffectObject { ... }}
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
public abstract class EffectObject {

    /**
     * This message sends to chat on set available weather.
     * @see #sendMessage(ServerPlayerEntity, String)
     */
    protected String triggerMessage;
    /** weathers contains weathers, that available this effect. */
    protected List<Weather> weathers;

    /**
     * Effect registration.<br><br>
     * This method will be called during registration.
     * Method must contain effect data.<br>
     * <code>
     *     this.triggerMessage = "Your message";<br>
     *     this.weathers = Arrays.asList(Weather.FIRST_WEATHER, Weather.SECOND_WEATHER);<br>
     * </code>
     * or<br>
     * {@code this.weathers = Collections.singletonList(Weather.WEATHER);}<br><br>
     * This method must be realized in your effect. Use {@code @Override} annotation for this method.<br>
     * {@code public void register() { ... }}
     */
    public abstract void register();

    /**
     * Effect logic. <br><br>
     * This method will be called every {@code conf.tick.secondsPerTick} (from config.txt) seconds.
     * Method contains effect logic for one thing player.<br><br>
     * This method must be realized in your effect. Use {@code @Override} annotation for this method.<br>
     * {@code public void logic(ServerPlayerEntity player, int countOfInARowCalls) { ... }}
     * @param player player to whom the logic applies.
     * @param countOfInARowCalls count of fulfilled conditions in "logic" in a row.
     * @return new value for countOfInARowCalls
     */
    public abstract int logic(ServerPlayerEntity player, int countOfInARowCalls, int ticksPerAction);

    /**
     * You can use this method for damage player.
     * @param player player, who we will damage
     * @param amount (optional) amount of damage (hp) | default = 1.0f
     */
    protected void damage(ServerPlayerEntity player, float amount) {
        player.damage(WeatherDamageType.of(player.getServerWorld(), WeatherDamageType.WEATHER_DAMAGE_TYPE), amount);
    }
    /** See {@link  #damage(ServerPlayerEntity, float)} */
    protected void damage(ServerPlayerEntity player) { damage(player, 1.0f); }

    /**
     * You can use this method for give player effect.
     * @param player player, who we will give effect
     * @param effect effect, who we will give (you can use StatusEffects.EFFECT_NAME)
     * @param duration (optional) effect duration (seconds) | default = -1
     * @param amplifier (optional) effect level | default = 1
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
    protected void giveEffect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect) { giveEffect(player, effect, -1, 1, false); }

    protected void removeEffect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect)  {
        player.removeStatusEffect(effect);
    }

    /**
     * You can use this for call your method on event.<br>
     * {@code registerOnEventMethod("TARGET_EVENT_ID", this::yourMethod)  { ... }} <br><br>
     * You can create onEffect method for event in this way:<br>
     * {@code public void yourMethod(Object... args)  { ... }}
     * <p>For get arguments of event, you can use <br>{@code Object arg = (Object) args[0];} <br>Object may be int, String, etc.</p>
     * @param eventID id of target event.
     * @param method method, who we will call.
     * @return EventObject of target event.
     */
    protected EventObject registerOnEventMethod(String eventID, Consumer<Object> method) {
        EventObject event = Event.getEventByID(eventID);
        event.addMethod(method);
        return event;
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
     */
    protected void sendMessage(ServerPlayerEntity player, String message) { Message.sendMessage2Player(message, player); }

    /** This method check this effect available in current weather.
     * @return true, if {@link #weathers} contains current weather | false, if not.
     */
    public boolean isAllowed() { return weathers.contains(Weather.getCurrent()); }
    /**
     * @return {@link #triggerMessage}
     */
    public String getTriggerMessage() { return this.triggerMessage; }
    /** @return {@link #isGood}
     */
    public boolean isGood() { return this.isGood; }
    /** This method returns weathers, that available this effect.
     * @return {@link #weathers}
     */
    public List<Weather> getWeathers() { return this.weathers; }
}
