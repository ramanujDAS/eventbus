import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    private final Map<String , List<Event>> topics;
    private final ConsistentExecutor consistentExecutor ;

    public EventBus(int nThreads ) {
        this.consistentExecutor =  new ConsistentExecutor(nThreads);
        topics = new ConcurrentHashMap<>();
    }
    public void publish(String topic, Event event) {
        consistentExecutor.submit(topic , () -> topics.get(topic).add(event));
    }
}


