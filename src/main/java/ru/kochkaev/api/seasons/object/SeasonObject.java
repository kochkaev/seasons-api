package ru.kochkaev.api.seasons.object;


/**
 * It's SeasonObject, object for create your own season.<br><br>
 * For create weather you must extend SeasonObject.<br>
 * {@code public class YourClass extends SeasonObject { ... }}
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
public abstract class SeasonObject {

    /** Season ID, must be unique. */
    protected String id;
    /** Display season name, sends to chat, title, etc. */
    protected String name;
    /** Message, who sends to chat when this season is coming. */
    protected String message;

    /** It's season enabled. */
    protected boolean enabled = true;

    /**
     * That's constructor.<br><br>
     * Use this for set weather data (call from your class constructor).<br>
     * <code>
     *     public YourClass() { super("YOUR_SEASON", "Your season", "Your message!"); }<br>
     * </code>
     * You can also create anonymous class for create weather.<br>
     * <code>
     *     WeatherObject yourSeason = new ChallengeObject("YOUR_SEASON", "Your season", "Your message!") { ... };<br>
     * </code>
     * @param id {@link #id}
     * @param name {@link #name}
     * @param message {@link #message}
     */
    public SeasonObject(String id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    /**
     * This method will be called when this season is set.
     * You must realize this method in your season.
     */
    public abstract void onSeasonSet();
    /**
     * This method will be called when this season was removed.
     * You must realize this method in your season.
     */
    public abstract void onSeasonRemove();

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

    public boolean isEnabled() { return this.enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

}
