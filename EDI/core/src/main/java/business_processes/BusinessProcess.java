package business_processes;

import abstract_entity.AbstractDocumentEdi;
import categories.User;
import enumerations.ProcessResult;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
* New business process starts when user (author) sends task(s) on recipient(s)
*
* Created by kostya on 9/15/2016.
*/

@SuppressWarnings("ALL")
@EntityListeners({BusinessProcess.class, ExecutorTaskFolderStructure.class, ExecutorTask.class, BusinessProcessSequence.class})
@Entity
@Table(schema = "EDI", name = "BP_BUSINESS_PROCESS")
public class BusinessProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Поле 'Дата' должно быть заполнено")
    @Column(name = "date")
    private Timestamp date;

    @Column(name = "completed")
    private boolean completed;

    @NotNull(message = "Поле 'Автор' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "final_date")
    private Timestamp finalDate;

    @NotNull(message = "Поле 'Документ' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "document_id")
    private AbstractDocumentEdi document;

    @NotNull(message = "Поле 'Комментарий' не должно быть null")
    @Size(min = 0, max = 255, message = "Поле 'Комментарий' не должно превышать 255 символов")
    @Column(name = "comment")
    private String comment;  // comment to recipients

    @Enumerated(EnumType.ORDINAL)
    private ProcessResult result;

    @OneToMany(mappedBy = "businessProcess")
    private Set<BusinessProcessSequence> businessProcessSequencesSet = new HashSet<BusinessProcessSequence>();

    @OneToMany(mappedBy = "businessProcess")
    private Set<ExecutorTask> executorTaskSet = new HashSet<ExecutorTask>();

    public BusinessProcess() {
    }

    public BusinessProcess(Timestamp date, boolean completed, User author, Timestamp finalDate, AbstractDocumentEdi document, String comment, ProcessResult result) {
        this.date = date;
        this.completed = completed;
        this.author = author;
        this.finalDate = finalDate;
        this.document = document;
        this.comment = comment;
        this.result = result;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Timestamp getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Timestamp finalDate) {
        this.finalDate = finalDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ProcessResult getResult() {
        return result;
    }

    public void setResult(ProcessResult result) {
        this.result = result;
    }

    public AbstractDocumentEdi getDocument() {
        return document;
    }

    public void setDocument(AbstractDocumentEdi document) {
        this.document = document;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<BusinessProcessSequence> getBusinessProcessSequencesSet() {
        return businessProcessSequencesSet;
    }

    public void setBusinessProcessSequencesSet(Set<BusinessProcessSequence> businessProcessSequencesSet) {
        this.businessProcessSequencesSet = businessProcessSequencesSet;
    }

    public Set<ExecutorTask> getExecutorTaskSet() {
        return executorTaskSet;
    }

    public void setExecutorTaskSet(Set<ExecutorTask> executorTaskSet) {
        this.executorTaskSet = executorTaskSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessProcess that = (BusinessProcess) o;
        return completed == that.completed && Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(author, that.author) && Objects.equals(finalDate, that.finalDate) && Objects.equals(document, that.document) && Objects.equals(comment, that.comment) && result == that.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, completed, author, finalDate, document, comment, result);
    }
}
