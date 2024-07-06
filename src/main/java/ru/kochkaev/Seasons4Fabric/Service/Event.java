package ru.kochkaev.Seasons4Fabric.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.kochkaev.Seasons4Fabric.Objects.EffectObject;
import ru.kochkaev.Seasons4Fabric.Objects.EventObject;

public class Event {

    private static final Map<String, EventObject> events = new HashMap<>();

    public static void registerEvent(EventObject event) {
        events.put(event.getEventID(), event);
    }

    public static EventObject getEventByID(String eventID) {
        return events.get(eventID);
    }
}