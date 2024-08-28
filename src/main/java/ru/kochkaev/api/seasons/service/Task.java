package ru.kochkaev.api.seasons.service;

import ru.kochkaev.api.seasons.util.functional.IFuncRet;
import ru.kochkaev.api.seasons.util.map.Map1Key2Values;

import java.util.*;

public class Task {

    private static final Map1Key2Values<Object, IFuncRet, List<Object>> tasks = new Map1Key2Values<>();
    private static final List<Object> forRemove = new ArrayList<>();

    public static void addTask(Object key, IFuncRet task, List<Object> args) {
        tasks.put(key, task, args);
    }
    public static void addTask(IFuncRet task, List<Object> args) {
        addTask(task, task, args);
    }
    public static void removeTask(Object key) {
        forRemove.add(key);
    }

    public static void runTasks() {
        for (IFuncRet task : tasks.getFirstValuesSet()) {
            tasks.setSecond(task, task.function(tasks.getSecond(task)));
        }
        for (Object key : forRemove) {
            tasks.remove(key);
        }
        forRemove.clear();
    }

}
