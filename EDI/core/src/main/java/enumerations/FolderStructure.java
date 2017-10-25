package enumerations;

import business_processes.ExecutorTaskFolderStructure;

import javax.persistence.*;

/*
* Folders for executors tasks
* It could be categories if I decided to develop folders for search
*
* Created by kostya on 12/15/2016.
*/

@EntityListeners({ExecutorTaskFolderStructure.class})
@Table(schema = "EDI", name = "ENUM_EXECUTION_ORDER_TYPE")
public enum FolderStructure {

    INBOX(0, "Inbox", "Входящие"),
    MARKED(1, "Marked", "Отмеченные"),
    SENT(2, "Sent", "Отправленные"),
    DRAFT(3, "Draft", "Черновики"),
    TRASH(4, "Trash", "Корзина");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "EN_NAME")
    private String enName;

    @Column(name = "RU_NAME")
    private String ruName;

    FolderStructure() {
    }

    FolderStructure(long id, String enName, String ruName) {
        this.id = id;
        this.enName = enName;
        this.ruName = ruName;
    }

    @SuppressWarnings("WeakerAccess")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(long id) {
        this.id = id;
    }

    @SuppressWarnings("WeakerAccess")
    public String getEnName() {
        return enName;
    }

    @SuppressWarnings("unused")
    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getRuName() {
        return ruName;
    }

    @SuppressWarnings("unused")
    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    @SuppressWarnings("unused")
    public static FolderStructure findById(long id) throws EnumConstantNotPresentException {
        for (FolderStructure folderStructure : FolderStructure.values()) {
            if (folderStructure.getId() == id) {
                return folderStructure;
            }
        }
        throw new EnumConstantNotPresentException( FolderStructure.class , String.valueOf(id));
    }

    @SuppressWarnings("unused")
    public static FolderStructure findByName(String name) throws EnumConstantNotPresentException {
        for (FolderStructure folderStructure : FolderStructure.values()) {
            if (folderStructure.getEnName().equals(name)) {
                return folderStructure;
            }
        }
        throw new EnumConstantNotPresentException( FolderStructure.class , name);
    }
}
