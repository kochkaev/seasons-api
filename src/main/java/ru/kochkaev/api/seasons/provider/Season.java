package ru.kochkaev.api.seasons.provider;

//import ru.kochkaev.seasons-api.Config.OldConfig;

import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.object.ConfigFileObject;
import ru.kochkaev.api.seasons.object.SeasonObject;
import ru.kochkaev.api.seasons.object.Tree;
import ru.kochkaev.api.seasons.object.TreeBranch;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class Season {

    private static SeasonObject CURRENT_SEASON;
    private static TreeBranch<SeasonObject> CURRENT_SEASON_BRANCH;
    private static final Map<String, SeasonObject> allSeasons = new HashMap<>();
    private static final Tree<SeasonObject> seasonsTree = new Tree<>();

    private static final Random random = new Random();



    public static void register(SeasonObject season) {
        allSeasons.put(season.getId(), season);
    }

    public static void onServerStartup() {
        allSeasons.values().forEach(SeasonObject::init);
        addToTree(allSeasons.values());
        String currentStr = Config.getCurrent("season");
        if (currentStr.isEmpty() || currentStr.equals("'example'") || currentStr.equals("example")) {
            TreeBranch<SeasonObject> season = getRandomSeason();
            setCurrent(season);
            Config.writeCurrent("next_day_to_season_cycle", String.valueOf(getNextSeasonEndDay(season)));
        }
        else {
            Deque<SeasonObject> path = Arrays.stream(currentStr.split(" -> ")).map(line -> line.substring(line.startsWith("'") ? line.indexOf("'")+1 : 0, line.endsWith("'") ? line.lastIndexOf("'") : line.length()-1)).map(Season::getSeasonByID).collect(Collectors.toCollection(ArrayDeque::new));
            TreeBranch<SeasonObject> branch = seasonsTree.getBranchByComponentsPathDeque(path);
            if (branch!=null){
                CURRENT_SEASON_BRANCH = branch;
                CURRENT_SEASON = branch.get();
            }
            else {
                TreeBranch<SeasonObject> season = getRandomSeason();
                setCurrent(season);
                Config.writeCurrent("next_day_to_season_cycle", String.valueOf(getNextSeasonEndDay(season)));
            }
        }
        reloadCycleTimer();
    }



    public static void saveCurrentToConfig() {
        String currentStr = CURRENT_SEASON_BRANCH.getBranchPathDeque().stream().map(branch -> (branch.hasComponent()) ? ("'"+branch.get().getId()+"'") : "").filter(line -> !line.isEmpty()).collect(Collectors.joining(" -> "));
        Config.writeCurrent("season", currentStr);
        Config.saveCurrent();
    }
    public static void reloadFromConfig() {
        String currentStr = Config.getCurrent("season");
        Deque<SeasonObject> path = Arrays.stream(currentStr.split(" -> ")).map(line -> line.substring(1, line.length()-1)).map(Season::getSeasonByID).collect(Collectors.toCollection(ArrayDeque::new));
        TreeBranch<SeasonObject> branch = seasonsTree.getBranchByComponentsPathDeque(path);
        if (CURRENT_SEASON_BRANCH != branch) {
            setSeason(branch);
        }
        SeasonsAPI.getLogger().info("Seasons was reloaded!");
    }
    public static void reloadCycleTimer() {
        if (SeasonsAPI.isStarted()){
            int[] current = Arrays.stream(Config.getCurrent("seasons_cycle").split(":")).mapToInt(Integer::parseInt).toArray();
            ConfigFileObject config = Config.getModConfig("API").getConfig();
            int maxOrderToCycle = config.getInt("conf.seasonsCycle.maxOrderToCycle");
            int daysPerSeason = config.getInt("conf.seasonsCycle.daysPerSeason");
            int subSeasonsPerSeason = config.getInt("conf.seasonsCycle.subSeasonsPerSeason");
            if (current[0] != maxOrderToCycle || current[1] != daysPerSeason || current[2] != subSeasonsPerSeason) {
                Config.writeCurrent("seasons_cycle", String.format("%1$s:%2$s:%3$s", maxOrderToCycle, daysPerSeason, subSeasonsPerSeason));
                Config.saveCurrent();
                setSeasonIgnoringPrevious(CURRENT_SEASON_BRANCH);
            }
        }
    }
    public static void reloadDynamics() {
        for (SeasonObject season : allSeasons.values()) {
            season.onReload();
        }
    }



    public static SeasonObject getCurrent() {
        return CURRENT_SEASON;
    }
    public static TreeBranch<SeasonObject> getCurrentSeasonBranch() {
        return CURRENT_SEASON_BRANCH;
    }
    public static void setCurrent(TreeBranch<SeasonObject> season) {
        CURRENT_SEASON_BRANCH = season;
        CURRENT_SEASON = season.get();
        saveCurrentToConfig();
    }



    public static List<SeasonObject> getAll() {
        return allSeasons.values().stream().toList();
    }
    public static List<SeasonObject> getHighestOrder() {
        return seasonsTree.getBranches().stream().map(TreeBranch::get).toList();
    }
    public static Tree<SeasonObject> getTree() {
        return seasonsTree;
    }
    public static void addToTree(Collection<SeasonObject> seasons) {
        seasons = seasons.stream().filter(season -> !seasonsTree.contains(season)).filter(season -> {
            if (!season.getParents().isEmpty()) return true;
            else {
                seasonsTree.add(season);
                return false;
            }
        }).toList();
        while (!seasons.isEmpty()){
            seasons = seasons.stream().filter(season -> {
//                season.getParents().forEach(Season::addToTree);
                addToTree(season);
                return !(seasonsTree.findAll(season).size() == season.getParents().size());
            }).toList();
        }
    }
    public static void addToTree(SeasonObject season) {
        season.getParents().forEach(parent -> seasonsTree.findAll(parent).stream().filter(paren -> !paren.haveBranchOf(season)).forEach(paren -> paren.add(season)));
    }



    public static SeasonObject getRandomSeason(Collection<SeasonObject> seasons) {
        return !seasons.isEmpty() ? seasons.stream().filter(season -> !season.getId().equals("example")).findAny().orElseThrow() : allSeasons.get("example");
    }
    public static TreeBranch<SeasonObject> getRandomSeason() {
        TreeBranch<SeasonObject> branch = seasonsTree;
        while (!branch.getBranches().isEmpty()) {
            branch = getChancedSeason(branch.getBranches().stream().toList(), getSeasonByID("example"));
        }
        return (!(branch.getParent() == null)) ? branch : getSeasonAnyBranchByID("example");
    }
    public static TreeBranch<SeasonObject> getChancedSeason(Collection<TreeBranch<SeasonObject>> seasons, SeasonObject previous) {
        Map<TreeBranch<SeasonObject>, Integer> chances = new HashMap<>();
        Set<TreeBranch<SeasonObject>> possibles = seasons.stream().filter(branch -> branch.get().isEnabled()).filter(branch -> branch.get().getChance() > 0).collect(Collectors.toSet());
        possibles = (possibles.stream().anyMatch(branch -> branch.get().getPreviousSeasons().contains(previous))) ? possibles.stream().filter(branch -> branch.get().getPreviousSeasons().contains(previous)).collect(Collectors.toSet()) : possibles;
        int maxChance = possibles.stream().mapToInt(branch -> {
            int chance = branch.get().getChance();
            chances.put(branch, chance);
            return chance;
        }).sum();
        int randInt = maxChance - (int) (random.nextFloat() * (maxChance - 1) + 1);
        AtomicInteger past = new AtomicInteger(0);
        Optional<TreeBranch<SeasonObject>> output = chances.keySet().stream().filter(branch -> randInt-past.getAndAdd(chances.get(branch)) <= chances.get(branch))
                .findAny();
        return output.orElse(seasonsTree.find(getSeasonByID("example")));
    }



    private static TreeBranch<SeasonObject> getNextSeason() {
         TreeBranch<SeasonObject> branch = CURRENT_SEASON_BRANCH;
         int ordersToUp = CURRENT_SEASON_BRANCH.getBranchPathDeque().size() - getNextSeasonOrder();
         for (int i = 0; i < ordersToUp; i++) branch = branch.getParent();
         branch = getLowestOrderSubSeason(branch, CURRENT_SEASON);
         return branch;
    }
    private static int getNextSeasonEndDay(TreeBranch<SeasonObject> branch) {
        final int startDay = Integer.parseInt(Config.getCurrent("days_after_season_set"))+1;
        final int subSeasonsPerSeason = Config.getModConfig("API").getConfig().getInt("conf.seasonsCycle.subSeasonsPerSeason");
        final int cycleOrder = branch.getBranchPathDeque().size() - Config.getModConfig("API").getConfig().getInt("conf.seasonsCycle.maxOrderToCycle")-1;
        final int daysPerSeason = Config.getModConfig("API").getConfig().getInt("conf.seasonsCycle.daysPerSeason");
        int tempSeasonDuration = daysPerSeason;
        int tempDays = startDay!=tempSeasonDuration ? startDay : 0;
        int tempSeasonDurationLeftover;
        int tempSeasonDurationRounded = tempSeasonDuration;
        for (int j = 0; j<cycleOrder; j++) {
            tempSeasonDurationLeftover = tempSeasonDurationRounded%subSeasonsPerSeason;
            tempSeasonDuration = (tempSeasonDurationRounded - tempSeasonDurationLeftover)/subSeasonsPerSeason;
            for (int i = 0; i<subSeasonsPerSeason; i++) {
                tempSeasonDurationRounded = i<tempSeasonDurationLeftover ? tempSeasonDuration+1 : tempSeasonDuration;
                if (tempDays>=tempSeasonDurationRounded) tempDays = tempDays-tempSeasonDurationRounded;
                else break;
            }
        }
        return (startDay!=daysPerSeason ? startDay : 0) + tempSeasonDurationRounded - 1;
    }
    private static int getNextSeasonOrder() {
        final int days = Integer.parseInt(Config.getCurrent("days_after_season_set"))+1;
        final ConfigFileObject config = Config.getModConfig("API").getConfig();
        final int subSeasonsPerSeason = config.getInt("conf.seasonsCycle.subSeasonsPerSeason");
        int tempSeasonDuration = config.getInt("conf.seasonsCycle.daysPerSeason");
        int nextOrder = config.getInt("conf.seasonsCycle.maxOrderToCycle");
        int tempDays = days!=tempSeasonDuration ? days : 0;
        int tempSeasonDurationRounded = tempSeasonDuration;
        int tempSeasonDurationLeftover;
        while (tempDays != 0){
            nextOrder++;
            tempSeasonDurationLeftover = tempSeasonDurationRounded%subSeasonsPerSeason;
            tempSeasonDuration = (tempSeasonDurationRounded - tempSeasonDurationLeftover)/subSeasonsPerSeason;
            for (int i = 0; i<subSeasonsPerSeason; i++) {
                tempSeasonDurationRounded = i<tempSeasonDurationLeftover ? tempSeasonDuration+1 : tempSeasonDuration;
                if (tempDays>=tempSeasonDurationRounded) tempDays = tempDays-tempSeasonDurationRounded;
                else break;
            }
        }
        return nextOrder;
    }
    private static TreeBranch<SeasonObject> getLowestOrderSubSeason(TreeBranch<SeasonObject> parent) {
        return getLowestOrderSubSeason(parent, getSeasonByID("example"));
    }
    private static TreeBranch<SeasonObject> getLowestOrderSubSeason(TreeBranch<SeasonObject> parent, SeasonObject previous) {
        while (!parent.getBranches().isEmpty())
            parent = getChancedSeason(parent.getBranches(), previous);
        return parent;
    }



    public static void setSeason(TreeBranch<SeasonObject> season) {
        CURRENT_SEASON.onSeasonRemove();
        setCurrent(season);
        season.get().onSeasonSet();
        SeasonsAPI.getLogger().info("Season was set to \"{}\"", season.get().getId());
    }
    public static void setSeasonIgnoringPrevious(TreeBranch<SeasonObject> season) {
        Config.writeCurrent("days_after_season_set", String.valueOf(Config.getModConfig("API").getConfig().getInt("conf.seasonsCycle.daysPerSeason")-1));
        Config.saveCurrent();
        TreeBranch<SeasonObject> newSeason = getLowestOrderSubSeason(season);
        setSeason(newSeason);
        Config.writeCurrent("next_day_to_season_cycle", String.valueOf(getNextSeasonEndDay(newSeason)));
        Config.saveCurrent();
    }
    public static void setNextSeason() {
        if (Config.getModConfig("API").getConfig().getBoolean("conf.seasonsCycle.doSeasonsCycle")) {
            TreeBranch<SeasonObject> nextSeason = getNextSeason();
            setSeason(nextSeason);
            Config.writeCurrent("next_day_to_season_cycle", String.valueOf(getNextSeasonEndDay(nextSeason)));
            Config.saveCurrent();
        }
    }



    public static SeasonObject getSeasonByID(String id) {
        return allSeasons.containsKey(id) ? allSeasons.get(id) : allSeasons.get("example");
    }
    public static TreeBranch<SeasonObject> getSeasonAnyBranchByID(String id) {
        return allSeasons.containsKey(id) ? seasonsTree.find(allSeasons.get(id)) : seasonsTree.find(allSeasons.get("example"));
    }

}
