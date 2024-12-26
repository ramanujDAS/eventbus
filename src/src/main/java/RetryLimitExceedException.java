public class RetryLimitExceedException extends RuntimeException {
    public RetryLimitExceedException(String message) {
        super(message);
    }
}
