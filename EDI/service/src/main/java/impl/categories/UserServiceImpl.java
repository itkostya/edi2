package impl.categories;

import categories.User;
import hibernate.impl.categories.UserImpl;

import java.util.List;
import java.util.Objects;

/*
 * Created by kostya on 9/5/2016.
 */
public enum UserServiceImpl {

    INSTANCE;

    public boolean isUserExist(User user){
        return ( user != null && UserImpl.INSTANCE.getUserByLogin(user.getLogin())!= null );
    }

    public boolean isPasswordCorrect(User user){
        User userInDatabase = UserImpl.INSTANCE.getUserByLogin(user.getLogin());
        return (Objects.nonNull(userInDatabase) &&
                user.getPassword().equals(userInDatabase.getPassword())
        );
    }

    public List<User> getCoworkers(String filterString){
        return UserImpl.INSTANCE.getCoworkers(filterString);
    }

    public Integer getNewUsersCount(){
        List<User> userList = UserImpl.INSTANCE.getNewUsers();
        return userList.size();
    }
}
