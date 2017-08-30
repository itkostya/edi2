package hibernate.impl.categories;

import abstract_entity.AbstractDocumentEdi;
import business_processes.ExecutorTask;
import categories.UploadedFile;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;

/*
* Created by kostya on 3/30/2017.
*/
public enum UploadedFileImpl implements HibernateDAO<UploadedFile>{

    INSTANCE;

    public void save(UploadedFile uploadedFile) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(uploadedFile);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(UploadedFile uploadedFile) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(uploadedFile);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(UploadedFile uploadedFile) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(uploadedFile);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public UploadedFile getByFileNameAndDocument(String fileName, AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask){

        Session session = HibernateUtil.getSession();

        Query query = session.createQuery((Objects.isNull(executorTask)?
                "from UploadedFile where fileName =:fileName and document.id =:abstractDocumentEdiId and executorTask is null" :
                "from UploadedFile where fileName =:fileName and document.id =:abstractDocumentEdiId and executorTask.id =:executorTaskId"));
        query.setParameter("fileName", fileName);
        query.setParameter("abstractDocumentEdiId", abstractDocumentEdi.getId());
        if (Objects.nonNull(executorTask)) query.setParameter("executorTaskId", executorTask.getId());
        UploadedFile uploadedFile = (UploadedFile) query.uniqueResult();
        session.close();
        return uploadedFile;

    }

    public List<UploadedFile> getListByDocument(AbstractDocumentEdi abstractDocumentEdi){

        Session session = HibernateUtil.getSession();
        Query<UploadedFile> query = session.createQuery("from UploadedFile where document.id =:abstractDocumentEdiId and executorTask is null", UploadedFile.class);
        query.setParameter("abstractDocumentEdiId", abstractDocumentEdi.getId());
        List<UploadedFile>  list = query.getResultList();
        session.close();
        return list;

    }

    public List<UploadedFile> getListByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask){

        Session session = HibernateUtil.getSession();
        Query<UploadedFile> query = session.createQuery("from UploadedFile where document.id =:abstractDocumentEdiId and executorTask.id =:executorTaskId", UploadedFile.class);
        query.setParameter("abstractDocumentEdiId", abstractDocumentEdi.getId());
        query.setParameter("executorTaskId", executorTask.getId());
        List<UploadedFile> list = query.getResultList();
        session.close();
        return list;

    }


}
