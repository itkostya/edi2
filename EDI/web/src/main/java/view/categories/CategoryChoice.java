package view.categories;

import impl.categories.AbstractCategoryServiceImpl;
import model.SessionParameter;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {
        PageContainer.CATEGORY_CONTRACTOR_CHOICE_PAGE,
        PageContainer.CATEGORY_COST_ITEM_CHOICE_PAGE,
        PageContainer.CATEGORY_CURRENCY_CHOICE_PAGE,
        PageContainer.CATEGORY_DEPARTMENT_CHOICE_PAGE,
        PageContainer.CATEGORY_LEGAL_ORGANIZATION_CHOICE_PAGE,
        PageContainer.CATEGORY_PLANNING_PERIOD_CHOICE_PAGE,
        PageContainer.CATEGORY_POSITION_CHOICE_PAGE,
        PageContainer.CATEGORY_PROPOSAL_TEMPLATE_CHOICE_PAGE,
        PageContainer.CATEGORY_USER_CHOICE_PAGE
})
public class CategoryChoice extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionParameter.INSTANCE.accessAllowed(req)) {
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
        doGet(req, resp);
    }

    private void setAttributesForCategory(HttpServletRequest req, String pageName){

        req.setAttribute("ruPluralShortName", PageContainer.getCategoryProperty(req.getRequestURI()).getRuPluralShortName());
        req.setAttribute("categoryTable",
                AbstractCategoryServiceImpl.INSTANCE.getCategoryTable( PageContainer.getAbstractCategoryClass(req.getRequestURI()), ""));

    }
}
