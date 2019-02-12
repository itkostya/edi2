package hibernate.impl.information_registers;

import categories.User;
import enumerations.MetadataType;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import information_registers.UserAccessRight;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    public List<UserAccessRight> getCurrentUserRights(User user) {

        // All type of rights in Metadata. We should check current user rights with all type of rights

        Session session = HibernateUtil.getSession();
        Query<UserAccessRight> query = session.createQuery("from UserAccessRight where user=:user", UserAccessRight.class);
        query.setParameter("user", user);
        List<UserAccessRight> currentRights = query.getResultList();
        session.close();

        return currentRights;

    }

    public void createUserRights(User user, List<UserAccessRight> userAccessRights) {
        Session session = HibernateUtil.getSessionWithTransaction();
        userAccessRights.forEach(this::save);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    // Comparing current rights with empty rights
    public List<UserAccessRight> getUserRights(User user) {

        // All type of rights in Metadata. We should check current user rights with all type of rights

        // Empty rights for user
        List<UserAccessRight> metadataValues = Arrays.stream(MetadataType.values())
                .map(e -> new UserAccessRight(e, user, false, false))
                .collect(Collectors.toList());

        return metadataValues.stream().map(m -> getCurrentUserRights(user).stream().filter(c -> c.getMetadataType() == m.getMetadataType()).findFirst().orElse(m))
                .collect(Collectors.toList());

    }

    public void updateUserRights(User user, List<UserAccessRight> userAccessRights) {

        // Delete current data and save new. All data compared by metadataType & user (id doesn't matter)
        Session session = HibernateUtil.getSessionWithTransaction();
        getCurrentUserRights(user).forEach(this::delete);
        userAccessRights.forEach(this::save);
        HibernateUtil.closeSessionWithTransaction(session);

    }

    private UserAccessRight getUserAccessRight (User user, MetadataType metadataType){

        Session session = HibernateUtil.getSession();
        Query<UserAccessRight> query = session.createQuery("from UserAccessRight where user=:user and metadataType=:metadataType", UserAccessRight.class);
        query.setParameter("user", user);
        query.setParameter("metadataType", metadataType);
        UserAccessRight userAccessRight = query.getSingleResult();
        session.close();

        return userAccessRight;

    }

    public boolean viewAccess(User user, MetadataType metadataType) {

        UserAccessRight userAccessRight = getUserAccessRight(user, metadataType);
        return (!Objects.isNull(userAccessRight) && userAccessRight.isView());
    }

    public boolean editAccess(User user, MetadataType metadataType) {

        UserAccessRight userAccessRight = getUserAccessRight(user, metadataType);
        return (!Objects.isNull(userAccessRight) && userAccessRight.isEdit());

    }

}
