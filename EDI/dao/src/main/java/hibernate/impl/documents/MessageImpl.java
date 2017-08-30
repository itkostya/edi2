package hibernate.impl.documents;

import documents.Message;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;

/*
* 12/16/2016
* Actually I have AbstractDocumentEdiImpl. But I don't sure it will be later
* Today I will write code in AbstractDocumentEdiImpl, but MessageImpl I don't want to delete now
*
* Created by kostya on 11/9/2016.
*/
public enum MessageImpl implements HibernateDAO<Message> {

    INSTANCE;

    public void save(Message message) {
        AbstractDocumentEdiImpl.INSTANCE.save(message);
    }

    public void update(Message message) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(message);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(Message message) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(message);
        HibernateUtil.closeSessionWithTransaction(session);
    }
}
