import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

class ConsistentExecutor {
    private final Executor[] executor;

    ConsistentExecutor(int nThreads) {
        this.executor = new Executor[nThreads];
        initiateExecutor(nThreads);
    }

    public CompletableFuture<Void> submit(final String topicId, final Runnable task) {
        return CompletableFuture.runAsync(task, executor[topicId.hashCode() % executor.length]);

    }

    public <T> CompletableFuture<T> submit(final String topicId, final Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor[topicId.hashCode() % executor.length]);

    }


    private void initiateExecutor(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            executor[i] = Executors.newSingleThreadExecutor();
        }
    }

}
