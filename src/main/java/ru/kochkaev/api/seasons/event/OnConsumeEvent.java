package ru.kochkaev.api.seasons.event;

import ru.kochkaev.api.seasons.object.EventObject;

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
