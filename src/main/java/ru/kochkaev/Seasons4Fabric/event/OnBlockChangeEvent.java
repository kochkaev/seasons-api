package ru.kochkaev.Seasons4Fabric.event;

import ru.kochkaev.Seasons4Fabric.object.EventObject;

import java.util.List;

public class OnBlockChangeEvent extends EventObject {


    public OnBlockChangeEvent() {
        super("ON_BLOCK_CHANGE");
    }

    @Override
    public void onEvent(List<Object> args) {
        invokeMethods(args);
    }
}
