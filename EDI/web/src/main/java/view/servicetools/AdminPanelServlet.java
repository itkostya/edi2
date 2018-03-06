package view.servicetools;


import categories.User;
import hibernate.impl.categories.UserImpl;
import model.SessionParameter;
import service_tools.CreateData;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/*
 * Created by kostya on 9/2/2016.
 */
@WebServlet(urlPatterns = {PageContainer.ADMIN_PAGE})
public class AdminPanelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isDatabaseEmpty =  UserImpl.INSTANCE.isDatabaseEmpty();
        req.setAttribute("isDatabaseEmpty", UserImpl.INSTANCE.isDatabaseEmpty());
        if ((isDatabaseEmpty) || (SessionParameter.INSTANCE.adminAccessAllowed(req))) {
            req.getRequestDispatcher(PageContainer.ADMIN_JSP).forward(req, resp);
        }else{
            User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
            req.setAttribute("error_message", (Objects.isNull(currentUser) ? "You should pass authorization first" : String.format("User %s doesn't have admin role", currentUser.getName())));
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

            Integer param = Integer.valueOf(req.getParameter("param"));
            switch (param) {
                case 1:
                    // Creation tables only if database is empty
                    if (UserImpl.INSTANCE.isDatabaseEmpty()) {
                        CreateData.createCategories();
                        // After creation user has admin access
                        SessionParameter.INSTANCE.setCurrentUser(req, UserImpl.INSTANCE.getUserById(8L));
                    }
                    break;
            }

        doGet(req, resp);

    }
}
