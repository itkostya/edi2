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

/*
 * Created by kostya on 9/2/2016.
 */
@WebServlet(urlPatterns = {PageContainer.ADMIN_PAGE})
public class AdminPanelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(PageContainer.ADMIN_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        Integer param = Integer.valueOf(req.getParameter("param"));
        switch (param) {
            case 1:
                 CreateData.createDepartments();
                break;
            case 2:
                CreateData.createPositions();
                break;
            case 3:
                CreateData.createUsers();
                break;
            case 4:
                CreateData.createCategories();
                break;
            case 5:
                CreateData.createMemorandums();
                break;
            case 6:
                CreateData.createBusinessProcess();
                break;
            case 7:
                CreateData.createBusinessProcessSequence();
                break;
            case 8:
                CreateData.createExecutorTasks();
                break;
            case 20:
                CreateData.createCheckFileFolder();
                break;
            case 21:
                CreateData.createAll();
                break;
            case 31:
                SessionParameter.INSTANCE.setCurrentUser(req, UserImpl.INSTANCE.getUserById(9L));
                break;
        }

        System.out.println(param);

        doGet(req, resp);

    }
}
