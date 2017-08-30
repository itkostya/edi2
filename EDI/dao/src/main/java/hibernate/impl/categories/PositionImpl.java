package hibernate.impl.categories;

import categories.Position;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;

/*
* Created by kostya on 9/5/2016.
*/
public enum PositionImpl  implements HibernateDAO<Position> {

    INSTANCE;

    public void save(Position position) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(position);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(Position position) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(position);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(Position position) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(position);
        HibernateUtil.closeSessionWithTransaction(session);
    }

}
