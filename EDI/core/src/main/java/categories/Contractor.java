package categories;

import abstract_entity.AbstractCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/*
* Contractor can be used for customers and providers
*
* Created by kostya on 11/13/2017.
*/

@Entity
@Table(name = "CAT_CONTRACTOR")
public class Contractor extends AbstractCategory {

    @Column(name = "okpo")
    private String okpo;    // International id code has 12 symbols (not only digits)

    @Column(name = "inn")
    private String inn;     // International id code has 12 symbols (not only digits)

    public Contractor() {
        super();
    }

    public Contractor(String okpo, String inn) {
        super();
        this.okpo = okpo;
        this.inn = inn;
    }

    public Contractor(String name, boolean deletionMark, Long code, boolean isFolder, String okpo, String inn) {
        super(name, deletionMark, code, isFolder);
        this.okpo = okpo;
        this.inn = inn;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Contractor that = (Contractor) o;
        return Objects.equals(okpo, that.okpo) &&
                Objects.equals(inn, that.inn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), okpo, inn);
    }
}
