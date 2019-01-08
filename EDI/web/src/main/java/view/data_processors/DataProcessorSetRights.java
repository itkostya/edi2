package view.data_processors;

import categories.User;
import hibernate.impl.categories.UserImpl;
import impl.information_registers.UserAccessRightServiceImpl;
import information_registers.UserAccessRight;
import model.SessionParameter;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE
})

public class DataProcessorSetRights extends HttpServlet {

    private final static List<User> userList = UserImpl.INSTANCE.getUsers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionParameter.INSTANCE.accessAllowed(req)) {
            User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
            User selectedUser  = (userList.size() > 0 ? userList.get(0) : currentUser);
            List<UserAccessRight> userAccessRightList = UserAccessRightServiceImpl.INSTANCE.getUserRights(selectedUser);
            req.setAttribute("userList", userList);
            req.setAttribute("selectedUser", selectedUser);
            req.setAttribute("userAccessRightList", userAccessRightList);
            req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);
        }
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (SessionParameter.INSTANCE.accessAllowed(req)) {
            doGet(req, resp);
        }else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

}