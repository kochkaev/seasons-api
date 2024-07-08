package ru.kochkaev.Seasons4Fabric.event;

import ru.kochkaev.Seasons4Fabric.object.EventObject;

public class OnBlockChangeEvent extends EventObject {


    public OnBlockChangeEvent() {
        super("ON_BLOCK_CHANGE");
    }

    @Override
    public void onEvent(Object... args) {
        invokeMethods(args);
    }
}
