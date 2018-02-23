package hibernate.impl.categories;

import abstract_entity.AbstractCategory;
import abstract_entity.AbstractDocumentEdi;
import categories.Department;
import categories.Position;
import categories.User;
import common.CommonCollections;
import exсeption.AbstractCategoryNotFoundException;
import exсeption.AbstractDocumentEdiNotFoundException;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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

    public AbstractCategory getById(Class<? extends AbstractCategory> abstractCategoryClass, Long id) {

        Session session = HibernateUtil.getSession();
        AbstractCategory abstractCategory = session.get(abstractCategoryClass, id);
        if (abstractCategory == null) throw new AbstractCategoryNotFoundException(abstractCategoryClass, id);
        session.close();

        return abstractCategory;

    }

    public List<AbstractCategory> getCategoryTable(Class<? extends AbstractCategory> abstractCategoryClass,  String filterString, List<String> columnNames) {

        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(abstractCategoryClass);
        Root<AbstractCategory> categoryRoot = cq.from(abstractCategoryClass);
//        Join<AbstractCategory, Position> positionJoin = categoryRoot.join("position", JoinType.LEFT);
//        Join<AbstractCategory, Department> departmentJoin = categoryRoot.join("department", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!filterString.isEmpty()){
            Set<? extends SingularAttribute<? extends AbstractCategory, ?>> setColumns = getCategoryColumns(abstractCategoryClass, columnNames);
            for ( SingularAttribute<? extends AbstractCategory, ?> col: setColumns){

                if ("BASIC".equals(col.getType().getPersistenceType().name())){
                    predicates.add(cb.like(cb.lower(categoryRoot.get(col.getName()).as(String.class)), "%" + filterString.toLowerCase() + "%"));
                }else{
                    Join<AbstractCategory, ?> elementJoin = categoryRoot.join(col.getName(), JoinType.LEFT);
                    predicates.add(cb.like(cb.lower(elementJoin.get("name").as(String.class)), "%" + filterString.toLowerCase() + "%"));
                }
            }
        }

        cq.where((("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                    cb.or(predicates.toArray(new Predicate[predicates.size()]))
                )
        );

        cq.select(categoryRoot);
        TypedQuery<AbstractCategory> q = session.createQuery(cq);
        List<AbstractCategory> list = q.getResultList();

        session.close();
        return list;

    }

    private class AttrComparator implements Comparator<SingularAttribute> {
        public int compare(SingularAttribute s1, SingularAttribute s2) {
            String str1 = s1.getName().toLowerCase();
            String str2 = s2.getName().toLowerCase();
            if (str1.equals("id")) str1 = "aaa" + str1;
                else if (str1.equals("name")) str1 = "aa" + str1;
            if (str2.equals("id")) str2 = "aaa" + str2;
                else if (str2.equals("name")) str2 = "aa" + str2;
            return str1.compareTo(str2);
        }
    }

    public Set<? extends SingularAttribute<? extends AbstractCategory, ?>> getCategoryColumns(Class<? extends AbstractCategory> abstractCategoryClass, List<String> columnNames) {

       Session session = HibernateUtil.getSession();

        // TODO: Union of 2 sets should be type casted

        Set<? extends SingularAttribute<? extends AbstractCategory, ?>> set = new TreeSet<>(new AttrComparator());
        Set<? extends SingularAttribute<? extends AbstractCategory, ?>> set1 = new HashSet<>(HibernateUtil.getSession().getMetamodel().managedType(abstractCategoryClass).getDeclaredSingularAttributes());
        Set set2 = HibernateUtil.getSession().getMetamodel().managedType(AbstractCategory.class).getDeclaredSingularAttributes();
        set.addAll(CommonCollections.merge(set1, set2));

        set.removeIf(o -> o.getName().equals("isFolder"));
        set.removeIf(o -> o.getName().equals("code"));

        if (!columnNames.isEmpty())
            set.removeIf(o -> !columnNames.contains(o.getName()));

        session.close();

        return set;
    }

    public SingularAttribute<? extends AbstractCategory, ?> getCategoryColumnByPosition(Class<? extends AbstractCategory> abstractCategoryClass, String filterString, int pos, List<String> columnNames) {

        Set<? extends SingularAttribute<? extends AbstractCategory, ?>> setColumns =  getCategoryColumns(abstractCategoryClass, columnNames);
        int i = 0;

        for ( SingularAttribute<? extends AbstractCategory, ?> col: setColumns){
            if (i++ == pos) return col;
        }

        return null;

    }

}
