package abstract_entity;

/*
* This category has properties used by different categories
*
* Created by kostya on 9/2/2016.
*/

import categories.User;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "deletion_mark")
    private boolean deletionMark;

    @Column(name = "code")
    private Long code;  // String and Long ?

//    @Column(name = "PARENT")
    //   private AbstractCategory parent;

    @Column(name = "is_folder")
    private boolean isFolder;

    public AbstractCategory() {
    }

    protected AbstractCategory(String name) {
        this.name = name;
        this.deletionMark = false;
        this.isFolder = false;
    }

    protected AbstractCategory(String name, boolean deletionMark, Long code, boolean isFolder) {
        this.name = name;
        this.deletionMark = deletionMark;
        this.code = code;
        this.isFolder = isFolder;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public boolean isDeletionMark() {
        return deletionMark;
    }

    @SuppressWarnings("unused")
    public void setDeletionMark(boolean deletionMark) {
        this.deletionMark = deletionMark;
    }

    @SuppressWarnings("unused")
    public Long getCode() {
        return code;
    }

    @SuppressWarnings("unused")
    public void setCode(Long code) {
        this.code = code;
    }

    @SuppressWarnings("unused")
    public boolean isFolder() {
        return isFolder;
    }

    @SuppressWarnings("unused")
    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCategory that = (AbstractCategory) o;
        return deletionMark == that.deletionMark && isFolder == that.isFolder && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, deletionMark, code, isFolder);
    }
}
