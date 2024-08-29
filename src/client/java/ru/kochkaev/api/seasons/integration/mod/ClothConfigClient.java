package ru.kochkaev.api.seasons.integration.mod;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.object.TXTConfigObject;
import ru.kochkaev.api.seasons.service.Config;
import ru.kochkaev.api.seasons.util.map.Map1Key2Values;
import ru.kochkaev.api.seasons.util.map.Map1Key3Values;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothConfigClient extends ClothConfig {

//    private static final Map<String, Map<String, Map1Key2Values<String, String, String>>> categoryMatrix = new HashMap<>();
    private static final List<ConfigObject> configs = new ArrayList<>();

    public ClothConfigClient() {

    }

    public Screen getConfigScreen(Object parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen((Screen) parent)
                .setTitle(Text.of(Identifier.of("seasons.config.title")));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
//        for (String modName : categoryMatrix.keySet()) {
//            for (String configName : categoryMatrix.get(modName).keySet()) {
//                ConfigCategory modCategory = builder.getOrCreateCategory(Text.of(modName+"-"+configName));
//                for (String key : categoryMatrix.get(modName).get(configName).getKeySet())
//                    modCategory.addEntry(entryBuilder.startStrField(Text.of(key), Config.getModConfig(modName).getConfig(configName).getString(key))
//                            .setDefaultValue(categoryMatrix.get(modName).get(configName).getSecond(key))
//                            .setTooltip(Text.of(categoryMatrix.get(modName).get(configName).getFirst(key)))
//                            .setSaveConsumer(newValue -> Config.getModConfig(modName).getConfig(configName).write(key, newValue))
//                            .build()
//                    );
//            }
//        }
        for (ConfigObject mod : configs) {
            for (TXTConfigObject config : mod.getConfigs().values()) {
                ConfigCategory modCategory = builder.getOrCreateCategory(Text.of(mod.getModName()+"-"+config.getFilename()));
                Map1Key2Values<String, String, String> valuesMap = config.getKeyCommentAndDefaultMap();
                for (String key : valuesMap.getKeySet()) {
                    String temp = Config.getModConfig(mod.getModName())
                            .getConfig(config.getFilename())
                            .getString(key);
                    modCategory.addEntry(builder.entryBuilder()
                            .startStrField(
                                    Text.of(key),
                                    temp)
                            .setDefaultValue(valuesMap.getSecond(key))
                            .setTooltip(Text.of(valuesMap.getFirst(key)))
                            .setSaveConsumer(newValue -> Config.getModConfig(mod.getModName()).getConfig(config.getFilename()).write(key, newValue))
                            .build()
                    );
                }
            }
        }
        return builder.build();
    }
    public void addConfigCategory(ConfigObject config) {
//        Map<String, Map1Key2Values<String, String, String>> modConfigs = new HashMap<>();
//        for (TXTConfigObject configObject : config.getConfigs().values()) modConfigs.put(configObject.getFilename(), configObject.getKeyCommentAndDefaultMap());
//        categoryMatrix.put(config.getModName(), modConfigs);
        configs.add(config);
    }
}
