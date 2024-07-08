package ru.kochkaev.Seasons4Fabric.service;

import java.util.HashMap;
import java.util.Map;

import ru.kochkaev.Seasons4Fabric.object.EventObject;

public class Event {

    private static final Map<String, EventObject> events = new HashMap<>();

    public static void register(Object... args) {
        EventObject event = (EventObject) args[0];
        events.put(event.getEventID(), event);
    }

    public static EventObject getEventByID(String eventID) {
        return events.get(eventID);
    }
}