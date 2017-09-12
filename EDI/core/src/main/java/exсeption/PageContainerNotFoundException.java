package exсeption;

/*
* Error when we can't find the document
*
* Created by kostya on 9/12/2017.
*/

public class PageContainerNotFoundException extends RuntimeException {
    public PageContainerNotFoundException(String str) {
        super("PageContainerString = " + str);
    }
}
