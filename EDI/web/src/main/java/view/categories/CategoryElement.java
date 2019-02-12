package view.categories;

import abstract_entity.AbstractCategory;
import categories.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import enumerations.MetadataType;
import hibernate.impl.categories.AbstractCategoryImpl;
import impl.categories.AbstractCategoryServiceImpl;
import impl.information_registers.UserAccessRightServiceImpl;
import information_registers.UserAccessRight;
import model.ElementStatus;
import model.SessionDataElement;
import model.SessionParameter;
import tools.CommonModule;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {
        PageContainer.CATEGORY_CONTRACTOR_ELEMENT_PAGE,
        PageContainer.CATEGORY_COST_ITEM_ELEMENT_PAGE,
        PageContainer.CATEGORY_CURRENCY_ELEMENT_PAGE,
        PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE,
        PageContainer.CATEGORY_LEGAL_ORGANIZATION_ELEMENT_PAGE,
        PageContainer.CATEGORY_PLANNING_PERIOD_ELEMENT_PAGE,
        PageContainer.CATEGORY_POSITION_ELEMENT_PAGE,
        PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE,
        PageContainer.CATEGORY_USER_ELEMENT_PAGE
})
@MultipartConfig
public class CategoryElement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);
            SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

            setAttributesForCategory(req, PageContainer.getPageName(req.getRequestURI()));
            req.setAttribute("sessionDataElement", sessionDataElement);
            req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);

        }
        else if (CommonModule.getBooleanFromRequest(req,"createNew")){
            Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);

            SessionParameter.INSTANCE.setCurrentUser(req, new User());

            SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);
            setAttributesForCategory(req, PageContainer.getPageName(req.getRequestURI()));
            req.setAttribute("sessionDataElement", sessionDataElement);
            req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);
        }
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AbstractCategory elementEditable = null;

        Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);
        SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        try {
            elementEditable = gson.fromJson(req.getParameter("param"), PageContainer.getAbstractCategoryClass(req.getRequestURI()) );
        }catch (JsonSyntaxException e){
            e.printStackTrace();
            sessionDataElement.setErrorMessage("JsonSyntaxException: "+e.getMessage());
            sessionDataElement.setElementStatus(ElementStatus.ERROR);
        }

        Boolean createNew = CommonModule.getBooleanFromRequest(req, "createNew");

        if (Objects.nonNull(elementEditable)) {
            if (Objects.nonNull(elementEditable.getId())) {
                Long elementRequestId = (Long) CommonModule.getNumberFromRequest(req, "elementId", Long.class);
                if (elementEditable.getId().equals(elementRequestId)) {
                    AbstractCategoryImpl.INSTANCE.update(elementEditable);
                    sessionDataElement.setElementStatus(ElementStatus.CLOSE);
                }
            } else if (createNew) {
                AbstractCategoryImpl.INSTANCE.save(elementEditable);

                if (elementEditable.getClass().equals(User.class)) {
                    UserAccessRightServiceImpl.INSTANCE.createDefaultUserRights((User) elementEditable);
                }

                sessionDataElement.setElementStatus(ElementStatus.CLOSE);
            }
        }

        req.setAttribute("infoResult", sessionDataElement.getErrorMessage());
        req.setAttribute("sessionDataElement", sessionDataElement);

        req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);

       }

   private void setAttributesForCategory(HttpServletRequest req, String pageName){

        req.setAttribute("ruPluralShortName", PageContainer.getCategoryProperty(req.getRequestURI()).getRuPluralShortName());

        Boolean createNew = CommonModule.getBooleanFromRequest(req,"createNew");
        if (createNew){
            Class c =  PageContainer.getAbstractCategoryClass(req.getRequestURI());
            AbstractCategory newElement = null;
            try {
                newElement = (AbstractCategory) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            req.setAttribute("categoryElement", newElement);
        }else {
            req.setAttribute("categoryElement",
                    AbstractCategoryImpl.INSTANCE.getById(PageContainer.getAbstractCategoryClass(req.getRequestURI()), (Long) CommonModule.getNumberFromRequest(req, "elementId", Long.class)));
        }

        req.setAttribute("columnSet",
                AbstractCategoryServiceImpl.INSTANCE.getCategoryColumns( PageContainer.getAbstractCategoryClass(req.getRequestURI()),
                        createNew && !SessionParameter.INSTANCE.adminAccessAllowed(req) ?
                                PageContainer.getNewUserAvailableProperties(PageContainer.getAbstractCategoryClass(req.getRequestURI())):
                                Collections.emptyList()));

    }

}
