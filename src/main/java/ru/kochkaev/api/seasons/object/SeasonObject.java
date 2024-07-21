package ru.kochkaev.api.seasons.object;


import net.minecraft.server.MinecraftServer;
import ru.kochkaev.api.seasons.config.Config;
import ru.kochkaev.api.seasons.util.Message;
import ru.kochkaev.api.seasons.util.MessageFormat;

import java.util.HashMap;
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
     */
    public SeasonObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * This method will be called when this season is set.
     * You must realize this method in your season.
     */
    public abstract void onSeasonSet(MinecraftServer server);
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
     * @param server Minecraft server.
     * @param message message for send.
     * @param placeholders Map of placeholders.
     */
    protected void sendMessage(MinecraftServer server, String message, Map<String, String> placeholders) {
        Map<String, String> placeholders1 = new HashMap<>();
        placeholders1.put("%message%", MessageFormat.formatMessage(message, placeholders1));
        Message.sendMessage2Server(Config.getModConfig("API").getConfig().getString("conf.format.chat.message"), server.getPlayerManager(), placeholders1);
    }
    /**
     * @see #sendMessage(MinecraftServer, String, Map)
     * @param server Minecraft server.
     * @param message message for send.
     */
    protected void sendMessage(MinecraftServer server, String message) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%season%", name);
        sendMessage(server, message, placeholders);
    }


    public boolean isEnabled() { return this.enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

}
