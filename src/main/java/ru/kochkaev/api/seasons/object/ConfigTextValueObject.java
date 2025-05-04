package ru.kochkaev.api.seasons.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class ConfigTextValueObject extends ConfigValueObject<String> {

    public ConfigTextValueObject(String value, String defaultValue, String header, String description) {
        super(value, defaultValue, header, description);
    }
    public ConfigTextValueObject(String value, String defaultValue, String header, String description, BiConsumer<String, String> onChangeConsumer) {
        super(value, defaultValue, header, description, onChangeConsumer);
    }

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static DynamicRegistryManager.Immutable getRegistries() {
        return SeasonsAPI.getServer().getRegistryManager();
    }
    @NotNull
    Text adventureToMinecraft(Component adventure) {
        final var serializedTree = GsonComponentSerializer.gson().serializeToTree(adventure);
        return Objects.requireNonNull(Text.Serialization.fromJsonTree(
                serializedTree,
                getRegistries()
        ));
    }
    Component minecraftToAdventure(Text minecraft) {
        final var jsonString = Text.Serialization.toJsonString(
                minecraft,
                getRegistries()
        );
        return GsonComponentSerializer.gson().deserialize(jsonString);
    }

    public Component getAdventure(
            List<Pair<String, String>> plainPlaceholders,
            List<Pair<String, Component>> componentPlaceholders
    ) {
        var res = getValue();
        for (var it: plainPlaceholders) {
            res = res.replace("{"+it.left()+"}", mm.escapeTags(it.right()));
        }
        List<TagResolver> allPlaceholders = new ArrayList<>();
        allPlaceholders.addAll(plainPlaceholders.stream().filter(it -> it.left().matches("[!?#]?[a-z0-9_-]*")).map(it -> Placeholder.unparsed(it.left(), it.right())).toList());
        allPlaceholders.addAll(componentPlaceholders.stream().map(it -> Placeholder.component(it.left(), it.right())).toList());
        return mm.deserialize(res, TagResolver.resolver(allPlaceholders.toArray(new TagResolver[0])));
    }
    public Component getAdventure() {
        return getAdventure(new ArrayList<>(), new ArrayList<>());
    }
    public Text getMinecraft(List<Pair<String, String>> plainPlaceholders, List<Pair<String, Component>> componentPlaceholders) {
        return adventureToMinecraft(getAdventure(plainPlaceholders, componentPlaceholders));
    }
    public Text getMinecraft() {
        return getMinecraft(new ArrayList<>(), new ArrayList<>());
    }
}
