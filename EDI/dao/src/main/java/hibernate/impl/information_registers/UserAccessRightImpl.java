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

}
