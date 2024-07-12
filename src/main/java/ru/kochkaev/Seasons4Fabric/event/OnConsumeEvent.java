package ru.kochkaev.Seasons4Fabric.event;

import ru.kochkaev.Seasons4Fabric.object.EventObject;

import java.util.List;

public class OnConsumeEvent extends EventObject {

    public OnConsumeEvent() {
        super("ON_CONSUME");
    }

    @Override
    public void onEvent(List<Object> args) {
        invokeMethods(args);
    }
}
