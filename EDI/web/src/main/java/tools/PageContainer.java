package tools;

import abstract_entity.AbstractCategory;
import abstract_entity.AbstractDocumentEdi;
import categories.*;
import documents.Memorandum;
import documents.Message;
import exсeption.PageContainerNotFoundException;

import java.util.Locale;

/*
 * Created by kostya on 9/2/2016.
 */
public enum PageContainer {

    @SuppressWarnings("unused")
    INSTANCE;

    public static final String ADMIN_PAGE = "/admin";
    public static final String ADMIN_JSP = "WEB-INF/views/jsp/work_area/admin.jsp";

    public static final String USER_PAGE = "/user";
    public static final String USER_JSP = "WEB-INF/views/jsp/authorization/user.jsp";

    public static final String ERROR_JSP = "WEB-INF/views/jsp/authorization/error.jsp";

    public static final String WORK_AREA_PAGE = "/work_area";
    public static final String WORK_AREA_JSP = "WEB-INF/views/jsp/work_area/work_area_main.jsp";

    public static final String CATEGORY_DEPARTMENT_JOURNAL_PAGE = "/cat_department_journal";
    public static final String CATEGORY_DEPARTMENT_JOURNAL_JSP = "WEB-INF/views/jsp/category/category_journal.jsp";

    public static final String CATEGORY_POSITION_JOURNAL_PAGE = "/cat_position_journal";
    public static final String CATEGORY_POSITION_JOURNAL_JSP = "WEB-INF/views/jsp/category/category_journal.jsp";

    public static final String CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE = "/cat_proposal_template_journal";
    public static final String CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_JSP = "WEB-INF/views/jsp/category/category_journal.jsp";

    public static final String CATEGORY_USER_JOURNAL_PAGE = "/cat_user_journal";
    public static final String CATEGORY_USER_JOURNAL_JSP = "WEB-INF/views/jsp/category/category_journal.jsp";

    public static final String DOCUMENT_MEMORANDUM_JOURNAL_PAGE = "/doc_memorandum_journal";
    @SuppressWarnings("WeakerAccess")
    public static final String DOCUMENT_MEMORANDUM_JOURNAL_JSP = "WEB-INF/views/jsp/documents/memorandum_journal.jsp";

    public static final String DOCUMENT_MEMORANDUM_CREATE_PAGE = "/doc_memorandum_create";
    public static final String DOCUMENT_MEMORANDUM_CREATE_JSP = "WEB-INF/views/jsp/documents/memorandum_create.jsp";

    public static final String DOCUMENT_MESSAGE_JOURNAL_PAGE = "/doc_message_journal";
    @SuppressWarnings("WeakerAccess")
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
        throw new PageContainerNotFoundException("getCreatePageStringByDocumentProperty - documentProperty: "+documentProperty);

    }

    public static String getPageName(String requestURI){

        switch (requestURI){
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE: return "Department";
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE: return "Position";
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE: return "Proposal template";
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE: return "User";
            case PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE: return "Memorandum";
            case PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE: return "Message";
        }
        throw new PageContainerNotFoundException("getPageName - requestURI: "+requestURI);
    }

    public static String getJspName(String requestURI){

        switch (requestURI){
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE: return PageContainer.CATEGORY_DEPARTMENT_JOURNAL_JSP;
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE: return PageContainer.CATEGORY_POSITION_JOURNAL_JSP;
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE: return PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_JSP;
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE: return PageContainer.CATEGORY_USER_JOURNAL_JSP;
            case PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE: return PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_JSP;
            case PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE: return PageContainer.DOCUMENT_MESSAGE_JOURNAL_JSP;
        }
        throw new PageContainerNotFoundException("getJspName - requestURI: "+requestURI);
    }

    public static Class<? extends AbstractDocumentEdi> getAbstractDocumentClass(String requestURI){

        switch (requestURI){
            case PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE: return  Memorandum.class;
            case PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE: return  Message.class;
        }
        throw new PageContainerNotFoundException("getAbstractDocumentClass - requestURI: "+requestURI);
    }

    public static Class<? extends AbstractCategory> getAbstractCategoryClass(String requestURI){

        switch (requestURI){
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE: return Department.class;
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE: return Position.class;
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE: return ProposalTemplate.class;
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE: return User.class;
        }
        throw new PageContainerNotFoundException("getAbstractDocumentClass - requestURI: "+requestURI);
    }

    public static CategoryProperty getCategoryProperty(String requestURI){

        switch (requestURI){
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE: return CategoryProperty.DEPARTMENT;
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE: return CategoryProperty.POSITION;
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE: return CategoryProperty.PROPOSAL_TEMPLATE;
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE: return CategoryProperty.USER;
        }
        throw new PageContainerNotFoundException("getCategoryProperty - requestURI: "+requestURI);
    }


}
