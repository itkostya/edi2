package view.data_processors;

import app_info.Constant;
import categories.User;
import enumerations.MetadataType;
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
import java.util.ArrayList;
import java.util.Arrays;
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
    private User currentSelectedUser = null;
    private final static List<User> userList = UserImpl.INSTANCE.getUsers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (SessionParameter.INSTANCE.adminAccessAllowed(req)) {
            if (Objects.isNull(currentSelectedUserId) && userList.size() > 0) {
                currentSelectedUserId = userList.get(0).getId();
            }
            currentSelectedUser = UserImpl.INSTANCE.getUserById(currentSelectedUserId);
            List<UserAccessRight> userAccessRightList = UserAccessRightServiceImpl.INSTANCE.getUserRights(currentSelectedUser);
            req.setAttribute("userList", userList);
            req.setAttribute("userAccessRightList", userAccessRightList);
            req.setAttribute("currentSelectedUserId", currentSelectedUserId);
            req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);
        } else {
            req.setAttribute("error_message", "Admin access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        if (SessionParameter.INSTANCE.adminAccessAllowed(req)) {

            String param = req.getParameter("param");
            switch (param) {
                case "changeUser":
                    currentSelectedUserId = Long.valueOf(req.getParameter("currentSelectedUserId"));
                    break;
                case "save":
                    String[] metadataStringArray = req.getParameterValues("metadataStringArray[]");
                    String[] metadataTypeStringArray = req.getParameterValues("metadataTypeStringArray[]");
                    String[] viewStringArray = req.getParameterValues("viewStringArray[]");
                    String[] editStringArray = req.getParameterValues("editStringArray[]");
                    List<UserAccessRight> userAccessRightList = new ArrayList<>();
                    for (int i = 0; i < metadataStringArray.length; i++) {
                        userAccessRightList.add(new UserAccessRight(MetadataType.valueOf(metadataTypeStringArray[i]), currentSelectedUser, Boolean.valueOf(viewStringArray[i]), Boolean.valueOf(editStringArray[i])));
                    }
                    UserAccessRightServiceImpl.INSTANCE.updateUserRights(currentSelectedUser, userAccessRightList);
                    break;
            }
        } else {
            req.setAttribute("error_message", "Admin access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

}