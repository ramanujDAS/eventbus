package retry;

import exception.RetryLimitExceedException;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class RetryAlgorithm<P , R> {
    final int maxAttempts;
    final int intervalTime;
    final List<RuntimeException> retryableExceptions = new ArrayList<>();
    protected RetryAlgorithm(int maxAttempts, int intervalTime) {
        this.maxAttempts = maxAttempts;
        this.intervalTime = intervalTime;
    }

    public void addRetryableException(RuntimeException exception) {
        retryableExceptions.add(exception);
    }


    public  void retry(Function<P , R> task, P parameter , int attempt){
        try {
            task.apply(parameter);
        } catch (Exception e) {
            if ((e.getCause() != null && (e.getCause() instanceof RuntimeException)) && retryableExceptions.contains(e.getCause()) ) {
                if (attempt < maxAttempts) {
                    attempt++;
                    long timeToWait = getWaitTime();
                    try {
                        Thread.sleep(timeToWait);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    retry(task, parameter, attempt + 1);
                } else {
                    throw new RetryLimitExceedException("Retry limit exceeded");
                }
            } else {
                throw new RuntimeException(e);
            }
            System.out.println("got some error");
        }

    }

     abstract long getWaitTime();

}
