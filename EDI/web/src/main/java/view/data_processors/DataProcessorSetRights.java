package view.data_processors;

import app_info.Constant;
import categories.User;
import hibernate.impl.categories.UserImpl;
import impl.information_registers.UserAccessRightServiceImpl;
import information_registers.UserAccessRight;
import model.SessionParameter;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet(urlPatterns = {
        PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE
})
@MultipartConfig(
        fileSizeThreshold = Constant.FILE_SIZE_THRESHOLD,
        maxFileSize = Constant.MAX_FILE_SIZE,
        maxRequestSize = Constant.MAX_REQUEST_SIZE)
public class DataProcessorSetRights extends HttpServlet {

    private Long currentSelectedUserId = null;
    private final static List<User> userList = UserImpl.INSTANCE.getUsers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO: Run this code twice when one click in userList table - some actions do this
        if (SessionParameter.INSTANCE.accessAllowed(req)) {
//            User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
            if (Objects.isNull(currentSelectedUserId) && userList.size() > 0){
                currentSelectedUserId  = userList.get(0).getId();
            }
            List<UserAccessRight> userAccessRightList = UserAccessRightServiceImpl.INSTANCE.getUserRights(UserImpl.INSTANCE.getUserById(currentSelectedUserId));
            req.setAttribute("userList", userList);
            req.setAttribute("userAccessRightList", userAccessRightList);
            req.setAttribute("currentSelectedUserId", currentSelectedUserId);
            req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);
        }
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            currentSelectedUserId =  Long.valueOf(req.getParameter("currentSelectedUserId"));

            doGet(req, resp);
        }else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

}