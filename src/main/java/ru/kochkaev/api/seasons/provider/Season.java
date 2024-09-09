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

public class Season {

    private static SeasonObject CURRENT_SEASON;
    private static TreeBranch<SeasonObject> CURRENT_SEASON_BRANCH;
    private static final Map<String, SeasonObject> allSeasons = new HashMap<>();
//    private static final List<SeasonObject> highestOrderSeasons = new ArrayList<>();
    private static final Tree<SeasonObject> seasonsTree = new Tree<>();

    private static final Random random = new Random();

    public static void register(SeasonObject season) {
        allSeasons.put(season.getId(), season);
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

    public static void onServerStartup() {
        allSeasons.values().forEach(SeasonObject::init);
        addToTree(allSeasons.values());
//        seasonsTree.getBranchesComponentsSet().forEach(SeasonObject::init);
//        highestOrderSeasons.addAll(seasonsTree.getBranchesComponentsSet().stream().filter(season -> !season.getParents().isEmpty()).toList());
        String currentStr = Config.getCurrent("season");
        if (currentStr.isEmpty() || currentStr.equals("'example'") || currentStr.equals("example")) setCurrent(getRandomSeason());
        else {
            Deque<SeasonObject> path = Arrays.stream(currentStr.split(" -> ")).map(line -> line.substring(line.startsWith("'") ? line.indexOf("'") : 0, line.endsWith("'") ? line.lastIndexOf("'") : 0)).map(Season::getSeasonByID).collect(Collectors.toCollection(ArrayDeque::new));
            TreeBranch<SeasonObject> branch = seasonsTree.getBranchByComponentsPathDeque(path);
            CURRENT_SEASON_BRANCH = branch;
            CURRENT_SEASON = branch.get();
        }
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
                season.getParents().forEach(Season::addToTree);
                return seasonsTree.findAll(season).size() == season.getParents().size();
            }).toList();
        }
    }
    public static void addToTree(SeasonObject season) {
        season.getParents().forEach(parent -> seasonsTree.findAll(parent).stream().filter(paren -> paren.haveBranchOf(season)).forEach(paren -> paren.add(season)));
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
//        SeasonObject season = branch.get();
//        SeasonObject season = getSeasonByID(currentStr);
        if (CURRENT_SEASON_BRANCH != branch) {
            setSeason(branch);
        }
        SeasonsAPI.getLogger().info("Seasons was reloaded!");
    }

    public static void reloadDynamics() {
        for (SeasonObject season : allSeasons.values()) {
            season.onReload();
        }
    }

    public static void setSeason(TreeBranch<SeasonObject> season) {
        CURRENT_SEASON.onSeasonRemove();
        setCurrent(season);
        season.get().onSeasonSet();
        SeasonsAPI.getLogger().info("Season was set to \"{}\"", season.get().getId());
    }


    public static SeasonObject getSeasonByID(String id) {
        return allSeasons.containsKey(id) ? allSeasons.get(id) : allSeasons.get("example");
    }

    public static TreeBranch<SeasonObject> getRandomSeason() {
        TreeBranch<SeasonObject> branch = seasonsTree;
        while (!branch.getBranches().isEmpty()) {
            branch = getChancedSeason(branch.getBranches().stream().toList());
        }
        return (!(branch.getParent() == null)) ? branch : seasonsTree.find(getSeasonByID("example"));
    }
//    public static SeasonObject getRandomSeason() {
//        return allSeasons.size() > 1 ? allSeasons.entrySet().stream().filter(entry -> !entry.getKey().equals("example")).findAny().orElseThrow().getValue() : allSeasons.get("example");
//    }
    public static SeasonObject getRandomSeason(Collection<SeasonObject> seasons) {
        return !seasons.isEmpty() ? seasons.stream().filter(season -> !season.getId().equals("example")).findAny().orElseThrow() : allSeasons.get("example");
    }
    public static TreeBranch<SeasonObject> getChancedSeason(Collection<TreeBranch<SeasonObject>> seasons) {
        Map<TreeBranch<SeasonObject>, Integer> chances = new HashMap<>();
        int maxChance = seasons.stream().filter(branch -> branch.get().isEnabled()).filter(branch -> branch.get().getChance() > 0).mapToInt(branch -> {
            int chance = branch.get().getChance();
            chances.put(branch, chance);
            return chance;
        }).sum();
        int randInt = maxChance - (int) (random.nextFloat() * (maxChance - 1) + 1);
        AtomicInteger past = new AtomicInteger(0);
        return chances.keySet().stream().filter(branch -> randInt-past.getAndSet(past.get() + chances.get(branch)) <= chances.get(branch)).findAny().orElse(seasonsTree.find(getSeasonByID("example")));
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

    public static TreeBranch<SeasonObject> getNextSeason() {
        int days = Integer.parseInt(Config.getCurrent("days_after_season_set"));
        ConfigFileObject config = Config.getModConfig("API").getConfig();
        int maxOrder = config.getInt("conf.seasonsCycle.maxOrderToCycle");
        int order = CURRENT_SEASON_BRANCH.getBranchPathDeque().size();
        int daysPerSeason = config.getInt("conf.seasonsCycle.daysPerSeason");
        int subSeasonsPerSeason = config.getInt("conf.seasonsCycle.subSeasonsPerSeason");
        if (config.getBoolean("conf.seasonsCycle.doSeasonsCycle")) {
//            if (days == daysPerSeason) {
//                TreeBranch<SeasonObject> branch = CURRENT_SEASON_BRANCH.getBranchPathDeque().stream().skip(maxOrder-1).findFirst().orElseThrow();
//                while (!branch.getBranches().isEmpty()) {
//                    branch = branch.getAnyBranch();
//                }
//                return branch;
//            }
            int tempOrder;
            if (days == daysPerSeason) tempOrder = maxOrder;
            else {
                int tempModifier = 1;
                tempOrder = maxOrder + 1;
                while (days % (subSeasonsPerSeason * tempModifier) != 0) {
                    tempModifier++;
                    tempOrder++;
                }
            }
            TreeBranch<SeasonObject> branch = CURRENT_SEASON_BRANCH;
            for (int i = 0; i < order-tempOrder; i++) branch = branch.getParent();
            while (!branch.getBranches().isEmpty()) {
//                branch = branch.getAnyBranch();
                branch = getChancedSeason(branch.getBranches());
            }
            branch = getChancedSeason(branch.getParent().getBranches());
//            Config.writeCurrent("next_day_to_season_cycle", String.valueOf(days + branch.getBranchPathDeque().size()));
            return branch;
        }
        return CURRENT_SEASON_BRANCH;
    }
    public static void setNextSeason() {
        TreeBranch<SeasonObject> nextSeason = getNextSeason();
        setSeason(nextSeason);
        int days = Integer.parseInt(Config.getCurrent("days_after_season_set"));
        int daysPerSeason = Config.getModConfig("API").getConfig().getInt("conf.seasonsCycle.daysPerSeason");
        int subSeasonsPerSeason = Config.getModConfig("API").getConfig().getInt("conf.seasonsCycle.subSeasonsPerSeason");
        int order = nextSeason.getBranchPathDeque().size();
        int nextDay = ((days < daysPerSeason) ? days : 0) + daysPerSeason/(order > 1 ? (subSeasonsPerSeason ^ (order-1)) : 1);
        if (nextDay > daysPerSeason) nextDay = daysPerSeason;
        Config.writeCurrent("next_day_to_season_cycle", String.valueOf(nextDay));
        Config.saveCurrent();
    }

    public static int getLowestSeasonOrder(SeasonObject season) {
        List<SeasonObject> parents = season.getParents();
        if (parents.isEmpty()) return 1;
        else return parents.stream().mapToInt(Season::getLowestSeasonOrder).max().orElseThrow() + 1;
    }
    public static int getHighestSeasonOrder(SeasonObject season) {
        List<SeasonObject> parents = season.getParents();
        if (parents.isEmpty()) return 1;
        else return parents.stream().mapToInt(Season::getHighestSeasonOrder).min().orElseThrow() + 1;
    }


}
