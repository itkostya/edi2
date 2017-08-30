package categories;

/*
* Users department in the company
*
* Created by kostya on 02.09.2016
*
*/

import abstract_entity.AbstractCategory;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "EDI", name = "CAT_DEPARTMENT")
public class Department extends AbstractCategory {

    @OneToMany(mappedBy = "department")
    private Set<User> users = new HashSet<>();

    public Department() {
        super();
    }

    public Department(String name) {
        super(name);
    }

    public Department(String name, boolean deletionMark, Long code, boolean isFolder) {
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

}
