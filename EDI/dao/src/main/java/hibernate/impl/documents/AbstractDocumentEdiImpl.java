package hibernate.impl.documents;

import abstract_entity.AbstractDocumentEdi;
import exсeption.AbstractDocumentEdiNotFoundException;
import hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.PersistenceException;

/*
 * Created by kostya on 12/15/2016.
 */
public enum AbstractDocumentEdiImpl{

    INSTANCE;

    public void save(AbstractDocumentEdi abstractDocumentEdi) {

        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        abstractDocumentEdi.setNumber(DocumentNumerationImpl.INSTANCE.getNextNumber(abstractDocumentEdi.getDocumentProperty(), ""));
        session.save(abstractDocumentEdi);
        try {
            session.getTransaction().commit();
        }catch (PersistenceException e){
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                if (e.getCause().getCause().getMessage().contains("Duplicate entry")) {
                    session.close();
                    session = HibernateUtil.getSession();
                    session.beginTransaction();
                    abstractDocumentEdi.setNumber(DocumentNumerationImpl.INSTANCE.getNextNumberUsingMax(abstractDocumentEdi.getDocumentProperty(), ""));
                    session.save(abstractDocumentEdi);
                    session.getTransaction().commit();
                }
            }
        }
        session.close();
    }

    @SuppressWarnings("unused")
    public void update(AbstractDocumentEdi abstractDocumentEdi) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(abstractDocumentEdi);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    @SuppressWarnings("unused")
    public void delete(AbstractDocumentEdi abstractDocumentEdi) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(abstractDocumentEdi);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public AbstractDocumentEdi getById(Long id){

        Session session = HibernateUtil.getSession();
        AbstractDocumentEdi documentEdi = session.get(AbstractDocumentEdi.class,id);
        if (documentEdi == null) throw new AbstractDocumentEdiNotFoundException(id);
        session.close();

        return documentEdi;
    }

}
