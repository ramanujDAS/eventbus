package model;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Subscription {
    private final String topic;
    private final String subscriber;
    private final Function<BusEvent, CompletableFuture<Void>> handler;
    private final boolean isRetryable;
    private final int maxRetries;
    private final int currentIndex;

    public Subscription(String topic,
                        String subscriber,
                        Function<BusEvent, CompletableFuture<Void>> handler,
                        boolean isRetryable,
                        int maxRetries,
                        int currentIndex
    ) {
        this.topic = topic;
        this.subscriber = subscriber;
        this.handler = handler;
        this.isRetryable = isRetryable;
        this.maxRetries = maxRetries;
        this.currentIndex = currentIndex;
    }

    public String getTopic() {
        return topic;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public Function<BusEvent, CompletableFuture<Void>> getHandler() {
        return handler;
    }

    public boolean isRetryable() {
        return isRetryable;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}

