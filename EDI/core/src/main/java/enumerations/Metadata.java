package enumerations;

import javax.persistence.*;

@Table(schema = "EDI", name = "ENUM_METADATA")
public enum Metadata {

    CATEGORY(0, "Category"),
    DOCUMENT(1, "Document");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "EN_NAME")
    private String enName;

    Metadata() {
    }

    Metadata(long id, String enName) {
        this.id = id;
        this.enName = enName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }
}
