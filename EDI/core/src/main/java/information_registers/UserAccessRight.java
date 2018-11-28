package information_registers;

import categories.User;
import enumerations.MetadataType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/*
* Users rights for metadata objects: Categories, Documents
*
*/

@SuppressWarnings("ALL")
@Entity
@Table(schema = "EDI",
        name = "IR_USER_ACCESS_RIGHT",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"metadataType", "user_id"})})
public class UserAccessRight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;  // TODO. I should delete "id" in UserAccessRight or change equals, hashcode

    @NotNull(message = "Поле 'Тип метаданных' должно быть заполнено")
    @Enumerated(EnumType.ORDINAL)
    private MetadataType metadataType;

    @NotNull(message = "Поле 'Пользователь' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "view")
    private boolean view;

    @Column(name = "edit")
    private boolean edit;

    public UserAccessRight() {
    }

    public UserAccessRight(MetadataType metadataType, User user, boolean view, boolean edit) {
        this.metadataType = metadataType;
        this.user = user;
        this.view = view;
        this.edit = edit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetadataType getMetadataType() {
        return metadataType;
    }

    public void setMetadataType(MetadataType metadataType) {
        this.metadataType = metadataType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccessRight that = (UserAccessRight) o;
        return view == that.view &&
                edit == that.edit &&
                Objects.equals(id, that.id) &&
                metadataType == that.metadataType &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {return Objects.hash(id, metadataType, user, view, edit);}
}
