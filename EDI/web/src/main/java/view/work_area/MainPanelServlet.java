package view.work_area;

import categories.User;
import enumerations.FolderStructure;
import impl.business_processes.ExecutorTaskFolderStructureServiceImpl;
import impl.business_processes.ExecutorTaskServiceImpl;
import impl.categories.UserServiceImpl;
import model.SessionParameter;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/*
 * Created by kostya on 9/7/2016.
 */
@WebServlet(urlPatterns = {PageContainer.WORK_AREA_PAGE})
@MultipartConfig
public class MainPanelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (SessionParameter.INSTANCE.accessAllowed(req)) {
            setAttributesDependOnBookMark(req);
            req.getRequestDispatcher(PageContainer.WORK_AREA_JSP).forward(req, resp);
        } else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String bookMark;

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            SessionParameter.INSTANCE.getUserSettings(req).getMapFilter("MainPanelServlet").keySet().stream().filter(s -> Objects.nonNull(req.getParameter((s+"FilterString")))).
                    forEach( s1 -> SessionParameter.INSTANCE.getUserSettings(req).setMapFilterParameter("MainPanelServlet", s1, req.getParameter(s1+"FilterString")));

            if (Objects.nonNull(req.getParameter("bookMark"))) bookMark = req.getParameter("bookMark");
            else bookMark = SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", "bookMark");

            StringBuilder sortColumnNumber = new StringBuilder(Objects.isNull(req.getParameter("sortColumn")) ? "" : req.getParameter("sortColumn"));
            SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameterChanged("MainPanelServlet", bookMark, sortColumnNumber);
            SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameter("MainPanelServlet", "bookMark", bookMark);

            doGet(req, resp);

        } else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    private void setAttributesDependOnBookMark(HttpServletRequest req) {

        String bookMark;

        User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
        SessionParameter.INSTANCE.getUserSettings(req).setDocumentPropertyMap("Memorandum");
        SessionParameter.INSTANCE.getUserSettings(req).setDocumentPropertyMap("Message");

        if (Objects.nonNull(req.getParameter("bookMark"))) bookMark = req.getParameter("bookMark");
        else bookMark = SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", "bookMark");

        req.setAttribute("userPresentation", currentUser.getFio());
        req.setAttribute("bookMark", bookMark);
        req.setAttribute("memorandumCount", SessionParameter.INSTANCE.getUserSettings(req).getMapDocumentPropertyParameter("Memorandum",FolderStructure.INBOX)); // documentPropertyMap.get("Memorandum").get(FolderStructure.INBOX));
        req.setAttribute("messageCount", SessionParameter.INSTANCE.getUserSettings(req).getMapDocumentPropertyParameter("Message",FolderStructure.INBOX));    // documentPropertyMap.get("Message").get(FolderStructure.INBOX));

        switch (bookMark) {
            case "reviewTasksList":
                req.setAttribute("reviewTasksList",
                        ExecutorTaskServiceImpl.INSTANCE.getReviewTask(currentUser,
                                SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark),
                                SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                req.setAttribute("mapSortValue", SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark));
                break;
            case "controlledTasksList":
                req.setAttribute("controlledTasksList",
                        ExecutorTaskServiceImpl.INSTANCE.getControlledTask(currentUser,
                                SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark),
                                SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                req.setAttribute("mapSortValue", SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark));
                break;
            case "markedTasksList":
                req.setAttribute("markedTasksList",
                        ExecutorTaskFolderStructureServiceImpl.INSTANCE.getMarkedTask(currentUser,
                                SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark),
                                SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                req.setAttribute("mapSortValue", SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter("MainPanelServlet", bookMark));
                break;
            case "coworkersList":
                req.setAttribute("coworkersList", UserServiceImpl.INSTANCE.getCoworkers(
                        SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark)));
                break;
        }

        req.setAttribute("filterString", SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter("MainPanelServlet", bookMark));

    }


}
