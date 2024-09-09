package ru.kochkaev.api.seasons.provider;

import ru.kochkaev.api.seasons.util.map.Map1Key2Values;

import java.util.*;
import java.util.function.Function;

public class Task {

    private static final Map1Key2Values<Object, Function<List<?>, List<?>>, List<?>> tasks = new Map1Key2Values.HashMap1Key2Values<>();
    private static final List<Object> forRemove = new ArrayList<>();

    public static void addTask(Object key, Function<List<?>, List<?>> task, List<Object> args) {
        tasks.put(key, task, args);
    }
    public static void addTask(Function<List<?>, List<?>> task, List<Object> args) {
        addTask(task, task, args);
    }
    public static void removeTask(Object key) {
        forRemove.add(key);
    }
    public static Map1Key2Values<Object, Function<List<?>, List<?>>, List<?>> getTasks() {
        return tasks;
    }

    public static void runTasks() {
        for (Object key : tasks.getKeySet()) {
            tasks.setSecond(key, tasks.getFirst(key).apply(tasks.getSecond(key)));
        }
        for (Object key : forRemove) {
            tasks.remove(key);
        }
        forRemove.clear();
    }

}
