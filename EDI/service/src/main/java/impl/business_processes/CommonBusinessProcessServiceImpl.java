package impl.business_processes;

import abstract_entity.AbstractDocumentEdi;
import business_processes.BusinessProcess;
import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;
import business_processes.ExecutorTaskFolderStructure;
import categories.User;
import enumerations.FolderStructure;
import enumerations.ProcessOrderType;
import enumerations.ProcessResult;
import enumerations.ProcessType;
import hibernate.impl.business_processes.BusinessProcessImpl;
import hibernate.impl.business_processes.BusinessProcessSequenceImpl;
import hibernate.impl.business_processes.ExecutorTaskFolderStructureImpl;
import hibernate.impl.business_processes.ExecutorTaskImpl;
import hibernate.impl.categories.UploadedFileImpl;
import hibernate.impl.categories.UserImpl;
import impl.categories.UploadedFileServiceImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * Created by kostya on 1/30/2017.
 */
public enum CommonBusinessProcessServiceImpl {

    INSTANCE;

    public void createAndStartBusinessProcess(User currentUser, AbstractDocumentEdi m1, ExecutorTask draftExecutorTask, java.sql.Timestamp timeStamp,
                                                     String[] usersIdArray, String[] orderTypeArray, String[] processTypeArray,
                                                     ProcessType processTypeCommon, String comment, java.sql.Timestamp finalDate) {

        BusinessProcess businessProcess = new BusinessProcess(timeStamp, false, currentUser, finalDate, m1, comment, null);
        BusinessProcessImpl.INSTANCE.save(businessProcess);

        boolean createNewTask = true;

        for (int k = 0; k < usersIdArray.length; k++) {

            ProcessOrderType processOrderType = (Objects.nonNull(orderTypeArray) && (k < orderTypeArray.length) ? ProcessOrderType.values()[Integer.valueOf(orderTypeArray[k])] : null);

            ProcessType processType = (Objects.isNull(processTypeCommon) ? (k < processTypeArray.length ? ProcessType.values()[Integer.valueOf(processTypeArray[k])] : null) : processTypeCommon);

            User userExecutor = UserImpl.INSTANCE.getUserById(Long.valueOf(usersIdArray[k]));

            BusinessProcessSequence businessProcessSequence =
                    new BusinessProcessSequence(null, businessProcess, userExecutor, false, processOrderType, null, processType, false, null);
            BusinessProcessSequenceImpl.INSTANCE.save(businessProcessSequence);

            if ((k == 0) || (createNewTask)) {
                ExecutorTask executorTask;
                if (k == 0 && Objects.nonNull(draftExecutorTask)) {
                    executorTask = draftExecutorTask;
                    executorTask.setBusinessProcess(businessProcess);
                    executorTask.setCompleted(false);
                    executorTask.setProcessType(processType);
                    executorTask.setExecutor(userExecutor);
                    executorTask.setDraft(false);
                    ExecutorTaskImpl.INSTANCE.update(executorTask);

                    for (ExecutorTaskFolderStructure executorTaskFolderStructure : ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorTaskFolderStructureByUser(currentUser, executorTask))
                        ExecutorTaskFolderStructureImpl.INSTANCE.delete(executorTaskFolderStructure);

                } else {
                    executorTask = new ExecutorTask(timeStamp, businessProcess, false, currentUser, m1, null, "", null, processType, finalDate, userExecutor, false, false, false);
                    ExecutorTaskImpl.INSTANCE.save(executorTask);
                }

                businessProcessSequence.setDate(timeStamp);
                businessProcessSequence.setExecutorTask(executorTask);
                BusinessProcessSequenceImpl.INSTANCE.update(businessProcessSequence);

                ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.SENT, executorTask.getAuthor(), executorTask, false));
                ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.INBOX, executorTask.getExecutor(), executorTask, false));

                if (processOrderType == ProcessOrderType.AFTER) createNewTask = false;

            }

        }

    }

    public void stopBusinessProcessSequence(AbstractDocumentEdi documentEdi, Long[] businessProcessSequenceArrayId, ExecutorTask currentExecutorTask) {

        java.sql.Timestamp timeStamp = new Timestamp(new java.util.Date().getTime());

        Map<BusinessProcess, List<BusinessProcessSequence>> mapBusinessProcess = BusinessProcessSequenceServiceImpl.INSTANCE.getHistoryByDocumentMap(documentEdi);
        for (Map.Entry<BusinessProcess, List<BusinessProcessSequence>> businessProcessListEntry : mapBusinessProcess.entrySet()) {
            boolean shouldBeCanceled = false;
            for (int i = 0; i < businessProcessListEntry.getValue().size(); i++) {
                BusinessProcessSequence businessProcessSequence = businessProcessListEntry.getValue().get(i);
                if (shouldBeCanceled || (Arrays.stream(businessProcessSequenceArrayId).anyMatch(t -> t.equals(businessProcessSequence.getId())))) {
                    if (Objects.isNull(businessProcessSequence.getExecutorTask())) {
                        BusinessProcessSequenceImpl.INSTANCE.delete(businessProcessSequence);
                        shouldBeCanceled = true;
                    } else {
                        businessProcessSequence.setResult(ProcessResult.CANCELED);
                        BusinessProcessSequenceImpl.INSTANCE.update(businessProcessSequence);
                        ExecutorTask executorTask = businessProcessSequence.getExecutorTask();
                        cancelExecutorTask(executorTask.equals(currentExecutorTask) ? currentExecutorTask : executorTask, timeStamp);
                        if (businessProcessSequence.getOrderBp() == ProcessOrderType.AFTER) shouldBeCanceled = true;
                    }

                }

            }
        }
    }

    private void cancelExecutorTask(ExecutorTask executorTask, java.sql.Timestamp timeStamp) {
        executorTask.setCompleted(true);
        executorTask.setResult(ProcessResult.CANCELED);
        executorTask.setDateCompleted(timeStamp);
        ExecutorTaskImpl.INSTANCE.update(executorTask);
    }

    private void clearExecutorTask(ExecutorTask executorTask) {
        executorTask.setCompleted(false);
        executorTask.setResult(null);
        executorTask.setDateCompleted(null);
        executorTask.setComment("");
        ExecutorTaskImpl.INSTANCE.update(executorTask);
    }

    public void withdrawExecutorTasks(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask currentExecutorTask) {

        List<ExecutorTask> withdrawAvailableList = ExecutorTaskServiceImpl.INSTANCE.getWithdrawAvailable(currentUser, documentEdi);
        withdrawAvailableList.stream().filter(Objects::nonNull).forEach(withdrawExecutorTask -> {

            clearExecutorTask(withdrawExecutorTask.equals(currentExecutorTask) ? currentExecutorTask : withdrawExecutorTask);

            BusinessProcess businessProcess = withdrawExecutorTask.getBusinessProcess();
            if (Objects.nonNull(businessProcess)) {
                businessProcess.setCompleted(false);
                businessProcess.setResult(null);
                BusinessProcessImpl.INSTANCE.update(businessProcess);
            }

            BusinessProcessSequence businessProcessSequence = withdrawExecutorTask.getBusinessProcessSequence();
            if (Objects.nonNull(businessProcessSequence)) {
                businessProcessSequence.setCompleted(false);
                businessProcessSequence.setResult(null);
                BusinessProcessSequenceImpl.INSTANCE.update(businessProcessSequence);
            }

            UploadedFileServiceImpl.INSTANCE.getListByDocumentAndExecutorTask(documentEdi, withdrawExecutorTask).forEach(UploadedFileImpl.INSTANCE::delete);

        });
    }

}
