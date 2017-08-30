package documents;

import javax.persistence.*;
import java.util.Objects;

/*
* Final class contain max document's numbers
* depend on document type and prefix
*
* Created by kostya on 2/3/2017.
*/

@Entity
@Table(schema = "EDI", name = "NUMERATION")
public final class DocumentNumeration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "document_property")
    private DocumentProperty documentProperty;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "number")
    private Long number;

    public DocumentNumeration() {
    }

    public DocumentNumeration(DocumentProperty documentProperty, String prefix, Long number) {
        this.documentProperty = documentProperty;
        this.prefix = prefix;
        this.number = number;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public DocumentProperty getDocumentProperty() {
        return documentProperty;
    }

    @SuppressWarnings("unused")
    public void setDocumentProperty(DocumentProperty documentProperty) {
        this.documentProperty = documentProperty;
    }

    public String getPrefix() {
        return prefix;
    }

    @SuppressWarnings("unused")
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentNumeration that = (DocumentNumeration) o;
        return documentProperty == that.documentProperty &&
                Objects.equals(prefix, that.prefix) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentProperty, prefix, number);
    }
}

