package abstract_entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

/*
* This abstract document has properties used by AbstractDocumentEdi
* It should be used not only by AbstractDocumentEdi (specific by this application),
* but now there is only one implementation
*
* Created by kostya on 9/8/2016.
*/

@MappedSuperclass
public abstract class AbstractDocument {

    @NotNull(message = "Поле 'Дата' должно быть заполнено")
    @Column(name = "date")
    private Timestamp date;

    @Column(name = "deletion_mark")
    private boolean deletionMark;

    @NotNull(message = "Поле 'Номер' должно быть заполнено")
    @Column(name = "number", length = 10)
    private String number;

    @Column(name = "posted")
    private boolean posted;     // Проведен

    public AbstractDocument() {
    }

    AbstractDocument(Timestamp date, boolean deletionMark, String number, boolean posted) {
        this.date = date;
        this.deletionMark = deletionMark;
        this.number = number;
        this.posted = posted;
    }

    public Timestamp getDate() {
        return date;
    }

    @SuppressWarnings("unused")
    public void setDate(Timestamp date) {
        this.date = date;
    }

    @SuppressWarnings("unused")
    public boolean isDeletionMark() {
        return deletionMark;
    }

    @SuppressWarnings("unused")
    public void setDeletionMark(boolean deletionMark) {
        this.deletionMark = deletionMark;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @SuppressWarnings("unused")
    public boolean isPosted() {
        return posted;
    }

    @SuppressWarnings("unused")
    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDocument that = (AbstractDocument) o;
        return deletionMark == that.deletionMark &&
                posted == that.posted &&
                Objects.equals(date, that.date) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, deletionMark, number, posted);

    }
}
