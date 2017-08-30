package impl.categories;

import abstract_entity.AbstractDocumentEdi;
import business_processes.ExecutorTask;
import categories.UploadedFile;
import hibernate.impl.categories.UploadedFileImpl;

import java.util.List;

/*
 * Created by kostya on 3/31/2017.
 */
public enum UploadedFileServiceImpl {

    INSTANCE;

    public UploadedFile getByFileNameAndDocument(String fileName, AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask) {
        return UploadedFileImpl.INSTANCE.getByFileNameAndDocument(fileName, abstractDocumentEdi, executorTask);
    }

    public List<UploadedFile> getListByDocument(AbstractDocumentEdi abstractDocumentEdi){
        return UploadedFileImpl.INSTANCE.getListByDocument(abstractDocumentEdi);
    }

    public List<UploadedFile> getListByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask){
        return UploadedFileImpl.INSTANCE.getListByDocumentAndExecutorTask(abstractDocumentEdi, executorTask);
    }
}
