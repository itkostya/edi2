package impl.business_processes;

import abstract_entity.AbstractDocumentEdi;
import business_processes.BusinessProcess;
import business_processes.BusinessProcessSequence;
import business_processes.ExecutorTask;
import categories.User;
import enumerations.ProcessType;
import hibernate.impl.business_processes.BusinessProcessSequenceImpl;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Created by kostya on 1/19/2017.
 */
public enum BusinessProcessSequenceServiceImpl {

    INSTANCE;

    public BusinessProcessSequence getBusinessProcessSequence(ExecutorTask executorTask) {
        return BusinessProcessSequenceImpl.INSTANCE.getBusinessProcessSequence(executorTask);
    }

    private List<BusinessProcessSequence> getHistoryByDocumentList(AbstractDocumentEdi abstractDocumentEdi) {
        return BusinessProcessSequenceImpl.INSTANCE.getHistoryByDocumentList(abstractDocumentEdi);
    }

    public Map<BusinessProcess, List<BusinessProcessSequence>> getHistoryByDocumentMap(AbstractDocumentEdi abstractDocumentEdi) {

        LinkedHashMap<BusinessProcess, List<BusinessProcessSequence>> linkedHashMap =
                getHistoryByDocumentList(abstractDocumentEdi).stream()
                        .collect(Collectors.groupingBy(
                                BusinessProcessSequence::getBusinessProcess,
                                LinkedHashMap::new,
                                Collectors.toList()));

        //noinspection ResultOfMethodCallIgnored
        linkedHashMap.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().getDate())).map(Map.Entry::getKey).collect(Collectors.toList());

        return linkedHashMap;

    }

    public Map<ProcessType, List<BusinessProcessSequence>> getNotCompletedSequenceByDocumentAndUser(AbstractDocumentEdi abstractDocumentEdi, User user) {

        return BusinessProcessSequenceImpl.INSTANCE.getNotCompletedSequenceByDocumentAndUser(abstractDocumentEdi, user).stream()
                        .collect(Collectors.groupingBy(
                                BusinessProcessSequence::getProcessType,
                                LinkedHashMap::new,
                                Collectors.toList()));

    }


}
