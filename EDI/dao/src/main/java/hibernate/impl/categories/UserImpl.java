package hibernate.impl.categories;

import categories.Department;
import categories.Position;
import categories.User;
import exсeption.UserNotFoundException;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;

/*
 * Created by kostya on 9/5/2016.
 */
public enum UserImpl implements HibernateDAO<User> {

    INSTANCE;

    public void save(User user) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(user);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(User user) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(user);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(User user) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(user);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public User getUserByLogin(String login) {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("from User where login =:paramLogin");
        query.setParameter("paramLogin", login);
        User user = (User) query.uniqueResult();
        session.close();
        return user;
    }

    public User getUserById(Long id) {
        Session session = HibernateUtil.getSession();
        User user = session.get(User.class, id);
        if (user == null) throw new UserNotFoundException(id);
        session.close();
        return user;
    }

    public List<User> getUsers() {
        Session session = HibernateUtil.getSession();
        Query<User> query = session.createQuery("from User where role is not null and deletionMark = false", User.class);
        List<User> list = query.getResultList();
        session.close();
        return list;
    }

    public List<User> getCoworkers(String filterString) {

        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> userRoot = cq.from(User.class);
        Join<User, Position> positionJoin = userRoot.join("position", JoinType.LEFT);
        Join<User, Department> departmentJoin = userRoot.join("department", JoinType.LEFT);

        cq.where((("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                cb.like(cb.lower(userRoot.get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(positionJoin.get("name")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(departmentJoin.get("name")), "%" + filterString.toLowerCase() + "%")
                        )
                )
        );

        cq.select(userRoot);
        TypedQuery<User> q = session.createQuery(cq);
        List<User> list = q.getResultList();

        session.close();
        return list;

//        Session session = HibernateUtil.getSession();
//        Query<User> query = session.createQuery("from User", User.class);
//        List<User> list = query.getResultList();
//        session.close();
//        return list;

    }

    public boolean isDatabaseEmpty(){
        return getUsers().isEmpty();
    }

}
