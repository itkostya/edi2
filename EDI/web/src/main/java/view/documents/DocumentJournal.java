package view.documents;

import categories.User;
import enumerations.FolderStructure;
import impl.business_processes.ExecutorTaskFolderStructureServiceImpl;
import model.SessionParameter;
import tools.CommonModule;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet(urlPatterns = {
        PageContainer.DOCUMENT_MEMORANDUM_JOURNAL_PAGE,
        PageContainer.DOCUMENT_MESSAGE_JOURNAL_PAGE
})
public class DocumentJournal extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (SessionParameter.INSTANCE.accessAllowed(req)) {
            setAttributesDependOnBookMark(req, PageContainer.getPageName(req.getRequestURI()));
            req.getRequestDispatcher(PageContainer.getJspName(req.getRequestURI())).forward(req, resp);
        }
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String bookMark1;
        String pageName = PageContainer.getPageName(req.getRequestURI());

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            Map<String, String> mapSort = SessionParameter.INSTANCE.getUserSettings(req).getMapSort(pageName+"Journal");

            SessionParameter.INSTANCE.getUserSettings(req).getMapFilter(pageName+"Journal").keySet().stream().filter(s -> Objects.nonNull(req.getParameter((s+"FilterString")))).
                    forEach( s1 -> SessionParameter.INSTANCE.getUserSettings(req).setMapFilter(pageName+"Journal", s1, req.getParameter(s1+"FilterString")));

            StringBuilder sortColumnNumber = new StringBuilder(Objects.isNull(req.getParameter("sortColumn")) ? "" : req.getParameter("sortColumn"));
            if (Objects.nonNull(req.getParameter("bookMark1"))) bookMark1 = req.getParameter("bookMark1"); else bookMark1 = mapSort.get("bookMark1");
            CommonModule.INSTANCE.replaceSortingParameter(mapSort, bookMark1, sortColumnNumber);
            mapSort.put("bookMark1", bookMark1);

            doGet(req, resp);
        }
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }

    }

    private void setAttributesDependOnBookMark(HttpServletRequest req, String pageName) {

        String bookMark1, bookMark2, groupBy;
        Map<String, String> mapSort = SessionParameter.INSTANCE.getUserSettings(req).getMapSort(pageName+"Journal");
        Map<String, String> mapFilter = SessionParameter.INSTANCE.getUserSettings(req).getMapFilter(pageName+"Journal");
        SessionParameter.INSTANCE.getUserSettings(req).setDocumentPropertyMap(pageName);
        Map<String, Map<FolderStructure, Integer>> documentPropertyMap = SessionParameter.INSTANCE.getUserSettings(req).getDocumentPropertyMap();

        if (req.getParameter("bookMark1") != null) bookMark1 = req.getParameter("bookMark1"); else bookMark1 = mapSort.get("bookMark1");
        if (req.getParameter("bookMark2") != null) bookMark2 = req.getParameter("bookMark2"); else bookMark2 = mapSort.get("bookMark2");
        if (req.getParameter("groupBy") != null) groupBy = req.getParameter("groupBy"); else groupBy = mapSort.get("groupBy");

        req.setAttribute("bookMark1", bookMark1);
        req.setAttribute("bookMark2", bookMark2);
        req.setAttribute("groupBy", groupBy);
        req.setAttribute("propertyMap", documentPropertyMap.get(pageName));

        mapSort.put("bookMark2", bookMark2);
        mapSort.put("groupBy", groupBy);
        User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);

        switch (bookMark1) {
            case "tasksListByGroup":
                req.setAttribute("tasksList", ExecutorTaskFolderStructureServiceImpl.INSTANCE.getTasksByFolder(currentUser, FolderStructure.valueOf(bookMark2), groupBy, mapSort.get(bookMark1), mapFilter.get(bookMark1), PageContainer.getAbstractDocumentClass(req.getRequestURI())));
                req.setAttribute("mapSortValue", mapSort.get(bookMark1));
                break;
            case "fullTasksList":
                req.setAttribute("tasksList", ExecutorTaskFolderStructureServiceImpl.INSTANCE.getCommonList(currentUser,  mapSort.get(bookMark1), mapFilter.get(bookMark1), PageContainer.getAbstractDocumentClass(req.getRequestURI())));
                req.setAttribute("mapSortValue", mapSort.get(bookMark1));
                break;
        }

        req.setAttribute("filterString", mapFilter.get(bookMark1));

    }


}