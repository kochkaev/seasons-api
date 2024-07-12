package ru.kochkaev.Seasons4Fabric.event;

import ru.kochkaev.Seasons4Fabric.object.EventObject;

import java.util.List;

public class OnHealEvent extends EventObject {


    public OnHealEvent() {
        super("ON_HEAL");
    }

    @Override
    public void onEvent(List<Object> args) {
        invokeMethods(args);
    }
}
