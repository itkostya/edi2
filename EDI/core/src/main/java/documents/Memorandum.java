package documents;

import abstract_entity.AbstractDocumentEdi;
import categories.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
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

    public String getMemorandumField() {
        return memorandumField;
    }

    public void setMemorandumField(String memorandumField) {
        this.memorandumField = memorandumField;
    }

    public Memorandum() {
        super();
        setDocumentProperty(DocumentProperty.MEMORANDUM);
    }

    public Memorandum(Timestamp date, boolean deletionMark, String number, boolean posted, User author, User whom, String theme, String text) {
        super(date, deletionMark, number, posted, author, whom, theme, text);
        setDocumentProperty(DocumentProperty.MEMORANDUM);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Memorandum that = (Memorandum) o;
        return Objects.equals(memorandumField, that.memorandumField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), memorandumField);
    }
}
