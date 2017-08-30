package exсeption;

/*
* Error in creating the new number of the document
*
* Created by kostya on 2/3/2017.
*/

public class DocumentNumerationException extends RuntimeException {
    public DocumentNumerationException(String prefix, Long number) {
        super("Length of number is out of range. Prefix=" +prefix+", number="+number);
    }
}
