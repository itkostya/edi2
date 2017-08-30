package abstract_entity;

import business_processes.BusinessProcess;
import categories.UploadedFile;
import categories.User;
import documents.DocumentProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
* Main document for EDI application
* This document has properties used by different documents
*
* Created by kostya on 9/8/2016.
*/

//@MappedSuperclass
@Entity
@Table(name = "DOC_ABSTRACT_DOCUMENT_EDI"
        , uniqueConstraints = @UniqueConstraint(columnNames = "number")
)
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AbstractDocumentEdi extends AbstractDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Поле 'Автор' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @NotNull(message = "Поле 'Кому' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "whom_id")
    private User whom;

    @Size(min = 1, max = 100, message = "Поле 'Тема' должно быть заполнено и не должно превышать 100 символов")
    @Column(name = "theme", nullable = false)
    private String theme;

    @Size(min = 1, max = 255, message = "Поле 'Текст' должно быть заполнено и не должно превышать 255 символов")
    @Column(name = "text_doc")
    private String text;

    @OneToMany(mappedBy = "document")
    private Set<BusinessProcess> businessProcessSet = new HashSet<>();

    @OneToMany(mappedBy = "document")
    private Set<UploadedFile> uploadedFileSet = new HashSet<>();

    //@Column(name = "document_property")
    private static DocumentProperty documentProperty;

    public AbstractDocumentEdi() {
        super();
    }

    public AbstractDocumentEdi(Timestamp date, boolean deletionMark, String number, boolean posted, User author, User whom, String theme, String text) {
        super(date, deletionMark, number, posted);
        this.author = author;
        this.whom = whom;
        this.theme = theme;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    @SuppressWarnings("unused")
    public void setAuthor(User author) {
        this.author = author;
    }

    public User getWhom() {
        return whom;
    }

    public void setWhom(User whom) {
        this.whom = whom;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @SuppressWarnings("unused")
    public Set<BusinessProcess> getBusinessProcessSet() {
        return businessProcessSet;
    }

    @SuppressWarnings("unused")
    public void setBusinessProcessSet(Set<BusinessProcess> businessProcessSet) {
        this.businessProcessSet = businessProcessSet;
    }

    @SuppressWarnings("unused")
    public Set<UploadedFile> getUploadedFileSet() {
        return uploadedFileSet;
    }

    @SuppressWarnings("unused")
    public void setUploadedFileSet(Set<UploadedFile> uploadedFileSet) {
        this.uploadedFileSet = uploadedFileSet;
    }

    public DocumentProperty getDocumentProperty() {
        return documentProperty;
    }

    protected void setDocumentProperty(DocumentProperty documentProperty) {
        this.documentProperty = documentProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractDocumentEdi that = (AbstractDocumentEdi) o;
        return Objects.equals(id, that.id) && Objects.equals(author, that.author) && Objects.equals(whom, that.whom) && Objects.equals(theme, that.theme) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, author, whom, theme, text);
    }

    public String getDocumentView(String dateFormat) {
        return "" + getDocumentProperty().getRuName() + " " + getNumber() + " от " + new SimpleDateFormat(dateFormat).format(getDate());
    }

}



