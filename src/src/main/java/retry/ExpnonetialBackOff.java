package retry;

public class ExpnonetialBackOff<P , R> extends RetryAlgorithm<P , R> {


    public ExpnonetialBackOff(int maxAttempts) {
        super(maxAttempts, -1);
    }


    @Override
    long getWaitTime() {
        return (long) Math.pow(2, maxAttempts - 1 ) * 1000;
    }
}


