package ru.kochkaev.Seasons4Fabric.service;

import net.minecraft.server.network.ServerPlayerEntity;
import ru.kochkaev.Seasons4Fabric.IFuncRet;
import ru.kochkaev.Seasons4Fabric.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {

    private static final List<IFuncRet> tasks = new ArrayList<>();
    private static final Map<IFuncRet, List<Object>> tasksMap = new HashMap<>();
    private static List<IFuncRet> forRemove = new ArrayList<>();

    public static void addTask(IFuncRet task, List<Object> args) {
        tasks.add(task);
        tasksMap.put(task, args);
    }
    public static void removeTask(IFuncRet task) {
        forRemove.add(task);
    }

    public static void runTasks() {
        for (IFuncRet task : tasks) {
            tasksMap.put(task, task.function(tasksMap.get(task)));
        }
        for (IFuncRet task : forRemove) {
            tasks.remove(task);
            tasksMap.remove(task);
        }
        forRemove.clear();
    }

}
