package hibernate.impl.documents;

import abstract_entity.AbstractDocumentEdi;
import documents.DocumentNumeration;
import documents.DocumentProperty;
import exсeption.DocumentNumerationException;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Objects;

/*
 * Created by kostya on 2/3/2017.
 */
public enum DocumentNumerationImpl implements HibernateDAO<DocumentNumeration> {

    INSTANCE;

    @Override
    public void save(DocumentNumeration documentNumeration) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(documentNumeration);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    @Override
    public void update(DocumentNumeration documentNumeration) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(documentNumeration);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    @Override
    public void delete(DocumentNumeration documentNumeration) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(documentNumeration);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    private DocumentNumeration getDocumentNumeration(Session session, DocumentProperty documentProperty, String currentPrefix, Long newNumber){

        Query query = session.createQuery("from DocumentNumeration " +
                "where documentProperty =:documentProperty and prefix =:prefix");
        query.setParameter("documentProperty", documentProperty);
        query.setParameter("prefix", currentPrefix);

        DocumentNumeration documentNumeration = (DocumentNumeration) query.uniqueResult();

        if (Objects.isNull(documentNumeration)){
            documentNumeration = new DocumentNumeration(documentProperty, currentPrefix, Objects.isNull(newNumber) ? 1L : newNumber );
            save(documentNumeration);
        } else {
            documentNumeration.setNumber(Objects.isNull(newNumber) ? documentNumeration.getNumber()+ 1L: newNumber);
            update(documentNumeration);
        }

        return documentNumeration;
    }

    private StringBuilder getFullNumber(DocumentNumeration documentNumeration, DocumentProperty documentProperty, String currentPrefix) throws DocumentNumerationException{

        StringBuilder result = new StringBuilder();
        result.append("").append(documentNumeration.getPrefix()).append("-");
        for (int i = documentProperty.getPrefixLength() - (documentNumeration.getPrefix()+"-"+documentNumeration.getNumber()).length(); i > 0; i--)
            result.append("0");
        result.append(documentNumeration.getNumber());
        if (result.length() >  documentProperty.getPrefixLength()) throw new DocumentNumerationException(currentPrefix, documentNumeration.getNumber());
        return result;

    }

    public String getNextNumber(AbstractDocumentEdi abstractDocumentEdi, String prefix) {

        DocumentProperty documentProperty = abstractDocumentEdi.getDocumentProperty();
        String currentPrefix = ((Objects.isNull(prefix) || prefix.equals("")) ? documentProperty.getDefaultPrefix(): prefix);
        StringBuilder result = new StringBuilder();

        if (Objects.nonNull(currentPrefix)) {

            Session session = HibernateUtil.getSession();
            session.beginTransaction();

            DocumentNumeration documentNumeration = getDocumentNumeration(session, documentProperty, currentPrefix, null);
            result = getFullNumber(documentNumeration, documentProperty, currentPrefix);

            session.getTransaction().commit();
            session.close();
        }

        return result.toString();
    }

    public String getNextNumberUsingMax(AbstractDocumentEdi abstractDocumentEdi, String prefix) {

        DocumentProperty documentProperty = abstractDocumentEdi.getDocumentProperty();
        String currentPrefix = ((Objects.isNull(prefix) || prefix.equals("")) ? documentProperty.getDefaultPrefix(): prefix);
        StringBuilder result = new StringBuilder();
        Long number;

        if (Objects.nonNull(currentPrefix)) {

            Session session = HibernateUtil.getSession();
            session.beginTransaction();

            // SELECT number FROM edi.doc_abstract_document_edi where date = (select max(date) FROM edi.doc_abstract_document_edi);
            // SELECT number FROM edi.doc_abstract_document_edi order by date desc limit 1;

            // Create new number using existed documents in database - get max number and increase it

            Query query = session.createQuery("from AbstractDocumentEdi as abstractdocument " +
                    "where type(abstractdocument)= :abstractDocumentEdiClass " +
                    "order by date desc");
            query.setParameter("abstractDocumentEdiClass", abstractDocumentEdi.getClass());
            query.setMaxResults(1);

            AbstractDocumentEdi maxAbstractDocumentEdi = (AbstractDocumentEdi) query.uniqueResult();

            if (Objects.nonNull(maxAbstractDocumentEdi)) {

                String stringMaxNumber = maxAbstractDocumentEdi.getNumber();

                if (!stringMaxNumber.isEmpty()) {
                    if (stringMaxNumber.contains(currentPrefix)) {
                        result.append(stringMaxNumber, currentPrefix.length() + 1, stringMaxNumber.length());
                    } else {
                        // get digits from last to first number and new prefix
                        boolean unexpectedSymbol = false;
                        StringBuilder newPrefix = new StringBuilder();
                        for (int i = stringMaxNumber.length() - 1; i >= 0; i--) {
                            if (!unexpectedSymbol && (stringMaxNumber.charAt(i) >= '0') && (stringMaxNumber.charAt(i) <= '9'))
                                result.insert(0, stringMaxNumber.charAt(i));
                            else {
                                unexpectedSymbol = true;
                                if (stringMaxNumber.charAt(i) != '-') newPrefix.insert(0, stringMaxNumber.charAt(i));
                            }
                        }
                        if (newPrefix.length() > 0) currentPrefix = newPrefix.toString();
                    }
                    number = Long.parseLong(result.toString());
                    DocumentNumeration documentNumeration = getDocumentNumeration(session, documentProperty, currentPrefix, ++number);
                    result = getFullNumber(documentNumeration, documentProperty, currentPrefix);
                }
            }

            session.getTransaction().commit();
            session.close();
        }

        return result.toString();
    }
}
