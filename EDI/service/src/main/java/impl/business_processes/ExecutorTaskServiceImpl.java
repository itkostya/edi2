package impl.business_processes;

import abstract_entity.AbstractDocumentEdi;
import business_processes.BusinessProcess;
import business_processes.ExecutorTask;
import categories.UploadedFile;
import categories.User;
import enumerations.ProcessType;
import hibernate.impl.business_processes.ExecutorTaskImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * Created by kostya on 11/8/2016.
 */
public enum ExecutorTaskServiceImpl {

    INSTANCE;

    public List<ExecutorTask> getReviewTask(User user, String sortingSequence, String filterString){

        List<ExecutorTask> executorTaskList = ExecutorTaskImpl.INSTANCE.getReviewTask(user, filterString);

        if (sortingSequence.equals("default")) {
            // ProcessType asc, FinalDate asc, Date desc
            executorTaskList.sort(Comparator.comparing(ExecutorTask::getProcessType).thenComparing(ExecutorTask::getFinalDate).thenComparing(Comparator.comparing(ExecutorTask::getDate).reversed()));
        }
        else {

            boolean ascSorting = (sortingSequence.charAt(2)=='+' || sortingSequence.charAt(2)=='n');
            switch (sortingSequence.charAt(0)) {
                case '0':  // cell.processType.ruName (but it's natural enum sorting - not by ruName)
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(ExecutorTask::getProcessType));
                    else executorTaskList.sort(Comparator.comparing(ExecutorTask::getProcessType).reversed());
                    break;
                case '1':  // cell.author.getFio()
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(o -> o.getAuthor().getFio()));
                    else executorTaskList.sort((o1, o2) -> o2.getAuthor().getFio().compareTo(o1.getAuthor().getFio()));
                case '2':  //cell.document.getDocumentView("dd.MM.yyyy HH:mm:ss")
                    // Just by date. Or it could be document property + date
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(o -> o.getDocument().getDate()));
                    else executorTaskList.sort((o1, o2) -> o2.getDocument().getDate().compareTo(o1.getDocument().getDate()));
                    break;
                case '3':  // cell.document.theme
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(o -> o.getDocument().getTheme()));
                    else executorTaskList.sort((o1, o2) -> o2.getDocument().getTheme().compareTo(o1.getDocument().getTheme()));
                    break;
                case '4':  // TimeModule.getDate(cell.date, 'dd.MM.yyyy HH:mm:ss')
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(ExecutorTask::getDate));
                    else executorTaskList.sort(Comparator.comparing(ExecutorTask::getDate).reversed());
                    break;
                case '5':  // TimeModule.getDate(cell.finalDate, 'dd.MM.yyyy')
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(ExecutorTask::getFinalDate));
                    else executorTaskList.sort(Comparator.comparing(ExecutorTask::getFinalDate).reversed());
                    break;
            }

        }

        return executorTaskList;
    }

    public List<ExecutorTask> getControlledTask(User user, String sortingSequence, String filterString) {

        List<ExecutorTask> executorTaskList = ExecutorTaskImpl.INSTANCE.getControlledTask(user, filterString);
        if (sortingSequence.equals("default")) {
            // ProcessType asc, FinalDate asc, Date desc
            executorTaskList.sort(Comparator.comparing(ExecutorTask::getProcessType).thenComparing(ExecutorTask::getFinalDate).thenComparing(Comparator.comparing(ExecutorTask::getDate).reversed()));
        }
        else {

            boolean ascSorting = (sortingSequence.charAt(2)=='+' || sortingSequence.charAt(2)=='n');
            switch (sortingSequence.charAt(0)) {
                case '0':  // cell.processType.ruName (but it's natural enum sorting - not by ruName)
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(ExecutorTask::getProcessType));
                    else executorTaskList.sort(Comparator.comparing(ExecutorTask::getProcessType).reversed());
                    break;
                case '1':  // cell.executor.getFio()
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(o -> o.getExecutor().getFio()));
                    else executorTaskList.sort((o1, o2) -> o2.getExecutor().getFio().compareTo(o1.getExecutor().getFio()));
                    break;
                case '2':  // cell.document.getDocumentView("dd.MM.yyyy HH:mm:ss")
                    // Just by date. Or it could be document property + date
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(o -> o.getDocument().getDate()));
                    else executorTaskList.sort((o1, o2) -> o2.getDocument().getDate().compareTo(o1.getDocument().getDate()));
                    break;
                case '3':   // cell.document.theme
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(o -> o.getDocument().getTheme()));
                    else executorTaskList.sort((o1, o2) -> o2.getDocument().getTheme().compareTo(o1.getDocument().getTheme()));
                    break;
                case '4':   // TimeModule.getDate(cell.finalDate, 'dd.MM.yyyy')
                    if (ascSorting) executorTaskList.sort(Comparator.comparing(ExecutorTask::getFinalDate));
                    else executorTaskList.sort(Comparator.comparing(ExecutorTask::getFinalDate).reversed());
                    break;
            }

        }

        return executorTaskList;
    }

    public List<ExecutorTask> getFilterByBusinessProcess(BusinessProcess businessProcess) {
        return ExecutorTaskImpl.INSTANCE.getFilterByBusinessProcess(businessProcess);
    }

    public List<ExecutorTask> getSignaturesByList(AbstractDocumentEdi abstractDocumentEdi) {
        return ExecutorTaskImpl.INSTANCE.getSignatures(abstractDocumentEdi);
    }

    public Map<ExecutorTask, List<UploadedFile>> getSignaturesByMap(AbstractDocumentEdi abstractDocumentEdi){
        return ExecutorTaskImpl.INSTANCE.getSignaturesWithUploadedFiles(abstractDocumentEdi);
    }

    public List<ExecutorTask> getFilterByExecutorAndDocument(User executor, AbstractDocumentEdi documentEdi){
        return ExecutorTaskImpl.INSTANCE.getFilterByExecutorAndDocument(executor, documentEdi);
    }

    public List<ExecutorTask> getFilterByUserAndDocument(User user, AbstractDocumentEdi documentEdi){
        return ExecutorTaskImpl.INSTANCE.getFilterByUserAndDocument(user, documentEdi);
    }

    public List<ExecutorTask> getFilterByExecutorDocumentAndProcessType(User executor, AbstractDocumentEdi documentEdi, ProcessType processType){
        return ExecutorTaskImpl.INSTANCE.getFilterByExecutorDocumentAndProcessType(executor, documentEdi, processType);
    }

    public List<ExecutorTask> getWithdrawAvailable(User executor, AbstractDocumentEdi documentEdi){
        return ExecutorTaskImpl.INSTANCE.getWithdrawAvailable(executor, documentEdi);
    }

    public void checkAndSetDeletionDependOnUser(User currentUser, ExecutorTask executorTask, boolean deletionMark) {

        if (Objects.nonNull(executorTask) && Objects.nonNull(currentUser)) {

            boolean updateExecutorTask = false;

            if (currentUser.equals(executorTask.getAuthor()) && deletionMark != executorTask.isDeletedByAuthor()) {
                executorTask.setDeletedByAuthor(deletionMark);
                updateExecutorTask = true;
            }
            if (currentUser.equals(executorTask.getExecutor()) && deletionMark != executorTask.isDeletedByExecutor()) {
                executorTask.setDeletedByExecutor(deletionMark);
                updateExecutorTask = true;
            }

            if (updateExecutorTask) ExecutorTaskImpl.INSTANCE.update(executorTask);
        }
    }

    public ExecutorTask getDraft(User author, AbstractDocumentEdi documentEdi){
        return ExecutorTaskImpl.INSTANCE.getDraft(author, documentEdi);
    }

}
