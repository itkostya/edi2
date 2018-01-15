package impl.categories;

import abstract_entity.AbstractCategory;
import hibernate.impl.categories.AbstractCategoryImpl;

import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Method;
import java.util.*;

public enum AbstractCategoryServiceImpl {

    INSTANCE;

    private class AbstractCategoryComparator implements Comparator<AbstractCategory> {
        public int compare(AbstractCategory s1, AbstractCategory s2) {
            return s1.getName().compareTo(s2.getName());
        }
    }

    private class NamedMethodComparator implements Comparator<Object> {

        //
        // instance variables
        //

        private String methodName;

        private boolean isAsc;

        //
        // constructor
        //

        public NamedMethodComparator(String methodName, boolean isAsc) {
            this.methodName = methodName;
            this.isAsc = isAsc;
        }

        /**
         * Method to compare two objects using the method named in the constructor.
         */
        @Override
        public int compare(Object obj1, Object obj2) {
            Comparable comp1 = getValue(obj1, methodName);
            Comparable comp2 = getValue(obj2, methodName);
            if (isAsc) {
                //return comp1.compareTo(comp2);
                return (comp1 == comp2) ? 0 : (Objects.isNull(comp1) ? -1 : Objects.isNull(comp2) ? 1 : comp1.compareTo(comp2));
            } else {
                //return comp2.compareTo(comp1);
                return (comp1 == comp2)? 0 : (Objects.isNull(comp2) ? -1 : Objects.isNull(comp1) ? 1 : comp2.compareTo(comp1));
            }
        }

        //
        // implementation
        //

        private Comparable getValue(Object obj, String methodName) {
            Method method = getMethod(obj, methodName);
            Comparable comp = getValue(obj, method);
            return comp;
        }

        private Method getMethod(Object obj, String methodName) {
            try {
                Class[] signature = {};
                Method method = obj.getClass().getMethod(methodName, signature);
                return method;
            } catch (Exception exp) {
                throw new RuntimeException(exp);
            }
        }

        private Comparable getValue(Object obj, Method method) {
            Object[] args = {};
            try {
                Object rtn = method.invoke(obj, args);
                Comparable comp = (Comparable) rtn;
                return comp;
            } catch (java.lang.ClassCastException exp){
                try {
                    AbstractCategory rtn = (AbstractCategory) method.invoke(obj, args);
                    Comparable comp = rtn.getName();
                    return comp;
                }catch (Exception exp2) {
                    throw new RuntimeException(exp2);
                }
            }
            catch (Exception exp2) {
                throw new RuntimeException(exp2);
            }
        }

    }


    public List<? extends AbstractCategory> getCategoryTable(Class<? extends AbstractCategory> abstractCategoryClass, String sortingSequence, String filterString) {

        List<? extends AbstractCategory> abstractCategoryList = AbstractCategoryImpl.INSTANCE.getCategoryTable(abstractCategoryClass, filterString);

        if (sortingSequence.equals("default")) {
            abstractCategoryList.sort(Comparator.comparing(AbstractCategory::getId));
        } else {
            SingularAttribute singularAttribute =
                    AbstractCategoryImpl.INSTANCE.getCategoryColumnByPosition(abstractCategoryClass, filterString, Integer.valueOf("" + sortingSequence.substring(0, sortingSequence.indexOf("."))));
            boolean ascSorting = (sortingSequence.charAt(sortingSequence.length()-1) == '+' || sortingSequence.charAt(sortingSequence.length()-1) == 'n');
            String methodName = singularAttribute.getName();
            if (singularAttribute.getJavaType().getName().equals("boolean")) {
                methodName = "is" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
            } else if (singularAttribute.getType().getPersistenceType().name().equals("BASIC")) {
                methodName = "get" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
            } else {
                // ENTITY
                methodName = "get" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
            }
            Collections.sort(abstractCategoryList, new NamedMethodComparator(methodName, ascSorting));
        }

        return abstractCategoryList;
    }

    public Set<? extends SingularAttribute<? extends AbstractCategory, ?>> getCategoryColumns(Class<? extends AbstractCategory> abstractCategoryClass, String filterString) {
        return AbstractCategoryImpl.INSTANCE.getCategoryColumns(abstractCategoryClass, filterString);
    }
}
