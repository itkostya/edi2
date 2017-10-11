package view.documents;

import abstract_entity.AbstractDocumentEdi;
import categories.UploadedFile;
import hibernate.impl.categories.UploadedFileImpl;
import impl.categories.UploadedFileServiceImpl;
import model.ElementStatus;
import model.SessionDataElement;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum DocumentCreate {

    INSTANCE;

    public static void setResultDocumentCreation(HttpServletRequest req, SessionDataElement sessionDataElement, AbstractDocumentEdi documentEdi) {

        if (Objects.nonNull(sessionDataElement)) {
            if (Objects.isNull(documentEdi) || Objects.isNull(documentEdi.getId()) || sessionDataElement.getElementStatus() == ElementStatus.ERROR) {
                // Some error - stay on page
                if (sessionDataElement.getElementStatus() != ElementStatus.ERROR) {
                    sessionDataElement.setElementStatus(ElementStatus.ERROR);
                    sessionDataElement.setErrorMessage("Document hasn't been created");
                }
                req.setAttribute("infoResult", sessionDataElement.getErrorMessage());

            } else {
                // Ok - document saved or send
                sessionDataElement.setDocumentEdi(documentEdi);
            }
        }
    }

    public static List<UploadedFile> getUploadedFileListFromRequest(HttpServletRequest req, AbstractDocumentEdi documentEdi) {

        List<UploadedFile> result = new ArrayList<>();
        String uploadedFileArrayString[] = req.getParameter("uploadedFileString").split(";");

        for (String fileName : uploadedFileArrayString)
            result.add(UploadedFileServiceImpl.INSTANCE.getByFileNameAndDocument(fileName, documentEdi, null));

        return result;
    }

    public static void addFilesToDocument(List<UploadedFile> fileList, AbstractDocumentEdi documentEdi)
    {

        for (UploadedFile uploadedFile : fileList) {
            UploadedFile uploadedFileInBase = UploadedFileServiceImpl.INSTANCE.getByFileNameAndDocument(uploadedFile.getFileName(), documentEdi, null);
            if (Objects.isNull(uploadedFileInBase)) {
                uploadedFile.setDocument(documentEdi);
                UploadedFileImpl.INSTANCE.save(uploadedFile);
            }
        }
    }

}
