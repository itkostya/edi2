package view.documents;

import categories.User;
import enumerations.FolderStructure;
import impl.business_processes.ExecutorTaskFolderStructureServiceImpl;
import model.SessionParameter;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

            SessionParameter.INSTANCE.getUserSettings(req).getMapFilter(pageName+"Journal").keySet().stream().filter(s -> Objects.nonNull(req.getParameter((s+"FilterString")))).
                    forEach( s1 -> SessionParameter.INSTANCE.getUserSettings(req).setMapFilterParameter(pageName+"Journal", s1, req.getParameter(s1+"FilterString")));

            StringBuilder sortColumnNumber = new StringBuilder(Objects.isNull(req.getParameter("sortColumn")) ? "" : req.getParameter("sortColumn"));
            if (Objects.nonNull(req.getParameter("bookMark1"))) bookMark1 = req.getParameter("bookMark1"); else bookMark1 = SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter(pageName+"Journal","bookMark1");

            SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameterChanged(pageName+"Journal", bookMark1, sortColumnNumber);
            SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameter(pageName+"Journal", "bookMark1", bookMark1);

            doGet(req, resp);
        }
        else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }

    }

    private void setAttributesDependOnBookMark(HttpServletRequest req, String pageName) {

        SessionParameter.INSTANCE.getUserSettings(req).setDocumentPropertyMap(pageName);

        String bookMark1 = ((Objects.nonNull(req.getParameter("bookMark1"))) ? req.getParameter("bookMark1"): SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter(pageName+"Journal", "bookMark1"));
        String bookMark2 = ((Objects.nonNull(req.getParameter("bookMark2"))) ? req.getParameter("bookMark2"): SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter(pageName+"Journal", "bookMark2"));
        String groupBy = ((Objects.nonNull(req.getParameter("groupBy"))) ? req.getParameter("groupBy"): SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter(pageName+"Journal", "groupBy"));

        req.setAttribute("bookMark1", bookMark1);
        req.setAttribute("bookMark2", bookMark2);
        req.setAttribute("groupBy", groupBy);
        req.setAttribute("propertyMap", SessionParameter.INSTANCE.getUserSettings(req).getDocumentPropertyMap(pageName));

        SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameter(pageName+"Journal","bookMark2", bookMark2);
        SessionParameter.INSTANCE.getUserSettings(req).setMapSortParameter(pageName+"Journal","groupBy", groupBy);

        User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);

        switch (bookMark1) {
            case "tasksListByGroup":
                req.setAttribute("tasksList",
                        ExecutorTaskFolderStructureServiceImpl.INSTANCE.getTasksByFolder(currentUser,
                                FolderStructure.valueOf(bookMark2), groupBy,
                                SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter(pageName+"Journal", bookMark1),
                                SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter(pageName+"Journal", bookMark1),
                                PageContainer.getAbstractDocumentClass(req.getRequestURI())));
                break;
            case "fullTasksList":
                req.setAttribute("tasksList",
                        ExecutorTaskFolderStructureServiceImpl.INSTANCE.getCommonList(currentUser,
                        SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter(pageName+"Journal", bookMark1),
                        SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter(pageName+"Journal", bookMark1),
                        PageContainer.getAbstractDocumentClass(req.getRequestURI())));
                break;
        }

        req.setAttribute("mapSortValue", SessionParameter.INSTANCE.getUserSettings(req).getMapSortParameter(pageName+"Journal", bookMark1));
        req.setAttribute("filterString", SessionParameter.INSTANCE.getUserSettings(req).getMapFilterParameter(pageName+"Journal", bookMark1));

    }


}
