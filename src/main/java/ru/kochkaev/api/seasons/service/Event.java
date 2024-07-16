package ru.kochkaev.api.seasons.service;

import java.util.HashMap;
import java.util.Map;

import ru.kochkaev.api.seasons.object.EventObject;

public class Event {

    private static final Map<String, EventObject> events = new HashMap<>();

    public static void register(EventObject event) {
        events.put(event.getEventID(), event);
    }

    public static EventObject getEventByID(String eventID) {
        return events.get(eventID);
    }
}