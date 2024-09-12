package ru.kochkaev.api.seasons.loader;

import java.nio.file.Path;

public abstract class Loader {

//    public abstract ServerWorld getOverworld();
//    public abstract MinecraftServer getServer();
    public abstract void registerCommands();
    public abstract Path getConfigPath();

}
