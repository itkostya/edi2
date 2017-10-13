package tools;

import abstract_entity.AbstractDocumentEdi;
import documents.Memorandum;
import documents.Message;
import exсeption.PageContainerNotFoundException;

/*
 * Created by kostya on 9/2/2016.
 */
public enum PageContainer {

    INSTANCE;

    public static final String ADMIN_PAGE = "/admin";
    public static final String ADMIN_JSP = "WEB-INF/views/jsp/service_tools/admin.jsp";

    public static final String USER_PAGE = "/user";
    public static final String USER_JSP = "WEB-INF/views/jsp/authorization/user.jsp";

    public static final String ERROR_JSP = "WEB-INF/views/jsp/authorization/error.jsp";

    public static final String WORK_AREA_PAGE = "/work_area";
    public static final String WORK_AREA_JSP = "WEB-INF/views/jsp/work_area/work_area_main.jsp";

    public static final String DOCUMENT_MEMORANDUM_JOURNAL_PAGE = "/doc_memorandum_journal";
    public static final String DOCUMENT_MEMORANDUM_JOURNAL_JSP = "WEB-INF/views/jsp/documents/memorandum_journal.jsp";

    public static final String DOCUMENT_MEMORANDUM_CREATE_PAGE = "/doc_memorandum_create";
    public static final String DOCUMENT_MEMORANDUM_CREATE_JSP = "WEB-INF/views/jsp/documents/memorandum_create.jsp";

    public static final String DOCUMENT_MESSAGE_JOURNAL_PAGE = "/doc_message_journal";
    public static final String DOCUMENT_MESSAGE_JOURNAL_JSP = "WEB-INF/views/jsp/documents/message_journal.jsp";

    public static final String DOCUMENT_MESSAGE_CREATE_PAGE = "/doc_message_create";
    public static final String DOCUMENT_MESSAGE_CREATE_JSP = "WEB-INF/views/jsp/documents/message_create.jsp";

    public static final String EXECUTOR_TASK_PAGE = "/executor_task";
    public static final String EXECUTOR_TASK_JSP = "WEB-INF/views/jsp/business_processes/executor_task.jsp";

    public static final String DOWNLOAD_PAGE = "/download";

    public static String getCreatePageStringByDocumentProperty(String documentProperty){

        switch (documentProperty.toUpperCase()) {
            case ("MEMORANDUM"): return DOCUMENT_MEMORANDUM_CREATE_PAGE;
            case ("MESSAGE"): return DOCUMENT_MESSAGE_CREATE_PAGE;
        }
        throw new PageContainerNotFoundException("documentProperty");

    }

    public static String getPageName(String requestURI){

        switch (requestURI){
            case PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE: return "Memorandum";
            case PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE: return "Message";
        }
        return "";
    }

    public static String getJspName(String requestURI){

        switch (requestURI){
            case PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE: return PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_JSP;
            case PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE: return PageContainer.DOCUMENT_MESSAGE_JOURNAL_JSP;
        }
        return "";
    }

    public static Class<? extends AbstractDocumentEdi> getAbstractDocumentClass(String requestURI){

        switch (requestURI){
            case PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE: return  Memorandum.class;
            case PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE: return  Message.class;
        }
        return null;
    }


}
