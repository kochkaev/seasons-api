package ru.kochkaev.Seasons4Fabric.Events;

import ru.kochkaev.Seasons4Fabric.Objects.EventObject;

public class OnConsumeEvent extends EventObject {

    OnConsumeEvent() {
        super("ON_CONSUME");
    }

    @Override
    public void onEvent(Object... args) {
        invokeMethods(args);
    }
}
