package hibernate.impl.categories;

import abstract_entity.AbstractCategory;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.metamodel.internal.SingularAttributeImpl;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.*;
import java.util.stream.Collectors;

public enum AbstractCategoryImpl implements HibernateDAO<AbstractCategory> {

    INSTANCE;

    public void save(AbstractCategory abstractCategory) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(abstractCategory);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(AbstractCategory abstractCategory) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(abstractCategory);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(AbstractCategory abstractCategory) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(abstractCategory);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public List<AbstractCategory> getCategoryTable(Class<? extends AbstractCategory> abstractCategoryClass, String filterString) {
        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(abstractCategoryClass);
        Root categoryRoot = cq.from(abstractCategoryClass);
//        Join<AbstractCategory, Position> positionJoin = userRoot.join("position", JoinType.LEFT);
//        Join<AbstractCategory, Department> departmentJoin = userRoot.join("department", JoinType.LEFT);

        cq.where((("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
//                                cb.like(cb.lower(userRoot.get("fio")), "%" + filterString.toLowerCase() + "%"),
//                                cb.like(cb.lower(positionJoin.get("name")), "%" + filterString.toLowerCase() + "%"),
//                                cb.like(cb.lower(departmentJoin.get("name")), "%" + filterString.toLowerCase() + "%")
                        )
                )
        );

        cq.select(categoryRoot);
        TypedQuery<AbstractCategory> q = session.createQuery(cq);
        List<AbstractCategory> list = q.getResultList();

        session.close();
        return list;

    }

    public Set<? extends SingularAttribute<? extends AbstractCategory, ?>> getCategoryColumns(Class<? extends AbstractCategory> abstractCategoryClass, String filterString) {
            Session session = HibernateUtil.getSession();

        Set<? extends SingularAttribute<? extends AbstractCategory, ?>> set = new HashSet<>(HibernateUtil.getSession().getMetamodel().managedType(abstractCategoryClass).getDeclaredSingularAttributes()) ;
        Set set2 = new HashSet<>(HibernateUtil.getSession().getMetamodel().managedType(AbstractCategory.class).getDeclaredSingularAttributes());
        set.addAll(set2);
        set = set.stream().filter(
                o -> !o.getName().equals("position")
               // && !o.getName().equals("department")
                && !o.getName().equals("isFolder")
        ).collect(Collectors.toSet());

        session.close();
        return set;
    }


}
