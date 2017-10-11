package view.documents;

import abstract_entity.AbstractDocumentEdi;
import app_info.Constant;
import app_info.TimeModule;
import business_processes.ExecutorTask;
import business_processes.ExecutorTaskFolderStructure;
import categories.UploadedFile;
import categories.User;
import documents.DocumentProperty;
import documents.Message;
import enumerations.FolderStructure;
import enumerations.ProcessType;
import hibernate.impl.business_processes.ExecutorTaskFolderStructureImpl;
import hibernate.impl.business_processes.ExecutorTaskImpl;
import hibernate.impl.categories.UploadedFileImpl;
import hibernate.impl.categories.UserImpl;
import hibernate.impl.documents.AbstractDocumentEdiImpl;
import hibernate.impl.documents.MessageImpl;
import impl.business_processes.CommonBusinessProcessServiceImpl;
import impl.business_processes.ExecutorTaskServiceImpl;
import impl.categories.UploadedFileServiceImpl;
import model.ElementStatus;
import model.SessionDataElement;
import model.SessionParameter;
import tools.CommonModule;
import tools.PageContainer;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/*
 * Created by kostya on 8/14/2017
 */

@WebServlet(urlPatterns = {PageContainer.DOCUMENT_MESSAGE_CREATE_PAGE})
@MultipartConfig(
        fileSizeThreshold = Constant.FILE_SIZE_THRESHOLD,
        maxFileSize = Constant.MAX_FILE_SIZE,
        maxRequestSize = Constant.MAX_REQUEST_SIZE)
public class MessageCreate extends HttpServlet {

    private java.sql.Date finalDate = TimeModule.getFinalDateOfProcess(3);
    private final static List<User> userList = UserImpl.INSTANCE.getUsers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AbstractDocumentEdi documentEdi;

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);

            req.setAttribute("userList", userList);
            req.setAttribute("documentTypeId", DocumentProperty.MESSAGE.getId());

            Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);
            SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

            if (sessionDataElement.getElementStatus() == ElementStatus.CREATE
                    || sessionDataElement.getElementStatus() == ElementStatus.ERROR
                    || sessionDataElement.getElementStatus() == ElementStatus.STORE
                    ) {
                documentEdi = null;

                Long documentId = (Long) CommonModule.getNumberFromRequest(req, "documentId", Long.class);
                if (Objects.nonNull(documentId)) {
                    // Work with draft
                    documentEdi = AbstractDocumentEdiImpl.INSTANCE.getById(documentId);
                    sessionDataElement.setDocumentEdi(documentEdi);
                    sessionDataElement.setElementStatus(ElementStatus.CREATE);
                } else if (Objects.nonNull(sessionDataElement.getDocumentEdi())) {
                    documentEdi = sessionDataElement.getDocumentEdi();
                }

                if (Objects.nonNull(documentEdi)) {
                    // Get for draft
                    req.setAttribute("theme", documentEdi.getTheme());
                    req.setAttribute("textInfo", documentEdi.getText());
                    req.setAttribute("userWhomList", Arrays.stream(documentEdi.getWhomString().split(",")).map(s -> (  s.matches("[0-9]+") ? UserImpl.INSTANCE.getUserById(Long.valueOf(s)) : null)).toArray(User[]::new));
                    req.setAttribute("uploadedFiles", UploadedFileServiceImpl.INSTANCE.getListByDocument(documentEdi));
                }else {
                    // Reply, Forward
                    Long documentCopyId = (Long) CommonModule.getNumberFromRequest(req, "documentCopyId", Long.class);
                    if (Objects.nonNull(documentCopyId)) {
                        AbstractDocumentEdi documentCopyEdi = AbstractDocumentEdiImpl.INSTANCE.getById(documentCopyId);
                        String operationType = req.getParameter("operationType");
                        if (operationType.equals("reply")){
                            req.setAttribute("theme", "Ответ на "+documentCopyEdi.getTheme());
                            req.setAttribute("textInfo", CommonModule.getReplyString(documentCopyEdi.getText()));
                            req.setAttribute("userWhomList", Collections.singletonList(documentCopyEdi.getAuthor()));
                        }else if (operationType.equals("forward")){
                            req.setAttribute("theme", documentCopyEdi.getTheme());
                            req.setAttribute("textInfo", "Пересылаю "+CommonModule.getReferenceOnDocument(documentCopyEdi));
                            req.setAttribute("uploadedFiles", UploadedFileServiceImpl.INSTANCE.getListByDocument(documentCopyEdi));
                            sessionDataElement.setDocumentCopyEdi(documentCopyEdi);
                        }
                    }
                }

                if (sessionDataElement.getElementStatus() == ElementStatus.ERROR)
                    req.setAttribute("infoResult", sessionDataElement.getErrorMessage());

                // For review
                req.setAttribute("docDate", new SimpleDateFormat("dd.MM.yyyy").format(Objects.nonNull(documentEdi) ? documentEdi.getDate() : TimeModule.getCurrentDate()));
                req.setAttribute("docNumber", Objects.nonNull(documentEdi) ? documentEdi.getNumber() : "БЕЗ НОМЕРА");
                if (Objects.nonNull(currentUser)) {
                    if (Objects.nonNull(currentUser.getPosition()))
                        req.setAttribute("positionFrom", currentUser.getPosition().getName());
                    req.setAttribute("userFrom", currentUser.getFioInitials());
                }
                req.setAttribute("docType", DocumentProperty.MESSAGE.getRuName());

            }
            req.setAttribute("tempId", tempId);
            req.setAttribute("sessionDataElement", sessionDataElement);

            req.getRequestDispatcher(PageContainer.DOCUMENT_MESSAGE_CREATE_JSP).forward(req, resp);

        } else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AbstractDocumentEdi documentEdi = null;
        ExecutorTask executorTask = null;

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        if (SessionParameter.INSTANCE.accessAllowed(req)) {

            try {

                req.getParts();  // Check is it possible to work (catch error maxFileSize with exception IllegalStateException)

                String param = req.getParameter("param");
                if (Objects.nonNull(param)) {

                    java.sql.Timestamp timeStamp = TimeModule.getCurrentDate();
                    User currentUser = SessionParameter.INSTANCE.getCurrentUser(req);
                    String theme = req.getParameter("theme");
                    String textInfo = req.getParameter("textInfo");

                    Long tempId = (Long) CommonModule.getNumberFromRequest(req, "tempId", Long.class);
                    SessionDataElement sessionDataElement = SessionParameter.INSTANCE.getUserSettings(req).getSessionDataElement(tempId);

                    try {

                        if ((Objects.nonNull(sessionDataElement) && Objects.nonNull(sessionDataElement.getDocumentEdi()))) {
                            documentEdi = sessionDataElement.getDocumentEdi();
                            if (Objects.nonNull(documentEdi)) {
                                // Check if draft exists
                                executorTask = ExecutorTaskServiceImpl.INSTANCE.getDraft(currentUser, documentEdi);
                            }
                        }

                        List<UploadedFile> fileList = CommonModule.getFileListFromRequest(req, "fileList[]"); // new files from client
                        // --- Parse data ---
                        String[] usersIdArray = "".equals(req.getParameter("post_users[]")) ? null : req.getParameter("post_users[]").split(",");

                        if (Objects.isNull(usersIdArray)) {

                            sessionDataElement.setElementStatus(ElementStatus.ERROR);
                            sessionDataElement.setErrorMessage("Не выбран(ы) получатели документа");

                        } else {

                            String whomString;

                            switch (param) {

                                case "save":

                                    whomString = req.getParameter("post_users[]");
                                    whomString = (whomString.length() > 255 ? whomString.substring(0,254): whomString);

                                    documentEdi = createOrUpdateDocument(req, documentEdi, timeStamp, currentUser, theme, textInfo, fileList, whomString, "save", sessionDataElement.getDocumentCopyEdi());
                                    sessionDataElement.setDocumentCopyEdi(null);
                                    if (Objects.isNull(executorTask)) {
                                        executorTask = new ExecutorTask(timeStamp, null, true, currentUser, documentEdi, null, "", null, null, new java.sql.Timestamp(finalDate.getTime()), null, false, false, true);
                                        ExecutorTaskImpl.INSTANCE.save(executorTask);
                                        ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.DRAFT, executorTask.getAuthor(), executorTask, false));
                                    }
                                    sessionDataElement.setElementStatus(ElementStatus.CREATE);

                                    break;

                                case "send":

                                    User[] usersWhom = Arrays.stream(usersIdArray).map(s -> UserImpl.INSTANCE.getUserById(Long.valueOf(s))).toArray(User[]::new);
                                    String tempStr = (Arrays.toString(Arrays.stream(usersWhom).map(User::getFioInitials).toArray())).replace("[", "").replace("]", "");
                                    whomString = (tempStr.length() > 255 ? tempStr.substring(0,254): tempStr);

                                    // Process type
                                    ProcessType processTypeCommon = DocumentProperty.MESSAGE.getProcessTypeList().get(0);

                                    // --- Create document Message, business_process' classes: BusinessProcess, BusinessProcessSequence, ExecutorTask ---

                                    documentEdi = createOrUpdateDocument(req, documentEdi, timeStamp, currentUser, theme, textInfo, fileList, whomString, "send", sessionDataElement.getDocumentCopyEdi());
                                    CommonBusinessProcessServiceImpl.INSTANCE.createAndStartBusinessProcess(currentUser, (Message) documentEdi, executorTask, timeStamp, usersIdArray, null, null, processTypeCommon, null, new java.sql.Timestamp(finalDate.getTime()));
                                    sessionDataElement.setElementStatus(ElementStatus.CLOSE);

                                    break;
                            }
                        }

                    } catch (ConstraintViolationException e) {
                        sessionDataElement.setElementStatus(ElementStatus.ERROR);
                        sessionDataElement.setErrorMessage("Документ не создан (ConstraintViolationException): " +
                                e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
                    } catch (PersistenceException e) {
                        sessionDataElement.setElementStatus(ElementStatus.ERROR);
                        sessionDataElement.setErrorMessage("PersistenceException: " +
                                e.getMessage());
                    }

                    DocumentCreate.setResultDocumentCreation(req, sessionDataElement, documentEdi);

                }
            }
            catch (IllegalStateException e) {
                    req.setAttribute("infoResult",(e.getCause().getMessage().contains("exceeds its maximum permitted size") ?
                            "Ошибка (превышен максимальный размер файла "+Constant.MAX_FILE_SIZE +" или всех файлов "+Constant.MAX_REQUEST_SIZE :
                            e.getCause().getMessage()));

                }

            req.getRequestDispatcher(PageContainer.DOCUMENT_MESSAGE_CREATE_JSP).forward(req, resp);

        } else {
            req.setAttribute("error_message", "Access denied");
            req.getRequestDispatcher(PageContainer.ERROR_JSP).forward(req, resp);
        }
    }

    private AbstractDocumentEdi createOrUpdateDocument(HttpServletRequest req, AbstractDocumentEdi documentEdi, java.sql.Timestamp timeStamp, User currentUser, String theme, String textInfo, List<UploadedFile> fileList, String whomString, String operationType, AbstractDocumentEdi documentCopyEdi) {

        if (Objects.isNull(documentEdi)) {
            documentEdi = new Message(timeStamp, false, null, false, currentUser, CommonModule.getCorrectStringForWeb(theme), (operationType.equals("save") ? textInfo : CommonModule.getCorrectStringForWeb(textInfo)), whomString);
            MessageImpl.INSTANCE.save((Message) documentEdi);
        } else {
            documentEdi.setWhomString(whomString);
            documentEdi.setTheme(CommonModule.getCorrectStringForWeb(theme));
            documentEdi.setText(operationType.equals("save") ? textInfo : CommonModule.getCorrectStringForWeb(textInfo));
            MessageImpl.INSTANCE.update((Message) documentEdi);
        }

        if (Objects.nonNull(documentCopyEdi)) {
            List<UploadedFile> uploadedFilesForCopyFromRequest = DocumentCreate.getUploadedFileListFromRequest(req, documentCopyEdi);
            for (UploadedFile uploadedFileForCopy : uploadedFilesForCopyFromRequest){
                UploadedFile uploadedFileNew = new UploadedFile();
                uploadedFileNew.setDocument(documentEdi);
                uploadedFileNew.setFileName(uploadedFileForCopy.getFileName());
                uploadedFileNew.setFilePath(uploadedFileForCopy.getFilePath());
                uploadedFileNew.setName(uploadedFileForCopy.getName());
                UploadedFileImpl.INSTANCE.save(uploadedFileNew);
            }

        }else {
            // files from request should be the same as the files in database - if this condition doesn't work we should delete files in database
            List<UploadedFile> uploadedFilesFromRequest = DocumentCreate.getUploadedFileListFromRequest(req, documentEdi);
            List<UploadedFile> filesInDatabase = UploadedFileServiceImpl.INSTANCE.getListByDocument(documentEdi);
            filesInDatabase.removeAll(uploadedFilesFromRequest);
            for (UploadedFile uploadedFile : filesInDatabase)
                UploadedFileImpl.INSTANCE.delete(uploadedFile);
        }

        DocumentCreate.addFilesToDocument(fileList, documentEdi);

        return documentEdi;
    }

}
