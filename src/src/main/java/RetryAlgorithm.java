import java.util.function.Function;

public abstract class RetryAlgorithm<P , R> {

    public abstract void retry(Function<P , R> task, P parameter , int attempt);
}
