package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Created by kostya on 9/2/2016.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory =
            new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static Session getSessionWithTransaction() {
        Session session = getSession();
        session.beginTransaction();
        return session;
    }

    public static void closeSessionWithTransaction(Session session) {
        session.getTransaction().commit();
        session.close();
    }
}
