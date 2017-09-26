package hibernate.impl.business_processes;

import abstract_entity.AbstractDocumentEdi;
import business_processes.ExecutorTask;
import business_processes.ExecutorTaskFolderStructure;
import categories.User;
import enumerations.FolderStructure;
import enumerations.ProcessType;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Created by kostya on 12/16/2016.
 */
public enum ExecutorTaskFolderStructureImpl implements HibernateDAO<ExecutorTaskFolderStructure> {

    INSTANCE;

    public void save(ExecutorTaskFolderStructure executorTaskFolderStructure) {

        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(executorTaskFolderStructure);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(ExecutorTaskFolderStructure executorTaskFolderStructure) {

        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(executorTaskFolderStructure);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(ExecutorTaskFolderStructure executorTaskFolderStructure) {

        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(executorTaskFolderStructure);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public List<ExecutorTaskFolderStructure> getExecutorList(User user, FolderStructure folderStructure, String filterString, String groupBy, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {

        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTaskFolderStructure> cq = cb.createQuery(ExecutorTaskFolderStructure.class);
        Root<ExecutorTaskFolderStructure> executorTaskFolderStructureRoot = cq.from(ExecutorTaskFolderStructure.class);
        Join<ExecutorTaskFolderStructure, ExecutorTask> executorTaskJoin = executorTaskFolderStructureRoot.join("executorTask", JoinType.LEFT);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = executorTaskJoin.join("document", JoinType.LEFT);

        // cb.and() - always true, cb.or() - always false
        cq.where(cb.and(
                cb.equal(executorTaskFolderStructureRoot.get("user"), user),
                cb.equal(executorTaskFolderStructureRoot.get("folder"), folderStructure),
                (Objects.isNull(abstractDocumentEdiClass)) ?
                        (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                                cb.or(
                                        cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, executorTaskJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"))) :
                        cb.and(
                                cb.equal(abstractDocumentJoin.type(), abstractDocumentEdiClass),
                                ("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                                        cb.or(
                                                ( (folderStructure == FolderStructure.INBOX || folderStructure == FolderStructure.TRASH)&&("author".equals(groupBy)) ? cb.like(cb.lower(abstractDocumentJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%") :
                                                  (folderStructure == FolderStructure.INBOX || folderStructure == FolderStructure.TRASH)&&("sender".equals(groupBy)) ? cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%") :
                                                  (folderStructure == FolderStructure.SENT || folderStructure == FolderStructure.DRAFT)&&("author".equals(groupBy)) ? cb.like(cb.lower(abstractDocumentJoin.get("whomString")), "%" + filterString.toLowerCase() + "%") :
                                                  (folderStructure == FolderStructure.SENT || folderStructure == FolderStructure.DRAFT)&&("sender".equals(groupBy)) ? cb.like(cb.lower(executorTaskJoin.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%") :
                                                  (folderStructure == FolderStructure.MARKED)&&("sender".equals(groupBy)) ?
                                                          cb.or(cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                                                cb.like(cb.lower(executorTaskJoin.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%")  ) :
                                                  (folderStructure == FolderStructure.MARKED)&&("author".equals(groupBy)) ?
                                                                  cb.or(cb.like(cb.lower(abstractDocumentJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                                                          cb.like(cb.lower(abstractDocumentJoin.get("whomString")), "%" + filterString.toLowerCase() + "%")  ) :
                                                          cb.or()
                                                ),
                                                (Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).count() == 0 ? cb.or() :
                                                        executorTaskJoin.get("processType").in(Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList()))),
                                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, executorTaskJoin.get("date"), cb.literal("'%d.%m.%Y %k %i'"))), "%" + filterString.toLowerCase() + "%")
                                        )
                        )
                )
        );

        // MainPanelServlet
        //  cell.executorTask.document.number
        //  cell.executorTask.document.getDocumentView("dd.MM.yyyy HH:mm:ss")
        //  CommonModule.getCorrectStringForWeb(cell.executorTask.document.theme)
        //  cell.executorTask.author.fio
        //  TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss')

        // MemorandumJournal
        //  ${groupBy=='sender' ? cell.executorTask.author.fio : cell.executorTask.document.author.fio }');
        //  ${cell.executorTask.processType.ruName}, ${cell.executorTask.document.number}, ${CommonModule.getCorrectStringForWeb(cell.executorTask.document.theme)}
        //  ${TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm')}'

        cq.select(executorTaskFolderStructureRoot);
        TypedQuery<ExecutorTaskFolderStructure> q = session.createQuery(cq);
        List<ExecutorTaskFolderStructure> list = q.getResultList();

        session.close();
        return list;

//        Session session = HibernateUtil.getSession();
//
//        Query<ExecutorTaskFolderStructure> query = session.createQuery(
//                "select executorTaskFolderStructure " +
//                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
//                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
//                        "and (executorTask.author = :user or executorTask.executor = :user)" +
//                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
//                        "where executorTaskFolderStructure.user = :user " +
//                        "and executorTaskFolderStructure.folder = :folderStructure " +
//                        "and type(abstractdocument)= Memorandum", ExecutorTaskFolderStructure.class);
//
//        query.setParameter("user", user);
//        query.setParameter("folderStructure", folderStructure);
//
//        List<ExecutorTaskFolderStructure> list = query.getResultList();
//        session.close();
//        return list;

        //    select * from bp_executor_task_folder_structure
        //    left join bp_executor_task
        //    on bp_executor_task_folder_structure.executor_task_id = bp_executor_task.ID
        //    left join doc_abstract_document_edi
        //    on doc_abstract_document_edi.id =  bp_executor_task.document_id
        //            where
        //    bp_executor_task_folder_structure.user_id = 254
        //    and bp_executor_task_folder_structure.folder = 0
        //    and doc_abstract_document_edi.DTYPE = 'Memorandum'
    }

    public List<ExecutorTaskFolderStructure> getCommonList(User user, String filterString, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {

        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTaskFolderStructure> cq = cb.createQuery(ExecutorTaskFolderStructure.class);
        Root<ExecutorTaskFolderStructure> executorTaskFolderStructureRoot = cq.from(ExecutorTaskFolderStructure.class);
        Join<ExecutorTaskFolderStructure, ExecutorTask> executorTaskJoin = executorTaskFolderStructureRoot.join("executorTask", JoinType.LEFT);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = executorTaskJoin.join("document", JoinType.LEFT);

        cq.where(cb.and(
                cb.equal(executorTaskFolderStructureRoot.get("user"), user),
                cb.notEqual(executorTaskFolderStructureRoot.get("folder"), FolderStructure.DRAFT),
                cb.equal(abstractDocumentJoin.type(), abstractDocumentEdiClass),
                (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, executorTaskJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(executorTaskJoin.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%")
                        )
                )
        ));

//        cell.executorTask.document.number
//        TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss'
//        cell.executorTask.document.getDocumentView("dd.MM.yyyy")}'
//        cell.executorTask.document.author.fio}'
//        cell.executorTask.author.fio}'
//        cell.executorTask.executor.fio}'
//        CommonModule.getCorrectStringForWeb(cell.executorTask.document.theme)}

        cq.select(executorTaskFolderStructureRoot);
        TypedQuery<ExecutorTaskFolderStructure> q = session.createQuery(cq);
        List<ExecutorTaskFolderStructure> list = q.getResultList();

        session.close();
        return list;

//        Session session = HibernateUtil.getSession();
//
//        //abstractdocument.author as doc_author, executorTask.date, executorTask.author as exec_author
//
//        Query<ExecutorTaskFolderStructure> query = session.createQuery(
//                "select executorTaskFolderStructure " +
//                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
//                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
//                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
//                        "where executorTaskFolderStructure.user = :user " +
//                        "and executorTaskFolderStructure.folder != :folderStructure " +
//                        "and type(abstractdocument)= Memorandum", ExecutorTaskFolderStructure.class);
//
//        query.setParameter("user", user);
//        query.setParameter("folderStructure", FolderStructure.DRAFT);
//
//        List<ExecutorTaskFolderStructure> list = query.getResultList();
//        session.close();
//        return list;

    }

    public List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUser(User user, ExecutorTask executorTask) {

        Session session = HibernateUtil.getSession();
        Query<ExecutorTaskFolderStructure> query = session.createQuery(
                "select executorTaskFolderStructure " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and executorTaskFolderStructure.executorTask = :executorTask ", ExecutorTaskFolderStructure.class);

        query.setParameter("user", user);
        query.setParameter("executorTask", executorTask);

        List<ExecutorTaskFolderStructure> list = query.getResultList();
        session.close();
        return list;

    }

    public List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUserDocumentProcessType(User user, AbstractDocumentEdi documentEdi, ProcessType processType) {

        Session session = HibernateUtil.getSession();
        Query<ExecutorTaskFolderStructure> query = session.createQuery(
                "select executorTaskFolderStructure " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and executorTask.processType = :processType " +
                        "and abstractdocument = :documentEdi ", ExecutorTaskFolderStructure.class);

        query.setParameter("user", user);
        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processType", processType);

        List<ExecutorTaskFolderStructure> list = query.getResultList();
        session.close();
        return list;

    }


    public List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUserDocument(User user, AbstractDocumentEdi documentEdi) {

        Session session = HibernateUtil.getSession();
        Query<ExecutorTaskFolderStructure> query = session.createQuery(
                "select executorTaskFolderStructure " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and abstractdocument = :documentEdi ", ExecutorTaskFolderStructure.class);

        query.setParameter("user", user);
        query.setParameter("documentEdi", documentEdi);

        List<ExecutorTaskFolderStructure> list = query.list();
        session.close();
        return list;

    }

    public HashMap<FolderStructure, Integer> getTaskCountByFolders(User user, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {

        Session session = HibernateUtil.getSession();
        Query query = session.createQuery(

                "select new map(executorTaskFolderStructure.folder, count( distinct abstractdocument.id)) " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
                        "and (executorTask.author = :user or executorTask.executor = :user) " +
                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and executorTask.result is null " +
                        "and type(abstractdocument)= :abstractDocumentEdiClass " +
                        "group by executorTaskFolderStructure.folder"
        );// Set correct type of Query ?

        query.setParameter("user", user);
        query.setParameter("abstractDocumentEdiClass", abstractDocumentEdiClass);

        List<HashMap<String, Object>> list = query.list(); // This type because you can't get everything from the query

        HashMap<FolderStructure, Integer> mapFolderStructure
                = list.stream().filter(t -> Objects.nonNull(t.get("0")) && Objects.nonNull(t.get("1"))).collect(HashMap::new,
                (m, v) ->
                        m.put((FolderStructure) v.get("0"), ((Long) v.get("1")).intValue()),
                HashMap::putAll);

        session.close();

        return mapFolderStructure;

//        doc_count - count of different documents, fold_str_count - count of executor tasks in folder
//
//        select folder, count( DISTINCT document_id) as doc_count, count(*) as fold_str_count from bp_executor_task_folder_structure
//          left join bp_executor_task on bp_executor_task_folder_structure.executor_task_id = bp_executor_task.ID
//                  and (bp_executor_task.author_id = 9 or bp_executor_task.executor_id = 9)
//          left join doc_abstract_document_edi on doc_abstract_document_edi.id =  bp_executor_task.document_id
//           where
//              bp_executor_task_folder_structure.user_id = 9
//               and bp_executor_task.result is null
//               and doc_abstract_document_edi.DTYPE = 'Memorandum'
//              group by folder

    }

}