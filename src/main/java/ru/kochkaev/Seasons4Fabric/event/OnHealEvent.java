package ru.kochkaev.Seasons4Fabric.event;

import ru.kochkaev.Seasons4Fabric.object.EventObject;

public class OnHealEvent extends EventObject {


    public OnHealEvent() {
        super("ON_HEAL");
    }

    @Override
    public void onEvent(Object... args) {
        invokeMethods(args);
    }
}
