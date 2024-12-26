import java.util.function.Function;

public class ExpnonetialBackOff<P , R> extends RetryAlgorithm<P , R> {

    private final int maxAttempts;

    public ExpnonetialBackOff(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
    @Override
    public void retry(Function<P, R> task , P parameter , int attempt) {

        try{
           task.apply(parameter);
        }catch (Exception e){
            if(e.getCause() instanceof RetryableException){
               if(attempt < maxAttempts){
                   attempt++;
                   long timeToWait = (long) (Math.pow(2, attempt - 1 ) * 1000);
                   try {
                       Thread.sleep(timeToWait);
                   } catch (InterruptedException ex) {
                       throw new RuntimeException(ex);
                   }
                   retry(task,parameter , attempt + 1 );
               }else{
                   throw new RetryLimitExceedException("Retry limit exceeded");
               }
            }
            else {
                throw new RuntimeException(e);
            }
            System.out.println("got some error");
        }

    }
}


