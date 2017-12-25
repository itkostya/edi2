package common;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum CommonCollections {

    INSTANCE;

    public static <T> List<T> addElementAndReturnList(List<T> list, T t) {
        list.add(t);
        return list;
    }

    public static <T> Set<T> merge(Collection<? extends T>... collections) {
        Set<T> newSet = new HashSet<T>();
        for (Collection<? extends T> collection : collections)
            newSet.addAll(collection);
        return newSet;
    }

}
