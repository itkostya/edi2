package enumerations;

import business_processes.BusinessProcess;
import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;

import javax.persistence.*;

/*
*  User set result after reading document
*/

@EntityListeners({BusinessProcess.class, BusinessProcessSequence.class, ExecutorTask.class})
@Table(schema = "EDI", name = "ENUM_PROCESS_RESULT")
public enum ProcessResult {

    ACCOMMODATED(0, "Accommodated", "Согласовал"),
    AFFIRMED(1, "Affirmed", "Утвердил"),
    DECLINED(2, "Declined", "Отклонил"),
    INFORMED(3, "Informed", "Ознакомился"),
    POSITIVE(4, "Positive", "Положительный"),
    NEGATIVE(5, "Negative", "Отрицательный"),
    COMPLETED(6, "Completed", "Выполнил"),
    CANCELED(7, "Canceled", "Отменил"),
    VISED(8, "Vised", "Завизировал"),
    DISBURSED(9, "Disbursed", "Выплатил");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "EN_NAME")
    private String enName;

    @Column(name = "RU_NAME")
    private String ruName;

    ProcessResult() {
    }

    ProcessResult(long id, String enName, String ruName) {
        this.id = id;
        this.enName = enName;
        this.ruName = ruName;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
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

}
