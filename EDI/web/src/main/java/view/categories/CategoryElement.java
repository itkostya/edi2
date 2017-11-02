package view.categories;

import categories.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hibernate.impl.categories.AbstractCategoryImpl;
import impl.categories.AbstractCategoryServiceImpl;
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
import java.util.Objects;

@WebServlet(urlPatterns = {
        PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE,
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
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User userEdit = null;

        Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);
        SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

        Gson gson = new Gson();  //https://stackoverflow.com/questions/443499/convert-json-to-map
        try {
            userEdit = gson.fromJson(req.getParameter("param"), User.class);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
            sessionDataElement.setErrorMessage("JsonSyntaxException");
            sessionDataElement.setElementStatus(ElementStatus.ERROR);
        }

        if (Objects.nonNull(userEdit)
                && Objects.nonNull(userEdit.getId())){
            Long elementId = (Long) CommonModule.getNumberFromRequest(req, "elementId", Long.class);
            if (userEdit.getId().equals(elementId)){
                // Update element
                AbstractCategoryImpl.INSTANCE.update(userEdit);
                sessionDataElement.setElementStatus(ElementStatus.CLOSE);
            }
        }

        req.setAttribute("infoResult", sessionDataElement.getErrorMessage());
        req.setAttribute("sessionDataElement", sessionDataElement);

        req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);

       }

   private void setAttributesForCategory(HttpServletRequest req, String pageName){

        req.setAttribute("ruPluralShortName", PageContainer.getCategoryProperty(req.getRequestURI()).getRuPluralShortName());
        req.setAttribute("categoryElement",
                AbstractCategoryImpl.INSTANCE.getById( PageContainer.getAbstractCategoryClass(req.getRequestURI()), (Long) CommonModule.getNumberFromRequest(req, "elementId", Long.class)));
        req.setAttribute("columnSet",
                AbstractCategoryServiceImpl.INSTANCE.getCategoryColumns( PageContainer.getAbstractCategoryClass(req.getRequestURI()), ""));

    }

}
