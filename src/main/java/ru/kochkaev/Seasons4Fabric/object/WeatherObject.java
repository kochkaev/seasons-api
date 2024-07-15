package ru.kochkaev.Seasons4Fabric.object;


import org.jetbrains.annotations.Nullable;
import ru.kochkaev.Seasons4Fabric.service.Season;
import ru.kochkaev.Seasons4Fabric.service.Weather;

import java.util.List;

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
    /** Display weather name, sends to chat, title, etc. */
    protected String name;
    /** Message, who sends to chat when this weather is coming. */
    protected String message;
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
    /** List of seasons, that this weather will available.
     * Can be null (if you will set this weather manually) */
    @Nullable
    protected List<SeasonObject> seasons;
    /** Set true, if this weather must be set when night coming.
     * Can be null (if you will set this weather manually) */
    @Nullable
    protected Boolean nightly;

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
     * @param message {@link #message}
     * @param raining {@link #raining}
     * @param thundering {@link #thundering}
     * @param chance {@link #chance}
     * @param seasons {@link #seasons}
     * @param nightly {@link #nightly}
     */
    public WeatherObject(String id, String name, String message, @Nullable Boolean raining, @Nullable Boolean thundering, @Nullable Integer chance, @Nullable List<SeasonObject> seasons, @Nullable Boolean nightly) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.raining = raining;
        this.thundering = thundering;
        this.chance = chance;
        this.seasons = seasons;
        this.nightly = nightly;
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
    public String getName() { return this.name; }
    /** This method returns message, who sends to chat when this weather is coming.
     * @return {@link #message}
     */
    public String  getMessage() { return this.message; }
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
    /** This method returns seasons, that this weather will available.
     * @return {@link #seasons}
     */
    public @Nullable List<SeasonObject> getSeasons() { return this.seasons; }
    /** This method returns true, if this weather must be set when night coming.
     * @return {@link #nightly}
     */
    public @Nullable Boolean isNightly() { return this.nightly; }

}
