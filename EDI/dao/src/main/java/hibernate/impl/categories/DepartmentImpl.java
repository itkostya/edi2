package hibernate.impl.categories;

import categories.Department;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * Created by kostya on 9/2/2016.
 */

public enum DepartmentImpl implements HibernateDAO <Department> {

    INSTANCE;

    public void save(Department department) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(department);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(Department department) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(department);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(Department department) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(department);
        HibernateUtil.closeSessionWithTransaction(session);
    }

}
