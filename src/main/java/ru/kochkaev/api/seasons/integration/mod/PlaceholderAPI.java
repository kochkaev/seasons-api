package ru.kochkaev.api.seasons.integration.mod;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.PlaceholderHandler;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.kochkaev.api.seasons.SeasonsAPI;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class PlaceholderAPI {

    private final String namespace;

    private PlaceholderAPI(String namespace) {
        this.namespace = namespace;
    }

    public PlaceholderAPI() {
        this.namespace = "seasons";
        SeasonsAPI.getLogger().info("PlaceholderAPI available, integrating!");
    }

    /** Register placeholder use original PlaceholderHandler lambda expression */
    public void register(String namespace, String path, PlaceholderHandler handler) {
        Placeholders.register(Identifier.of(namespace, path), handler);
    }
    /** Register static placeholder use String */
    public void register(String namespace, String path, String placeholder) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(placeholder));
    }
    /** Register static placeholder use Text */
    public void register(String namespace, String path, Text placeholder) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(placeholder));
    }
    /** Register dynamic placeholder use String supplier */
    public void register(String namespace, String path, StringSupplier supplier) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(supplier.get()));
    }
    /** Register dynamic placeholder use Text supplier */
    public void register(String namespace, String path, TextSupplier supplier) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(supplier.get()));
    }
    /** Register dynamic double-parsing placeholder use Text supplier */
    public void registerDoubleParse(String namespace, String path, TextSupplier supplier) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(Placeholders.parseText(supplier.get(), context)));
    }
    /** Register static double-parsing placeholder use Text */
    public void registerDoubleParse(String namespace, String path, Text text) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(Placeholders.parseText(text, context)));
    }

    /** Register placeholder use original PlaceholderHandler lambda expression and local {@link #namespace} */
    public void register(String path, PlaceholderHandler handler) {
        register(this.namespace, path, handler);
    }
    /** Register static placeholder use String and local {@link #namespace} */
    public void register(String path, String placeholder) {
        register(this.namespace, path, placeholder);
    }
    /** Register static placeholder use Text and local {@link #namespace} */
    public void register(String path, Text placeholder) {
        register(this.namespace, path, placeholder);
    }
    /** Register dynamic placeholder use String supplier and local {@link #namespace} */
    public void register(String path, StringSupplier supplier) {
        register(this.namespace, path, supplier);
    }
    /** Register dynamic placeholder use Text supplier and local {@link #namespace} */
    public void register(String path, TextSupplier supplier) {
        register(this.namespace, path, supplier);
    }
    /** Register dynamic double-parsing placeholder use Text supplier and local {@link #namespace} */
    public void registerDoubleParse(String path, TextSupplier supplier) {
        registerDoubleParse(namespace, path, supplier);
    }
    /** Register static double-parsing placeholder use Text and local {@link #namespace} */
    public void registerDoubleParse(String path, Text text) {
        register(namespace, path, text);
    }

    public static PlaceholderAPI regNamespace(String namespace) {
        return new PlaceholderAPI(namespace);
    }

    public Text parseText(Text message) {
        return Placeholders.parseText(message, PlaceholderContext.of(SeasonsAPI.getServer()));
    }
    public Text parseText(Text message, Map<String, Text> placeholders) {
        placeholders.forEach((key, value) -> {
            Placeholders.register(Identifier.of(key), (ctx, text) -> {
                return PlaceholderResult.value(value);
            });
        });
        return Placeholders.parseText(message, PlaceholderContext.of(SeasonsAPI.getServer()));
    }
    public String parseString(String message) {
        return parseText(Text.of(message)).getString();
    }
    public Text parseStringToText(String message) {
        return parseText(Text.of(message));
    }

    public TextSupplier getTextSupplier(Supplier<Text> supplier){
        return supplier::get;
    }
    public StringSupplier getStringSupplier(Supplier<String> supplier){
//        return (StringSupplier) supplier;
        return supplier::get;
    }

    @FunctionalInterface
    public interface TextSupplier extends Supplier<Text> {}
    @FunctionalInterface
    public interface StringSupplier extends Supplier<String> {}
}
