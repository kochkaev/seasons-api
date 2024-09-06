package ru.kochkaev.api.seasons.object;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ConfigSelectionObject<T, L extends List<T>> extends ConfigValueObject<T> {

    private L list;
    private Supplier<L> listGetter;
    private final String selectionType;

    public ConfigSelectionObject(L list, T value, T defaultValue, String header, String description) {
        super(value, defaultValue, header, description);
        this.list = list;
        this.listGetter = null;
        this.selectionType = "Button";
    }
    public ConfigSelectionObject(Supplier<L> listGetter, T value, T defaultValue, String header, String description) {
        super(value, defaultValue, header, description);
        this.listGetter = listGetter;
        this.list = null;
        this.selectionType = "Button";
    }
    public ConfigSelectionObject(L list, T value, T defaultValue, String header, String description, String selectionType) {
        super(value, defaultValue, header, description);
        this.list = list;
        this.listGetter = null;
        this.selectionType = selectionType;
    }
    public ConfigSelectionObject(Supplier<L> listGetter, T value, T defaultValue, String header, String description, String selectionType) {
        super(value, defaultValue, header, description);
        this.listGetter = listGetter;
        this.list = null;
        this.selectionType = selectionType;
    }
    public ConfigSelectionObject(L list, T value, T defaultValue, String header, String description, BiConsumer<T, T> consumer) {
        super(value, defaultValue, header, description, consumer);
        this.list = list;
        this.listGetter = null;
        this.selectionType = "Button";
    }
    public ConfigSelectionObject(Supplier<L> listGetter, T value, T defaultValue, String header, String description, BiConsumer<T, T> consumer) {
        super(value, defaultValue, header, description, consumer);
        this.listGetter = listGetter;
        this.list = null;
        this.selectionType = "Button";
    }
    public ConfigSelectionObject(L list, T value, T defaultValue, String header, String description, String selectionType, BiConsumer<T, T> consumer) {
        super(value, defaultValue, header, description, consumer);
        this.list = list;
        this.listGetter = null;
        this.selectionType = selectionType;
    }
    public ConfigSelectionObject(Supplier<L> listGetter, T value, T defaultValue, String header, String description, String selectionType, BiConsumer<T, T> consumer) {
        super(value, defaultValue, header, description, consumer);
        this.listGetter = listGetter;
        this.list = null;
        this.selectionType = selectionType;
    }

    public L getList() {
        return list!=null ? list : Objects.requireNonNull(listGetter).get();
    }
    public String getSelectionType() {
        return selectionType;
    }

    public void setList(L list) {
        this.list = list;
    }
    public void setList(Supplier<L> listGetter) {
        this.listGetter = listGetter;
    }

    public boolean isDynamic() {
        return listGetter!=null;
    }
}
