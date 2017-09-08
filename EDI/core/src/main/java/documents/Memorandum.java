package documents;

import abstract_entity.AbstractDocumentEdi;
import categories.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

/*
* Document Memorandum (Служебная записка)
*
* Document contains main topic
* different business processes (and different tasks)
* are created around this topic
*
* Created by kostya on 9/8/2016.
*/

@Entity
@Table(name = "DOC_MEMORANDUM")
public class Memorandum extends AbstractDocumentEdi {

    @Column(name = "memorandum_field")
    private String memorandumField;

    @NotNull(message = "Поле 'Кому' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "whom_id")
    private User whom;

    public Memorandum() {
        super();
        //setDocumentProperty(DocumentProperty.MEMORANDUM);
    }

    public Memorandum(Timestamp date, boolean deletionMark, String number, boolean posted, User author, String theme, String text, String whomString, User whom) {
        super(date, deletionMark, number, posted, author, theme, text, whomString);
        this.whom = whom;
        //setDocumentProperty(DocumentProperty.MEMORANDUM);
    }

    public DocumentProperty getDocumentProperty() {
        return DocumentProperty.MEMORANDUM;
    }

    public String getMemorandumField() {
        return memorandumField;
    }

    public void setMemorandumField(String memorandumField) {
        this.memorandumField = memorandumField;
    }

    public User getWhom() {
        return whom;
    }

    public void setWhom(User whom) {
        this.whom = whom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Memorandum that = (Memorandum) o;
        return Objects.equals(memorandumField, that.memorandumField) && Objects.equals(whom, that.whom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), memorandumField, whom);
    }
}
