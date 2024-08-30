package ru.kochkaev.api.seasons.integration.mod;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringFieldBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import ru.kochkaev.api.seasons.object.ConfigObject;
import ru.kochkaev.api.seasons.object.TXTConfigObject;
import ru.kochkaev.api.seasons.service.Config;
import ru.kochkaev.api.seasons.util.map.Map1Key2Values;
import ru.kochkaev.api.seasons.util.map.Map1Key3Values;

import javax.lang.model.type.UnionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothConfigClient extends ClothConfig {

//    private static final Map<String, Map<String, Map1Key2Values<String, String, String>>> categoryMatrix = new HashMap<>();
//    private static final List<ConfigObject> configs = new ArrayList<>();

    public ClothConfigClient() {

    }

    public Screen getConfigScreen(Object parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen((Screen) parent)
                .setTitle(Text.of("Seasons Config"));
//        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
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
        for (ConfigObject mod : Config.getMods().values()) {
            for (TXTConfigObject config : mod.getConfigs().values()) {
                ConfigCategory modCategory = builder.getOrCreateCategory(Text.of(mod.getModName()+"-"+config.getFilename()));
                Map<String, TXTConfigObject.ConfigValueObject<?>> valuesMap = config.getTypedValuesMap();
                for (String key : valuesMap.keySet()) {
                    modCategory.addEntry(
//                            (
//                                    value instanceof String ?
//                                            builder.entryBuilder().startStrField(Text.of(key), (String) value)
//                                                    .setDefaultValue((String) value) :
//                                            value instanceof Boolean ?
//                                                    builder.entryBuilder().startBooleanToggle(Text.of(key), (Boolean) value)
//                                                            .setDefaultValue((Boolean) value) :
//                                                    value instanceof Integer ?
//                                                            builder.entryBuilder().startIntField(Text.of(key), (Integer) value)
//                                                                    .setDefaultValue((Integer) value) :
//                                                            value instanceof Long ?
//                                                                    builder.entryBuilder().startLongField(Text.of(key), (Long) value)
//                                                                            .setDefaultValue((Long) value) :
//                                                                    value instanceof Float ?
//                                                                            builder.entryBuilder().startFloatField(Text.of(key), (Float) value)
//                                                                                    .setDefaultValue((Float) value) :
//                                                                            value instanceof Double ?
//                                                                                    builder.entryBuilder().startDoubleField(Text.of(key), (Double) value)
//                                                                                            .setDefaultValue((Double) value) :
//                                                                                    builder.entryBuilder().startStrField(Text.of(key), StringUtils.join(value))
//                                                                                            .setDefaultValue(StringUtils.join(value))
//                            )
                            typedFieldBuilder(builder, key, valuesMap.get(key), mod.getModName(), config.getFilename())
                    );
                }
            }
        }
        return builder.build();
    }
//    public void addConfigCategory(ConfigObject config) {
////        Map<String, Map1Key2Values<String, String, String>> modConfigs = new HashMap<>();
////        for (TXTConfigObject configObject : config.getConfigs().values()) modConfigs.put(configObject.getFilename(), configObject.getKeyCommentAndDefaultMap());
////        categoryMatrix.put(config.getModName(), modConfigs);
//        configs.add(config);
//    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static <T, E extends TooltipListEntry<T>, B extends FieldBuilder<T, E, B>, R extends AbstractFieldBuilder<T, E, B>> AbstractConfigListEntry typedFieldBuilder(ConfigBuilder builder, String key, TXTConfigObject.ConfigValueObject<T> valueObject, String modName, String configName) {
        T value = valueObject.getValue();
        T defaultValue = valueObject.getDefaultValue();
        R entryBuilder = switch (value) {
            case String s -> (R) builder.entryBuilder().startStrField(Text.of(key), s)
                    .setDefaultValue((String) defaultValue);
            case Boolean b -> (R) builder.entryBuilder().startBooleanToggle(Text.of(key), b)
                    .setDefaultValue((Boolean) defaultValue);
            case Integer i -> (R) builder.entryBuilder().startIntField(Text.of(key), i)
                    .setDefaultValue((Integer) defaultValue);
            case Long l -> (R) builder.entryBuilder().startLongField(Text.of(key), l)
                    .setDefaultValue((Long) defaultValue);
            case Float f -> (R) builder.entryBuilder().startFloatField(Text.of(key), f)
                    .setDefaultValue((Float) defaultValue);
            case Double d -> (R) builder.entryBuilder().startDoubleField(Text.of(key), d)
                    .setDefaultValue((Double) defaultValue);
            case null, default -> (R) builder.entryBuilder().startStrField(Text.of(key), StringUtils.join(value))
                    .setDefaultValue(StringUtils.join(defaultValue));
        };
        entryBuilder = (R) entryBuilder
                .setTooltip(Text.of(valueObject.getHeader()), Text.of(valueObject.getDescription()));
        entryBuilder = (R) entryBuilder.setSaveConsumer(newValue -> {
            assert value != null;
            if (!value.equals(newValue)) Config.getModConfig(modName).getConfig(configName).setValue(key, newValue);
        });
        return entryBuilder.build();
    }

//    protected static <T> AbstractConfigListEntry<T> createConfigEntry(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, String modName, String configName, String key, String description, T defaultValue) {
//        if (defaultValue instanceof Double) {
//            DoubleListEntry e = builder.doubleEntry(Text.of(key), Config.getModConfig(modName).getConfig(configName).getDouble(key), 0.0, Double.MAX_VALUE);
//        }
//            return (AbstractConfigListEntry<T>) entryBuilder
//                    .startDoubleField(Text.of(key), Config.getModConfig(modName).getConfig(configName).getDouble(key))
//                    .setTooltip(Text.of(description))
//                    .setMin(e.getMin())
//                    .setMax(e.getMax())
//                    .setDefaultValue(e::getDefault)
//                    .setSaveConsumer(d -> {
//                        e.set(d);
//                        e.save();
//                    })
//                    .build();
//        } else if (entry instanceof IntegerConfigEntry e) {
//            return (AbstractConfigListEntry<T>) entryBuilder
//                    .startIntField(name, e.get())
//                    .setTooltip(description)
//                    .setMin(e.getMin())
//                    .setMax(e.getMax())
//                    .setDefaultValue(e::getDefault)
//                    .setSaveConsumer(d -> e.set(d).save())
//                    .build();
//        } else if (entry instanceof BooleanConfigEntry e) {
//            return (AbstractConfigListEntry<T>) entryBuilder
//                    .startBooleanToggle(name, e.get())
//                    .setTooltip(description)
//                    .setDefaultValue(e::getDefault)
//                    .setSaveConsumer(d -> e.set(d).save())
//                    .build();
//        } else if (entry instanceof StringConfigEntry e) {
//            return (AbstractConfigListEntry<T>) entryBuilder
//                    .startStrField(name, e.get())
//                    .setTooltip(description)
//                    .setDefaultValue(e::getDefault)
//                    .setSaveConsumer(d -> e.set(d).save())
//                    .build();
//        }
}
