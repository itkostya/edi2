package view.categories;

import categories.User;
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
        PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE,
        PageContainer.CATEGORY_POSITION_JOURNAL_PAGE,
        PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE,
        PageContainer.CATEGORY_USER_JOURNAL_PAGE
})
public class CategoryJournal extends HttpServlet {

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
        System.out.println("doPost CategoryJournal");
    }

    private void setAttributesForCategory(HttpServletRequest req, String pageName){

        req.setAttribute("ruPluralShortName", PageContainer.getCategoryProperty(req.getRequestURI()).getRuPluralShortName());
        req.setAttribute("ruPluralFullName", PageContainer.getCategoryProperty(req.getRequestURI()).getRuPluralFullName());
        req.setAttribute("categoryTable",
                AbstractCategoryServiceImpl.INSTANCE.getCategoryTable( PageContainer.getAbstractCategoryClass(req.getRequestURI()), ""));
        req.setAttribute("columnSet",
                AbstractCategoryServiceImpl.INSTANCE.getCategoryColumns( PageContainer.getAbstractCategoryClass(req.getRequestURI()), ""));


    }
}
