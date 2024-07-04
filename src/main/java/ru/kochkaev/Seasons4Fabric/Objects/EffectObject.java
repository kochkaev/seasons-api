package ru.kochkaev.Seasons4Fabric.Objects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.Service.Weather;
import ru.kochkaev.Seasons4Fabric.Util.Message;
import ru.kochkaev.Seasons4Fabric.WeatherDamageType;

import java.util.List;

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
 * </p>
 */
public abstract class EffectObject {

    /**
     * This message sends to chat on set available weather.
     * @see #sendMessage(ServerPlayerEntity, String)
     */
    protected String triggerMessage;
    /** isGood true if effect gives a buff | false if effect gives a debuff. */
    protected boolean isGood;
    /** weathers contains weathers, that available this effect. */
    protected List<Weather> weathers;

    /**
     * Effect registration.<br><br>
     * This method will be called during registration.
     * Method must contain effect data.<br>
     * <code>
     *     this.triggerMessage = "Your message";<br>
     *     this.isGood = true;<br>
     *     this.weathers = Arrays.asList(Weather.FIRST_WEATHER, Weather.SECOND_WEATHER);<br>
     * </code>
     * or<br>
     * {@code this.weathers = Collections.singletonList(Weather.WEATHER);}<br><br>
     * This method must be realized in your effect.<br>
     * {@code public void register() { ... }}
     */
    public abstract void register();

    /**
     * Effect logic. <br><br>
     * This method will be called every {@code conf.tick.secondsPerTick} (from config.txt) seconds.
     * Method contains effect logic for one thing player.<br><br>
     * This method must be realized in your effect.<br>
     * {@code public void logic(ServerPlayerEntity player, int countOfInARowCalls) { ... }}
     */
    public abstract void logic(ServerPlayerEntity player, int countOfInARowCalls);

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
    protected void effect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect, int duration, int amplifier, boolean hideParticles) {
        player.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, hideParticles, hideParticles));
    }
    /** See {@link  #effect(ServerPlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void effect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect, int duration, int amplifier) { effect(player, effect, duration, amplifier, false); }
    /** See {@link  #effect(ServerPlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void effect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect, int amplifier) { effect(player, effect, -1, amplifier, false); }
    /** See {@link  #effect(ServerPlayerEntity, RegistryEntry, int, int, boolean)} */
    protected void effect(ServerPlayerEntity player, RegistryEntry<StatusEffect> effect) { effect(player, effect, -1, 1, false); }

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

    //protected void registerOnSteppedOn(Method method) {  }

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
