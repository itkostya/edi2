package categories;

import abstract_entity.AbstractCategory;
import abstract_entity.AbstractDocumentEdi;
import business_processes.ExecutorTask;

import javax.persistence.*;
import java.util.Objects;

/*
* Attached files by users
*
*  Created by kostya on 3/30/2017.
*/

@Entity
@Table(schema = "EDI", name = "CAT_UPLOADED_FILE")
public class UploadedFile extends AbstractCategory
        //implements Cloneable
{

    @Column(name = "FILE_NAME", length = 32)
    private String fileName; // unique - created with MD5 (depends on file consistence)

    @Column(name = "FILE_PATH")
    private String filePath;  // Absolute path

    @ManyToOne
    @JoinColumn(name = "document_id")
    // Actually it's Many to Many -
    // I don't think it's a good idea to add new entity (classes, tables)
    // "InsertedFile" (column UploadedFile, column AbstractDocumentEdi)
    // It's make sense only if you store files in database
    private AbstractDocumentEdi document;

    @ManyToOne
    @JoinColumn(name = "executor_task_id")
    private ExecutorTask executorTask;

    public UploadedFile() {
    }

    public UploadedFile(String name, boolean deletionMark, Long code, boolean isFolder, String fileName, String filePath, AbstractDocumentEdi document, ExecutorTask executorTask) {
        super(name, deletionMark, code, isFolder);
        this.fileName = fileName;
        this.filePath = filePath;
        this.document = document;
        this.executorTask = executorTask;
    }

    public String getFileName() {
        return fileName;
    }

    @SuppressWarnings("unused")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @SuppressWarnings("unused")
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @SuppressWarnings("unused")
    public AbstractDocumentEdi getDocument() {
        return document;
    }

    public void setDocument(AbstractDocumentEdi document) {
        this.document = document;
    }

    @SuppressWarnings("unused")
    public ExecutorTask getExecutorTask() {
        return executorTask;
    }

    public void setExecutorTask(ExecutorTask executorTask) {
        this.executorTask = executorTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UploadedFile that = (UploadedFile) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(filePath, that.filePath) &&
                Objects.equals(document, that.document) &&
                Objects.equals(executorTask, that.executorTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileName, filePath, document, executorTask);
    }
}
