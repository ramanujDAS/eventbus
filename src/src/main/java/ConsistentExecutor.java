import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

class ConsistentExecutor {
    private final Executor[] executor;

    ConsistentExecutor(int nThreads) {
        this.executor = new Executor[nThreads];
        initiateExecutor(nThreads);
    }

    public CompletableFuture<Void> getThreadFor(final String topicId, final Runnable task) {
        return CompletableFuture.runAsync(task, executor[topicId.hashCode() % executor.length]);

    }
    public <T> CompletableFuture<T> getThreadFor(final String topicId, final Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor[topicId.hashCode() % executor.length]);

    }
    public <T> CompletableFuture<T> getThreadFor(final String topicId, final CompletableFuture<T> task) {
        return CompletableFuture.supplyAsync(()->task , executor[topicId.hashCode() % executor.length]).thenCompose(Function.identity());

    }
    private void initiateExecutor(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            executor[i] = Executors.newSingleThreadExecutor();
        }
    }



}
