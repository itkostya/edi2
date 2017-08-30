package exсeption;

import enumerations.ProcessType;

/*
* Error when we can't find process result
*
* Created by kostya on 1/20/2017.
*/

public class ProcessResultNotFoundException extends RuntimeException {

    public ProcessResultNotFoundException(ProcessType processType) {
        super("ProcessResultNotFoundException = " + processType);
    }
}