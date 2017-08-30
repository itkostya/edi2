package model;

/*
 * Created by kostya on 3/2/2017.
 */

// TODO? move it to service or delete?

public enum delSortParameter {

    ASC("Ascending", "По возрастанию"),
    DESC("Descending", "По возрастанию"),
    NO("No sorting", "По убыванию");

    private String enName;
    private String ruName;

    delSortParameter() {
    }

    delSortParameter(String enName, String ruName) {
        this.enName = enName;
        this.ruName = ruName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }
}
