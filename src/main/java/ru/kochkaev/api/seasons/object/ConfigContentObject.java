package ru.kochkaev.api.seasons.object;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConfigContentObject extends HashMap<String, ConfigValueObject<?>> {

    private final Queue<String> queue = new ArrayDeque<>();

//    private String generateMode = "structure";
    private final Consumer<ConfigContentObject> generate;
    private final String copyright;
    private String currentHeader = "";

    public ConfigContentObject(Consumer<ConfigContentObject> generate) {
        this.generate = generate;
        this.copyright = "";
    }
    public ConfigContentObject(Consumer<ConfigContentObject> generate, String copyright) {
        this.generate = generate;
        this.copyright = copyright;
    }

    public void reload() {
        generate.accept(this);
    }

    public Queue<String> getQueue() {
        return queue;
    }

    protected String getGenerated() {
        String generated = Arrays.stream(copyright.split("\n")).map(line -> "# " + line).collect(Collectors.joining("\n"));
        generated += (!generated.isEmpty() ? "\n\n\n" : "");
        var ref = new Object() {
            String tempHeader = "";
        };
        generated += queue.stream().map(key -> {
            ConfigValueObject<?> valueObject = get(key);
            String header = "";
            if (!valueObject.getHeader().equals(ref.tempHeader) && !valueObject.getHeader().isEmpty()) {
                header = "\n\n# * " + valueObject.getHeader();
                ref.tempHeader = valueObject.getHeader();
            }
            return header + (!valueObject.getDescription().isEmpty() ? "\n# "+ String.join("\n#", valueObject.getDescription().split("\n")) : "") +
                        "\n# type: \"" + valueObject.getValue().getClass().getSimpleName() + "\" | default: \"" + valueObject.getDefaultValue() + "\"" +
                        ((valueObject instanceof ConfigSelectionObject<?,? extends List<?>> selectionObject && !selectionObject.isDynamic()) ? "\n# available values: ["+selectionObject.getList().stream().map(value -> "\""+value+"\"").collect(Collectors.joining(", "))+"]" : "") +
                        "\n" + key + ": \"" + valueObject.getValue() + "\"";
            }).collect(Collectors.joining());
        return generated;
    }

    private String getCorrectHeader(String header) {
        if (header.isEmpty()) header = currentHeader;
        if (!header.equals(currentHeader)) currentHeader = header;
        return header;
    }

    // ** Generating

    // * Values
    public ConfigContentObject addValue(String key, Object value) {
        return addValue(key, value, "", "", null);
    }
    public ConfigContentObject addValue(String key, Object value, String description) {
        return addValue(key, value, "", description, null);
    }
    public ConfigContentObject addValue(String key, Object value, String header, String description) {
        return addValue(key, value, header, description, null);
    }
    public <T> ConfigContentObject addValue(String key, T value, String header, String description, BiConsumer<T, T> consumer) {
        header = getCorrectHeader(header);
        put(key, new ConfigValueObject<>(value, value, header, description, consumer));
        queue.add(key);
        return this;
    }

    // * Selection buttons
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionButton(String key, S supplier, T def){
        return addDynamicSelectionButton(key, supplier, def, "", "", null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionButton(String key, S supplier, T def, String description){
        return addDynamicSelectionButton(key, supplier, def, "", description, null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionButton(String key, S supplier, T def, String description, String header){
        return addDynamicSelectionButton(key, supplier, def, header, description, null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionButton(String key, S supplier, T def, String header, String description, BiConsumer<T, T> consumer){
        header = getCorrectHeader(header);
        put(key, new ConfigSelectionObject<>(supplier, def, def, header, description, "Button", consumer));
        queue.add(key);
        return this;
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionButton(String key, L list, T def){
        return addStaticSelectionButton(key, list, def, "", "", null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionButton(String key, L list, T def, String description){
        return addStaticSelectionButton(key, list, def, "", description, null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionButton(String key, L list, T def, String header, String description){
        return addStaticSelectionButton(key, list, def, header, description, null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionButton(String key, L list, T def, String header, String description, BiConsumer<T, T> consumer){
        header = getCorrectHeader(header);
        put(key, new ConfigSelectionObject<>(list, def, def, header, description, "Button", consumer));
        queue.add(key);
        return this;
    }

    // * Selection dropdowns
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionDropdown(String key, S supplier, T def){
        return addDynamicSelectionDropdown(key, supplier, def, "", "", null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionDropdown(String key, S supplier, T def, String description){
        return addDynamicSelectionDropdown(key, supplier, def, "", description, null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionDropdown(String key, S supplier, T def, String header, String description){
        return addDynamicSelectionDropdown(key, supplier, def, header, description, null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionDropdown(String key, S supplier, T def, String header, String description, BiConsumer<T, T> consumer){
        header = getCorrectHeader(header);
        put(key, new ConfigSelectionObject<>(supplier, def, def, header, description, "Dropdown", consumer));
        queue.add(key);
        return this;
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionDropdown(String key, L list, T def){
        return addStaticSelectionDropdown(key, list, def, "", "", null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionDropdown(String key, L list, T def, String description){
        return addStaticSelectionDropdown(key, list, def, "", description, null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionDropdown(String key, L list, T def, String header, String description){
        return addStaticSelectionDropdown(key, list, def, header, description, null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionDropdown(String key, L list, T def, String header, String description, BiConsumer<T, T> consumer){
        header = getCorrectHeader(header);
        put(key, new ConfigSelectionObject<>(list, def, def, header, description, "Dropdown", consumer));
        queue.add(key);
        return this;
    }

    // * Selection suggestion entries
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionSuggestion(String key, S supplier, T def){
        return addDynamicSelectionSuggestion(key, supplier, def, "", "", null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionSuggestion(String key, S supplier, T def, String description){
        return addDynamicSelectionSuggestion(key, supplier, def, "", description, null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionSuggestion(String key, S supplier, T def, String header, String description){
        return addDynamicSelectionSuggestion(key, supplier, def, header, description, null);
    }
    public <T, S extends Supplier<List<T>>> ConfigContentObject addDynamicSelectionSuggestion(String key, S supplier, T def, String header, String description, BiConsumer<T, T> consumer){
        header = getCorrectHeader(header);
        put(key, new ConfigSelectionObject<>(supplier, def, def, header, description, "Suggestion", consumer));
        queue.add(key);
        return this;
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionSuggestion(String key, L list, T def){
        return addStaticSelectionSuggestion(key, list, def, "", "", null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionSuggestion(String key, L list, T def, String description){
        return addStaticSelectionSuggestion(key, list, def, "", description, null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionSuggestion(String key, L list, T def, String header, String description){
        return addStaticSelectionSuggestion(key, list, def, header, description, null);
    }
    public <T, L extends List<T>> ConfigContentObject addStaticSelectionSuggestion(String key, L list, T def, String header, String description, BiConsumer<T, T> consumer){
        header = getCorrectHeader(header);
        put(key, new ConfigSelectionObject<>(list, def, def, header, description, "Suggestion", consumer));
        queue.add(key);
        return this;
    }

    public ConfigContentObject addHeader(String header){
        currentHeader = header;
        return this;
    }
}

//  This object was created by @kochkaev
//    - GitHub: https://github.com/kochkaev/
//    - VK: https://vk.com/kleverdi/
//    - YouTube: https://youtube.com/@kochkaev/
//    - Contact email: kleverdi@vk.com