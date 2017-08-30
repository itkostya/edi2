package enumerations;

import business_processes.BusinessProcessSequence;

import javax.persistence.*;

/*
*  Users should do something after or together
*  (for BusinessProcessSequence)
*
* Created by kostya on 9/15/2016.
*/
@EntityListeners({BusinessProcessSequence.class})
@Table(schema = "EDI", name = "ENUM_EXECUTION_ORDER_TYPE")
public enum ProcessOrderType {

    AFTER(0, "After", "После", "Ждать"),
    TOGETHER(1, "Together", "Вместе", "Не ждать");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "EN_NAME")
    private String enName;

    @Column(name = "RU_NAME")
    private String ruName;

    private String ruDescription;

    ProcessOrderType() {
    }

    ProcessOrderType(long id, String enName, String ruName, String ruDescription) {
        this.id = id;
        this.enName = enName;
        this.ruName = ruName;
        this.ruDescription = ruDescription;
    }

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

    @SuppressWarnings("unused")
    public String getRuName() {
        return ruName;
    }

    @SuppressWarnings("unused")
    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public String getRuDescription() {
        return ruDescription;
    }

    @SuppressWarnings("unused")
    public void setRuDescription(String ruDescription) {
        this.ruDescription = ruDescription;
    }
}
