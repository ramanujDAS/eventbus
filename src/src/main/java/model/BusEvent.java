package model;

import java.util.Map;

public class BusEvent {
    private final String id;
    private final String name;
    private final Map<String, String> attributes;

    public BusEvent(String id, String name, Map<String, String> attributes) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
    }

}
