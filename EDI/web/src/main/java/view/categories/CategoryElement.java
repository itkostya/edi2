package view.categories;

import hibernate.impl.categories.AbstractCategoryImpl;
import impl.categories.AbstractCategoryServiceImpl;
import tools.CommonModule;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {
        PageContainer.CATEGORY_DEPARTMENT_ELEMENT_PAGE,
        PageContainer.CATEGORY_POSITION_ELEMENT_PAGE,
        PageContainer.CATEGORY_PROPOSAL_TEMPLATE_ELEMENT_PAGE,
        PageContainer.CATEGORY_USER_ELEMENT_PAGE
})
public class CategoryElement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //if (SessionParameter.INSTANCE.accessAllowed(req)) {
        if (true){
            setAttributesForCategory(req, PageContainer.getPageName(req.getRequestURI()));
            req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);
        }
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost CategoryElement");
    }

    private void setAttributesForCategory(HttpServletRequest req, String pageName){

        req.setAttribute("ruPluralShortName", PageContainer.getCategoryProperty(req.getRequestURI()).getRuPluralShortName());
        req.setAttribute("categoryElement",
                AbstractCategoryImpl.INSTANCE.getById( PageContainer.getAbstractCategoryClass(req.getRequestURI()), (Long) CommonModule.getNumberFromRequest(req, "elementId", Long.class)));
        req.setAttribute("columnSet",
                AbstractCategoryServiceImpl.INSTANCE.getCategoryColumns( PageContainer.getAbstractCategoryClass(req.getRequestURI()), ""));

    }

}
