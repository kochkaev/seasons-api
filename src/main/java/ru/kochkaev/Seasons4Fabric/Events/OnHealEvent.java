package ru.kochkaev.Seasons4Fabric.Events;

import ru.kochkaev.Seasons4Fabric.Objects.EventObject;

public class OnHealEvent extends EventObject {


    protected OnHealEvent() {
        super("ON_HEAL");
    }

    @Override
    public void onEvent(Object... args) {
        invokeMethods(args);
    }
}
