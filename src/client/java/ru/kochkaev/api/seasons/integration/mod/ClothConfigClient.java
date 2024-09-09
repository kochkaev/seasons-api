package ru.kochkaev.api.seasons.integration.mod;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.kochkaev.api.seasons.object.*;
import ru.kochkaev.api.seasons.provider.Config;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public class ClothConfigClient extends ClothConfig {

    public ClothConfigClient() {

    }

    public Screen getConfigScreen(Object parent, ConfigObject priority) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen((Screen) parent)
                .setTitle(Text.of("Seasons Config"));
        for (ConfigObject mod : Config.getMods().values()) {
            ConfigCategory modCategory = addCategory(builder, mod);
            if (mod.getModName().equals(priority.getModName())) builder.setFallbackCategory(modCategory);
        }
        return builder.build();
    }

    protected static AbstractConfigListEntry<?> addEntry(String key, Object valueObject, ConfigBuilder builder, String modName, String filename) {
        if (valueObject instanceof ConfigSelectionObject<?, ? extends List<?>> selectionObj) {
            return (selectionObj.getSelectionType().equals("Dropdown") || selectionObj.getSelectionType().equals("Suggestion")) ?
                    dropdownBuilder(builder, key, selectionObj, modName, filename, selectionObj.getSelectionType().equals("Suggestion")) :
                    selectionBuilder(builder, key, selectionObj, modName, filename);
        }
        else if (valueObject instanceof ConfigValueObject<?> valueObj) return typedFieldBuilder(builder, key, valueObj, modName, filename);
        return null;
    }

    protected static ConfigCategory addCategory(ConfigBuilder builder, ConfigObject mod) {
        ConfigCategory modCategory = builder.getOrCreateCategory(Text.of(mod.getModName()));
        Collection<ConfigFileObject> configs = mod.getConfigs().values();
        if (configs.size() > 1) for (ConfigFileObject config : configs) {
            Queue<String> keys = config.getKeysQueue();
            SubCategoryBuilder configCategory = builder.entryBuilder().startSubCategory(Text.of(config.getFilename()));
            for (String key : keys) {
                configCategory.add(
                        addEntry(key, config.getValueObject(key), builder, mod.getModName(), config.getFilename())
                );
            }
            modCategory.addEntry(configCategory.build());
        }
        else if (configs.size() == 1) {
            ConfigFileObject config = mod.getConfig();
            Queue<String> keys = config.getKeysQueue();
            for (String key : keys) {
                modCategory.addEntry(
                        addEntry(key, config.getValueObject(key), builder, mod.getModName(), config.getFilename())
                );
            }
        }
        return modCategory;
    }

    @SuppressWarnings({"unchecked"})
    protected static <T, E extends TooltipListEntry<T>, B extends FieldBuilder<T, E, B>, R extends AbstractFieldBuilder<T, E, B>> AbstractConfigListEntry<T> typedFieldBuilder(ConfigBuilder builder, String key, ConfigValueObject<T> valueObject, String modName, String configName) {
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
                .setTooltip(getTooltip(valueObject));
        entryBuilder = (R) entryBuilder.setSaveConsumer(newValue -> {
            assert value != null;
            if (!value.equals(newValue)) Config.getModConfig(modName).getConfig(configName).setValue(key, newValue);
        });
        return entryBuilder.build();
    }

    @SuppressWarnings({"unchecked"})
    protected static @NotNull <T, L extends List<T>, S extends SelectorBuilder<T>> SelectionListEntry<T> selectionBuilder(ConfigBuilder builder, String key, ConfigSelectionObject<T, L> valueObject, String modName, String configName) {
        L list = valueObject.getList();
        T value = valueObject.getValue();
        T defaultValue = valueObject.getDefaultValue();
        S entryBuilder = (S) builder.entryBuilder().startSelector(Text.of(key), list.toArray(), value)
                .setDefaultValue(defaultValue);
        entryBuilder = (S) entryBuilder
                .setTooltip(getTooltip(valueObject));
        entryBuilder = (S) entryBuilder.setSaveConsumer(newValue -> {
            if (!value.equals(newValue)) Config.getModConfig(modName).getConfig(configName).setValue(key, newValue);
        });
        return entryBuilder.build();
    }

    @SuppressWarnings({"unchecked"})
    protected static @NotNull <T, L extends List<T>, D extends DropdownMenuBuilder<T>> DropdownBoxEntry<T> dropdownBuilder(ConfigBuilder builder, String key, ConfigSelectionObject<T, L> valueObject, String modName, String configName, boolean suggestionMode) {
        L list = valueObject.getList();
        T value = valueObject.getValue();
        T defaultValue = valueObject.getDefaultValue();
        D entryBuilder = switch (value) {
            case String s -> (D) builder.entryBuilder().startDropdownMenu(Text.of(key), DropdownMenuBuilder.TopCellElementBuilder.of(s, (str) -> str), DropdownMenuBuilder.CellCreatorBuilder.of())
                    .setDefaultValue((String) defaultValue);
            case Boolean b -> (D) builder.entryBuilder().startDropdownMenu(Text.of(key), DropdownMenuBuilder.TopCellElementBuilder.of(b, Boolean::parseBoolean), DropdownMenuBuilder.CellCreatorBuilder.of())
                    .setDefaultValue((Boolean) defaultValue);
            case Integer i -> (D) builder.entryBuilder().startDropdownMenu(Text.of(key), DropdownMenuBuilder.TopCellElementBuilder.of(i, Integer::parseInt), DropdownMenuBuilder.CellCreatorBuilder.of())
                    .setDefaultValue((Integer) defaultValue);
            case Long l -> (D) builder.entryBuilder().startDropdownMenu(Text.of(key), DropdownMenuBuilder.TopCellElementBuilder.of(l, Long::parseLong), DropdownMenuBuilder.CellCreatorBuilder.of())
                    .setDefaultValue((Long) defaultValue);
            case Float f -> (D) builder.entryBuilder().startDropdownMenu(Text.of(key), DropdownMenuBuilder.TopCellElementBuilder.of(f, Float::parseFloat), DropdownMenuBuilder.CellCreatorBuilder.of())
                    .setDefaultValue((Float) defaultValue);
            case Double d -> (D) builder.entryBuilder().startDropdownMenu(Text.of(key), DropdownMenuBuilder.TopCellElementBuilder.of(d, Double::parseDouble), DropdownMenuBuilder.CellCreatorBuilder.of())
                    .setDefaultValue((Double) defaultValue);
            case null, default -> {
                assert value != null;
                yield (D) builder.entryBuilder().startDropdownMenu(Text.of(key), DropdownMenuBuilder.TopCellElementBuilder.of(value, (str) -> str), DropdownMenuBuilder.CellCreatorBuilder.of())
                        .setDefaultValue(defaultValue);
            }
        };
        entryBuilder = (D) entryBuilder.setSelections(list);
        entryBuilder = (D) entryBuilder.setSuggestionMode(suggestionMode);
        entryBuilder = (D) entryBuilder
                .setTooltip(getTooltip(valueObject));
        entryBuilder = (D) entryBuilder.setSaveConsumer(newValue -> {
            if (!value.equals(newValue)) Config.getModConfig(modName).getConfig(configName).setValue(key, newValue);
        });
        return entryBuilder.build();
    }

    protected static Text getTooltip(ConfigValueObject<?> valueObject) {
        String header = valueObject.getHeader();
        String description = valueObject.getDescription();
        return Text.of(header + ((!header.isEmpty() && !description.isEmpty()) ? "\n—————\n" : "") + description);
    }
}
