import java.util.function.Function;

public class PeriodicRetry<P, R> extends RetryAlgorithm<P, R> {


    public PeriodicRetry(int maxAttempts, int intervalTime) {
        super(maxAttempts, intervalTime);
    }

    @Override
    long getWaitTime() {
        return intervalTime;
    }



}
