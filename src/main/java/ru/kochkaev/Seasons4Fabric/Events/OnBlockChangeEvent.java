package ru.kochkaev.Seasons4Fabric.Events;

import ru.kochkaev.Seasons4Fabric.Objects.EventObject;

public class OnBlockChangeEvent extends EventObject {


    protected OnBlockChangeEvent() {
        super("ON_BLOCK_CHANGE");
    }

    @Override
    public void onEvent(Object... args) {
        invokeMethods(args);
    }
}
