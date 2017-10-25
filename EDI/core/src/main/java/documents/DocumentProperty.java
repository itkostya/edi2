package documents;

import enumerations.ProcessType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
* Documents' default properties
*
*/

public enum DocumentProperty {

    MESSAGE (0, "Message", "Сообщение", Collections.singletonList(ProcessType.INFORMATION), "ПС", 10,  Arrays.asList("mapHistory","mapStop", "withdrawAvailable")),
    MEMORANDUM(1, "Memorandum", "Служебная записка", Arrays.asList(ProcessType.ACCOMMODATION, ProcessType.INFORMATION, ProcessType.EXECUTION, ProcessType.AFFIRMATION, ProcessType.VISA),"СЗ", 10, Collections.EMPTY_LIST);

    private int id;
    private String enName;
    private String ruName;
    private List<ProcessType> processTypeList;
    private String defaultPrefix;
    private int prefixLength;
    private List<String> declinedFieldList;

    DocumentProperty(int id, String enName, String ruName, List<ProcessType> processTypeList, String defaultPrefix, int prefixLength,  List<String> declinedFieldList) {
        this.id = id;
        this.enName = enName;
        this.ruName = ruName;
        this.processTypeList = processTypeList;
        this.defaultPrefix = defaultPrefix;
        this.prefixLength = prefixLength;
        this.declinedFieldList = declinedFieldList;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getEnName() {
        return enName;
    }

    @SuppressWarnings("unused")
    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getRuName() {
        return ruName;
    }

    @SuppressWarnings("unused")
    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public List<ProcessType> getProcessTypeList() {
        return processTypeList;
    }

    @SuppressWarnings("unused")
    public void setProcessTypeList(List<ProcessType> processTypeList) {
        this.processTypeList = processTypeList;
    }

    public String getDefaultPrefix() {
        return defaultPrefix;
    }

    @SuppressWarnings("unused")
    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    public int getPrefixLength() {
        return prefixLength;
    }

    @SuppressWarnings("unused")
    public void setPrefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
    }

    public List<String> getDeclinedFieldList() {
        return declinedFieldList;
    }

    @SuppressWarnings("unused")
    public void setDeclinedFieldList(List<String> declinedFieldList) {
        this.declinedFieldList = declinedFieldList;
    }
}
