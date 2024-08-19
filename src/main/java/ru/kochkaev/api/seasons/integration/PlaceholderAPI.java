package ru.kochkaev.api.seasons.integration;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.PlaceholderHandler;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.kochkaev.api.seasons.SeasonsAPI;
import ru.kochkaev.api.seasons.util.functional.IFuncStringRet;
import ru.kochkaev.api.seasons.util.functional.IFuncTextRet;

public class PlaceholderAPI {

    private final String namespace;

    private PlaceholderAPI(String namespace) {
        this.namespace = namespace;
    }

    public PlaceholderAPI() {
        this.namespace = "seasons";
        SeasonsAPI.getLogger().info("PlaceholderAPI available, integrating!");
    }

    /** Register placeholder use original PlaceholderHandler lambda function */
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
    /** Register dynamic placeholder use IFuncStringRet simple lambda function */
    public void register(String namespace, String path, IFuncStringRet lmbd) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(lmbd.function()));
    }
    /** Register dynamic placeholder use IFuncTextRet simple lambda function */
    public void register(String namespace, String path, IFuncTextRet lmbd) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(lmbd.function()));
    }
    /** Register dynamic double-parsing placeholder use IFuncTextRet simple lambda function */
    public void registerDoubleParse(String namespace, String path, IFuncTextRet lmbd) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(Placeholders.parseText(lmbd.function(), context)));
    }
    /** Register static double-parsing placeholder use Text */
    public void registerDoubleParse(String namespace, String path, Text text) {
        register(namespace, path, (context, arg) -> PlaceholderResult.value(Placeholders.parseText(text, context)));
    }

    /** Register placeholder use original PlaceholderHandler lambda function and local {@link #namespace} */
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
    /** Register dynamic placeholder use IFuncStringRet simple lambda function and local {@link #namespace} */
    public void register(String path, IFuncStringRet lmbd) {
        register(this.namespace, path, lmbd);
    }
    /** Register dynamic placeholder use IFuncTextRet simple lambda function and local {@link #namespace} */
    public void register(String path, IFuncTextRet lmbd) {
        register(this.namespace, path, lmbd);
    }
    /** Register dynamic double-parsing placeholder use IFuncTextRet simple lambda function and local {@link #namespace} */
    public void registerDoubleParse(String path, IFuncTextRet lmbd) {
        registerDoubleParse(namespace, path, lmbd);
    }
    /** Register static double-parsing placeholder use Text and local {@link #namespace} */
    public void registerDoubleParse(String path, Text text) {
        register(namespace, path, text);
    }

    public static PlaceholderAPI regNamespace(String namespace) {
        return new PlaceholderAPI(namespace);
    }

    public Text parseText(Text message) {
        Text ret =  Placeholders.parseText(message, PlaceholderContext.of(SeasonsAPI.getServer()));
        return ret;
    }
    public String parseString(String message) {
        return parseText(Text.of(message)).getString();
    }
}
