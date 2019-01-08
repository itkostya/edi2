package hibernate.impl.information_registers;

import categories.User;
import enumerations.MetadataType;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import hibernate.impl.categories.UserImpl;
import information_registers.UserAccessRight;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public enum UserAccessRightImpl implements HibernateDAO<UserAccessRight> {

    INSTANCE;

    public void save(UserAccessRight userAccessRight) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(userAccessRight);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(UserAccessRight userAccessRight) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(userAccessRight);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(UserAccessRight userAccessRight) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(userAccessRight);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public List<UserAccessRight> getUserRights(User user){

        // test begin
        UserAccessRight userAccessRight = new UserAccessRight(MetadataType.CONTRACTOR, user, true, true);
        save(userAccessRight);
        // test end

        // All type of rights in Metadata. We should check current user rights with all type of rights

        Session session = HibernateUtil.getSession();
        Query<UserAccessRight> query = session.createQuery("from UserAccessRight where user=:user", UserAccessRight.class);
        query.setParameter("user", user);
        List<UserAccessRight> currentRights = query.getResultList();
        session.close();

        // Empty rights for user
        List<UserAccessRight> metadataValues = Arrays.stream(MetadataType.values())
                .map(e -> new UserAccessRight(e, user, false, false))
                .collect(Collectors.toList());

        List<UserAccessRight> resultRights =
                metadataValues.stream().map(m -> currentRights.stream().filter(c -> c.getMetadataType() == m.getMetadataType()).findFirst().orElse(m))
                .collect(Collectors.toList());

        delete(userAccessRight);  // test

        return resultRights;

    }

    public boolean setUserRights(User user, List<UserAccessRight> userAccessRights){

        Session session = HibernateUtil.getSession();
        userAccessRights.forEach(session::update);
        session.close();
        return true;

    }

}
