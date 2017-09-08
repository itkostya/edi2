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

    @Size(min = 1, max = 100, message = "Поле 'Тема' должно быть заполнено и не должно превышать 100 символов")
    @Column(name = "theme", nullable = false)
    private String theme;

    @Size(min = 1, max = 255, message = "Поле 'Текст' должно быть заполнено и не должно превышать 255 символов")
    @Column(name = "text_doc")
    private String text;

    @Size(min = 1, max = 255, message = "Поле 'Кому(строкой)' должно быть заполнено и не должно превышать 255 символов")
    @Column(name = "whom_string")
    private String whomString;

    @OneToMany(mappedBy = "document")
    private Set<BusinessProcess> businessProcessSet = new HashSet<>();

    @OneToMany(mappedBy = "document")
    private Set<UploadedFile> uploadedFileSet = new HashSet<>();

    public AbstractDocumentEdi() {
        super();
    }

    public AbstractDocumentEdi(Timestamp date, boolean deletionMark, String number, boolean posted, User author, String theme, String text, String whomString) {
        super(date, deletionMark, number, posted);
        this.author = author;
        this.theme = theme;
        this.text = text;
        this.whomString = whomString;
    }

    public abstract DocumentProperty getDocumentProperty();

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

    public String getWhomString() {
        return whomString;
    }

    public void setWhomString(String whomString) {
        this.whomString = whomString;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractDocumentEdi that = (AbstractDocumentEdi) o;
        return Objects.equals(id, that.id) && Objects.equals(author, that.author)  && Objects.equals(theme, that.theme) && Objects.equals(text, that.text)&& Objects.equals(whomString, that.whomString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, author, theme, text, whomString);
    }

    public String getDocumentView(String dateFormat) {
        return "" + getDocumentProperty().getRuName() + " " + getNumber() + " от " + new SimpleDateFormat(dateFormat).format(getDate());
    }

}



