package exсeption;

/*
* Error when we can't find user
*
* Created by kostya on 1/18/2017.
*/

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super("UserNotFoundException = " + id);
    }
}
