package view.work_area;

import categories.User;
import enumerations.FolderStructure;
import impl.business_processes.ExecutorTaskFolderStructureServiceImpl;
import impl.business_processes.ExecutorTaskServiceImpl;
import impl.categories.UserServiceImpl;
import model.SessionParameter;
import tools.CommonModule;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
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

            Map<String, String> mapSort = SessionParameter.INSTANCE.getUserSettings(req).getMapSort("MainPanelServlet");

            SessionParameter.INSTANCE.getUserSettings(req).getMapFilter("MainPanelServlet").keySet().stream().filter(s -> Objects.nonNull(req.getParameter((s+"FilterString")))).
                    forEach( s1 -> SessionParameter.INSTANCE.getUserSettings(req).setMapFilter("MainPanelServlet", s1, req.getParameter(s1+"FilterString")));

            if (Objects.nonNull(req.getParameter("bookMark"))) bookMark = req.getParameter("bookMark");
            else bookMark = mapSort.get("bookMark");

            StringBuilder sortColumnNumber = new StringBuilder(Objects.isNull(req.getParameter("sortColumn")) ? "" : req.getParameter("sortColumn"));
            CommonModule.INSTANCE.replaceSortingParameter(mapSort, bookMark, sortColumnNumber);
            mapSort.put("bookMark", bookMark);

            doGet(req, resp);

        } else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    private void setAttributesDependOnBookMark(HttpServletRequest req) {

        String bookMark;

        User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
        Map<String, String> mapSort = SessionParameter.INSTANCE.getUserSettings(req).getMapSort("MainPanelServlet");
        Map<String, String> mapFilter = SessionParameter.INSTANCE.getUserSettings(req).getMapFilter("MainPanelServlet");
        SessionParameter.INSTANCE.getUserSettings(req).setDocumentPropertyMap("Memorandum");

        Map<String, Map<FolderStructure, Integer>> documentPropertyMap = SessionParameter.INSTANCE.getUserSettings(req).getDocumentPropertyMap();
        if (Objects.nonNull(req.getParameter("bookMark"))) bookMark = req.getParameter("bookMark");
        else bookMark = mapSort.get("bookMark");

        req.setAttribute("userPresentation", currentUser.getFio());
        req.setAttribute("bookMark", bookMark);
        req.setAttribute("messageCount", 0);  // documentPropertyMap.get("Message")
        req.setAttribute("memorandumCount", documentPropertyMap.get("Memorandum").get(FolderStructure.INBOX));  // documentPropertyMap.get("Memorandum")

        switch (bookMark) {
            case "reviewTasksList":
                req.setAttribute("reviewTasksList", ExecutorTaskServiceImpl.INSTANCE.getReviewTask(currentUser, mapSort.get(bookMark), mapFilter.get(bookMark)));
                req.setAttribute("mapSortValue", mapSort.get(bookMark));
                break;
            case "controlledTasksList":
                req.setAttribute("controlledTasksList", ExecutorTaskServiceImpl.INSTANCE.getControlledTask(currentUser, mapSort.get(bookMark), mapFilter.get(bookMark)));
                req.setAttribute("mapSortValue", mapSort.get(bookMark));
                break;
            case "markedTasksList":
                req.setAttribute("markedTasksList", ExecutorTaskFolderStructureServiceImpl.INSTANCE.getMarkedTask(currentUser, mapSort.get(bookMark), mapFilter.get(bookMark)));
                req.setAttribute("mapSortValue", mapSort.get(bookMark));
                break;
            case "coworkersList":
                req.setAttribute("coworkersList", UserServiceImpl.INSTANCE.getCoworkers(mapFilter.get(bookMark)));
                break;
        }

        req.setAttribute("filterString", mapFilter.get(bookMark));

    }


}
