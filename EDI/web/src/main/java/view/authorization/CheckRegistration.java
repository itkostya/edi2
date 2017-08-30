package view.authorization;

import model.SessionParameter;
import categories.User;
import documents.DocumentProperty;
import hibernate.impl.categories.UserImpl;
import impl.categories.UserServiceImpl;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*
 * Created by kostya on 9/5/2016.
 */
@WebServlet(urlPatterns = {PageContainer.USER_PAGE})
public class CheckRegistration extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<User> userList = UserImpl.INSTANCE.getUsers();
        req.setAttribute("userList", userList);

        req.getRequestDispatcher(PageContainer.USER_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        User userClient = getUserFromRequest(req);

        if (UserServiceImpl.INSTANCE.isUserExist(userClient)) {
            if (UserServiceImpl.INSTANCE.isPasswordCorrect(userClient)) {
                User databaseUser = UserImpl.INSTANCE.getUserByLogin(userClient.getLogin());
                SessionParameter.INSTANCE.setCurrentUser(req, databaseUser);
                req.setAttribute("UserPresentation", databaseUser.getFio());
                req.setAttribute("Bookmark", "1");
                req.setAttribute("DocumentPropertyList", DocumentProperty.values());
                //req.getRequestDispatcher(PageContainer.WORK_AREA_JSP).forward(req, resp);
                resp.sendRedirect(PageContainer.WORK_AREA_PAGE);

            } else {
                req.setAttribute("error_message", String.format("Password for user %s is not correct", userClient.getLogin()));
                req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
            }
        } else {
            req.setAttribute("error_message", String.format("User %s does not exist", userClient.getLogin()));
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }

    }

    private User getUserFromRequest(HttpServletRequest req) {

        String login = req.getParameter("login");
        String password = req.getParameter("pass");

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        return user;
    }


}
