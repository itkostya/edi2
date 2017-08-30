package hibernate.impl.business_processes;

import abstract_entity.AbstractDocumentEdi;
import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;
import categories.User;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/*
 * Created by kostya on 9/16/2016.
 */
public enum BusinessProcessSequenceImpl implements HibernateDAO<BusinessProcessSequence>  {

    INSTANCE;

    public void save(BusinessProcessSequence businessProcessSequence) {

        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(businessProcessSequence);
        HibernateUtil.closeSessionWithTransaction(session);

    }

    public void update(BusinessProcessSequence businessProcessSequence) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(businessProcessSequence);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(BusinessProcessSequence businessProcessSequence) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(businessProcessSequence);
        HibernateUtil.closeSessionWithTransaction(session);
    }

//    public List<BusinessProcessSequence> getFilterByBusinessProcess(BusinessProcess businessProcess){
//
//        Session session = HibernateUtil.getSession();
//        Query query = session.createQuery("select b from BusinessProcessSequence b " +
//                "where b.businessProcess =:businessProcess order by id");
//
//        query.setParameter("businessProcess", businessProcess);
//        List<BusinessProcessSequence> list = query.list();
//        session.close();
//        return list;
//    }

    public List<BusinessProcessSequence> getHistoryByDocumentList(AbstractDocumentEdi abstractDocumentEdi){

        Session session = HibernateUtil.getSession();
        Query<BusinessProcessSequence> query = session.createQuery("select businessProcessSequence from BusinessProcessSequence businessProcessSequence " +
                "left join BusinessProcess as businessProcess on businessProcess.id = businessProcessSequence.businessProcess.id "+
                "left join AbstractDocumentEdi as abstractDocumentEdi on abstractDocumentEdi.id = businessProcess.document.id "+
                "where abstractDocumentEdi =:abstractDocumentEdi", BusinessProcessSequence.class);

        query.setParameter("abstractDocumentEdi", abstractDocumentEdi);
        List<BusinessProcessSequence> list = query.getResultList();
        session.close();
        return list;

//        SELECT * FROM edi.bp_sequence as businessProcessSequence
//        left join edi.bp_business_process as businessProcess on businessProcessSequence.bp_id = businessProcess.ID
//        left join edi.doc_abstract_document_edi as abstractDocumentEdi on abstractDocumentEdi.ID = businessProcess.document_id
//        where abstractDocumentEdi.ID = 209;

    }

    public List<BusinessProcessSequence> getNotCompletedSequenceByDocumentAndUser(AbstractDocumentEdi abstractDocumentEdi, User user) {

        Session session = HibernateUtil.getSession();
        Query<BusinessProcessSequence> query = session.createQuery("select businessProcessSequence from BusinessProcessSequence businessProcessSequence " +
                "left join BusinessProcess as businessProcess on businessProcess.id = businessProcessSequence.businessProcess.id " +
                "left join AbstractDocumentEdi as abstractDocumentEdi on abstractDocumentEdi.id = businessProcess.document.id " +
                "where abstractDocumentEdi =:abstractDocumentEdi and  businessProcessSequence.result is null and businessProcess.author =:user", BusinessProcessSequence.class);

        query.setParameter("abstractDocumentEdi", abstractDocumentEdi);
        query.setParameter("user", user);
        List<BusinessProcessSequence> list = query.getResultList();
        session.close();
        return list;

    }

    public BusinessProcessSequence getBusinessProcessSequence(ExecutorTask executorTask){

        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("select businessProcessSequence from BusinessProcessSequence businessProcessSequence " +
                "left join BusinessProcess as businessProcess on businessProcess.id = businessProcessSequence.businessProcess.id "+
                "where businessProcessSequence.executorTask=:executorTask");

        query.setParameter("executorTask", executorTask);
        BusinessProcessSequence result = (BusinessProcessSequence)query.getSingleResult();
        session.close();
        return result;

    }


}
