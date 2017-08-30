package impl.business_processes;

import abstract_entity.AbstractDocumentEdi;
import business_processes.ExecutorTask;
import business_processes.ExecutorTaskFolderStructure;
import categories.User;
import enumerations.FolderStructure;
import enumerations.ProcessType;
import exсeption.ExecutorTaskFolderStructureException;
import hibernate.impl.business_processes.ExecutorTaskFolderStructureImpl;

import java.util.*;
import java.util.stream.Collectors;

/*
 * Created by kostya on 1/31/2017.
 */
public enum ExecutorTaskFolderStructureServiceImpl {

    INSTANCE;

    public List<ExecutorTaskFolderStructure> getMarkedTask(User user, String sortingSequence, String filterString) {

        List<ExecutorTaskFolderStructure> executorTaskFolderStructureList = ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorList(user, FolderStructure.MARKED, filterString, "MainPanelServlet", "");

        if (sortingSequence.equals("default")) {
            // Completed (false, true) asc, Date desc
            executorTaskFolderStructureList.sort(
                    (o1, o2) ->
                            (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) != 0 ?
                                    (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) :
                                    o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate())
            );
        } else {
            boolean ascSorting = (sortingSequence.charAt(2) == '+' || sortingSequence.charAt(2) == 'n');
            switch (sortingSequence.charAt(0)) {
                case '0':  // cell.executorTask.document.number
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getNumber()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()));
                    break;
                case '1':  // cell.executorTask.document.getDocumentView("dd.MM.yyyy HH:mm:ss")
                    // Just by date. Or it could be document property + date
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getDate().compareTo(o1.getExecutorTask().getDocument().getDate()));
                    break;
                case '2':  // cell.executorTask.document.theme
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getTheme()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getTheme().compareTo(o1.getExecutorTask().getDocument().getTheme()));
                    break;
                case '3':  // cell.executorTask.author.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getAuthor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getAuthor().getFio().compareTo(o1.getExecutorTask().getAuthor().getFio()));
                    break;
                case '4':  // TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss')
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
                    break;
            }
        }

        return executorTaskFolderStructureList;

    }

    public List<ExecutorTaskFolderStructure> getTasksByFolder(User user, FolderStructure folderStructure, String groupBy, String sortingSequence, String filterString) {

        List<ExecutorTaskFolderStructure> executorTaskFolderStructureList = ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorList(user, folderStructure, filterString, "MemorandumJournal", groupBy);

        if (Objects.nonNull(groupBy)) {
            switch (groupBy) {
                case "author":
                    // Delete duplicates using key - Folder, Marked, Result, Completed, ProcessType, Document
                    Map<Integer, ExecutorTaskFolderStructure> mapExecutorTask2 = executorTaskFolderStructureList.stream()
                            .collect(HashMap::new, (m, v) -> m.put(
                                    Objects.hash(v.getFolder(), v.isMarked(), v.getExecutorTask().getResult(), v.getExecutorTask().isCompleted(), v.getExecutorTask().getProcessType(), v.getExecutorTask().getDocument()), v),
                                    HashMap::putAll);

                    executorTaskFolderStructureList.clear();
                    executorTaskFolderStructureList.addAll(mapExecutorTask2.values());
                    break;
                case "sender":
                    // It's ok
                    break;
            }
        }

        if (sortingSequence.equals("default")) {
            // completed asc, date desc
            executorTaskFolderStructureList.sort(
                    (o1, o2) ->
                            (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) != 0 ?
                                    (Boolean.compare(o1.getExecutorTask().isCompleted(), o2.getExecutorTask().isCompleted())) :
                                    o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate())
            );
        } else {
            boolean ascSorting = (sortingSequence.charAt(2) == '+' || sortingSequence.charAt(2) == 'n');
            switch (sortingSequence.charAt(0)) {
                case '0':  // cell.executorTask.author.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getAuthor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getAuthor().getFio().compareTo(o1.getExecutorTask().getAuthor().getFio()));
                    break;
                case '1':  // cell.executorTask.processType.ruName + cell.executorTask.document.number + cell.executorTask.document.theme
                    // Draft doesn't have ProcessType that's why for this type sorting by 'Document number' + 'Document theme'
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(
                                (o1, o2) ->
                                        folderStructure != FolderStructure.DRAFT && o1.getExecutorTask().getProcessType().compareTo(o2.getExecutorTask().getProcessType()) != 0 ?
                                                o1.getExecutorTask().getProcessType().compareTo(o2.getExecutorTask().getProcessType()) :
                                                o1.getExecutorTask().getDocument().getNumber().compareTo(o2.getExecutorTask().getDocument().getNumber()) != 0 ?
                                                        o1.getExecutorTask().getDocument().getNumber().compareTo(o2.getExecutorTask().getDocument().getNumber()) :
                                                        o1.getExecutorTask().getDocument().getTheme().compareTo(o2.getExecutorTask().getDocument().getTheme()));
                    else
                        executorTaskFolderStructureList.sort(
                                (o1, o2) ->
                                        folderStructure != FolderStructure.DRAFT && o2.getExecutorTask().getProcessType().compareTo(o1.getExecutorTask().getProcessType()) != 0 ?
                                                o2.getExecutorTask().getProcessType().compareTo(o1.getExecutorTask().getProcessType()) :
                                                o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()) != 0 ?
                                                        o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()) :
                                                        o2.getExecutorTask().getDocument().getTheme().compareTo(o1.getExecutorTask().getDocument().getTheme()));
                    break;
                case '2':  // cell.executorTask.date
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
                    break;

            }

        }

        return executorTaskFolderStructureList;

    }

    public List<ExecutorTaskFolderStructure> getCommonList(User user, String sortingSequence, String filterString) {

        List<ExecutorTaskFolderStructure> executorTaskFolderStructureList = ExecutorTaskFolderStructureImpl.INSTANCE.getCommonList(user, filterString);

        if (sortingSequence.equals("default")) {
            // Date desc
            executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
        } else {
            boolean ascSorting = (sortingSequence.charAt(2) == '+' || sortingSequence.charAt(2) == 'n');
            switch (sortingSequence.charAt(0)) {
                case '0':  // cell.executorTask.document.number
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getNumber()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getNumber().compareTo(o1.getExecutorTask().getDocument().getNumber()));
                    break;
                case '1':  // TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss')
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDate().compareTo(o1.getExecutorTask().getDate()));
                    break;
                case '2':  // cell.executorTask.document.getDocumentView("dd.MM.yyyy")
                    // Just by date. Or it could be document property + date
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getDate()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getDate().compareTo(o1.getExecutorTask().getDocument().getDate()));
                    break;
                case '3':  // cell.executorTask.document.author.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getAuthor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getAuthor().getFio().compareTo(o1.getExecutorTask().getDocument().getAuthor().getFio()));
                    break;
                case '4':  // cell.executorTask.author.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getAuthor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getAuthor().getFio().compareTo(o1.getExecutorTask().getAuthor().getFio()));
                    break;
                case '5':  // cell.executorTask.executor.getFio()
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getExecutor().getFio()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getExecutor().getFio().compareTo(o1.getExecutorTask().getExecutor().getFio()));
                    break;
                case '6':  // cell.executorTask.document.theme
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().getDocument().getTheme()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> o2.getExecutorTask().getDocument().getTheme().compareTo(o1.getExecutorTask().getDocument().getTheme()));
                    break;
                case '7':  // cell.executorTask.completed
                    if (ascSorting)
                        executorTaskFolderStructureList.sort(Comparator.comparing(o -> o.getExecutorTask().isCompleted()));
                    else
                        executorTaskFolderStructureList.sort((o1, o2) -> Boolean.compare(o2.getExecutorTask().isCompleted(), o1.getExecutorTask().isCompleted()));
                    break;
            }
        }

        return executorTaskFolderStructureList;

    }

    public void checkDeletionMarkAndChangeFolder(User currentUser, ExecutorTask executorTask, boolean deletionMark) {

        if (Objects.nonNull(executorTask) && Objects.nonNull(currentUser)) {

            boolean foundElement = false;
            List<ExecutorTaskFolderStructure> listBaseExecutorTaskFolderStructureByUser = ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorTaskFolderStructureByUser(currentUser, executorTask);

            List<ExecutorTaskFolderStructure> listExecutorTaskFolderStructure =
                    listBaseExecutorTaskFolderStructureByUser.stream()
                            .filter(t -> (t.getFolder() != FolderStructure.MARKED))
                            .collect(Collectors.toList());

            boolean isMarked = listBaseExecutorTaskFolderStructureByUser.stream().anyMatch(t -> t.getFolder() == FolderStructure.MARKED);

            if (deletionMark) {

                for (ExecutorTaskFolderStructure executorTaskFolderStructure : listExecutorTaskFolderStructure) {
                    if (executorTaskFolderStructure.getFolder() != FolderStructure.TRASH) {
                        if (foundElement) {
                            ExecutorTaskFolderStructureImpl.INSTANCE.delete(executorTaskFolderStructure); // Because it's duplicate
                        } else {
                            executorTaskFolderStructure.setFolder(FolderStructure.TRASH);
                            executorTaskFolderStructure.setMarked(false);
                            ExecutorTaskFolderStructureImpl.INSTANCE.update(executorTaskFolderStructure);
                            foundElement = true;
                        }
                    }
                }

            } else {

                if (listExecutorTaskFolderStructure.size() == 1) {
                    if (currentUser.equals(executorTask.getAuthor())
                            && executorTask.getAuthor().equals(executorTask.getExecutor())) {
                        updateExecutorTaskFolderStructureImpl(listExecutorTaskFolderStructure.get(0), FolderStructure.SENT, isMarked);
                        ExecutorTaskFolderStructureImpl.INSTANCE.save(new ExecutorTaskFolderStructure(FolderStructure.INBOX, currentUser, executorTask, isMarked));
                    } else if (currentUser.equals(executorTask.getAuthor())) {
                        updateExecutorTaskFolderStructureImpl(listExecutorTaskFolderStructure.get(0), FolderStructure.SENT, isMarked);
                    } else if (currentUser.equals(executorTask.getExecutor())) {
                        updateExecutorTaskFolderStructureImpl(listExecutorTaskFolderStructure.get(0), FolderStructure.INBOX, isMarked);
                    }
                } else throw new ExecutorTaskFolderStructureException(executorTask.getId());

            }

        }
    }

    public void changeMarkedStatus(User currentUser, AbstractDocumentEdi documentEdi, ExecutorTask executorTask) {

        if (Objects.nonNull(currentUser)) {
            List<ExecutorTaskFolderStructure> listExecutorTaskFolderStructure =
                    (Objects.isNull(executorTask) || Objects.isNull(executorTask.getProcessType())) ?
                            ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorTaskFolderStructureByUserDocument(currentUser, documentEdi) :
                            ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorTaskFolderStructureByUserDocumentProcessType(currentUser, documentEdi, executorTask.getProcessType());

            if (listExecutorTaskFolderStructure.size() > 0) {

                boolean setMark = listExecutorTaskFolderStructure.stream().noneMatch(ExecutorTaskFolderStructure::isMarked);

                listExecutorTaskFolderStructure.stream().filter(t -> (t.getFolder() != FolderStructure.TRASH)).forEach(t -> {
                    t.setMarked(setMark);
                    ExecutorTaskFolderStructureImpl.INSTANCE.update(t);
                });

                if (setMark) {
                    ExecutorTaskFolderStructureImpl.INSTANCE.save(
                            new ExecutorTaskFolderStructure(FolderStructure.MARKED, currentUser,
                                    (Objects.isNull(executorTask) || Objects.isNull(executorTask.getProcessType())) ?
                                            listExecutorTaskFolderStructure.get(0).getExecutorTask() : executorTask, true));
                } else {
                    listExecutorTaskFolderStructure.stream().filter(t -> (t.getFolder() == FolderStructure.MARKED)).forEach(ExecutorTaskFolderStructureImpl.INSTANCE::delete);
                }
            }
        }

    }

    private void updateExecutorTaskFolderStructureImpl(ExecutorTaskFolderStructure executorTaskFolderStructure, FolderStructure folderStructure, boolean isMarked) {
        executorTaskFolderStructure.setFolder(folderStructure);
        executorTaskFolderStructure.setMarked(isMarked);
        ExecutorTaskFolderStructureImpl.INSTANCE.update(executorTaskFolderStructure);
    }

    public HashMap<FolderStructure, Integer> getTaskCountByFolders(User user) {
        return ExecutorTaskFolderStructureImpl.INSTANCE.getTaskCountByFolders(user);
    }

    public boolean isMarkedExecutorTask(User user, AbstractDocumentEdi documentEdi, ExecutorTask executorTask) {

        if (Objects.isNull(executorTask) || Objects.isNull(executorTask.getProcessType()))
            return isMarkedDocument(user, documentEdi);
        return isMarkedDocumentProcessType(user, documentEdi, executorTask.getProcessType());
    }

    private boolean isMarkedDocument(User user, AbstractDocumentEdi documentEdi) {

        return ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorTaskFolderStructureByUserDocument(user, documentEdi).stream().anyMatch(ExecutorTaskFolderStructure::isMarked);

    }

    private boolean isMarkedDocumentProcessType(User user, AbstractDocumentEdi documentEdi, ProcessType processType) {

        return ExecutorTaskFolderStructureImpl.INSTANCE.getExecutorTaskFolderStructureByUserDocumentProcessType(user, documentEdi, processType).stream().anyMatch(ExecutorTaskFolderStructure::isMarked);

    }

}



