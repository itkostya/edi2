package hibernate.impl.business_processes;

import business_processes.BusinessProcess;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;

/*
 * Created by kostya on 9/16/2016.
 */

public enum BusinessProcessImpl implements HibernateDAO<BusinessProcess> {

    INSTANCE;

    public void save(BusinessProcess businessProcess){

        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(businessProcess);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(BusinessProcess businessProcess) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(businessProcess);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(BusinessProcess businessProcess) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(businessProcess);
        HibernateUtil.closeSessionWithTransaction(session);
    }

}