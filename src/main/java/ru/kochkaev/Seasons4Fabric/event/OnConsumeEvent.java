package ru.kochkaev.Seasons4Fabric.event;

import ru.kochkaev.Seasons4Fabric.object.EventObject;

public class OnConsumeEvent extends EventObject {

    public OnConsumeEvent() {
        super("ON_CONSUME");
    }

    @Override
    public void onEvent(Object... args) {
        invokeMethods(args);
    }
}
