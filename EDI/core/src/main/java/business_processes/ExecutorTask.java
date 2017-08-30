package business_processes;

import abstract_entity.AbstractDocumentEdi;
import categories.UploadedFile;
import categories.User;
import enumerations.ProcessResult;
import enumerations.ProcessType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
* Task from author of the business process to executor
*
* Created by kostya on 9/15/2016.
*/
@SuppressWarnings("ALL")
@EntityListeners({BusinessProcess.class, ExecutorTaskFolderStructure.class, ExecutorTask.class, BusinessProcessSequence.class})
@Entity
@Table(schema = "EDI", name = "BP_EXECUTOR_TASK")
public class ExecutorTask {

    // ПометкаУдаления	Номер	Дата	БизнесПроцесс	ТочкаМаршрута	Наименование	Выполнена
    // Автор	Документ	НомерДокумента	ИсполнительСтрокой	ДатаИсполнения	СрокИсполнения
    // Комментарий	ТипПроцесса	Важность	Результат	ПометкаУдаленияАвтор	ПометкаУдаленияИсполнитель
    // ЗакрытаАвтоматически	ЗакрытаПоВремени	ЗапросКомментария	ВыполнитьПриПросмотре
    // ДатаДокумента	Черновик	Делегирующий	Исполнитель	РольИсполнителя	Представление

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Поле 'Дата' должно быть заполнено")
    @Column(name = "date")
    private java.sql.Timestamp date;

    @ManyToOne
    @JoinColumn(name = "bp_id")
    private BusinessProcess businessProcess;

    @Column(name = "completed")
    private boolean completed;

    @NotNull(message = "Поле 'Автор' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @NotNull(message = "Поле 'Документ' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "document_id")
    private AbstractDocumentEdi document;

    @Column(name = "date_completed")
    private java.sql.Timestamp dateCompleted;  // ДатаИсполнения

    @NotNull(message = "Поле 'Комментарий' не должно быть null")
    @Size(min = 0, max = 255, message = "Поле 'Комментарий' не должно превышать 255 символов")
    @Column(name = "comment")
    private String comment;  // comment to recipients

    @Enumerated(EnumType.ORDINAL)
    private ProcessResult result;  // Согласовал, Утвердил , ... , Выплатил

    @Enumerated(EnumType.ORDINAL)
    private ProcessType processType;  // Ознакомление, Согласование, ... ,  Выплата

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;

    @Column(name = "final_date")
    private java.sql.Timestamp finalDate;

    @OneToMany(mappedBy = "executorTask")
    private Set<ExecutorTaskFolderStructure> executorTaskFolderStructureSet = new HashSet<ExecutorTaskFolderStructure>();

    @OneToOne(mappedBy = "executorTask")
    private BusinessProcessSequence businessProcessSequence; // Don't use it in equals/hashCode !!!

    @Column(name = "deleted_by_author",nullable = false)
    private boolean deletedByAuthor;

    @Column(name = "deleted_by_executor",nullable = false)
    private boolean deletedByExecutor;

    @Column(name = "draft", nullable = false)
    private boolean draft;  // Черновик

    @OneToMany(mappedBy = "executorTask")
    private Set<UploadedFile> uploadedFileSet = new HashSet<UploadedFile>();

    public ExecutorTask() {
    }

    public ExecutorTask(Timestamp date, BusinessProcess businessProcess, boolean completed, User author, AbstractDocumentEdi document, Timestamp dateCompleted,
                        String comment, ProcessResult result, ProcessType processType, Timestamp finalDate,
                        User executor, boolean deletedByAuthor, boolean deletedByExecutor, boolean draft) {
        this.date = date;
        this.businessProcess = businessProcess;
        this.completed = completed;
        this.author = author;
        this.document = document;
        this.dateCompleted = dateCompleted;
        this.comment = comment;
        this.result = result;
        this.processType = processType;
        this.executor = executor;
        this.finalDate = finalDate;
        this.deletedByAuthor = deletedByAuthor;
        this.deletedByExecutor = deletedByExecutor;
        this.draft = draft;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public AbstractDocumentEdi getDocument() {
        return document;
    }

    public void setDocument(AbstractDocumentEdi document) {
        this.document = document;
    }

    public Timestamp getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Timestamp dateCompleted) {
        this.dateCompleted = dateCompleted;
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

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType type) {
        this.processType = type;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public Timestamp getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Timestamp finalDate) {
        this.finalDate = finalDate;
    }

    public Set<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureSet() {
        return executorTaskFolderStructureSet;
    }

    public void setExecutorTaskFolderStructureSet(Set<ExecutorTaskFolderStructure> executorTaskFolderStructureSet) {
        this.executorTaskFolderStructureSet = executorTaskFolderStructureSet;
    }

    public BusinessProcessSequence getBusinessProcessSequence() {
        return businessProcessSequence;
    }

    public void setBusinessProcessSequence(BusinessProcessSequence businessProcessSequence) {
        this.businessProcessSequence = businessProcessSequence;
    }

    public boolean isDeletedByAuthor() {
        return deletedByAuthor;
    }

    public void setDeletedByAuthor(boolean deletedByAuthor) {
        this.deletedByAuthor = deletedByAuthor;
    }

    public boolean isDeletedByExecutor() {
        return deletedByExecutor;
    }

    public void setDeletedByExecutor(boolean deletedByExecutor) {
        this.deletedByExecutor = deletedByExecutor;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public Set<UploadedFile> getUploadedFileSet() {
        return uploadedFileSet;
    }

    public void setUploadedFileSet(Set<UploadedFile> uploadedFileSet) {
        this.uploadedFileSet = uploadedFileSet;
    }

    @Override
    //!!! Don't use businessProcessSequence
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutorTask that = (ExecutorTask) o;
        return completed == that.completed && deletedByAuthor == that.deletedByAuthor && deletedByExecutor == that.deletedByExecutor && draft == that.draft && Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(businessProcess, that.businessProcess) && Objects.equals(author, that.author) && Objects.equals(document, that.document) && Objects.equals(dateCompleted, that.dateCompleted) && Objects.equals(comment, that.comment) && result == that.result && processType == that.processType && Objects.equals(executor, that.executor) && Objects.equals(finalDate, that.finalDate);
    }

    @Override
    //!!! Don't use businessProcessSequence
    public int hashCode() {
        return Objects.hash(id, date, businessProcess, completed, author, document, dateCompleted, comment, result, processType, executor, deletedByAuthor, deletedByExecutor, draft, finalDate);
    }
}
