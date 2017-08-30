package business_processes;

import categories.User;
import enumerations.ProcessOrderType;
import enumerations.ProcessResult;
import enumerations.ProcessType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

/*
* After creating and sending task(s) to recipient(s)
* has been created sequence,
* responsible which user should work with task at this very moment
*
* Created by kostya on 9/15/2016.
*/

@SuppressWarnings("ALL")
@EntityListeners({BusinessProcess.class, ExecutorTaskFolderStructure.class, ExecutorTask.class, BusinessProcessSequence.class})
@Entity
@Table(schema = "EDI", name = "BP_SEQUENCE")
public class BusinessProcessSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private java.sql.Timestamp date;

    @ManyToOne
    @JoinColumn(name = "bp_id")
    private BusinessProcess businessProcess;

    @NotNull(message = "Исполнитель бизнес-процесса должен быть заполнен")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User executor;

    @Column(name = "completed")
    private boolean completed;

    @Enumerated(EnumType.ORDINAL)
    private ProcessOrderType orderBp;

    @Enumerated(EnumType.ORDINAL)
    private ProcessResult result;

    @Enumerated(EnumType.ORDINAL)
    private ProcessType processType;

    @Column(name = "viewed")
    private boolean viewed;

    @OneToOne
    @JoinColumn(name = "executorTask_id")
    private ExecutorTask executorTask;

    public BusinessProcessSequence() {
    }

    public BusinessProcessSequence(Timestamp date, BusinessProcess businessProcess, User executor, boolean completed, ProcessOrderType orderBp, ProcessResult result, ProcessType processType, boolean viewed, ExecutorTask executorTask) {
        this.date = date;
        this.businessProcess = businessProcess;
        this.executor = executor;
        this.completed = completed;
        this.orderBp = orderBp;
        this.result = result;
        this.processType = processType;
        this.viewed = viewed;
        this.executorTask = executorTask;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public BusinessProcess getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(BusinessProcess businessProcess) {
        this.businessProcess = businessProcess;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ProcessOrderType getOrderBp() {
        return orderBp;
    }

    public void setOrderBp(ProcessOrderType orderBp) {
        this.orderBp = orderBp;
    }

    public ProcessResult getResult() {
        return result;
    }

    public void setResult(ProcessResult result) {
        this.result = result;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType type) {
        this.processType = type;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public ExecutorTask getExecutorTask() {
        return executorTask;
    }

    public void setExecutorTask(ExecutorTask executorTask) {
        this.executorTask = executorTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessProcessSequence that = (BusinessProcessSequence) o;
        return completed == that.completed && viewed == that.viewed && Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(businessProcess, that.businessProcess) && Objects.equals(executor, that.executor) && orderBp == that.orderBp && result == that.result && processType == that.processType && Objects.equals(executorTask, that.executorTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, businessProcess, executor, completed, orderBp, result, processType, viewed, executorTask);
    }
}
