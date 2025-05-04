package ru.kochkaev.api.seasons.object;


import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import ru.kochkaev.api.seasons.provider.Weather;
import ru.kochkaev.api.seasons.util.Format;
import ru.kochkaev.api.seasons.util.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * It's WeatherObject, object for create your own weather.<br><br>
 * For create weather you must extend WeatherObject.<br>
 * {@code public class YourClass extends WeatherObject { ... }}
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
public abstract class WeatherObject {

    /** Weather ID, must be unique. */
    protected String id;
    /**
     * Display weather name, sends to chat, title, etc. <br><br>
     * Name can be dynamic... <br>
     * {@code () -> Config.getModConfig("YourModConfigID").getLang().getString("yourDynamicNameID")} <br>
     * ...and static! <br>
     * {@code () -> "Your static name"}<br><br>
     * Lambda expression for get name is String Supplier.
     */
    protected Supplier<Text> name;
    /** Weather property, is it raining in this weather?
     * Can be null (if it shouldn't overwrite previous weather) */
    @Nullable
    protected Boolean raining;
    /** Weather property, is it thundering in this weather?
     *  Can be null (if it shouldn't overwrite previous weather) */
    @Nullable
    protected Boolean thundering;
    /** Chance of this weather coming (in percents, for example, 25 = 25%, less than 100).
     * Can be null (if you will set this weather manually)*/
    @Nullable
    protected Integer chance;
    /** List of seasons-api, that this weather will available.
     * Can be empty (if you will set this weather manually) */
    protected ArrayList<SeasonObject> seasons = new ArrayList<>();
    /** Set true, if this weather must be set when night coming.
     * Can be null (if you will set this weather manually) */
    @Nullable
    protected Boolean nightly;

    /** It's weather enabled. */
    protected boolean enabled = true;

    /**
     * That's constructor.<br><br>
     * Use this for set weather data (call from your class constructor).<br>
     * <code>
     *     public YourClass() { super("YOUR_WEATHER", "Your weather", "Your message!", false, false, 25, Arrays.asList(Season.getSeasonByID("FALL"), Season.getSeasonByID("SPRING"))); }<br>
     * </code>
     * You can also create anonymous class for create weather.<br>
     * <code>
     *     WeatherObject yourWeather = new ChallengeObject("YOUR_WEATHER", "Your weather", "Your message!", false, false, 25, Arrays.asList(Season.getSeasonByID("FALL"), Season.getSeasonByID("SPRING"))) { ... };<br>
     * </code>
     * @param id {@link #id}
     * @param name {@link #name}
     * @param raining {@link #raining}
     * @param thundering {@link #thundering}
     * @param chance {@link #chance}
     * @param seasons {@link #seasons}
     * @param nightly {@link #nightly}
     */
    public WeatherObject(Supplier<Text> name, String id, @Nullable Boolean raining, @Nullable Boolean thundering, @Nullable Integer chance, @Nullable List<SeasonObject> seasons, @Nullable Boolean nightly) {
        this.id = id;
        this.name = name;
        this.raining = raining;
        this.thundering = thundering;
        this.chance = chance;
        if (seasons!=null) this.seasons.addAll(seasons);
        this.nightly = nightly;
    }
    @Deprecated
    public WeatherObject(String id, Supplier<String> name, @Nullable Boolean raining, @Nullable Boolean thundering, @Nullable Integer chance, @Nullable List<SeasonObject> seasons, @Nullable Boolean nightly) {
        this(() -> Format.formatOld(name.get()), id, raining, thundering, chance, seasons, nightly);
    }

    /**
     * This method will be called when this weather is set.
     * You must realize this method in your weather.
     */
    public abstract void onWeatherSet();
    /**
     * This method will be called when this weather was removed.
     * You must realize this method in your weather.
     */
    public abstract void onWeatherRemove();

    /** This method returns this weather id.
     * @return {@link #id}
     */
    public String getId() { return this.id; }
    /** This method returns display name of this weather.
     * @return {@link #name}
     */
    public String getName() { return this.name.get().getString(); }
    public Text getTextName() { return this.name.get(); }
    /** This method returns chance of this weather coming.
     * @return {@link #chance}
     */
    public int  getChance() {
        return this.chance != null ? this.chance : 0;
    }
    /** This method returns weather raining.
     * @return {@link #raining}
     */
    public boolean getRaining() {
        return this.raining != null ? this.raining : Weather.getPreviousCurrent().getRaining();
    }
    /** This method returns weather thundering.
     * @return {@link #thundering}
     */
    public boolean getThundering() {
        return this.thundering != null ? this.thundering : Weather.getPreviousCurrent().getThundering();
    }

    @Deprecated
    protected void sendMessage(String message, Map<String, String> placeholders) {
        Message.sendMessage2Server(message, placeholders);
    }
    /**
     * You can use this method for send message to server players.
     * @see #sendMessage(Text)
     * @param message message for send.
     * @param placeholders Map of placeholders.
     */
    protected void sendMessage(Text message, Map<String, Text> placeholders) {
        Message.sendMessage2Server(message, placeholders);
    }
    @Deprecated
    protected void sendMessage(String message) {
        Message.sendMessage2ServerDefaultPlaceholders(message);
    }
    /**
     * You can use this method for send message to server players with default placeholders.
     * @see #sendMessage(Text, Map)
     * @param message message for send.
     */
    protected void sendMessage(Text message) {
        Message.sendMessage2Server(message);
    }

    /** This method returns seasons-api, that this weather will available.
     * @return {@link #seasons}
     */
    public ArrayList<SeasonObject> getSeasons() { return this.seasons; }
    public void removeSeason(SeasonObject season) {
        this.seasons.remove(season);
    }
    public void addSeason(SeasonObject season) {
        this.seasons.add(season);
    }

    /** This method returns true, if this weather must be set when night coming.
     * @return {@link #nightly}
     */
    public @Nullable Boolean isNightly() { return this.nightly; }

    public void onReload() {

    }

    public boolean isEnabled() { return this.enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

}
