package view.servicetools;

import abstract_entity.AbstractDocumentEdi;
import app_info.Constant;
import business_processes.ExecutorTask;
import categories.UploadedFile;
import hibernate.impl.business_processes.ExecutorTaskImpl;
import impl.categories.UploadedFileServiceImpl;
import model.SessionDataElement;
import model.SessionParameter;
import tools.CommonModule;
import tools.PageContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Objects;

/*
 * Created by kostya on 4/6/2017.
 */
@WebServlet(urlPatterns = {PageContainer.DOWNLOAD_PAGE})
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionParameter.INSTANCE.accessAllowed(req)) {
            System.out.println("doGet DownloadServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ExecutorTask executorTask = null;

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            String fileName = req.getParameter("uploadedFileName");
            String name = req.getParameter("uploadedName");
            Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);
            Long executorTaskId = (Long) CommonModule.getNumberFromRequest(req, "executorTaskId", Long.class);

            SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

            if ((Objects.nonNull(sessionDataElement) && Objects.nonNull(sessionDataElement.getDocumentEdi()))) {
                AbstractDocumentEdi documentEdi = sessionDataElement.getDocumentEdi();
                if (Objects.nonNull(executorTaskId)) executorTask = ExecutorTaskImpl.INSTANCE.getById(executorTaskId);
                processDownload(resp, documentEdi, executorTask, fileName, name);
            }
        }
    }

    private void processDownload(HttpServletResponse response, AbstractDocumentEdi documentEdi, ExecutorTask executorTask, String fileName, String name) {
        downloadFileFromDisk(response, documentEdi, executorTask, fileName, name);
    }

    private void downloadFileFromDisk(HttpServletResponse response, AbstractDocumentEdi documentEdi, ExecutorTask executorTask, String fileName, String name) {

        UploadedFile uploadedFile = UploadedFileServiceImpl.INSTANCE.getByFileNameAndDocument(fileName, documentEdi, executorTask);
        downloadFile(response, Constant.BASIC_FILE_PATH+uploadedFile.getFilePath(), name);

    }

    private void downloadFile(HttpServletResponse response, String fullFileName, String shortFileName) {

        if (fullFileName != null) {

            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");

            try {
                String resultFileName = URLEncoder.encode(shortFileName, "UTF-8");
                resultFileName = resultFileName.replace('+', ' ').replace("%28", "(").replace("%29", ")");
                response.setHeader("Content-Disposition", "filename=\"" + resultFileName + "\";");
            } catch (UnsupportedEncodingException e) {
                response.setHeader("Content-Disposition", "charset=UTF-8; filename=\"" + shortFileName + "\";");
            }

            try (OutputStream out = response.getOutputStream()) {
                Files.copy(Paths.get(fullFileName), out);
                out.flush();
            } catch (InvalidPathException e) {
                System.out.println("Error in path " + fullFileName);
            } catch (IOException e) {
                System.out.println("IOException - Unable to get access " + fullFileName);
            }
        }

    }

}
