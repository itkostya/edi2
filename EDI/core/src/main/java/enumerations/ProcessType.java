package enumerations;

import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Created by kostya on 9/15/2016.
 */

@EntityListeners({BusinessProcessSequence.class, ExecutorTask.class})
@Table(schema = "EDI", name = "ENUM_PROCESS_TYPE")
public enum ProcessType{

    INFORMATION(0, "Information" , "Ознакомление", Collections.singletonList("Ознакомиться"), ProcessResult.INFORMED),
    ACCOMMODATION(1, "Accommodation" , "Согласование", Arrays.asList("Согласовать", "Отклонить"), ProcessResult.ACCOMMODATED),
    EXECUTION(2, "Execution", "Исполнение", Arrays.asList("Выполнить", "Отказаться"), ProcessResult.COMPLETED),
    AFFIRMATION(3, "Affirmation", "Утверждение", Arrays.asList("Утвердить", "Отклонить"), ProcessResult.AFFIRMED),
    VISA(4,"Visa", "Визирование", Arrays.asList("Завизировать", "Отклонить"), ProcessResult.VISED),
    PAYMENT(5,"Payment", "Выплата", Arrays.asList("Выплатить", "Отклонить"), ProcessResult.DISBURSED);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "EN_NAME")
    private String enName;

    @Column(name = "RU_NAME")
    private String ruName;

    private List<String> availableStatus;

    private ProcessResult processResult;

    ProcessType(long id, String enName, String ruName, List<String> availableStatus, ProcessResult processResult) {
        this.id = id;
        this.enName = enName;
        this.ruName = ruName;
        this.availableStatus = availableStatus;
        this.processResult = processResult;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(long id) {
        this.id = id;
    }

    public String getEnName() {
        return enName;
    }

    @SuppressWarnings("unused")
    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getRuName() {
        return ruName;
    }

    @SuppressWarnings("unused")
    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    @SuppressWarnings("unused")
    public List<String> getAvailableStatus() {
        return availableStatus;
    }

    @SuppressWarnings("unused")
    public void setAvailableStatus(List<String> availableStatus) {
        this.availableStatus = availableStatus;
    }

    public ProcessResult getProcessResult() {
        return processResult;
    }

    @SuppressWarnings("unused")
    public void setProcessResult(ProcessResult processResult) {
        this.processResult = processResult;
    }
}
