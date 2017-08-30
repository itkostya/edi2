package business_processes;

import categories.User;
import enumerations.FolderStructure;

import javax.persistence.*;
import java.util.Objects;

/*
* Structure of folders, whrere tasks are situated
*
* Created by kostya on 12/15/2016.
*/
@SuppressWarnings("ALL")
@EntityListeners({BusinessProcess.class,ExecutorTaskFolderStructure.class, ExecutorTask.class, BusinessProcessSequence.class})
@Entity
@Table(schema = "EDI", name = "BP_EXECUTOR_TASK_FOLDER_STRUCTURE")
public class ExecutorTaskFolderStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private FolderStructure folder;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Overestimate data storage, but good for queries
    private User user;

    @ManyToOne
    @JoinColumn(name = "executor_task_id")
    private ExecutorTask executorTask;

    @Column(name = "marked",nullable = false)
    private boolean marked;        // Flag

    public ExecutorTaskFolderStructure() {
    }

    public ExecutorTaskFolderStructure(FolderStructure folder, User user, ExecutorTask executorTask, boolean marked) {
        this.folder = folder;
        this.user = user;
        this.executorTask = executorTask;
        this.marked = marked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FolderStructure getFolder() {
        return folder;
    }

    public void setFolder(FolderStructure folder) {
        this.folder = folder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExecutorTask getExecutorTask() {
        return executorTask;
    }

    public void setExecutorTask(ExecutorTask executorTask) {
        this.executorTask = executorTask;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutorTaskFolderStructure that = (ExecutorTaskFolderStructure) o;
        return Objects.equals(id, that.id) && marked == that.marked && folder == that.folder && Objects.equals(user, that.user) && Objects.equals(executorTask, that.executorTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, folder, user, executorTask, marked);
    }
}
