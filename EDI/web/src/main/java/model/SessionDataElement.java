package model;

import abstract_entity.AbstractDocumentEdi;
import business_processes.ExecutorTask;

import java.util.Objects;

/*
 * Created by kostya on 2/23/2017.
 */
public class SessionDataElement {

    private Long id;                            // Random id
    private ElementStatus elementStatus;
    private AbstractDocumentEdi documentEdi;
    private ExecutorTask executorTask;
    private String errorMessage;

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    public ElementStatus getElementStatus() {
        return elementStatus;
    }

    public void setElementStatus(ElementStatus elementStatus) {
        this.elementStatus = elementStatus;
    }

    public AbstractDocumentEdi getDocumentEdi() {
        return documentEdi;
    }

    public void setDocumentEdi(AbstractDocumentEdi documentEdi) {
        this.documentEdi = documentEdi;
    }

    public ExecutorTask getExecutorTask() {
        return executorTask;
    }

    public void setExecutorTask(ExecutorTask executorTask) {
        this.executorTask = executorTask;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SessionDataElement() {
    }

    public SessionDataElement(Long id, ElementStatus elementStatus) {
        this.id = id;
        this.elementStatus = elementStatus;
    }

    public SessionDataElement(Long id, ElementStatus elementStatus, AbstractDocumentEdi documentEdi, String errorMessage) {
        this.id = id;
        this.elementStatus = elementStatus;
        this.documentEdi = documentEdi;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionDataElement that = (SessionDataElement) o;
        return Objects.equals(id, that.id) &&
                elementStatus == that.elementStatus &&
                Objects.equals(documentEdi, that.documentEdi) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, elementStatus, documentEdi, errorMessage);
    }
}
