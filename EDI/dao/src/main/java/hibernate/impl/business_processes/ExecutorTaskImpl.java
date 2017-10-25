package hibernate.impl.business_processes;

import abstract_entity.AbstractDocumentEdi;
import app_info.Constant;
import app_info.TimeModule;
import business_processes.ExecutorTask;
import categories.UploadedFile;
import categories.User;
import documents.Message;
import enumerations.ProcessResult;
import enumerations.ProcessType;
import exсeption.ExecutorTaskNotFoundException;
import hibernate.HibernateDAO;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

/*
 * Created by kostya on 9/16/2016.
 */
public enum ExecutorTaskImpl implements HibernateDAO<ExecutorTask> {

    INSTANCE;

    public void save(ExecutorTask executorTask) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.save(executorTask);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void update(ExecutorTask executorTask) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.update(executorTask);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public void delete(ExecutorTask executorTask) {
        Session session = HibernateUtil.getSessionWithTransaction();
        session.delete(executorTask);
        HibernateUtil.closeSessionWithTransaction(session);
    }

    public ExecutorTask getById(Long id) {
        Session session = HibernateUtil.getSession();
        ExecutorTask executorTask = session.get(ExecutorTask.class, id);
        if (executorTask == null) throw new ExecutorTaskNotFoundException(id);
        session.close();
        return executorTask;
    }

    public List<ExecutorTask> getReviewTask(User executor, String filterString) {

        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTask> cq = cb.createQuery(ExecutorTask.class);
        Root<ExecutorTask> e = cq.from(ExecutorTask.class);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = e.join("document", JoinType.INNER);

        // cb.and() - always true, cb.or() - always false
        cq.where(cb.and(
                cb.equal(e.get("executor"), executor),
                cb.equal(e.get("completed"), false),
                (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                (Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).count() == 0 ? cb.or() :
                                        e.get("processType").in(Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList()))),
                                cb.like(cb.lower(e.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, e.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, e.get("finalDate"), cb.literal("'%d.%m.%Y'"))), "%" + filterString.toLowerCase() + "%")
                        )
                )
        ));

        cq.select(e);
        TypedQuery<ExecutorTask> q = session.createQuery(cq);
        List<ExecutorTask> list = q.getResultList();

        session.close();
        return list;

    }


    public List<ExecutorTask> getControlledTask(User author, String filterString) {

        Session session = HibernateUtil.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTask> cq = cb.createQuery(ExecutorTask.class);
        Root<ExecutorTask> e = cq.from(ExecutorTask.class);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = e.join("document", JoinType.INNER);

        // cb.and() - always true, cb.or() - always false
        cq.where(cb.and(
                cb.equal(e.get("author"), author),
                cb.equal(e.get("completed"), false),
                cb.notEqual(abstractDocumentJoin.type(), Message.class),
                (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                (Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).count() == 0 ? cb.or() :
                                        e.get("processType").in(Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList()))),
                                cb.like(cb.lower(e.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, e.get("finalDate"), cb.literal("'%d.%m.%Y'"))), "%" + filterString.toLowerCase() + "%")
                        )
                )
        ));

        cq.select(e);
        TypedQuery<ExecutorTask> q = session.createQuery(cq);
        List<ExecutorTask> list = q.getResultList();

        session.close();
        return list;

    }

    public List<ExecutorTask> getFilterByExecutorAndDocument(User executor, AbstractDocumentEdi documentEdi) {

        Session session = HibernateUtil.getSession();
        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and e.executor =:executor and e.completed = false", ExecutorTask.class);

        query.setParameter("executor", executor);
        query.setParameter("documentEdi", documentEdi);

        List<ExecutorTask> list = query.getResultList();
        session.close();

        return list;
    }

    public List<ExecutorTask> getFilterByUserAndDocument(User user, AbstractDocumentEdi documentEdi) {

        Session session = HibernateUtil.getSession();
        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and (e.author =:user or e.executor =:user)", ExecutorTask.class);

        query.setParameter("user", user);
        query.setParameter("documentEdi", documentEdi);

        List<ExecutorTask> list = query.getResultList();
        session.close();

        return list;
    }

    public List<ExecutorTask> getFilterByExecutorDocumentAndProcessType(User executor, AbstractDocumentEdi documentEdi, ProcessType processType) {

        Session session = HibernateUtil.getSession();
        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and e.executor =:executor and e.processType =:processType and e.completed = false", ExecutorTask.class);

        query.setParameter("executor", executor);
        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processType", processType);

        List<ExecutorTask> list = query.getResultList();
        session.close();

        return list;
    }

    public List<ExecutorTask> getWithdrawAvailable(User executor, AbstractDocumentEdi documentEdi) {

        Session session = HibernateUtil.getSession();

        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi and e.executor =:executor " +
                "and e.completed = true and e.result != :processResultCanceled " +
                "and e.draft = false and e.dateCompleted > :timeStamp", ExecutorTask.class);

        query.setParameter("executor", executor);
        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processResultCanceled", ProcessResult.CANCELED);
        query.setParameter("timeStamp", TimeModule.addSecondsToCurrentTime(-Constant.MINUTES_WITHDRAW_AVAILABLE * 60L));
        List<ExecutorTask> list = query.getResultList();
        session.close();
        return list;
    }

    public HashMap<ExecutorTask, List<UploadedFile>> getSignaturesWithUploadedFiles(AbstractDocumentEdi documentEdi) {

        Session session = HibernateUtil.getSession();

        Query query = session.createQuery("select new map(executor_task , uploaded_file) from ExecutorTask executor_task " +
                "left join UploadedFile AS uploaded_file " +
                "on executor_task.document.id = uploaded_file.document.id and  executor_task.id = uploaded_file.executorTask.id " +
                "where executor_task.document =:documentEdi and executor_task.completed = true and executor_task.result != :processResultCanceled " +
                "order by executor_task.dateCompleted, executor_task.date, executor_task.executor.id");  // Set correct type of Query ?

//        SELECT * FROM edi.bp_executor_task AS bp_executor_task
//        left join edi.cat_uploaded_file AS cat_uploaded_file
//        on bp_executor_task.id = cat_uploaded_file.executor_task_id
//        where (bp_executor_task.document_id = 182 and bp_executor_task.completed = true and bp_executor_task.result != 7);

        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processResultCanceled", ProcessResult.CANCELED);

        @SuppressWarnings("unchecked") List<HashMap<String, Object>> list = query.list();

        // Type LinkedHashMap because we need sorting list after creating (HashMap doesn't let it)
        LinkedHashMap<ExecutorTask, List<UploadedFile>> mapSignaturesWithUploadedFiles
                = list.stream().filter(t -> Objects.nonNull(t.get("0"))).collect(LinkedHashMap::new,
                (LinkedHashMap<ExecutorTask, List<UploadedFile>> m, HashMap<String, Object> v) ->
                        m.put((ExecutorTask) v.get("0"),
                                (v.get("1") == null ?
                                        null :
                                        (m.get((ExecutorTask) v.get("0")) == null ?
                                                new LinkedList<UploadedFile>(Collections.singletonList((UploadedFile) v.get("1"))) :
                                                (LinkedList<UploadedFile>) addElementAndReturnList(m.get(v.get("0")), (UploadedFile) v.get("1"))
                                        ))
                        ),
                LinkedHashMap::putAll);

        //noinspection ResultOfMethodCallIgnored
        mapSignaturesWithUploadedFiles.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().getDateCompleted())).map(Map.Entry::getKey).collect(Collectors.toList());

        session.close();

        return mapSignaturesWithUploadedFiles;
    }

    // Just now let it be here. If we need something like this in another classes - create something common
    private <T> List<T> addElementAndReturnList(List<T> list, T t) {
        list.add(t);
        return list;
    }

    public ExecutorTask getDraft(User author, AbstractDocumentEdi documentEdi) {

        Session session = HibernateUtil.getSession();

        Query<ExecutorTask> query = session.createQuery("select e from ExecutorTask e " +
                "where e.document =:documentEdi " +
                "and e.draft = true and e.author =:author ", ExecutorTask.class);

        query.setParameter("author", author);  // Maybe it's exaggerate
        query.setParameter("documentEdi", documentEdi);

        ExecutorTask executorTask = query.stream().findFirst().orElse(null);  // better than query.getSingleResult() cause can return null
        session.close();
        return executorTask;
    }


}
