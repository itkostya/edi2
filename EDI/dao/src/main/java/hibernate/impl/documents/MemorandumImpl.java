package hibernate.impl.documents;

import documents.Memorandum;
import exсeption.AbstractDocumentEdiNotFoundException;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.PersistenceException;

/*
* 12/16/2016
* Actually I have AbstractDocumentEdiImpl. But I don't sure it will be later
* Today I will write code in AbstractDocumentEdiImpl, but MemorandumImpl I don't want to delete now
*
* Created by kostya on 9/13/2016.
*/
public enum MemorandumImpl implements HibernateDAO<Memorandum> {

    INSTANCE;

    public void save(Memorandum memorandum)  {
        AbstractDocumentEdiImpl.INSTANCE.save(memorandum);
    }

    public void update(Memorandum memorandum) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(memorandum);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(Memorandum memorandum) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(memorandum);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public Memorandum getById(Long id){

        Session session = HibernateUtil.getSession();
        Memorandum documentEdi = session.get(Memorandum.class,id);
        if (documentEdi == null) throw new AbstractDocumentEdiNotFoundException(id);
        session.close();

        return documentEdi;
    }
}
