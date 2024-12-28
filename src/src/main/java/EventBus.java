import exception.InvalidSubscriptionException;
import exception.TopicsNotExistException;
import model.BusEvent;
import model.Subscription;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class EventBus {
    //it will contains topic -> list of events
    private final Map<String, List<BusEvent>> topics;
    private final ConsistentExecutor consistentExecutor;
    //it will contains topic -> (subscriber , subscriptionDetails);
    private final Map<String, Map<String, Subscription>> pullSubscribers;
    //it will contains topic -> (subscriber , subscriptionDetails);
    private final Map<String, Map<String, Subscription>> pushSubscribers;

    public EventBus(int nThreads) {
        this.consistentExecutor = new ConsistentExecutor(nThreads);
        this.pullSubscribers = new ConcurrentHashMap<>();
        this.pushSubscribers = new ConcurrentHashMap<>();
        this.topics = new ConcurrentHashMap<>();
    }

    public void publish(String topic, BusEvent event) {
        consistentExecutor.getThreadFor(topic, () -> publishToBus(topic, event));
    }

    private void publishToBus(String topic, BusEvent event) {
        topics.getOrDefault(topic, new CopyOnWriteArrayList<>()).add(event);
        notifyPushSubscriber(topic, event);
    }

    private CompletionStage<Void> notifyPushSubscriber(String topic, BusEvent event) {
        if (!pushSubscribers.containsKey(topic))
            return CompletableFuture.completedFuture(null);

        final Map<String, Subscription> subscribers = pushSubscribers.get(topic);

        CompletableFuture[] futures = subscribers.values()
                .stream()
                .filter(subscription -> subscription.getTopic().equals(topic))
                .map(subscription -> executeEvent(subscription, event))
                .toArray(CompletableFuture[]::new);

       return CompletableFuture.allOf(futures);

    }

    private CompletionStage<Void> executeEvent(Subscription subscription, BusEvent event) {
        return consistentExecutor
                .getThreadFor(subscription.getTopic() + subscription.getSubscriber(),
                        () -> subscription.getHandler().apply(event)
                )
                .exceptionally(
                throwable -> {
                    System.out.println("exception occurs in subscription " + subscription.getTopic() + " " + subscription.getSubscriber());
                    return null;
                }
        ).thenCompose(Function.identity());

    }
   // it is being used to poll event from topic by subscriber
    public CompletionStage<EventBus> poll(final String topic , final String subscriber) {
        return consistentExecutor.getThreadFor(topic + subscriber, () -> pollBusTopics(topic, subscriber));
    }

    private EventBus pollBusTopics(String topic, String subscriber) {
        if (!topics.containsKey(topic)) {
            throw new TopicsNotExistException("topic " + topic + " does not exist");
        }

        Subscription subscription = pullSubscribers.getOrDefault(topic, new ConcurrentHashMap<>()).get(subscriber);

        if (Objects.isNull(subscription)) {
            throw new InvalidSubscriptionException("subscriber " + subscriber + " does not exist");
        }

        // after polling index needed to be handle for( subscriber , topics)


    }
}


