package exсeption;

/*
* Error when we can't find executor task
*
* Created by kostya on 1/18/2017.
*/

public class ExecutorTaskNotFoundException extends RuntimeException{
    public ExecutorTaskNotFoundException(long executorTaskId) {
        super("executorTaskId = " + executorTaskId);
    }
}
