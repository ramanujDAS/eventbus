import java.util.Map;

public class Event {
    private final String id;
    private final String name;
    private final Map<String , String> attributes;

    public Event(String id, String name, Map<String, String> attributes) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
    }
}
