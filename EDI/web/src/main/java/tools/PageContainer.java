package tools;

import abstract_entity.AbstractCategory;
import abstract_entity.AbstractDocumentEdi;
import categories.*;
import documents.Memorandum;
import documents.Message;
import exсeption.PageContainerNotFoundException;

import java.util.Objects;

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

    private static final String CATEGORY_COMMON_CHOICE_JSP =  "WEB-INF/views/jsp/category/category_choice.jsp";
    private static final String CATEGORY_COMMON_ELEMENT_JSP = "WEB-INF/views/jsp/category/category_element.jsp";
    private static final String CATEGORY_COMMON_JOURNAL_JSP = "WEB-INF/views/jsp/category/category_journal.jsp";

    public static final String CATEGORY_CONTRACTOR_CHOICE_PAGE = "/cat_contractor_choice";
    public static final String CATEGORY_CONTRACTOR_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_CONTRACTOR_ELEMENT_PAGE = "/cat_contractor_element";
    public static final String CATEGORY_CONTRACTOR_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_CONTRACTOR_JOURNAL_PAGE = "/cat_contractor_journal";
    public static final String CATEGORY_CONTRACTOR_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_COST_ITEM_CHOICE_PAGE = "/cat_cost_item_choice";
    public static final String CATEGORY_COST_ITEM_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_COST_ITEM_ELEMENT_PAGE = "/cat_cost_item_element";
    public static final String CATEGORY_COST_ITEM_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_COST_ITEM_JOURNAL_PAGE = "/cat_cost_item_journal";
    public static final String CATEGORY_COST_ITEM_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_CURRENCY_CHOICE_PAGE = "/cat_currency_choice";
    public static final String CATEGORY_CURRENCY_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_CURRENCY_ELEMENT_PAGE = "/cat_currency_element";
    public static final String CATEGORY_CURRENCY_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_CURRENCY_JOURNAL_PAGE = "/cat_currency_journal";
    public static final String CATEGORY_CURRENCY_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_DEPARTMENT_CHOICE_PAGE = "/cat_department_choice";
    public static final String CATEGORY_DEPARTMENT_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_DEPARTMENT_ELEMENT_PAGE = "/cat_department_element";
    public static final String CATEGORY_DEPARTMENT_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_DEPARTMENT_JOURNAL_PAGE = "/cat_department_journal";
    public static final String CATEGORY_DEPARTMENT_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_LEGAL_ORGANIZATION_CHOICE_PAGE = "/cat_legal_organization_choice";
    public static final String CATEGORY_LEGAL_ORGANIZATION_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_LEGAL_ORGANIZATION_ELEMENT_PAGE = "/cat_legal_organization_element";
    public static final String CATEGORY_LEGAL_ORGANIZATION_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_LEGAL_ORGANIZATION_JOURNAL_PAGE = "/cat_legal_organization_journal";
    public static final String CATEGORY_LEGAL_ORGANIZATION_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_PLANNING_PERIOD_CHOICE_PAGE = "/cat_planning_period_choice";
    public static final String CATEGORY_PLANNING_PERIOD_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_PLANNING_PERIOD_ELEMENT_PAGE = "/cat_planning_period_element";
    public static final String CATEGORY_PLANNING_PERIOD_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_PLANNING_PERIOD_JOURNAL_PAGE = "/cat_planning_period_journal";
    public static final String CATEGORY_PLANNING_PERIOD_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_POSITION_CHOICE_PAGE = "/cat_position_choice";
    public static final String CATEGORY_POSITION_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_POSITION_ELEMENT_PAGE = "/cat_position_element";
    public static final String CATEGORY_POSITION_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_POSITION_JOURNAL_PAGE = "/cat_position_journal";
    public static final String CATEGORY_POSITION_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_PROPOSAL_TEMPLATE_CHOICE_PAGE = "/cat_proposal_template_choice";
    public static final String CATEGORY_PROPOSAL_TEMPLATE_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE = "/cat_proposal_template_element";
    public static final String CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE = "/cat_proposal_template_journal";
    public static final String CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

    public static final String CATEGORY_USER_CHOICE_PAGE = "/cat_user_choice";
    public static final String CATEGORY_USER_CHOICE_JSP = CATEGORY_COMMON_CHOICE_JSP;
    public static final String CATEGORY_USER_ELEMENT_PAGE = "/cat_user_element";
    public static final String CATEGORY_USER_ELEMENT_JSP = CATEGORY_COMMON_ELEMENT_JSP;
    public static final String CATEGORY_USER_JOURNAL_PAGE = "/cat_user_journal";
    public static final String CATEGORY_USER_JOURNAL_JSP = CATEGORY_COMMON_JOURNAL_JSP;

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
            case PageContainer.CATEGORY_CONTRACTOR_CHOICE_PAGE:
            case PageContainer.CATEGORY_CONTRACTOR_ELEMENT_PAGE:
            case PageContainer.CATEGORY_CONTRACTOR_JOURNAL_PAGE:
                return "Contractor";
            case PageContainer.CATEGORY_COST_ITEM_CHOICE_PAGE:
            case PageContainer.CATEGORY_COST_ITEM_ELEMENT_PAGE:
            case PageContainer.CATEGORY_COST_ITEM_JOURNAL_PAGE:
                return "Cost item"; // TODO - check
            case PageContainer.CATEGORY_CURRENCY_CHOICE_PAGE:
            case PageContainer.CATEGORY_CURRENCY_ELEMENT_PAGE:
            case PageContainer.CATEGORY_CURRENCY_JOURNAL_PAGE:
                return "Currency";
            case PageContainer.CATEGORY_DEPARTMENT_CHOICE_PAGE:
            case PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE:
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE:
                return "Department";
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_CHOICE_PAGE:
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_ELEMENT_PAGE:
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_JOURNAL_PAGE:
                return "Legal organization";
            case PageContainer.CATEGORY_PLANNING_PERIOD_CHOICE_PAGE:
            case PageContainer.CATEGORY_PLANNING_PERIOD_ELEMENT_PAGE:
            case PageContainer.CATEGORY_PLANNING_PERIOD_JOURNAL_PAGE:
                return "Planning period";
            case PageContainer.CATEGORY_POSITION_CHOICE_PAGE:
            case PageContainer.CATEGORY_POSITION_ELEMENT_PAGE:
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE:
                return "Position";
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_CHOICE_PAGE:
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE:
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE:
                return "Proposal template";
            case PageContainer.CATEGORY_USER_CHOICE_PAGE:
            case PageContainer.CATEGORY_USER_ELEMENT_PAGE:
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE:
                return "User";
            case PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE: return "Memorandum";
            case PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE: return "Message";
        }
        throw new PageContainerNotFoundException("getPageName - requestURI: "+requestURI);
    }

    public static String getJspName(String requestURI){

        switch (requestURI){
            case PageContainer.CATEGORY_CONTRACTOR_CHOICE_PAGE: return PageContainer.CATEGORY_CONTRACTOR_CHOICE_JSP;
            case PageContainer.CATEGORY_CONTRACTOR_ELEMENT_PAGE: return PageContainer.CATEGORY_CONTRACTOR_ELEMENT_JSP;
            case PageContainer.CATEGORY_CONTRACTOR_JOURNAL_PAGE: return PageContainer.CATEGORY_CONTRACTOR_JOURNAL_JSP;

            case PageContainer.CATEGORY_COST_ITEM_CHOICE_PAGE: return PageContainer.CATEGORY_COST_ITEM_CHOICE_JSP;
            case PageContainer.CATEGORY_COST_ITEM_ELEMENT_PAGE: return PageContainer.CATEGORY_COST_ITEM_ELEMENT_JSP;
            case PageContainer.CATEGORY_COST_ITEM_JOURNAL_PAGE: return PageContainer.CATEGORY_COST_ITEM_JOURNAL_JSP;

            case PageContainer.CATEGORY_CURRENCY_CHOICE_PAGE: return PageContainer.CATEGORY_CURRENCY_CHOICE_JSP;
            case PageContainer.CATEGORY_CURRENCY_ELEMENT_PAGE: return PageContainer.CATEGORY_CURRENCY_ELEMENT_JSP;
            case PageContainer.CATEGORY_CURRENCY_JOURNAL_PAGE: return PageContainer.CATEGORY_CURRENCY_JOURNAL_JSP;

            case PageContainer.CATEGORY_DEPARTMENT_CHOICE_PAGE: return PageContainer.CATEGORY_DEPARTMENT_CHOICE_JSP;
            case PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE: return PageContainer.CATEGORY_DEPARTMENT_ELEMENT_JSP;
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE: return PageContainer.CATEGORY_DEPARTMENT_JOURNAL_JSP;

            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_CHOICE_PAGE: return PageContainer.CATEGORY_LEGAL_ORGANIZATION_CHOICE_JSP;
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_ELEMENT_PAGE: return PageContainer.CATEGORY_LEGAL_ORGANIZATION_ELEMENT_JSP;
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_JOURNAL_PAGE: return PageContainer.CATEGORY_LEGAL_ORGANIZATION_JOURNAL_JSP;

            case PageContainer.CATEGORY_PLANNING_PERIOD_CHOICE_PAGE: return PageContainer.CATEGORY_PLANNING_PERIOD_CHOICE_JSP;
            case PageContainer.CATEGORY_PLANNING_PERIOD_ELEMENT_PAGE: return PageContainer.CATEGORY_PLANNING_PERIOD_ELEMENT_JSP;
            case PageContainer.CATEGORY_PLANNING_PERIOD_JOURNAL_PAGE: return PageContainer.CATEGORY_PLANNING_PERIOD_JOURNAL_JSP;

            case PageContainer.CATEGORY_POSITION_CHOICE_PAGE: return PageContainer.CATEGORY_POSITION_CHOICE_JSP;
            case PageContainer.CATEGORY_POSITION_ELEMENT_PAGE: return PageContainer.CATEGORY_POSITION_ELEMENT_JSP;
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE: return PageContainer.CATEGORY_POSITION_JOURNAL_JSP;

            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_CHOICE_PAGE: return PageContainer.CATEGORY_PROPOSAL_TEMPLATE_CHOICE_JSP;
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE: return PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_JSP;
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE: return PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_JSP;

            case PageContainer.CATEGORY_USER_CHOICE_PAGE: return PageContainer.CATEGORY_USER_CHOICE_JSP;
            case PageContainer.CATEGORY_USER_ELEMENT_PAGE: return PageContainer.CATEGORY_USER_ELEMENT_JSP;
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
            case PageContainer.CATEGORY_CONTRACTOR_CHOICE_PAGE:
            case PageContainer.CATEGORY_CONTRACTOR_ELEMENT_PAGE:
            case PageContainer.CATEGORY_CONTRACTOR_JOURNAL_PAGE:
                return Contractor.class;

            case PageContainer.CATEGORY_COST_ITEM_CHOICE_PAGE:
            case PageContainer.CATEGORY_COST_ITEM_ELEMENT_PAGE:
            case PageContainer.CATEGORY_COST_ITEM_JOURNAL_PAGE:
                return CostItem.class;

            case PageContainer.CATEGORY_CURRENCY_CHOICE_PAGE:
            case PageContainer.CATEGORY_CURRENCY_ELEMENT_PAGE:
            case PageContainer.CATEGORY_CURRENCY_JOURNAL_PAGE:
                return Currency.class;

            case PageContainer.CATEGORY_DEPARTMENT_CHOICE_PAGE:
            case PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE:
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE:
                return Department.class;

            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_CHOICE_PAGE:
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_ELEMENT_PAGE:
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_JOURNAL_PAGE:
                return LegalOrganization.class;

            case PageContainer.CATEGORY_PLANNING_PERIOD_CHOICE_PAGE:
            case PageContainer.CATEGORY_PLANNING_PERIOD_ELEMENT_PAGE:
            case PageContainer.CATEGORY_PLANNING_PERIOD_JOURNAL_PAGE:
                return PlanningPeriod.class;

            case PageContainer.CATEGORY_POSITION_CHOICE_PAGE:
            case PageContainer.CATEGORY_POSITION_ELEMENT_PAGE:
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE:
                return Position.class;

            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_CHOICE_PAGE:
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE:
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE:
                return ProposalTemplate.class;

            case PageContainer.CATEGORY_USER_CHOICE_PAGE:
            case PageContainer.CATEGORY_USER_ELEMENT_PAGE:
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE:
                return User.class;
        }
        throw new PageContainerNotFoundException("getAbstractDocumentClass - requestURI: "+requestURI);
    }

    public static CategoryProperty getCategoryProperty(String requestURI){

        switch (requestURI){
            case PageContainer.CATEGORY_CONTRACTOR_CHOICE_PAGE:
            case PageContainer.CATEGORY_CONTRACTOR_ELEMENT_PAGE:
            case PageContainer.CATEGORY_CONTRACTOR_JOURNAL_PAGE:
                return CategoryProperty.CONTRACTOR;

            case PageContainer.CATEGORY_COST_ITEM_CHOICE_PAGE:
            case PageContainer.CATEGORY_COST_ITEM_ELEMENT_PAGE:
            case PageContainer.CATEGORY_COST_ITEM_JOURNAL_PAGE:
                return CategoryProperty.COST_ITEM;

            case PageContainer.CATEGORY_CURRENCY_CHOICE_PAGE:
            case PageContainer.CATEGORY_CURRENCY_ELEMENT_PAGE:
            case PageContainer.CATEGORY_CURRENCY_JOURNAL_PAGE:
                return CategoryProperty.CURRENCY;

            case PageContainer.CATEGORY_DEPARTMENT_CHOICE_PAGE:
            case PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE:
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE:
                return CategoryProperty.DEPARTMENT;

            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_CHOICE_PAGE:
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_ELEMENT_PAGE:
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_JOURNAL_PAGE:
                return CategoryProperty.LEGAL_ORGANIZATION;

            case PageContainer.CATEGORY_PLANNING_PERIOD_CHOICE_PAGE:
            case PageContainer.CATEGORY_PLANNING_PERIOD_ELEMENT_PAGE:
            case PageContainer.CATEGORY_PLANNING_PERIOD_JOURNAL_PAGE:
                return CategoryProperty.PLANNING_PERIOD;

            case PageContainer.CATEGORY_POSITION_CHOICE_PAGE:
            case PageContainer.CATEGORY_POSITION_ELEMENT_PAGE:
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE:
                return CategoryProperty.POSITION;

            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_CHOICE_PAGE:
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE:
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE:
                return CategoryProperty.PROPOSAL_TEMPLATE;

            case PageContainer.CATEGORY_USER_CHOICE_PAGE:
            case PageContainer.CATEGORY_USER_ELEMENT_PAGE:
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE:
                return CategoryProperty.USER;
        }
        throw new PageContainerNotFoundException("getCategoryProperty - requestURI: "+requestURI);
    }

    public static String getElementPage(String requestURI){

        switch (requestURI){
            case PageContainer.CATEGORY_CONTRACTOR_JOURNAL_PAGE: return PageContainer.CATEGORY_CONTRACTOR_ELEMENT_PAGE;
            case PageContainer.CATEGORY_COST_ITEM_JOURNAL_PAGE: return PageContainer.CATEGORY_COST_ITEM_ELEMENT_PAGE;
            case PageContainer.CATEGORY_CURRENCY_JOURNAL_PAGE: return PageContainer.CATEGORY_CURRENCY_ELEMENT_PAGE;
            case PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE: return PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE;
            case PageContainer.CATEGORY_LEGAL_ORGANIZATION_JOURNAL_PAGE: return PageContainer.CATEGORY_LEGAL_ORGANIZATION_ELEMENT_PAGE;
            case PageContainer.CATEGORY_PLANNING_PERIOD_JOURNAL_PAGE: return PageContainer.CATEGORY_PLANNING_PERIOD_ELEMENT_PAGE;
            case PageContainer.CATEGORY_POSITION_JOURNAL_PAGE: return PageContainer.CATEGORY_POSITION_ELEMENT_PAGE;
            case PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE: return PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE;
            case PageContainer.CATEGORY_USER_JOURNAL_PAGE: return PageContainer.CATEGORY_USER_ELEMENT_PAGE;
        }
        throw new PageContainerNotFoundException("getElementType - requestURI: "+requestURI);
    }

    public static String getChoicePage(Class <? extends AbstractCategory> classAbstractCategory){

        if (Objects.isNull(classAbstractCategory)) throw new PageContainerNotFoundException("getChoicePage - classAbstractCategory: null");
        else if ( classAbstractCategory.equals(Contractor.class)) return PageContainer.CATEGORY_CONTRACTOR_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(CostItem.class)) return PageContainer.CATEGORY_COST_ITEM_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(Currency.class)) return PageContainer.CATEGORY_CURRENCY_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(Department.class)) return PageContainer.CATEGORY_DEPARTMENT_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(LegalOrganization.class)) return PageContainer.CATEGORY_LEGAL_ORGANIZATION_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(PlanningPeriod.class)) return PageContainer.CATEGORY_PLANNING_PERIOD_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(Position.class)) return PageContainer.CATEGORY_POSITION_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(ProposalTemplate.class))  return PageContainer.CATEGORY_PROPOSAL_TEMPLATE_CHOICE_PAGE;
        else if ( classAbstractCategory.equals(User.class))  return PageContainer.CATEGORY_USER_CHOICE_PAGE;

        throw new PageContainerNotFoundException("getChoicePage - classAbstractCategory: "+classAbstractCategory);

    }


}
