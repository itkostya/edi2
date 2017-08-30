package hibernate;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

/*
* 6/20/2017 - it's possible that update throws exception, but now it's not useful for me
*
* Created by kostya on 9/2/2016.
*/
public interface HibernateDAO <T>  {

    @SuppressWarnings("unused")
    void save(T t) throws ConstraintViolationException, PersistenceException;

    @SuppressWarnings("unused")
    void update(T t);

    @SuppressWarnings("unused")
    void delete(T t);

}
