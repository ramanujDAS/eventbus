import java.util.function.Function;

public abstract class RetryAlgorithm<P , R> {
    final int maxAttempts;
    final int intervalTime;

    protected RetryAlgorithm(int maxAttempts, int intervalTime) {
        this.maxAttempts = maxAttempts;
        this.intervalTime = intervalTime;
    }

    public  void retry(Function<P , R> task, P parameter , int attempt){
        try {
            task.apply(parameter);
        } catch (Exception e) {
            if (e.getCause() instanceof RetryableException) {
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
