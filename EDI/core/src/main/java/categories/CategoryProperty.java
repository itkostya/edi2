package categories;

public enum CategoryProperty {

    DEPARTMENT("Department", "Departments", "Департамент", "Департаменты", "Справочник - Департамент",  "Справочник - Департаменты"),
    POSITION("Position", "Positions", "Должность", "Должности", "Справочник - Должность",  "Справочник - Должности"),
    PROPOSAL_TEMPLATE("Proposal Template", "Proposal Templates", "Шаблон заявок", "Шаблоны заявок", "Справочник - Шаблон заявок", "Справочник - Шаблоны заявок"),
    UPLOADED_FILE("Uploaded file", "Uploaded files", "Файл", "Файлы", "Файл", "Файлы"),
    USER("User", "Users", "Пользователь", "Пользователи", "Справочник - Пользователь", "Справочник - Пользователи");

    private String enName;
    private String enPluralName;
    private String ruShortName;
    private String ruPluralShortName;
    private String ruFullName;
    private String ruPluralFullName;

    CategoryProperty() {
    }

    CategoryProperty(String enName, String enPluralName, String ruShortName, String ruPluralShortName, String ruFullName, String ruPluralFullName) {
        this.enName = enName;
        this.enPluralName = enPluralName;
        this.ruShortName = ruShortName;
        this.ruPluralShortName = ruPluralShortName;
        this.ruFullName = ruFullName;
        this.ruPluralFullName = ruPluralFullName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getEnPluralName() {
        return enPluralName;
    }

    public void setEnPluralName(String enPluralName) {
        this.enPluralName = enPluralName;
    }

    public String getRuShortName() {
        return ruShortName;
    }

    public void setRuShortName(String ruShortName) {
        this.ruShortName = ruShortName;
    }

    public String getRuPluralShortName() {
        return ruPluralShortName;
    }

    public void setRuPluralShortName(String ruPluralShortName) {
        this.ruPluralShortName = ruPluralShortName;
    }

    public String getRuFullName() {
        return ruFullName;
    }

    public void setRuFullName(String ruFullName) {
        this.ruFullName = ruFullName;
    }

    public String getRuPluralFullName() {
        return ruPluralFullName;
    }

    public void setRuPluralFullName(String ruPluralFullName) {
        this.ruPluralFullName = ruPluralFullName;
    }

}
