package ru.kochkaev.api.seasons.object;


import ru.kochkaev.api.seasons.provider.Season;
import ru.kochkaev.api.seasons.util.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    /**
     * Display season name, sends to chat, title, etc. <br><br>
     * Name can be dynamic... <br>
     * {@code () -> Config.getModConfig("YourModConfigID").getLang().getString("yourDynamicNameID")} <br>
     * ...and static! <br>
     * {@code () -> "Your static name"}<br><br>
     * Lambda expression for get name is String Supplier.
     */
    protected Supplier<String> name;

    /** It's season enabled. */
    protected boolean enabled = true;

    /** List of the parent seasons for this season */
    protected final ArrayList<String> parents = new ArrayList<>();

//    /** List of sub seasons for this season */
//    protected final List<String> subSeasons = new ArrayList<>();

    /** List of seasons after which this season has a chance to appear */
    protected final ArrayList<String> previousSeasons = new ArrayList<>();

    /** Chance of appear this season */
    protected Supplier<Integer> chance;

    /**
     * That's constructor.<br><br>
     * Use this for set weather data (call from your class constructor).<br>
     * <code>
     *     public YourClass() { super("YOUR_SEASON", "Your season", Arrays.listOf("parentSeason0", "parentSeason1"), Arrays.listOf("previousSeason0", "previousSeason1"), () -> Config.getModConfig("Your Mod ID").getConfig().getInt("keyOfYourSeasonChance")); }<br>
     * </code>
     * You can also create anonymous class for create weather.<br>
     * <code>
     *     WeatherObject yourSeason = new ChallengeObject("YOUR_SEASON", "Your season", new ArrayList<>(), Collections.singletonList("previousSeason"), () -> 100) { ... };<br>
     * </code>
     * @param id {@link #id}
     * @param name {@link #name}
     * @param parents {@link #parents}
     * @param previousSeasons {@link #previousSeasons}
     * @param chance {@link #chance}
     */
    public SeasonObject(String id, Supplier<String> name, List<String> parents, List<String> previousSeasons, Supplier<Integer> chance) {
        this.id = id;
        this.name = name;
        this.parents.addAll(parents);
        this.previousSeasons.addAll(previousSeasons);
        this.chance = chance;
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
    public String getName() { return this.name.get(); }

    public int getChance() { return this.chance.get(); }

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

    }

    public void init() {
//        parents = this.parents.stream().map(Season::getSeasonByID).collect(Collectors.toSet());
    }


    public boolean isEnabled() { return this.enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Collection<SeasonObject> getParents() {
        return this.parents.stream().map(Season::getSeasonByID).collect(Collectors.toSet());
    }
//    public List<SeasonObject> getSubSeasons() {
//        return this.subSeasons.stream().map(Season::getSeasonByID).collect(Collectors.toList());
//    }
    public void addParent(String seasonId) {
        if (!this.parents.contains(seasonId))
            this.parents.add(seasonId);
    }
//    public void addSubSeason(String seasonId) {
//        if (!this.subSeasons.contains(seasonId)) {
//            this.subSeasons.add(seasonId);
//            SeasonObject season = Season.getSeasonByID(seasonId);
//            if (!season.isParentOf(id)) addParent(id);
//        }
//    }
    public void removeParent(String seasonId) {
        this.parents.remove(seasonId);
    }
//    public void removeSubSeason(String seasonId) {
//        if (this.subSeasons.contains(seasonId)) {
//            this.subSeasons.remove(seasonId);
//            SeasonObject season = Season.getSeasonByID(seasonId);
//            if (season.isParentOf(id)) removeParent(id);
//        }
//    }
    public boolean isSubSeasonOf(String seasonId) {
        return this.parents.contains(seasonId);
    }
//    public boolean isSubSeasonOf(String seasonId) {
//        return this.subSeasons.contains(seasonId);
//    }

    public Collection<SeasonObject> getPreviousSeasons() {
        return this.previousSeasons.stream().map(Season::getSeasonByID).collect(Collectors.toSet());
    }
    public void addPreviousSeason(String seasonId) {
        this.previousSeasons.add(seasonId);
    }
    public void removePreviousSeason(String seasonId) {
        this.previousSeasons.remove(seasonId);
    }
}
