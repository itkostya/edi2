package impl.enumerations;

import enumerations.ProcessResult;
import enumerations.ProcessType;
import exсeption.ProcessResultNotFoundException;

/*
 * Created by kostya on 1/20/2017.
 */
public enum ProcessResultServiceImpl {

    INSTANCE;

    public ProcessResult getProcessResult(ProcessType processType){

        switch (processType){
            case INFORMATION: return ProcessResult.INFORMED;
            case ACCOMMODATION: return ProcessResult.ACCOMMODATED;
            case EXECUTION: return ProcessResult.COMPLETED;
            case AFFIRMATION: return ProcessResult.AFFIRMED;
            case VISA: return ProcessResult.VISED;
            case PAYMENT: return ProcessResult.DISBURSED;
        }

        throw new ProcessResultNotFoundException(processType);
    }

}
