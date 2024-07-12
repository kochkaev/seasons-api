package ru.kochkaev.Seasons4Fabric.object;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.IFunc;
import ru.kochkaev.Seasons4Fabric.IFuncRet;
import ru.kochkaev.Seasons4Fabric.Main;
import ru.kochkaev.Seasons4Fabric.mixin.LivingEntityMixin;
import ru.kochkaev.Seasons4Fabric.service.Event;
import ru.kochkaev.Seasons4Fabric.service.Task;
import ru.kochkaev.Seasons4Fabric.service.Weather;
import ru.kochkaev.Seasons4Fabric.util.Message;
import ru.kochkaev.Seasons4Fabric.WeatherDamageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
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

    /**
     * This message sends to chat on set available weather.
     * @see #sendMessage(ServerPlayerEntity, String)
     */
    protected String triggerMessage;
    /** weathers contains weathers, that available this challenge. */
    protected List<Weather> weathers;

    /**
     * Challenge registration.<br><br>
     * This method will be called during registration.
     * Method must contain challenge data.<br>
     * <code>
     *     this.triggerMessage = "Your message";<br>
     *     this.weathers = Arrays.asList(Weather.FIRST_WEATHER, Weather.SECOND_WEATHER);<br>
     * </code>
     * or<br>
     * {@code this.weathers = Collections.singletonList(Weather.WEATHER);}<br><br>
     * This method must be realized in your challenge. Use {@code @Override} annotation for this method.<br>
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
        Main.getLogger().info(String.valueOf(player.removeStatusEffect(effect)));
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
    protected EventObject registerOnEventMethod(String eventID, IFunc method) {
        EventObject event = Event.getEventByID(eventID);
        event.addMethod(method);
        return event;
    }

    /**
     * You can use this method for give player freezing effect (blue hurts and snowflakes on the screen).<br>
     * @param player player, who we will give effect.
     * @return task method.
     */
    protected IFuncRet giveFrozen(ServerPlayerEntity player) {
        IFuncRet task1 = (args) -> {
            ServerPlayerEntity playr = (ServerPlayerEntity) args.getFirst();
            playr.setFrozenTicks(140);
            return args;
        };
        IFuncRet task = (args) -> {
            int count = (int) args.getFirst();
            ServerPlayerEntity playr = (ServerPlayerEntity) args.get(1);
            IFuncRet tsk = (IFuncRet) args.get(2);
            IFuncRet tsk1 = (IFuncRet) args.get(3);
            playr.setFrozenTicks(count);
            if (count < 140) return Arrays.asList(count+1, playr, tsk, tsk1);
            Task.removeTask(tsk);
            Task.addTask(tsk1, Collections.singletonList(playr));
            return new ArrayList<>();
        };
        Task.addTask(task, Arrays.asList(1, player, task, task1));
        return task1;
    }
    /**
     * You can use this method for remove player freeze effect.<br>
     * @param task task, who we will remove.
     */
    protected void removeFrozen(IFuncRet task) {
        Task.removeTask(task);
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

    /** This method check this challenge available in current weather.
     * @return true, if {@link #weathers} contains current weather | false, if not.
     */
    public boolean isAllowed() { return weathers.contains(Weather.getCurrent()); }
    /**
     * @return {@link #triggerMessage}
     */
    public String getTriggerMessage() { return this.triggerMessage; }
    /** This method returns weathers, that available this challenge.
     * @return {@link #weathers}
     */
    public List<Weather> getWeathers() { return this.weathers; }
}
