package impl.categories;

import abstract_entity.AbstractCategory;
import hibernate.impl.categories.AbstractCategoryImpl;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;

public enum AbstractCategoryServiceImpl {

    INSTANCE;

    public List<AbstractCategory> getCategoryTable(Class<? extends AbstractCategory> abstractCategoryClass, String filterString){
        return AbstractCategoryImpl.INSTANCE.getCategoryTable(abstractCategoryClass, filterString);
    }

    public Set<? extends SingularAttribute<? extends AbstractCategory, ?>> getCategoryColumns(Class<? extends AbstractCategory> abstractCategoryClass, String filterString){
        return AbstractCategoryImpl.INSTANCE.getCategoryColumns(abstractCategoryClass, filterString);
    }
}
