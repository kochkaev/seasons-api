package ru.kochkaev.api.seasons.object;

import java.util.function.BiConsumer;

public class ConfigValueObject<T> {
    private T value;
    private final T defaultValue;
    private String header;
    private String description;
    private BiConsumer<T, T> onChangeConsumer;

    public ConfigValueObject(T value, T defaultValue, String header, String description) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.header = header;
        this.description = description;
        this.onChangeConsumer = null;
    }
    public ConfigValueObject(T value, T defaultValue, String header, String description, BiConsumer<T, T> onChangeConsumer) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.header = header;
        this.description = description;
        this.onChangeConsumer = onChangeConsumer;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setHeader(String header) {
        this.header = header;
    }
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        T oldValue = this.value;
        T newValue = (T) value;
        this.value = newValue;
        onChange(oldValue, newValue);
    }

    public String getDescription() {
        return description;
    }
    public String getHeader() {
        return header;
    }
    public T getValue() {
        return value;
    }
    public T getDefaultValue() {
        return defaultValue;
    }

    public void setOnChangeConsumer(BiConsumer<T, T> onChangeConsumer) {
        this.onChangeConsumer = onChangeConsumer;
    }
    public void delOnChangeConsumer() {
        this.onChangeConsumer = null;
    }
    public BiConsumer<T, T> getOnChangeConsumer() {
        return onChangeConsumer;
    }
    public void onChange(T oldValue, T newValue) {
        if (onChangeConsumer != null) onChangeConsumer.accept(oldValue, newValue);
    }
}
