package exсeption;

/*
* Error when we can't find folder structure
*
* Created by kostya on 2/1/2017.
*/

public class ExecutorTaskFolderStructureException extends RuntimeException {
    public ExecutorTaskFolderStructureException(long executorTaskId) {
        super("executorTaskId = " + executorTaskId);
    }
}
