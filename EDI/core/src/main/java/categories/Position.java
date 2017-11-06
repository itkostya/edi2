package categories;

import abstract_entity.AbstractCategory;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/*
* Users positions in the company
*
* Created by kostya on 9/2/2016.
*/

@Entity
@Table(schema = "EDI", name = "CAT_POSITION")
public class Position extends AbstractCategory{

    @OneToMany(mappedBy = "position")
    private transient Set<User> users = new HashSet<>();

    public Position() {
        super();
    }

    public Position(String name) {
        super(name);
    }

    public Position(String name, boolean deletionMark, Long code, boolean isFolder) {
        super(name, deletionMark, code, isFolder);
    }

    @SuppressWarnings("unused")
    public Set<User> getUsers() {
        return users;
    }

    @SuppressWarnings("unused")
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Position{" +
                "users=" + users +
                '}';
    }
}
