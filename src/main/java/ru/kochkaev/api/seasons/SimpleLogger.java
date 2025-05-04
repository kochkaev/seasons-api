package ru.kochkaev.api.seasons;

import org.slf4j.Logger;
import ru.kochkaev.api.seasons.provider.Config;

public class SimpleLogger {

    private Logger logger;

    SimpleLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String message) {
        logger.info("[Seasons] {}", message);
    }
    public void debug(String message) {
        if (Config.getModConfig("API").getConfig().getBoolean("conf.dev.logging"))
            logger.info("[Seasons debug] {}", message);
    }
}
