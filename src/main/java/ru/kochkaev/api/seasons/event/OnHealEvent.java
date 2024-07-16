package ru.kochkaev.api.seasons.event;

import ru.kochkaev.api.seasons.object.EventObject;

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
