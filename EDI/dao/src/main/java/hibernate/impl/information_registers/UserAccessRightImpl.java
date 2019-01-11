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

        return resultRights;

    }

    public void setUserRights(List<UserAccessRight> userAccessRights){

        // TODO: update in database - delete old user's data and create new
        Session session = HibernateUtil.getSessionWithTransaction();
        userAccessRights.forEach(this::delete);
        userAccessRights.forEach(this::save);
        //userAccessRights.forEach(this::update);
        HibernateUtil.closeSessionWithTransaction(session);

    }

}
