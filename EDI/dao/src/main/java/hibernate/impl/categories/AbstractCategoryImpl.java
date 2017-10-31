package hibernate.impl.categories;

import abstract_entity.AbstractCategory;
import abstract_entity.AbstractDocumentEdi;
import categories.Department;
import categories.Position;
import categories.User;
import exсeption.AbstractCategoryNotFoundException;
import exсeption.AbstractDocumentEdiNotFoundException;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public AbstractCategory getById(Class<? extends AbstractCategory> abstractCategoryClass, Long id) {

        Session session = HibernateUtil.getSession();
        AbstractCategory abstractCategory = session.get(abstractCategoryClass, id);
        if (abstractCategory == null) throw new AbstractCategoryNotFoundException(abstractCategoryClass, id);
        session.close();

        return abstractCategory;

    }

    public List<AbstractCategory> getCategoryTable(Class<? extends AbstractCategory> abstractCategoryClass, String filterString) {
        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(abstractCategoryClass);
        Root<AbstractCategory> categoryRoot = cq.from(abstractCategoryClass);
//        Join<AbstractCategory, Position> positionJoin = categoryRoot.join("position", JoinType.LEFT);
//        Join<AbstractCategory, Department> departmentJoin = categoryRoot.join("department", JoinType.LEFT);

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

        // TODO: Union of 2 sets should be type casted
        Set<? extends SingularAttribute<? extends AbstractCategory, ?>> set = new HashSet<>(HibernateUtil.getSession().getMetamodel().managedType(abstractCategoryClass).getDeclaredSingularAttributes()) ;
        Set set2 = new HashSet<>(HibernateUtil.getSession().getMetamodel().managedType(AbstractCategory.class).getDeclaredSingularAttributes());
        set.addAll(set2);
        set = set.stream().filter(
                o ->
                !o.getName().equals("isFolder")
        ).collect(Collectors.toSet());

        session.close();

        return set;
    }


}
