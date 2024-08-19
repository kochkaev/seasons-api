package ru.kochkaev.api.seasons.object;


import ru.kochkaev.api.seasons.util.Message;
import ru.kochkaev.api.seasons.util.functional.IFuncStringRet;

import java.util.Map;

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
    protected IFuncStringRet nameLambda;

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
     * @see #SeasonObject(String, IFuncStringRet)
     */
    public SeasonObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * You can use this constructor for dynamic update name on language change.
     * @param id {@link #id}
     * @param name lambda function, returns display name of this weather.
     * @see #SeasonObject(String, String)
     */
    public SeasonObject(String id, IFuncStringRet name) {
        this.id = id;
        this.nameLambda = name;
        this.name = name.function();
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

    /**
     * You can use this method for send message to server players.
     * @see #sendMessage(String)
     * @param message message for send.
     * @param placeholders Map of placeholders.
     */
    protected void sendMessage(String message, Map<String, String> placeholders) {
        Message.sendMessage2Server(message, placeholders);
    }
    /**
     * You can use this method for send message to server players with default placeholders.
     * @see #sendMessage(String, Map)
     * @param message message for send.
     */
    protected void sendMessage(String message) {
        Message.sendMessage2ServerDefaultPlaceholders(message);
    }


    public void onReload() {
        if (nameLambda != null) this.name = nameLambda.function();
    }


    public boolean isEnabled() { return this.enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

}
